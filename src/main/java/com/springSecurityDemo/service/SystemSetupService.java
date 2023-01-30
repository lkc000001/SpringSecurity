package com.springsecuritydemo.service;

import java.io.IOException;

import org.apache.poi.EncryptedDocumentException;
import org.springframework.web.multipart.MultipartFile;

public interface SystemSetupService {
	
	String apiGLRoleUpload(MultipartFile file, String userName) throws EncryptedDocumentException, IOException;
	
	String apiglFunctionUpload(MultipartFile file, String userName) throws EncryptedDocumentException, IOException;

	String userUpload(MultipartFile file, String userName) throws EncryptedDocumentException, IOException;
}
