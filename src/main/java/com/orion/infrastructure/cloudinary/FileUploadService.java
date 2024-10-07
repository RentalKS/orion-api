package com.orion.infrastructure.cloudinary;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.util.Map;

import static com.orion.util.Converter.converter;

@Service
public class FileUploadService {

    @Autowired
    private Cloudinary cloudinary;

    public String uploadFile(MultipartFile file) throws Exception {
        try {
            Map<String, Object> uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());
            return (String) uploadResult.get("url"); // Returns the URL of the uploaded file
        } catch (RuntimeException e) {
            throw new RuntimeException("Cloudinary upload file error process", e.getCause());
        }
    }

    public String setFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return null;
        }
        try {
            return uploadFile(file);
        } catch (Exception e) {
            throw new RuntimeException("Error uploading image");
        }
    }

    public String uploadSignatureToCloudinary(String paymentSignature) {
        try {
            MultipartFile signature = converter(paymentSignature);
            return setFile(signature);
        } catch (Exception e) {
            throw new RuntimeException("Error uploading payment signature: " + e.getMessage());
        }
    }
}
