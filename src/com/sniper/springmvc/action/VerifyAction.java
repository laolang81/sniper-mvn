package com.sniper.springmvc.action;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.sniper.springmvc.model.Lottery;
import com.sniper.springmvc.mybatis.service.impl.LotteryService;
import com.sniper.springmvc.utils.LotteryUtil;
import com.sniper.springmvc.utils.ValidateUtil;
import com.sniper.springmvc.utils.Verify;
import com.sniper.springmvc.utils.VerifyCode;

/**
 * 提供图片验证码
 * 
 * @author laolang
 * 
 */
@SessionAttributes("sessionVerifyName")
@Controller
public class VerifyAction {

	public static final String VALIDATECODE = "sessionVerifyName";

	@Resource
	LotteryService lotteryService;

	@ResponseBody
	@RequestMapping("verify")
	public ResponseEntity<byte[]> verify(Map<String, Object> map,
			HttpServletResponse response) throws Exception {
		// 如果开启Hard模式，可以不区分大小写
		String securityCode = VerifyCode.getSecurityCode(5,
				VerifyCode.SecurityCodeLevel.Hard, false).toLowerCase();
		// 获取默认难度和长度的验证码
		// String securityCode = VerifyCode.getSecurityCode();
		// 图片流
		ServletOutputStream outputStream = response.getOutputStream();
		BufferedImage bufferedImage = Verify.createImage(securityCode);
		ImageIO.write(bufferedImage, "jpeg", outputStream);
		// 放入session中
		map.put(VALIDATECODE, securityCode);
		byte[] body = new byte[response.getBufferSize()];
		outputStream.write(body);

		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "image/jpeg");
		headers.add("Pragma", "no-cache");
		headers.add("Expires", "0");

		HttpStatus httpState = HttpStatus.OK;

		ResponseEntity<byte[]> entity = new ResponseEntity<>(body, headers,
				httpState);

		return entity;

	}

	@ResponseBody
	@RequestMapping("dlt")
	public ResponseEntity<byte[]> dltshow(
			Map<String, Object> map,
			@RequestParam(name = "name", required = false, value = "name") String name)
			throws Exception {

		ByteArrayInputStream imageStream = null;
		List<Lottery> lists = LotteryUtil.dlt(10);
		Map<String, Object> params = new HashMap<>();
		params.put("order", "g.num desc");
		params.put("pageOffset", 0);
		params.put("pageSize", 40);
		List<Lottery> lotteries = lotteryService.pageList(params);
		lists.addAll(lotteries);
		// 图片流
		if (ValidateUtil.isValid(name)) {
			if (name.equals("trend")) {
				imageStream = LotteryUtil.dltTrend(lists, 10);
			}
		}
		if (imageStream == null) {
			imageStream = LotteryUtil.dlt(lists, 10);
		}

		byte[] body = new byte[imageStream.available()];
		imageStream.read(body);

		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "image/jpeg");
		headers.add("Pragma", "no-cache");
		headers.add("Expires", "0");

		HttpStatus httpState = HttpStatus.OK;

		ResponseEntity<byte[]> entity = new ResponseEntity<>(body, headers,
				httpState);

		return entity;

	}

}