package com.gongdaeoppa.demo;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.File;
import java.util.UUID;
@Controller
public class BasicController {
    
    private static final String UPLOAD_DIR = System.getProperty("user.dir") + File.separator + "tomcat" + File.separator + "webapps";
    
    @GetMapping("/del")
    public String del(){
        
        File folder = new File(UPLOAD_DIR + File.separator + "upload");
        System.out.println("폴더 경로: " + folder);
        
        // 폴더 삭제
        if (Util.deleteFolder(folder)) {
            System.out.println("폴더가 성공적으로 삭제되었습니다.");
        } else {
            System.out.println("폴더 삭제에 실패했습니다.");
        }
        
        // 폴더 재생성
        File uploadDir = new File(UPLOAD_DIR + File.separator + "upload");
        if (!uploadDir.exists()) {
            System.out.println("폴더가 존재하지 않아 새로 생성합니다.");
            if (uploadDir.mkdirs()) {
                System.out.println("폴더 생성 성공: " + uploadDir);
            } else {
                System.out.println("폴더 생성 실패: " + uploadDir);
            }
        }
        
        return "redirect:/";
    }
    
    
    
    
}
