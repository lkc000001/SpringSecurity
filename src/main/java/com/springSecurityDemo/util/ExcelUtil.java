package com.springsecuritydemo.util;

import java.io.FileInputStream;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.data.relational.core.conversion.DbActionExecutionException;
import org.springframework.stereotype.Component;

import com.springsecuritydemo.entity.ApiGLFunction;
import com.springsecuritydemo.entity.ApiGLRole;
import com.springsecuritydemo.entity.AppUser;
import com.springsecuritydemo.exception.FilterFormatException;


@Component
public class ExcelUtil {
	
	private static final String EXCEL_XLS = "xls";
    private static final String EXCEL_XLSX = "xlsx";
    
	public Workbook getWorkbook(String path) {
		Workbook wb = null;
		if (path == null)  {
			return null;
		}
		String extString = path.substring(path.lastIndexOf(".")+1);
		try (InputStream is = new FileInputStream(path);) {
			
			if (EXCEL_XLS.equals(extString)) {
				wb = new HSSFWorkbook(is);
			} else if (EXCEL_XLSX.equals(extString)) {
				wb = new XSSFWorkbook(is);
			}
		} catch (Exception e) {
			throw new EncryptedDocumentException(e.getMessage());
		}
		return wb;
	}

	public List<Object> parseExcel(Workbook workbook, int index) {
		List<Object> excelDataList = new ArrayList<>();
		String fileFormatError = "檔案格式錯誤";
		try {
			//遍歷每一個sheet
			int sheets = workbook.getNumberOfSheets();
			if( workbook != null && sheets <= 1 ) {
				for (int sheetNum = 0; sheetNum < sheets; sheetNum++) {
					Sheet sheet = workbook.getSheetAt(sheetNum);
					
					int firstRowNum = sheet.getFirstRowNum();
					Row firstRow = sheet.getRow(firstRowNum);
					if (firstRow != null) {
						int rowStart = firstRowNum + 1;
						int rowEnd = sheet.getPhysicalNumberOfRows();
						String titleStr = convertCellValueToString(sheet.getRow(0).getCell(0));
						for (int rowNum = rowStart; rowNum < rowEnd; rowNum++) {
							Row row = sheet.getRow(rowNum);
							if (row != null) {
								Object excelData = null;
								switch (index) {
								case 1:
									if(titleStr.equals("帳號種類")) {
										excelData = apiGLRoleConvertRowToData(row);
										break;
									} 
									throw new FilterFormatException(fileFormatError, 400);
								case 2:
									if(titleStr.equals("功能名稱")) { 
										excelData = apiglFunctionConvertRowToData(row);
										break;
									}
									throw new FilterFormatException(fileFormatError, 400);
								case 3:
									if(titleStr.equals("account")) {
										excelData = userConvertRowToData(row);
										break;	
									} 
									throw new FilterFormatException(fileFormatError, 400);
								default:
									throw new FilterFormatException(fileFormatError, 400);
									
								}
								if (excelData != null) {
									excelDataList.add(excelData);
								}
							}
						}
					}
				}
			} else {
				throw new FilterFormatException(fileFormatError, 400);
			}
		} catch (Exception e) {
			throw new FilterFormatException(fileFormatError, 400);
		}
		return excelDataList;
	}
	
	private ApiGLRole apiGLRoleConvertRowToData(Row row) {
		ApiGLRole excelData = new ApiGLRole();
		Cell cell;
		int cellNum = 0;
		// 
		cell = row.getCell(cellNum++);
		String roleType = convertCellValueToString(cell);
		String roleTypeNum;
		switch (roleType) {
		case "總行群組":
			roleTypeNum = "1";
			break;
		case "分行群組":
			roleTypeNum = "2";
			break;
		case "資訊部群組":
			roleTypeNum = "3";
			break;
		case "分行OTP發卡行員":
			roleTypeNum = "4";
			break;
		default:
			roleTypeNum = "1";
			
		}
		excelData.setApiglRoleType(roleTypeNum);
		
		cell = row.getCell(cellNum++);
		String roleNumber = convertCellValueToString(cell);
		excelData.setApiglRoleNumber(roleNumber);
		
		cell = row.getCell(cellNum++);
		String roleName = convertCellValueToString(cell);
		excelData.setApiglRoleName(roleName);
		
		cell = row.getCell(cellNum);
		String directions = convertCellValueToString(cell);
		excelData.setApiglRoleDirections(directions);
		
		excelData.setEnabled("1");
		excelData.setCreateTime(new Date());
		return excelData;
		
	}
	
	private ApiGLFunction apiglFunctionConvertRowToData(Row row) {
		ApiGLFunction excelData = new ApiGLFunction();
		Cell cell;
		int cellNum = 0;
		
		cell = row.getCell(cellNum++);
		String apiglFunctionName = convertCellValueToString(cell);
		excelData.setApiglFunctionName(apiglFunctionName);
		
		cell = row.getCell(cellNum++);
		String apiglFunctionShowName = convertCellValueToString(cell);
		excelData.setApiglFunctionShowName(apiglFunctionShowName);
		
		cell = row.getCell(cellNum++);
		String apiglFunctionUrl = convertCellValueToString(cell);
		excelData.setApiglFunctionUrl(apiglFunctionUrl);
		
		cell = row.getCell(cellNum++);
		String apiglFunctionSort = convertCellValueToString(cell);
		excelData.setApiglFunctionSort(Integer.parseInt(apiglFunctionSort));
		
		cell = row.getCell(cellNum++);
		String enabled = convertCellValueToString(cell);
		if(enabled != null && enabled.equals("1")) {
			excelData.setEnabled("1");
		} else {
			excelData.setEnabled("0");
		}
		
		cell = row.getCell(cellNum);
		String type = convertCellValueToString(cell);
		excelData.setType(type);

		excelData.setCreateTime(new Date());
		return excelData;
		
	}
	
	private AppUser userConvertRowToData(Row row) {
		AppUser excelData = new AppUser();
		Cell cell;
		int cellNum = 0;
		// 
		cell = row.getCell(cellNum++);
		String account = convertCellValueToString(cell);
		excelData.setAccount(account);
		
		cell = row.getCell(cellNum++);
		String accountId = convertCellValueToString(cell);
		excelData.setAccountId(Integer.parseInt(accountId));
		
		cell = row.getCell(cellNum++);
		String name = convertCellValueToString(cell);
		excelData.setName(name);
		
		cell = row.getCell(cellNum++);
		String groupName = convertCellValueToString(cell);
		excelData.setGroupName(groupName);
		
		cell = row.getCell(cellNum++);
		String branch = convertCellValueToString(cell);
		excelData.setBranch(branch);
		
		cell = row.getCell(cellNum);
		String enabled = convertCellValueToString(cell);
		if(enabled != null && enabled.equals("1")) {
			excelData.setEnabled("1");
		} else {
			excelData.setEnabled("0");
		}
		
		excelData.setCreateTime(new Date());
		return excelData;
		
	}
	
	public String convertCellValueToString(Cell cell) {
		if (cell == null) {
			return null;
		}
		String returnValue = null;
		switch (cell.getCellType()) {
		case NUMERIC:
			Double doubleValue = cell.getNumericCellValue();
			DecimalFormat df = new DecimalFormat("0");
			returnValue = df.format(doubleValue);
			break;
		case STRING:
			returnValue = cell.getStringCellValue();
			break;
		case BOOLEAN:
			Boolean booleanValue = cell.getBooleanCellValue();
			returnValue = booleanValue.toString();
			break;
		case BLANK:
			break;
		case FORMULA:
			returnValue = cell.getCellFormula();
			break;
		case ERROR:
			break;
		default:
			break;
		}
		return returnValue;
	}
}
