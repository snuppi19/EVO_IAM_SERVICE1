package com.mtran.mvc.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface UploadImageFileService {
    String uploadImageFile(MultipartFile file) throws IOException;
}
