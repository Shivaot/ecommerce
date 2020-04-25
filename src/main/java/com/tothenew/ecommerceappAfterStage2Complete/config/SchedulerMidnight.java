package com.tothenew.ecommerceappAfterStage2Complete.config;

import com.tothenew.ecommerceappAfterStage2Complete.utils.EmailSender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class SchedulerMidnight {

    @Autowired
    EmailSender emailSender;
    Logger logger = LoggerFactory.getLogger(SchedulerMidnight.class);

    @Scheduled(cron = "0 0 0 * * *",zone = "Indian/Maldives")
    public void sendEmailToSeller() {
        logger.trace("running scheduler");
        emailSender.sendEmail("ACCEPTED/REJECTED","SOME DETAILS","sellerEmail");
    }
}
