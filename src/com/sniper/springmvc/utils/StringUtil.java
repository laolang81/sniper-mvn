package com.sniper.springmvc.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.sniper.springmvc.freemarker.ViewHomeUtils;

public class StringUtil {
	/**
	 * 判断字符是否存在数组之中
	 * 
	 * @param values
	 * @param value
	 * @return
	 */
	public static boolean contains(String[] values, String value) {
		if (ValidateUtil.isValid(values)) {
			for (String s : values) {
				if (s.equals(value)) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * 数组连接
	 * 
	 * @param col
	 * @return
	 */
	public static String arr2Str(Object[] col) {
		String str = "";

		if (ValidateUtil.isValid(col)) {
			for (Object s : col) {
				str = str + s + ",";
			}
			return str.substring(0, str.length() - 1);
		}
		return str;

	}

	/**
	 * 字符串截取
	 * 
	 * @param str
	 * @return
	 */
	public static String getDescString(String str, int limit) {

		if (str != null && str.trim().length() > limit) {
			return str.substring(0, limit);
		}
		return str;
	}

	public static String join(List<Integer> ids) {
		return StringUtils.join(ids, ",");

	}

	public static Object getObject(Object key, Map<Object, Object> map) {

		if (map.containsKey(key)) {
			return map.get(key);
		}

		return "";
	}

	public static boolean isEmpty(byte[] data) {
		return (data == null || data.length == 0);
	}

	/**
	 * 序列化
	 * 
	 * @param object
	 * @return
	 */
	public static byte[] serialize(Object object) {

		byte[] result = null;

		if (object == null) {
			return new byte[0];
		}
		try {
			ByteArrayOutputStream byteStream = new ByteArrayOutputStream(128);
			try {
				if (!(object instanceof Serializable)) {
					throw new IllegalArgumentException(
							StringUtil.class.getSimpleName()
									+ " requires a Serializable payload "
									+ "but received an object of type ["
									+ object.getClass().getName() + "]");
				}
				ObjectOutputStream objectOutputStream = new ObjectOutputStream(
						byteStream);
				objectOutputStream.writeObject(object);
				objectOutputStream.flush();
				objectOutputStream.close();
				result = byteStream.toByteArray();
			} catch (Throwable ex) {
				throw new Exception("Failed to serialize", ex);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return result;
	}

	/**
	 * 反序列化
	 * 
	 * @param bytes
	 * @return
	 */
	public static Object unSerialize(byte[] bytes) {

		Object result = null;

		if (isEmpty(bytes)) {
			return null;
		}

		try {
			ByteArrayInputStream byteStream = new ByteArrayInputStream(bytes);
			try {
				ObjectInputStream objectInputStream = new ObjectInputStream(
						byteStream);
				try {
					result = objectInputStream.readObject();
					objectInputStream.close();
				} catch (ClassNotFoundException ex) {
					throw new Exception("Failed to deserialize object type", ex);
				}
			} catch (Throwable ex) {
				throw new Exception("Failed to deserialize", ex);
			}
		} catch (Exception e) {
		}
		return result;
	}

	/**
	 * 返回新闻的访问地址 ,不不包含域名部分
	 * 
	 * @param date
	 * @param sid
	 * @return
	 */
	public static String getSubjectUrl(int date, String sid) {

		String dateString = ViewHomeUtils.intToDateString(date);
		String weburl = "/public/html/news/201609/380635.html";
		return weburl.replace("201609", dateString).replace("380635", sid);

	}

}