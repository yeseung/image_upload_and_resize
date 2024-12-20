package com.gongdaeoppa.demo;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.File;
import java.time.LocalTime;
import java.util.logging.Logger;

@Component
public class SchedulerComponent {
    
    private static final Logger LOGGER = Logger.getLogger(SchedulerComponent.class.getName());
    
    private static final String UPLOAD_DIR = System.getProperty("user.dir") + File.separator + "tomcat" + File.separator + "webapps";
    
    //@Scheduled(initialDelay = 10000, fixedDelay = 10000)
    //@Scheduled(cron = "5 * * * * *")
    //@Scheduled(fixedRate=12000L)
    //@Scheduled(initialDelay = 300000, fixedDelay = 300000) //5분
    //@Scheduled(initialDelay = 1800000, fixedDelay = 1800000) //30분
    @Scheduled(initialDelay = 3600000, fixedDelay = 3600000) //1시간
    public void manageUploadFolder() {
        LOGGER.info("Scheduled task started at::::::::::::::::::::::::::::::::::::: " + LocalTime.now());
        
        File folder = new File(UPLOAD_DIR + File.separator + "upload");
        LOGGER.info("Folder path: " + folder);
        
        try {
            // 폴더 삭제
            if (Util.deleteFolder(folder)) {
                LOGGER.info("Folder successfully deleted.");
            } else {
                LOGGER.warning("Failed to delete folder.");
            }
            
            // 폴더 재생성
            File uploadDir = new File(UPLOAD_DIR + File.separator + "upload");
            if (!uploadDir.exists()) {
                LOGGER.info("Folder does not exist, creating new folder.");
                if (uploadDir.mkdirs()) {
                    LOGGER.info("Folder successfully created: " + uploadDir);
                } else {
                    LOGGER.warning("Failed to create folder: " + uploadDir);
                }
            }
        } catch (Exception e) {
            LOGGER.severe("Error during folder maintenance: " + e.getMessage());
        }
        
        LOGGER.info("Scheduled task finished at: " + LocalTime.now());

    }
    
}
