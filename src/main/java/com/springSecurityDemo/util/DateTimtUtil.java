package com.springsecuritydemo.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.regex.Pattern;

import org.springframework.stereotype.Component;

@Component
public class DateTimtUtil {
	
	private static final TimeZone TIME_ZONE = TimeZone.getTimeZone("Asia/Taipei");
	
	//取得現在日期
	public Date getNowDate() {
        Calendar cSchedStartCal = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
        cSchedStartCal.setTimeZone(TIME_ZONE);
        return cSchedStartCal.getTime();
    }
	
	public Date formatStrToDate(String dateStr) throws ParseException {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
		return dateFormat.parse(dateStr);
	}
	
	public String formatDateToStr(Date dateStr, String format) {
		SimpleDateFormat dateFormat = new SimpleDateFormat(format);
		return dateFormat.format(dateStr);
	}
	

	public boolean isDate(String date,int index) {
		String rexp = "";
		switch (index) {
		case 1:
			rexp = "^\\d{4}/\\d{2}/\\d{2}$";
			break;
		default:
			rexp = "";
		}
		
		Pattern pattern = Pattern.compile(rexp);
        return pattern.matcher(date).matches();
    }
	
	public boolean checkTimeFormat(String startTime, String endTime) {
        
		if(!isTime(startTime) || !isTime(endTime)) {
			return false;
		}
		
		if(Integer.parseInt(startTime.replace(":", "")) > Integer.parseInt(endTime.replace(":", ""))) {
			return false;
		}
        
        return true;
    }
	
	public boolean isTime(String time) {
        
		String[] times = time.split(":");
		
        if (times.length != 2) {
        	return false;
        }
        try {
        	if (Integer.parseInt(times[0]) > 23 || Integer.parseInt(times[0]) < 0 || 
        		Integer.parseInt(times[1]) > 59 || Integer.parseInt(times[1]) < 0) {
        		return false;
        	}
		} catch (Exception e) {
			return false;
		}
        return true;
    }

}
