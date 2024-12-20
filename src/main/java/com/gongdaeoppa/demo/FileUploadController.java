package com.gongdaeoppa.demo;

import com.gongdaeoppa.demo.FileUtils;
import org.imgscalr.Scalr;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.apache.commons.io.IOUtils;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.*;

@RestController
@RequestMapping("/api/images")
public class FileUploadController {
    
    
    private static String uploadPath = System.getProperty("user.dir");
    private static String uploadDirectPath = File.separator + "tomcat" + File.separator + "webapps" + File.separator + "upload" + File.separator;
    private static final String UPLOAD_DIR = uploadPath + uploadDirectPath;
    
    
    public void saveJpgWithQuality(BufferedImage image, File outputFile, float quality) throws IOException {
        // 품질 값은 0.0 (최저 품질) ~ 1.0 (최고 품질) 사이여야 함
        if (quality < 0.0f || quality > 1.0f) {
            throw new IllegalArgumentException("Quality must be between 0.0 and 1.0");
        }
        
        // ImageWriter 찾기
        ImageWriter jpgWriter = ImageIO.getImageWritersByFormatName("jpg").next();
        
        // OutputStream 생성
        try (ImageOutputStream ios = ImageIO.createImageOutputStream(new FileOutputStream(outputFile))) {
            jpgWriter.setOutput(ios);
            
            // 압축 품질 설정
            ImageWriteParam param = jpgWriter.getDefaultWriteParam();
            if (param.canWriteCompressed()) {
                param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
                param.setCompressionQuality(quality); // 품질 설정 (0.1~1.0)
            }
            
            // 이미지 쓰기
            jpgWriter.write(null, new javax.imageio.IIOImage(image, null, null), param);
        } finally {
            jpgWriter.dispose();
        }
        
        
    }
    
    @PostMapping("/upload")
    public ResponseEntity<Map<String, Object>> uploadImages(@RequestParam("files") List<MultipartFile> files,
                                                            @RequestParam("width") Integer width,
                                                            @RequestParam("quality") String quality) {
        Map<String, Object> data = new HashMap<>();
        List<String> uploadedFiles = new ArrayList<>();
        
        try {
            // 1. 품질 값 변환
            float qualityValue;
            try {
                qualityValue = Float.parseFloat(quality);
                if (qualityValue < 0.0f || qualityValue > 1.0f) {
                    throw new IllegalArgumentException("Quality must be between 0.0 and 1.0");
                }
            } catch (NumberFormatException e) {
                data.put("error", "Invalid quality value. Please provide a number between 0.0 and 1.0.");
                return ResponseEntity.badRequest().body(data);
            }
            
            // 2. 이미지 처리
            for (MultipartFile file : files) {
                // 2.1 파일 이름 확인
                String originalFileName = file.getOriginalFilename();
                if (originalFileName == null || originalFileName.isEmpty()) {
                    data.put("error", "Invalid file name");
                    return ResponseEntity.badRequest().body(data);
                }
                
                // 2.2 파일 저장 디렉토리 확인
                File uploadDir = new File(UPLOAD_DIR);
                if (!uploadDir.exists()) {
                    uploadDir.mkdirs();
                }
                
                // 2.3 원본 파일 저장
                File originalFile = new File(UPLOAD_DIR + originalFileName);
                file.transferTo(originalFile);
                
                // 2.4 원본 이미지 읽기
                BufferedImage originalImage = ImageIO.read(originalFile);
                
                // 2.5 리사이즈 처리
                BufferedImage resizedImage = resizeImage(originalImage, width);
                
                // 2.6 리사이즈된 이미지를 JPG로 저장
                String resizedFileName = UUID.randomUUID() + ".jpg"; // 고유 이름 생성
                File resizedFile = new File(UPLOAD_DIR + resizedFileName);
                
                // 2.7 품질 설정과 함께 저장
                saveJpgWithQuality(resizedImage, resizedFile, qualityValue);
                
                String url = "/api/images/data/" + resizedFileName;
                uploadedFiles.add(url);  // 업로드된 파일의 URL을 리스트에 추가
                
                
                new File(UPLOAD_DIR + originalFileName).delete();
                
            }
            
            // 3. 응답 데이터 생성
            data.put("src", uploadedFiles); // 업로드된 이미지들의 경로
            data.put("message", "Images uploaded and resized successfully.");
            
            return ResponseEntity.ok(data);
            
        } catch (Exception e) {
            e.printStackTrace();
            data.put("error", "Failed to resize images.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(data);
        }
    }
    
    
    private BufferedImage resizeImage(BufferedImage originalImage, Integer width) {
        if (width == null || width <= 0) {
            throw new IllegalArgumentException("Width must be a positive integer.");
        }
        
        // imgscalr 라이브러리 사용: 폭을 기준으로 비율 유지 리사이즈
        return Scalr.resize(originalImage, Scalr.Method.QUALITY, Scalr.Mode.FIT_TO_WIDTH, width);
    }
    
    
    @GetMapping("/data/{fileName}")
    public ResponseEntity<byte[]> displayFile(@PathVariable("fileName") String fileName) throws Exception {
        //System.out.println("Util.decode(q) = " + Util.decode(param));
        
        InputStream in = null;
        try {
            String formatName = FileUtils.getFileExtension(fileName);
            MediaType mType = FileUtils.getMediaType(formatName);
            HttpHeaders headers = new HttpHeaders();
            
            //String updir = isdirect ? uploadDirectPath : uploadPath;
            File file = new File(UPLOAD_DIR + fileName);
            //System.out.println("file.exists() = " + file.exists());
            //logger.info("exists={}", file.exists());
            if (!file.exists())
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            
            in = new FileInputStream(file);
            
            if (mType != null) {
                headers.setContentType(mType);
                
            } else {
                
                headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
                String dsp = new String(fileName.getBytes("UTF-8"), "ISO-8859-1");
                //logger.info("dsp={}", dsp);
                headers.add("Content-Disposition",
                        "attachment; filename=\"" + dsp + "\"" );
            }
            
            return new ResponseEntity<>(IOUtils.toByteArray(in), headers, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } finally {
            if (in != null)
                in.close();
        }
        
    }
}