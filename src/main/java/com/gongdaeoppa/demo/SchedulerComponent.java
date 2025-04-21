package com.gongdaeoppa.demo;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.File;
import java.time.LocalTime;
import java.util.logging.Logger;

@Component
@RequiredArgsConstructor
public class SchedulerComponent {
    
    private final EmailSenderService emailSenderService;
    private final EmailMapper emailMapper;
    
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
    
    
    @Scheduled(cron = "* * * * * *")
    public void sendEmail() {
        LOGGER.info("Scheduled task started at::::::::::::::::::::::::::::::::::::: " + LocalTime.now());

        final Email email = emailMapper.findByRAND().get();
        //System.out.println(email.getEmail());

        emailSenderService.sendSimpleEmail(email.getEmail(),
                "[광고] 여러분의 추억을 공유해 주세요! - https://한컴타자연습.kr",
                "추억 속 타자 연습, 다시 시작해볼까요?\n" +
                        "\n" +
                        "\uD83D\uDCCC 옛 감성을 느끼고 싶은 분들\n" +
                        "\uD83D\uDCCC 기본적인 타자 실력을 기르고 싶은 초보자\n" +
                        "\uD83D\uDCCC 아이들에게 안전한 환경에서 타자 연습을 시키고 싶은 부모님\n" +
                        "\n" +
                        "1990년대 컴퓨터를 사용하셨던 분이라면 한 번쯤 접해보셨을 한컴타자연습!\n" +
                        "익숙한 화면과 친숙한 인터페이스로 그 시절의 향수를 다시 한번 느껴보세요.\n" +
                        "\n" +
                        "\uD83D\uDD39 재미있게 실력 향상 – 초보자도 쉽게 따라 할 수 있는 연습 모드\n" +
                        "\uD83D\uDD39 추억 소환! – 90년대 감성 그대로, 향수를 자극하는 디자인과 구성\n" +
                        "\uD83D\uDD39 안심하고 연습 – 아이들도 안전하게 사용할 수 있는 학습 환경\n" +
                        "\n" +
                        "\uD83D\uDCE2 지금 바로 한컴타자연습을 만나보세요!\n" +
                        "\uD83D\uDD17 인터넷 주소창에 한글로 입력: https://한컴타자연습.kr\n" +
                        "\n" +
                        "오랜만에 키보드를 두드리며, 그때 그 시절을 떠올려 보세요!\n\n" + Math.random());

        LOGGER.info("Scheduled task finished at: " + LocalTime.now());

    }
    
}
