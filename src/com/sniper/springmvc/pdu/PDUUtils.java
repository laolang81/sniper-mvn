package com.sniper.springmvc.pdu;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 这是实现一些共有的方法 本文根据 http://www.doc88.com/p-707554537237.html解析 注意程序的编码会影响程序的输出结果
 * 本文是 utf-8格式
 * 
 * @author suzhen
 * 
 */
public class PDUUtils {
	/**
	 * 7-bit专用字符集
	 */
	private static String[] sevenbitdefault = new String[] { "@", "£", "$",
			"¥", "è", "é", "ù", "ì", "ò", "Ç", "\n", "Ø", "ø", "\r", "Å", "å",
			"\u0394", "_", "\u03a6", "\u0393", "\u039b", "\u03a9", "\u03a0",
			"\u03a8", "\u03a3", "\u0398", "\u039e", "&#8364;", "Æ", "æ", "ß",
			"É", " ", "!", "\"", "#", "¤", "%", "&", "\"", "(", ")", "*", "+",
			",", "-", ".", "/", "0", "1", "2", "3", "4", "5", "6", "7", "8",
			"9", ":", ";", "<", "=", ">", "?", "¡", "A", "B", "C", "D", "E",
			"F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R",
			"S", "T", "U", "V", "W", "X", "Y", "Z", "Ä", "Ö", "Ñ", "Ü", "§",
			"¿", "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l",
			"m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y",
			"z", "ä", "ö", "ñ", "ü", "à" };

	// 记录当前操作位置
	private static int cursorIndex = 0;

	/**
	 * 二进制转10进制
	 * 
	 * @param x
	 * @return
	 */
	private static int binToInt(String x) {
		return Integer.valueOf(x, 2).intValue();
	}

	private static void cursorNext(int size) {
		cursorIndex += size;
	}

	/**
	 * 16进制转二进制 1、10进制转16进制，运用辗转相除法，取余数补对应的位数，直到相除结果为0。
	 * 
	 * 2、16进制转10进制，对16进制数的每一位数乘以其对应的16的幂，相加。 16->10
	 * 
	 * @param str
	 * @return
	 */
	private static int hexToNum(String str) {
		return Integer.valueOf(str, 16).intValue();
	}

	/**
	 * 上面的反转函数 10->16
	 * 
	 * @param num
	 * @return
	 */
	private static String numToHex(int num) {
		String s = Integer.toHexString(num);
		for (int i = s.length(); i < 2; i++) {
			s = "0" + s;
		}
		return s;
	}

	/**
	 * 根据字码获取字符串
	 * 
	 * @param code
	 * @return
	 */
	private static String fromCharCode(int code) {
		char c = (char) code;
		return String.valueOf(c).toString();
	}

	/**
	 * 吧数字转成二进制 长度必须是l，不够的前边用0补齐,前边加的0不影响起还原的结果
	 * 
	 * @param x
	 * @param l
	 * @return
	 */
	private static String intToBin(int x, int l) {
		String b = Integer.toBinaryString(x);
		for (int i = b.length(); i < l; i++) {
			b = "0" + b;
		}
		return b;
	}

	/**
	 * 吧16进制转成字符串 感觉就像每隔两个数字就把前后颠倒
	 * 
	 * @return
	 */
	private static String hexToString(String inp) {

		StringBuffer out = new StringBuffer();
		for (int i = 0; i < inp.length();) {
			String temp = inp.substring(i, i + 2);
			out.append(temp.charAt(1));
			out.append(temp.charAt(0));
			i += 2;
		}
		return out.toString();
	}

	/**
	 * 解码发送时间，到达 解密时间戳
	 * 
	 * @param scts
	 * @return
	 */
	private static String getSCTS(String scts) {
		scts = hexToString(scts);
		String year = scts.substring(0, 2);
		String month = scts.substring(2, 4);
		String day = scts.substring(4, 6);
		String hours = scts.substring(6, 8);
		String mintes = scts.substring(8, 10);
		String seconds = scts.substring(10, 12);
		// 组装数据
		StringBuffer sct = new StringBuffer(year);
		sct.append("/");
		sct.append(month);
		sct.append("/");
		sct.append(day);
		sct.append(" ");
		sct.append(hours);
		sct.append(":");
		sct.append(mintes);
		sct.append(":");
		sct.append(seconds);

		return sct.toString();
	}

	/**
	 * 生成一个时间格式
	 * 
	 * @param scts
	 * @return
	 */
	private static String makeSCTS() {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyMMddhhmmss");
		return hexToString(dateFormat.format(new Date()) + "23");
	}

	/**
	 * 7-bit编码 马丹下面下的什么意思，不明白
	 * 
	 * @param msg
	 * @param size
	 * @return
	 */
	private static String decodeMsg7(String msg, int size) {

		Map<Integer, String> resetArray = new HashMap<>();
		Map<Integer, String> septetsArray = new HashMap<>();

		int s = 1;
		int count = 0;
		// 信息存放
		StringBuffer messge = new StringBuffer();
		for (int i = 0; i < msg.length(); i += 2) {
			// 截取2个字符,结果是10进制
			int hex = hexToNum(msg.substring(i, i + 2));

			// 转成字符转成int，再转二进制,这里不知道为什么要凑齐8位,
			String temp = intToBin(hex, 8);
			// 下面是计算当前字符在 7-bit字符集中的位置，
			// 截取字符串0开始，每次截取位置递增，长度是8的余数
			// 这个变量使用了一次就是当时7的余数是,且不是第一个
			resetArray.put(count, temp.substring(0, s % 8));
			// 这个又是干嘛的,每次截取字符串送8的余数开始，早结尾
			septetsArray.put(count, temp.substring(s % 8));
			s++;
			count++;
			if (s == 8) {
				s = 1;
			}
		}

		for (int j = 0; j < resetArray.size(); j++) {
			// 放位置是7的倍数时
			if (j % 7 == 0) {
				if (j != 0) {
					messge.append(sevenbitdefault[binToInt(resetArray
							.get(j - 1))]);
				}
				messge.append(sevenbitdefault[binToInt(septetsArray.get(j))]);
			} else {
				messge.append(sevenbitdefault[binToInt(septetsArray.get(j)
						+ resetArray.get(j - 1))]);
			}
		}

		return messge.toString();
	}

	/**
	 * 8-bit编码
	 * 
	 * @param msg
	 * @return
	 */
	private static String decodeMsg8(String msg) {

		StringBuffer buffer = new StringBuffer();
		for (int i = 0; i < msg.length(); i += 2) {
			String hex = msg.substring(i, i + 2);
			int hexI = hexToNum(hex);

			// hexToNum(msg.substring(i, i + 4)),可以得到同样的结果
			// System.out.println(hexI * 256 + hexI2);
			buffer.append(fromCharCode(hexI));
		}
		return buffer.toString();
	}

	/**
	 * ucs2 16编码
	 * 
	 * @param msg
	 * @return
	 */
	private static String decodeMsgUCS2(String msg) {
		StringBuffer buffer = new StringBuffer();
		for (int i = 0; i < msg.length(); i += 4) {
			String hex = msg.substring(i, i + 2);
			String hex2 = msg.substring(i + 2, i + 4);
			int hexI = hexToNum(hex);
			int hexI2 = hexToNum(hex2);

			// hexToNum(msg.substring(i, i + 4)),可以得到同样的结果
			buffer.append(fromCharCode(hexI * 256 + hexI2));
		}
		return buffer.toString();

	}

	/**
	 * 得到7Bit的位置
	 * 
	 * @param a
	 * @return
	 */
	public static int get7Bit(char a) {
		for (int i = 0; i < sevenbitdefault.length; i++) {
			if (String.valueOf(a).equals(sevenbitdefault[i])) {
				return i;
			}
		}
		return 0;
	}

	/**
	 * PDU短信解码解码
	 * 
	 * @param pdu
	 * @return
	 */
	public static Map<String, String> decode(String pdu) {
		// 每次操作的游标
		Map<String, String> decodeMsg = new HashMap<>();

		// 解密SCA的长度
		int SMSC_lengthInfo = hexToNum(pdu.substring(0, 2));
		String SMSC_info = pdu.substring(2, 2 + SMSC_lengthInfo * 2);
		// 获取scaType, 91,81,00是GSM协议
		String SMSC_typeOfAddress = SMSC_info.substring(0, 2);
		String SMSC_number = SMSC_info.substring(2);
		if (SMSC_lengthInfo != 0) {
			// 解析地址
			SMSC_number = hexToString(SMSC_number);
			// 去除补位F
			if (SMSC_number.toUpperCase().lastIndexOf("F") != -1) {
				SMSC_number = SMSC_number
						.substring(0, SMSC_number.length() - 1);
			}
			if (SMSC_typeOfAddress.equals("91")) {
				SMSC_number = "+" + SMSC_number;
			}
			decodeMsg.put("smsc", SMSC_number);
		}
		// 解析PDU type
		// 记录当前的游标
		cursorNext(SMSC_lengthInfo * 2 + 2);
		// 基本参数(TP-MTI/VFP)
		String fristOctet_SMSDeleivery = pdu.substring(cursorIndex,
				cursorIndex + 2);
		// 根据这2个字符串解析数pdu属于那种格式
		// System.out.println("确实是否有时间:" + fristOctet_SMSDeleivery);
		cursorNext(2);
		// 这个版本没有时间
		if ((hexToNum(fristOctet_SMSDeleivery) & 0x03) == 1) {
			// TODO 不同的地方一
			// 解析OA短信源发手机地址,如果等于0的话下面是没有的
			int messageReference = hexToNum(pdu.substring(cursorIndex,
					cursorIndex + 2));
			decodeMsg.put("tp_message_reference", messageReference + "");
			cursorNext(2);
			// dec的长度
			int senderAddressLength = hexToNum(pdu.substring(cursorIndex,
					cursorIndex + 2));
			if (senderAddressLength % 2 != 0) {
				senderAddressLength += 1;
			}
			cursorNext(2);
			String senderTypeOfAddress = pdu.substring(cursorIndex,
					cursorIndex + 2);
			cursorNext(2);

			String senderNumber = pdu.substring(cursorIndex, cursorIndex
					+ senderAddressLength);
			cursorNext(senderAddressLength);
			senderNumber = hexToString(senderNumber);
			if (senderNumber.toUpperCase().lastIndexOf("F") != -1) {
				senderNumber = senderNumber.substring(0,
						senderNumber.length() - 1);
			}
			if (senderTypeOfAddress.equals("91")) {
				senderNumber = "+" + senderNumber;
			}
			decodeMsg.put("sender_number", senderNumber);
			// 协议标志 00是所有服务器商都支持的建议采用00H
			String tp_pid = pdu.substring(cursorIndex, cursorIndex + 2);
			decodeMsg.put("tp_pid", tp_pid);
			cursorNext(2);
			// DCS数据编码方法
			// 表示数据编码方法和消息类别。一般为00H默认7位编码等级号0。UCS2编码0等
			// 级为08H可以传输中文。
			// pid如果是00H这里基本都是 07,这里解析数据好汉bit，class类型,这里还没写
			String tp_dcs = pdu.substring(cursorIndex, cursorIndex + 2);
			cursorNext(2);
			decodeMsg.put("tp_dcs", tp_dcs);
			// 这里我不懂什么意思,不过具体操作是一个没有时间戳一个有
			// 根据tp_dcs获取pdu编码
			// // TODO 不同的地方二
			// System.out.println(pdu.substring(cursorIndex, cursorIndex + 2));
			int validityPeriod = hexToNum(pdu.substring(cursorIndex,
					cursorIndex + 2));
			cursorNext(2);
			decodeMsg.put("tp_scts", validityPeriod + "");

			int msgLengthInt = hexToNum(pdu.substring(cursorIndex,
					cursorIndex + 2));
			cursorNext(2);
			String message = pdu.substring(cursorIndex);
			cursorNext(msgLengthInt);
			switch (tp_dcs) {
			case "00":
				// 英文编码
				// 表示7-bit编码
				message = decodeMsg7(message, msgLengthInt);
				break;
			case "04":
				// 这里包含图片，和铃声
				// 表示8-bit编码
				message = decodeMsg8(message);
				break;
			case "08":
				// 汉子编码
				// 表示 UCS2编码
				message = decodeMsgUCS2(message);
				// msgLengthInt是字节数，换成字符要除以2
				msgLengthInt = msgLengthInt / 2;
				break;

			default:
				break;
			}
			// message = message.substring(0, msgLengthInt);
			decodeMsg.put("send_message", message);
			decodeMsg.put("send_message_length", "" + msgLengthInt);
		} else if ((hexToNum(fristOctet_SMSDeleivery) & 0x03) == 0) {
			// dec的长度
			int senderAddressLength = hexToNum(pdu.substring(cursorIndex,
					cursorIndex + 2));
			if (senderAddressLength % 2 != 0) {
				senderAddressLength += 1;
			}
			cursorNext(2);
			String senderTypeOfAddress = pdu.substring(cursorIndex,
					cursorIndex + 2);
			cursorNext(2);

			String senderNumber = pdu.substring(cursorIndex, cursorIndex
					+ senderAddressLength);
			cursorNext(senderAddressLength);
			senderNumber = hexToString(senderNumber);
			if (senderNumber.toUpperCase().lastIndexOf("F") != -1) {
				senderNumber = senderNumber.substring(0,
						senderNumber.length() - 1);
			}
			if (senderTypeOfAddress.equals("91")) {
				senderNumber = "+" + senderNumber;
			}
			decodeMsg.put("sender_number", senderNumber);
			// 协议标志 00是所有服务器商都支持的建议采用00H
			String tp_pid = pdu.substring(cursorIndex, cursorIndex + 2);
			decodeMsg.put("tp_pid", tp_pid);
			cursorNext(2);
			String tp_dcs = pdu.substring(cursorIndex, cursorIndex + 2);
			cursorNext(2);
			decodeMsg.put("tp_dcs", tp_dcs);
			// 获取SCTS服务中心时间戳,用户短信达到时间
			String scts = pdu.substring(cursorIndex, cursorIndex + 14);
			scts = getSCTS(scts);
			decodeMsg.put("tp_scts", scts);
			cursorNext(14);
			// 解析UDL用户数据长度
			int msgLengthInt = hexToNum(pdu.substring(cursorIndex,
					cursorIndex + 2));
			cursorNext(2);
			String message = pdu.substring(cursorIndex);
			switch (tp_dcs) {
			case "00":
				// 英文编码,必须全部是英文
				// 表示7-bit编码
				message = decodeMsg7(message, msgLengthInt);
				break;
			case "04":
				// 这里包含图片，和铃声
				// 表示8-bit编码
				message = decodeMsg8(message);
				break;
			case "08":
				// 汉子编码
				// 表示 UCS2编码
				message = decodeMsgUCS2(message);
				// msgLengthInt是字节数，换成字符要除以2
				msgLengthInt = msgLengthInt / 2;
				break;

			default:
				break;
			}
			message = message.substring(0, msgLengthInt);
			decodeMsg.put("send_message", message);
			decodeMsg.put("send_message_length", "" + msgLengthInt);
		}
		// 重新设置游标
		cursorIndex = 0;
		return decodeMsg;

	}

	/**
	 * 加密
	 * 
	 * @param pdu
	 *            发送内容
	 * @param phoneNumber
	 *            发送号码
	 * @param smscNumber
	 *            服务中心号码
	 * @param byteSize
	 *            bit类型
	 * @param sctsValue
	 *            根据和 0x03的比较判断时间格式 ==1表示没有时间，==0表示正常时间格式
	 * @return 加密时候的字符串
	 */
	public static String encode(String msg, String phoneNumber,
			String smscNumber, int byteSize, String sctsValue) {

		// PDU编码开始
		// 结构 SCA 服务中心号码,PDU-type 1字节 , MR 1, DA 2-12字节, PID 1,
		// DCS1,VP服务中心时间戳,UDL用户数据长度,UD用户数据
		int pduLength = msg.length();
		String octetFirst = "";
		String octetSecond = "";
		StringBuffer output = new StringBuffer();

		// SCA设置
		String SMSC_INFO_LENGTH = "0";
		int SMSC_LENGTH = 0;
		String SMSC_NUMBER_FORMAT = "";
		String SMSC = "";
		// smsc组装
		if (!smscNumber.equals("0")) {
			SMSC_NUMBER_FORMAT = "92";
			if (smscNumber.startsWith("+")) {
				SMSC_NUMBER_FORMAT = "91";
				// 去除+
				smscNumber = smscNumber.substring(1);
			} else if (!smscNumber.startsWith("0")) {
				SMSC_NUMBER_FORMAT = "91";
			}

			if (smscNumber.length() % 2 != 0) {
				smscNumber += "F";
			}

			SMSC = hexToString(smscNumber);
			SMSC_LENGTH = (SMSC_NUMBER_FORMAT + SMSC).length() / 2;
			SMSC_INFO_LENGTH = SMSC_LENGTH + "";
		}
		// 如果长度不够10，前边加个0
		if (SMSC_LENGTH < 10) {
			SMSC_INFO_LENGTH = "0" + SMSC_INFO_LENGTH;
		}
		// PDU-11， MR-00
		// pdu一般都是 11H,这里确定其读取的形式，一个有时间，一个没有时间 04是有时间的值
		String PDU = "04";
		// 短消息参考
		String MR = "00";
		// 服务时间戳 AA
		// 如果是绝对时间 即VPF 11B则VP区和SCTS相同。
		String SCTS = "AA";
		if (!sctsValue.equals("")) {
			PDU = sctsValue;
		}
		if ((hexToNum(PDU) & 0x03) == 1) {
			// 填充2个字符串
			MR = "00";
			// 电话号码的长度
			// 非合法时间表示方式
		} else if ((hexToNum(PDU) & 0x03) == 0) {
			// 后面直接跟着电话号码的长度
			MR = "";
			// 完整时间戳
			SCTS = makeSCTS();
		}

		// System.out.println(SCTS);

		// DA设置，目的地手机号码
		// 发送号码组装
		String reiver_number_format = "92";
		if (phoneNumber.startsWith("+")) {
			reiver_number_format = "91";
			phoneNumber = phoneNumber.substring(1);
		} else if (!phoneNumber.startsWith("0")) {
			reiver_number_format = "91";
		}
		// 电话长度的10进制
		String reiver_number_length = numToHex(phoneNumber.length());
		if (phoneNumber.length() % 2 != 0) {
			phoneNumber += "F";
		}

		String reiver_number = hexToString(phoneNumber);
		// pid设置,建议00
		String PID = "00";
		// DCS设置数据编码方式 00表示 7-bit, 04表示 8bit, 08表示ucs2
		String DATA_ENCODING = "00";

		// 用户发送字符串的长度
		String userDataSize = "";

		switch (byteSize) {
		case 8:
			userDataSize = numToHex(pduLength);
			DATA_ENCODING = "04";
			int currentByte = 0;
			for (int i = 0; i < pduLength; i++) {
				currentByte = msg.codePointAt(i);
				output.append(numToHex(currentByte));
			}
			break;
		case 16:
			userDataSize = numToHex(pduLength * 2);
			DATA_ENCODING = "08";
			int currentByte16 = 0;
			for (int i = 0; i < pduLength; i++) {
				currentByte16 = msg.codePointAt(i);
				// 下面是转16进制
				// 0x是16进制标志，ff00表示65280， ff表示255
				// a & 0xff 会把a变成正值，byte--->char 都是这样转的
				String hex = (numToHex((currentByte16 & 0xff00) >> 8) + numToHex(currentByte16 & 0xff));
				output.append(hex);
			}
			break;
		case 7:
			userDataSize = numToHex(pduLength);
			for (int i = 0; i <= pduLength; i++) {
				if (i == pduLength) {
					if (!octetSecond.equals("")) {
						output.append(numToHex(binToInt(octetSecond)));
					}
					break;
				}

				String current = intToBin(get7Bit(msg.charAt(i)), 7);
				String currentOctet = "";

				if (i != 0 && i % 8 != 0) {
					octetFirst = current.substring(7 - i % 8);
					currentOctet = octetFirst + octetSecond;

					output.append(numToHex(binToInt(currentOctet)));
					octetSecond = current.substring(0, 7 - i % 8);
				} else {
					octetSecond = current.substring(0, 7 - i % 8);
				}
			}
			break;

		default:
			break;
		}

		StringBuffer pduBuffer = new StringBuffer();
		// sca部分
		pduBuffer.append(SMSC_INFO_LENGTH);

		pduBuffer.append(SMSC_NUMBER_FORMAT);
		pduBuffer.append(SMSC);
		// pid
		pduBuffer.append(PDU);
		pduBuffer.append(MR);
		// dsc DA操作
		pduBuffer.append(reiver_number_length);
		// vp
		pduBuffer.append(reiver_number_format);
		pduBuffer.append(reiver_number);
		// PID
		pduBuffer.append(PID);
		// DCS
		pduBuffer.append(DATA_ENCODING);
		// VP时间戳
		pduBuffer.append(SCTS);
		pduBuffer.append(userDataSize);
		pduBuffer.append(output);
		// String at = "AT+GMGW=" + (pdu.length() / 2 - SMSC_LENGTH - 1);
		return pduBuffer.toString();
	}

	public static void main(String[] args) {

		String encodePdu = "0891683108501305F0040D91683107359122F9000861907210818423208FD9662F621176846D4B8BD577ED4FE1FF0C662F54265DF27ECF65365230FF1F";
		System.out.println(PDUUtils.decode(encodePdu));
		System.out.println(encodePdu);
		String decode = PDUUtils.encode("这是我的测试短信，是否已经收到？", "+861370531922",
				"+8613800531500", 16, "");
		System.out.println(decode);
		System.out.println(PDUUtils.decode(decode));

	}

}
