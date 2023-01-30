package com.springsecuritydemo.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.apache.poi.EncryptedDocumentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.springsecuritydemo.entity.response.ErrorMsg;
import com.springsecuritydemo.service.SystemSetupService;
import com.springsecuritydemo.service.impl.UserServiceImpl;


@Controller
@RequestMapping(value = "/systemSetup")
public class SystemSetupController {
	
	@Autowired
	private SystemSetupService systemSetupService;
	
	@Autowired
	private UserServiceImpl userServiceImpl;
	
	private String fileErrorMsg = "檔案大小超過限制";
	
	@GetMapping("/")
    public String index(Model model, HttpServletRequest request) {
		model.addAttribute("selectFunction", "SmsOtpLog");
    	return "systemSetup";
    }
    
    /**
	 * 角色資料上傳,excel檔案
	 * @param file
	 * @return
	 * @throws EncryptedDocumentException
	 * @throws IOException
	 */
    @PostMapping("/roleupload")
    public ResponseEntity<ErrorMsg> roleUpload(@RequestParam("uploadFile") MultipartFile file, final Authentication authentication) throws EncryptedDocumentException, IOException {
    	if(file != null && file.getSize() < 104857600) {
    		String respMsg = systemSetupService.apiGLRoleUpload(file, userServiceImpl.getSecurityUserName(authentication));
    		return new ResponseEntity<>(new ErrorMsg(200, respMsg), HttpStatus.OK);
    	} else {
    		throw new EncryptedDocumentException(fileErrorMsg);
    	}
    }
    
    /**
	 * 功能資料上傳,excel檔案
	 * @param file
	 * @return
	 * @throws EncryptedDocumentException
	 * @throws IOException
	 */
    @PostMapping("/functionupload")
    public ResponseEntity<ErrorMsg> functionUpload(@RequestParam("uploadFile") MultipartFile file, final Authentication authentication) throws EncryptedDocumentException, IOException {
    	if(file != null && file.getSize() < 104857600) {
    		String respMsg = systemSetupService.apiglFunctionUpload(file, userServiceImpl.getSecurityUserName(authentication));
    		return new ResponseEntity<>(new ErrorMsg(200, respMsg), HttpStatus.OK);
    	} else {
    		throw new EncryptedDocumentException(fileErrorMsg);
    	}
    }
    
    /**
	 * 使用者資料上傳,excel檔案
	 * @param file
	 * @return
	 * @throws EncryptedDocumentException
	 * @throws IOException
	 */
    @PostMapping("/userupload")
    public ResponseEntity<ErrorMsg> userUpload(@RequestParam("uploadFile") MultipartFile file, final Authentication authentication) throws EncryptedDocumentException, IOException {
    	if(file != null && file.getSize() < 104857600) {
    		String respMsg = systemSetupService.userUpload(file, userServiceImpl.getSecurityUserName(authentication));
    		return new ResponseEntity<>(new ErrorMsg(200, respMsg), HttpStatus.OK);
    	} else {
    		throw new EncryptedDocumentException(fileErrorMsg);
    	}
    }
}
