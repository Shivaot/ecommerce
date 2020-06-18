package com.tothenew.ecommerceappAfterStage2Complete.services;

import com.tothenew.ecommerceappAfterStage2Complete.dtos.CustomerAllProductByCategoryDTO;
import com.tothenew.ecommerceappAfterStage2Complete.dtos.CustomerProductViewByIdDTO;
import com.tothenew.ecommerceappAfterStage2Complete.dtos.ProductDTO;
import com.tothenew.ecommerceappAfterStage2Complete.dtos.ProductVarPlusImagesDTO;
import com.tothenew.ecommerceappAfterStage2Complete.entities.category.Category;
import com.tothenew.ecommerceappAfterStage2Complete.entities.product.Product;
import com.tothenew.ecommerceappAfterStage2Complete.entities.users.Seller;
import com.tothenew.ecommerceappAfterStage2Complete.exceptions.FieldAlreadyExistException;
import com.tothenew.ecommerceappAfterStage2Complete.exceptions.ResourceNotFoundException;
import com.tothenew.ecommerceappAfterStage2Complete.repositories.CategoryRepo;
import com.tothenew.ecommerceappAfterStage2Complete.repositories.ProductRepo;
import com.tothenew.ecommerceappAfterStage2Complete.repositories.SellerRepo;
import com.tothenew.ecommerceappAfterStage2Complete.utils.EmailSender;
import com.tothenew.ecommerceappAfterStage2Complete.utils.UserEmailFromToken;
import org.apache.tomcat.util.codec.binary.Base64;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.util.*;

@Service
public class ProductService {

    @Autowired
    ProductRepo productRepo;
    @Autowired
    CategoryRepo categoryRepo;
    @Autowired
    UserEmailFromToken userEmailFromToken;
    @Autowired
    SellerRepo sellerRepo;
    @Autowired
    EmailSender emailSender;
    @Autowired
    ModelMapper modelMapper;

    Logger logger = LoggerFactory.getLogger(ProductService.class);

    public String addProduct(HttpServletRequest request, String name, String brand, java.lang.Long categoryId, Optional<String> desc, Optional<Boolean> isCancellable, Optional<Boolean> isReturnable) {
        // check for leaf category
        List<Object> parentIds = categoryRepo.findLeafCategories();
        List<Object> leafCategoryIds = categoryRepo.findCategoryId();
        leafCategoryIds.removeAll(parentIds);
        Set<java.lang.Long> leafs = new HashSet<>();
        leafCategoryIds.forEach(i->{
            leafs.add(java.lang.Long.parseLong(i.toString()));
        });
        if (!(leafs.contains(categoryId))) {
            throw new ResourceNotFoundException(categoryId +" not a leaf category");
        }
        String sellerEmail = userEmailFromToken.getUserEmail(request);
        Seller seller = sellerRepo.findByEmail(sellerEmail);
        Optional<Product> checkUniqueName = productRepo.checkUniqueProductName(brand,name,seller.getId(),categoryId);
        if (checkUniqueName.isPresent()) {
            throw new FieldAlreadyExistException(name + " product already exist");
        }
        Product product = new Product();
        Optional<Category> category = categoryRepo.findById(categoryId);
        product.setName(name);
        product.setBrand(brand);
        product.setActive(false);
        product.setDeleted(false);
        product.setCategory(category.get());
        product.setSeller(seller);
        if (desc.isPresent()) {
            product.setDescription(desc.get());
        }
        if (isCancellable.isPresent()) {
            product.setCancellable(isCancellable.get());
        }
        if (!isCancellable.isPresent()) {
            product.setCancellable(true);
        }
        if (isReturnable.isPresent()) {
            product.setReturnable(isReturnable.get());
        }
        if (!isReturnable.isPresent()) {
            product.setReturnable(true);
        }
        seller.getProducts().add(product);
        category.get().getProducts().add(product);

        productRepo.save(product);
        categoryRepo.save(category.get());
        sellerRepo.save(seller);
        Optional<Product> savedProduct = productRepo.checkUniqueProductName(brand,name,seller.getId(),categoryId);
        new File("/home/shiva/software/afterStage2/src/main/resources/static/products/"+savedProduct.get().getId()+"/variations").mkdirs();
        emailSender.sendEmail("ACTIVATE ADDED PRODUCT",name+" " +categoryId+" "+brand,"shiva@admin.com");

        return "Success";
    }

    public ProductDTO viewProduct(Long id, HttpServletRequest request) {
        String userEmail = userEmailFromToken.getUserEmail(request);
        Optional<Product> product = productRepo.findById(id);
        if (!product.isPresent()) {
            throw new ResourceNotFoundException(id+" product not found");
        }
        Seller seller = sellerRepo.findByEmail(userEmail);
        if (product.get().getSeller().getId().compareTo(seller.getId()) != 0) {
            throw new  ResourceNotFoundException("invalid seller");
        }
        if (product.get().getDeleted()) {
            throw new ResourceNotFoundException(id+" product is deleted");
        }
        ProductDTO productDTO = new ProductDTO();
        productDTO.setActive(product.get().getActive());
        productDTO.setBrand(product.get().getBrand());
        productDTO.setCancellable(product.get().getCancellable());
        productDTO.setCategory(product.get().getCategory());
        productDTO.setDescription(product.get().getDescription());
        productDTO.setName(product.get().getName());
        productDTO.setReturnable(product.get().getReturnable());
        productDTO.setSeller(product.get().getSeller().getId());
        productDTO.setId(id);

        return productDTO;
    }

    public List<?> viewAllProducts(HttpServletRequest request,String page, String size, String sortBy, String order, Optional<String> query) {
        if (query.isPresent()) {
            ProductDTO productDTO =viewProduct(Long.parseLong(query.get()),request);
            List<ProductDTO> productDTOS = new ArrayList<>();
            productDTOS.add(productDTO);
            return productDTOS;
        }
        String sellerEmail = userEmailFromToken.getUserEmail(request);
        Seller seller = sellerRepo.findByEmail(sellerEmail);
        List<Product> products = productRepo.productsOfSeller(seller.getId(), PageRequest.of(Integer.parseInt(page),Integer.parseInt(size), Sort.by(Sort.Direction.fromString(order),sortBy)));
        return products;
    }

    @Transactional
    public String deleteProductById(Long id, HttpServletRequest request) {
        String userEmail = userEmailFromToken.getUserEmail(request);
        Optional<Product> product = productRepo.findById(id);
        if (!product.isPresent()) {
            throw new ResourceNotFoundException(id+" product not found");
        }
        Seller seller = sellerRepo.findByEmail(userEmail);
        if (product.get().getSeller().getId().compareTo(seller.getId()) != 0) {
            throw new  ResourceNotFoundException("invalid seller");
        }
        product.get().setDeleted(true);
        productRepo.save(product.get());
        return "Success";
    }

    public String updateProductById(HttpServletRequest request, Long id, Optional<String> name, Optional<String> desc, Optional<Boolean> isCancellable, Optional<Boolean> isReturnable) {
        String userEmail = userEmailFromToken.getUserEmail(request);
        Optional<Product> product = productRepo.findById(id);
        if (!product.isPresent()) {
            throw new ResourceNotFoundException(id+" product not found");
        }
        Seller seller = sellerRepo.findByEmail(userEmail);
        if (product.get().getSeller().getId().compareTo(seller.getId()) != 0) {
            throw new  ResourceNotFoundException("invalid seller");
        }
        System.out.println(product.get().getBrand()+name.get()+seller.getId()+product.get().getCategory());
        if (name.isPresent()) {
            Optional<Product> checkUniqueName = productRepo.checkUniqueProductName(product.get().getBrand(),name.get(),seller.getId(),product.get().getCategory().getId());
            if (checkUniqueName.isPresent()) {
                throw new FieldAlreadyExistException(name.get() + " product already exist");
            }
            product.get().setName(name.get());
        }
        if (desc.isPresent()) {
            product.get().setDescription(desc.get());
        }
        if (isCancellable.isPresent()) {
            product.get().setCancellable(isCancellable.get());
        }
        if (isReturnable.isPresent()) {
            product.get().setReturnable(isReturnable.get());
        }
        productRepo.save(product.get());
        return "Success";
    }

    public CustomerProductViewByIdDTO viewProductCustomer(Long productId) throws IOException {
        Optional<Product> product = productRepo.findById(productId);
        if (!product.isPresent()) {
            throw new ResourceNotFoundException(productId+" product not found");
        }
        if (product.get().getDeleted()) {
            throw new ResourceNotFoundException(productId+" product is deleted");
        }
        if (!product.get().getActive()) {
            throw new ResourceNotFoundException(productId+" product is inactive");
        }
        try {
            if (product.get().getProductVariations().isEmpty()) {
                throw new ResourceNotFoundException(productId+" product not have any variations");
            }
        } catch (Exception ex) {
            logger.error("Exception occurred",ex);
        }

        CustomerProductViewByIdDTO customerProductViewByIdDTO = new CustomerProductViewByIdDTO();
        customerProductViewByIdDTO.setProduct(product.get());

        ProductVarPlusImagesDTO productVarPlusImagesDTO = new ProductVarPlusImagesDTO();
        productVarPlusImagesDTO.setProductVariation(product.get().getProductVariations());
        List<String> images = new ArrayList<>();
        try {
            product.get().getProductVariations().forEach(productVariation -> {
                File  f = new File("/home/shiva/software/afterStage2/src/main/resources/static/products/"  + productId + "/variations/");
                File[] matchingFiles = new File[20];
                try {
                    matchingFiles = f.listFiles(new FilenameFilter() {
                        public boolean accept(File dir, String name) {
                            return name.contains(productVariation.getId().toString());
                        }
                    });
                }
                catch (Exception ex) {
                    logger.error("Exception Occurred",ex);
                }
                for (int i=0;i<matchingFiles.length;i++) {
                    String[] arr = matchingFiles[i].toString().split("variations/");
                    System.out.println(arr[1]);
                    images.add("localhost:8080/products/"+ productId +"/variations/" + arr[1]);
                }
            });
        } catch (Exception ex) {
            logger.error("error",ex);
        }
        productVarPlusImagesDTO.setImages(images);
        customerProductViewByIdDTO.setProductVarPlusImagesDTO(productVarPlusImagesDTO);

        return customerProductViewByIdDTO;
    }

    public CustomerAllProductByCategoryDTO viewAllProductsOfCategory(Long categoryId, String page, String size, String sortBy, String order) {
        Optional<Category> category = categoryRepo.findById(categoryId);
        if (!category.isPresent()) {
            throw new ResourceNotFoundException("invalid id");
        }
        if (productRepo.findByCategoryId(categoryId).isEmpty()) {
            throw new ResourceNotFoundException("not a leaf node");
        }
        CustomerAllProductByCategoryDTO customerAllProductByCategoryDTO = new CustomerAllProductByCategoryDTO();
        List<String> images = new ArrayList<>();
        List<HashMap<Long, String>> prices = new ArrayList<>();
        List<Product> products = productRepo.getAllProductsOfCategory(categoryId,PageRequest.of(Integer.parseInt(page),Integer.parseInt(size),Sort.by(Sort.Direction.fromString(order),sortBy)));
        customerAllProductByCategoryDTO.setProducts(products);
        try {
            products.forEach(product -> {
                File  f = new File("/home/shiva/software/afterStage2/src/main/resources/static/products/"  + product.getId() + "/variations");
                File[] matchingFiles = new File[10];
                try {
                    matchingFiles = f.listFiles(new FilenameFilter() {
                        public boolean accept(File dir, String name) {
                            return name.contains("PI");
                        }
                    });
                }
                catch (Exception ex) {
                    logger.error("error: ",ex);
                }
                for (int i=0;i<matchingFiles.length;i++) {
                    String[] arr = matchingFiles[i].toString().split("variations/");
                    System.out.println(arr[1]);
                    images.add("localhost:8080/products/"+ product.getId() +"/variations/" + arr[1]);
                }
                prices.add(getPrice(product.getId()));
            });
        } catch (Exception ex) {
            logger.error("error",ex);
        }
        customerAllProductByCategoryDTO.setImages(images);
        customerAllProductByCategoryDTO.setPrices(prices);
        return customerAllProductByCategoryDTO;
    }

    private HashMap<Long, String> getPrice(Long id) {
        Optional<Product> product = productRepo.findById(id);
        HashMap<Long,String> hashMap = new HashMap<>();
        product.get().getProductVariations().forEach(pv->{
            hashMap.put(id,pv.getPrice().toString());
        });
        return hashMap;
    }

    public CustomerAllProductByCategoryDTO viewAllSimilarProducts(Long productId, String page, String size, String sortBy, String order) {
        Optional<Product> product = productRepo.findById(productId);
        if (!product.isPresent()) {
            throw new ResourceNotFoundException("id invalid");
        }
        return viewAllProductsOfCategory(product.get().getCategory().getId(),page,size,sortBy,order);
    }

    public CustomerProductViewByIdDTO viewProductAdmin(Long productId) {
        Optional<Product> product = productRepo.findById(productId);
        if (!product.isPresent()) {
            throw new ResourceNotFoundException(productId+" product not found");
        }
        CustomerProductViewByIdDTO customerProductViewByIdDTO = new CustomerProductViewByIdDTO();
        customerProductViewByIdDTO.setProduct(product.get());

        ProductVarPlusImagesDTO productVarPlusImagesDTO = new ProductVarPlusImagesDTO();
        try {
            productVarPlusImagesDTO.setProductVariation(product.get().getProductVariations());
        } catch (Exception ex) {}

        List<String> images = new ArrayList<>();
        product.get().getProductVariations().forEach(pv->{
            File f = new File("/home/shiva/software/afterStage2/src/main/resources/static/products/"  + productId + "/variations");
            File[] matchingFiles = new File[2];
            try {
                matchingFiles = f.listFiles(new FilenameFilter() {
                    public boolean accept(File dir, String name) {
                        return name.startsWith(pv.getId().toString());
                    }
                });
            }
            catch (Exception ex) {
                logger.error("Exception Occurred",ex);
            }
            if (matchingFiles.length>0) {
                File file = new File(matchingFiles[0].toString());
                byte[] fileContent = new byte[0];
                try {
                    fileContent = Files.readAllBytes(file.toPath());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                String encodedFile = null;
                try {
                    encodedFile = new String(Base64.encodeBase64(fileContent), "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                images.add(encodedFile);
            }
        });
        productVarPlusImagesDTO.setImages(images);
        customerProductViewByIdDTO.setProductVarPlusImagesDTO(productVarPlusImagesDTO);

        return customerProductViewByIdDTO;
    }

    public CustomerAllProductByCategoryDTO viewAllProductsAdmin(String page, String size, String sortBy, String order, Optional<Long> query) {
        if (query.isPresent()) {
            if (categoryRepo.findById(query.get()).isPresent()) {
                return viewAllProductsOfCategory(query.get(),page,size,sortBy,order);
            }
            if (sellerRepo.findById(query.get()).isPresent()) {
                List<Product> products = productRepo.productsOfSeller(query.get(),PageRequest.of(Integer.parseInt(page),Integer.parseInt(size),Sort.by(Sort.Direction.fromString(order),sortBy)));
                CustomerAllProductByCategoryDTO customerAllProductByCategoryDTO = new CustomerAllProductByCategoryDTO();
                customerAllProductByCategoryDTO.setProducts(products);
                customerAllProductByCategoryDTO.setImage("no images found");
                return customerAllProductByCategoryDTO;
            }
        }
        List<Product> products = productRepo.getAllProductsNonDeletedActive(PageRequest.of(Integer.parseInt(page),Integer.parseInt(size),Sort.by(Sort.Direction.fromString(order),sortBy)));
        CustomerAllProductByCategoryDTO customerAllProductByCategoryDTO = new CustomerAllProductByCategoryDTO();
        customerAllProductByCategoryDTO.setProducts(products);
        File f = new File("/home/shiva/software/afterStage2/src/main/resources/static/products/"  + "214" + "/variations");
        File[] matchingFiles = new File[2];
        System.out.println(matchingFiles.length);
        try {
            matchingFiles = f.listFiles(new FilenameFilter() {
                public boolean accept(File dir, String name) {
                    return name.startsWith("215");
                }
            });
        }
        catch (Exception ex) {
            logger.error("Exception occurred",ex);
        }
        if (matchingFiles.length>0) {
            File file = new File(matchingFiles[0].toString());
            System.out.println(file);
            byte[] fileContent = new byte[0];
            try {
                fileContent = Files.readAllBytes(file.toPath());
            } catch (IOException e) {
                e.printStackTrace();
            }
            String encodedFile = null;
            try {
                encodedFile = new String(Base64.encodeBase64(fileContent), "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            customerAllProductByCategoryDTO.setImage(encodedFile);
        }
        return customerAllProductByCategoryDTO;
    }

    public String activateProduct(Long productId) {
        Optional<Product> product = productRepo.findById(productId);
        if (!product.isPresent()) {
            throw new ResourceNotFoundException(productId+" not found");
        }
        if (product.get().getActive()) {
            throw new ResourceNotFoundException(productId+" already active");
        }
        product.get().setActive(true);
        emailSender.sendEmail("PRODUCT ACTIVATED",productId+" product activated",product.get().getSeller().getEmail());
        productRepo.save(product.get());
        return "Success";
    }

    public String deactivateProduct(Long productId) {
        Optional<Product> product = productRepo.findById(productId);
        if (!product.isPresent()) {
            throw new ResourceNotFoundException(productId+" not found");
        }
        if (!product.get().getActive()) {
            throw new ResourceNotFoundException(productId+" already de-active");
        }
        product.get().setActive(false);
        emailSender.sendEmail("PRODUCT DE-ACTIVATED",productId+" product deactivated",product.get().getSeller().getEmail());
        productRepo.save(product.get());
        return "Success";
    }
}
