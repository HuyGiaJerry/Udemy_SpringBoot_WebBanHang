package com.project.shopapp.helpers.file;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Component
public class FileHelper {
    public String storeFile(MultipartFile file) throws IOException
    {
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        // Thêm UUID vào trước tên File để có tên duy nhất
        String uniqueFileName = UUID.randomUUID().toString()+"_"+fileName;
        // Đường dẫn thư mục mà bạn muốn lưu File
        java.nio.file.Path uploadDir = Paths.get("uploads");
        // Kiểm tra và tạo thư mục nếu nó không tồn tại
        if(!Files.exists(uploadDir)){
            Files.createDirectory(uploadDir);
        }
        // Đường dẫn đầy đủ đến File
        java.nio.file.Path destination = Paths.get(uploadDir.toString(), uniqueFileName);
        // Sao chép vào thư mục đích
        Files.copy(file.getInputStream(), destination, StandardCopyOption.REPLACE_EXISTING);
        return uniqueFileName;
    }

    public boolean checkSize(MultipartFile file)
    {
       return (file.getSize() > 10 * 1024 * 1024) ? true : false;
    }
    public boolean checkContentType(MultipartFile file){
        String contentType = file.getContentType();
        return (contentType == null || !contentType.startsWith("image/")) ? true : false;
    }

}
