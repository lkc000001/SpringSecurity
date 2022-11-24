package com.springSecurityDemo.service.impl;

import static java.util.stream.Collectors.groupingBy;

import java.text.ParseException;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.springSecurityDemo.entity.response.JSGridResponse;
import com.springSecurityDemo.entity.response.JSGridReturnData;
import com.springSecurityDemo.exception.QueryNoDataException;
import com.springSecurityDemo.util.ValidateUtil;
import com.springSecurityDemo.entity.AppUser;
import com.springSecurityDemo.entity.PermissionResponse;
import com.springSecurityDemo.repositories.UserFunctionRepository;
import com.springSecurityDemo.repositories.UserRepository;
import com.springSecurityDemo.service.UserService;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	UserRepository userRepository;
	
	@Autowired
	ValidateUtil validateUtil;
	
	@Autowired
	HttpSession session;
	
	@Autowired
	UserFunctionRepository userFunctionRepository;
	
	@Override
	public AppUser getSecurityUser(final Authentication authentication) {
		AppUser appUser = (AppUser) session.getAttribute("AppUser");
		if(appUser == null) {
			UserDetails userDetails = null;
			//Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			if(authentication != null) {
				if(!authentication.getPrincipal().equals("anonymousUser")) {
				//System.out.println("authentication: " + authentication.getPrincipal());
				userDetails = (UserDetails) authentication.getPrincipal();
				AppUser userDetail = userRepository.findByAccount(userDetails.getUsername());
				userDetail.setPwd("");
				session.setAttribute("AppUser", userDetail);
				} else {
					new AppUser();
				}
			} else {
				new AppUser();
			}
		} 
		return appUser;
	}
	
	@Override
	public Map<String,List<PermissionResponse>> getFunctions(final Authentication authentication) {
		AppUser appUser = getSecurityUser(authentication);
		Map<String,List<PermissionResponse>> PermissionMap = null;
		if(validateUtil.isNotEmpty(appUser)) {
			Set<PermissionResponse> UserPermissions = userFunctionRepository.queryUserPermission(appUser.getUserId());
			if (validateUtil.isNotEmpty(UserPermissions)) {
	    		if(!appUser.getGroupName().equals("DC")) {
	    			UserPermissions = UserPermissions.stream()
					    			  .filter(u -> u.getPermissionenabled() != null) //過濾UserFunction Enabled的null
									  .filter(u -> u.getPermissionenabled().equals("1")) //過濾UserFunction 沒有權限的功能
				    				  .collect(Collectors.toSet());
	    		}
	    		//分群處理不同類別
	    		PermissionMap = UserPermissions.stream()
								 .sorted(Comparator.comparing(PermissionResponse::getApiglfunctionsort))
								 .collect(groupingBy(PermissionResponse::getType, LinkedHashMap::new, Collectors.toList()));
				
	    	}
		}
		return PermissionMap;
	}

	/**
	 * User Table依查詢條件查詢,依分頁及排序Response
	 * @param ConditionsRequest
	 * @return ResponseEntity
	 * @throws ParseException 
	 */
    @Override
	public ResponseEntity<?> queryUserPermission(AppUser user) throws ParseException {
    	checkData(user);
		List<AppUser> Users = userRepository.queryUserPermission(user.getBranch(), user.getGroupName(), 
				user.getAccountId(), user.getAccount(), user.getName(), user.getEnabled());
		if(Users.size() > 0 ) {
			System.out.println(Users);
			//分頁,排序
			int pageSize = user.getPageSize();
			List<AppUser> result = Users.stream()
				.sorted(userPermissionSort(user.getSortField(), user.getSortOrder()))
				.skip(pageSize * (user.getPageIndex() - 1))
				.limit(pageSize)
				.collect(Collectors.toList());

			return new ResponseEntity<JSGridReturnData<AppUser>>(JSGridResponse.getResponseData(result, Users.size()), HttpStatus.OK);
		} else {
			throw new QueryNoDataException("查無資料!!!", 404);
		}
    	//return new ResponseEntity(HttpStatus.OK);
	}
    
    @Override
    public AppUser findByUserId(Long userId) {
    	return userRepository.findById(userId).get();
    }
   
    @Override
	public String save(AppUser user, String func, final Authentication authentication) {
    	boolean isEnabled = true;
    	if(func.equals("新增")) {
    		user.setCreateTime(new Date());
    		user.setCreateUser(getSecurityUser(authentication).getAccount());
    		AppUser oldUser =  userRepository.findByAccount(user.getAccount());
    		if(validateUtil.isNotEmpty(oldUser)) {
    			isEnabled = false;
    		}
    	} else {
    		AppUser oldUser = findByUserId(user.getUserId());
    		user.setCreateTime(oldUser.getCreateTime());
    		user.setCreateUser(oldUser.getCreateUser());
    		user.setUpdateTime(new Date());
    		user.setUpdateUser(getSecurityUser(authentication).getAccount());
    	}

    	if(validateUtil.isBlank(user.getEnabled())) {
    		user.setEnabled("0");
    	}
    	AppUser userResp = null;
    	if(isEnabled) {
    		userResp = userRepository.save(user);
    	} else {
    		return func + "使用者資料失敗，帳號已存在";
    	}
    	
    	if(validateUtil.isEmpty(userResp)) {
    		return func + "使用者資料失敗";
    	}
    	
    	return func +"使用者資料成功";
	}
    
    /**
	 * 排序欄位設置
	 * @param sortField
	 * @param sortOrder
	 * @return Comparator<ApiGLRole>
	 */
	private Comparator<AppUser> userPermissionSort(String sortField, String sortOrder) {
		switch(sortField) {
		case "branch":
			return sortOrder.equals("asc") ? 
					Comparator.comparing(AppUser::getBranch) : 
						Comparator.comparing(AppUser::getBranch).reversed();
		case "accountid":
			return sortOrder.equals("asc") ? 
					Comparator.comparing(AppUser::getAccountId) : 
						Comparator.comparing(AppUser::getAccountId).reversed();
		case "account":
			return sortOrder.equals("asc") ? 
					Comparator.comparing(AppUser::getAccount) : 
						Comparator.comparing(AppUser::getAccount).reversed();
		case "name":
			return sortOrder.equals("asc") ? 
					Comparator.comparing(AppUser::getName) : 
						Comparator.comparing(AppUser::getName).reversed();	
		case "groupname":
			return sortOrder.equals("asc") ? 
					Comparator.comparing(AppUser::getGroupName) : 
						Comparator.comparing(AppUser::getGroupName).reversed();		
		default:
			return sortOrder.equals("asc") ? 
				Comparator.comparing(AppUser::getUserId) : 
					Comparator.comparing(AppUser::getUserId).reversed();
		}
	}
	
	/**
	 * 判斷是否有無資料,另外處理
	 * @param apiGLRole
	 */
	private void checkData(AppUser user) {
		if(validateUtil.isBlank(user.getSortField())) {
			user.setSortField("sort");
    	}
    	if(validateUtil.isBlank(user.getSortOrder())) {
    		user.setSortOrder("asc");
    	}
    	if(validateUtil.isNotBlank(user.getName())) {
    		user.setName("%" + user.getName() + "%");
    	}
	}
	
	private String getIpAdrress(HttpServletRequest request) {
		String Xip = request.getHeader("X-Real-IP");
		String XFor = request.getHeader("X-Forwarded-For");

		if(validateUtil.isNotEmpty(XFor) && !"unKnown".equalsIgnoreCase(XFor)){
			int index = XFor.indexOf(",");
			if(index != -1){
				return XFor.substring(0,index);
			}else{
				return XFor;
			}
		}
		XFor = Xip;
		if(validateUtil.isNotEmpty(XFor) && !"unKnown".equalsIgnoreCase(XFor)){
			return XFor;
		}
		if (validateUtil.isBlank(XFor) || "unknown".equalsIgnoreCase(XFor)) {
			XFor = request.getHeader("Proxy-Client-IP");
		}
		if (validateUtil.isBlank(XFor) || "unknown".equalsIgnoreCase(XFor)) {
			XFor = request.getHeader("WL-Proxy-Client-IP");
		}
		if (validateUtil.isBlank(XFor) || "unknown".equalsIgnoreCase(XFor)) {
			XFor = request.getHeader("HTTP_CLIENT_IP");
		}
		if (validateUtil.isBlank(XFor) || "unknown".equalsIgnoreCase(XFor)) {
			XFor = request.getHeader("HTTP_X_FORWARDED_FOR");
		}
		if (validateUtil.isBlank(XFor) || "unknown".equalsIgnoreCase(XFor)) {
			XFor = request.getRemoteAddr();
		}
		if (XFor.equals("0:0:0:0:0:0:0:1")) {
			XFor = "127.0.0.1";
		}
		return XFor;
	}
		
}
