package com.tothenew.ecommerceappAfterStage2Complete.config;

import com.tothenew.ecommerceappAfterStage2Complete.utils.SendEmail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class SchedulerMidnight {

    @Autowired
    SendEmail sendEmail;
    Logger logger = LoggerFactory.getLogger(SchedulerMidnight.class);

    @Scheduled(cron = "0 0 0 * * *",zone = "Indian/Maldives")
    public void sendEmailToSeller() {
        logger.trace("running scheduler");
        sendEmail.sendEmail("ACCEPTED/REJECTED","SOME DETAILS","sellerEmail");
    }
}
