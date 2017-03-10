package com.sniper.springmvc.freemarker;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.sniper.springmvc.utils.ValidateUtil;

public class ViewHomeUtils {

	public static String intToDateString(int dateInt) {

		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		if (dateInt == 0) {
			return dateFormat.format(new Date());
		}
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(dateInt * 1000l);
		return dateFormat.format(calendar.getTime());
	}

	public static int getTimeMillis(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		int result = (int) (calendar.getTimeInMillis() / 1000l);
		return result;
	}

	public static int getTimeMillis(String date) {

		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		try {
			dateFormat.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		Calendar calendar = dateFormat.getCalendar();
		int result = (int) (calendar.getTimeInMillis() / 1000l);
		return result;
	}

	public static Date intToDate(int dateInt) {
		if (dateInt == 0) {
			return new Date();
		}

		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(dateInt * 1000l);
		return calendar.getTime();
	}

	public static Date intToDate(long dateInt) {
		if (dateInt == 0) {
			return new Date();
		}

		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(dateInt * 1000l);
		return calendar.getTime();
	}

	public static String image(String img) {
		if (ValidateUtil.isValid(img)) {
			if (img.startsWith("/")) {
				return img;
			} else {
				return "/public/attachments/" + img;
			}
		}
		return "";
	}

	public static String substr(String str, int limit) {
		if (str.length() > limit) {
			return str.substring(0, limit);
		}
		return str;
	}

	public static void main(String[] args) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(1433388531 * 1000l);
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		System.out.println(dateFormat.format(calendar.getTime()));

		System.out.println("yyyy-MM-dd".substring(0, 7).replace("_", ""));
	}

}
