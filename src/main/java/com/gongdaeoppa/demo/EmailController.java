package com.gongdaeoppa.demo;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequiredArgsConstructor
@RequestMapping("/email")
public class EmailController {
    
    private final EmailSenderService emailSenderService;
    private final EmailMapper emailMapper;
    
    @GetMapping("/send")
    @ResponseBody
    public String sendTestEmail() {
        //final Email email = emailMapper.findByRAND().get();
        //System.out.println(email.getEmail());
        
        //emailSenderService.sendSimpleEmail("@gmail.com", "제목 " + Math.random(), "내용 " + Math.random());
        emailSenderService.sendSimpleEmail("@gmail.com",
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
        return "Email Sent!";
    }
    
}
