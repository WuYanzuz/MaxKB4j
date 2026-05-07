package com.asiainfo.oss.service;

import com.asiainfo.common.domain.dto.OssFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

public interface IOssService {

    OssFile getFile(String id);

    String storeFile(MultipartFile file) throws IOException;

    String storeFile(byte[] bytes,String fileName,String contentType);

    InputStream getStream(String fileId) throws IOException;

    OssFile uploadFile(String fileName, byte[] fileBytes);

    byte[] getBytes(String fileId);
}
