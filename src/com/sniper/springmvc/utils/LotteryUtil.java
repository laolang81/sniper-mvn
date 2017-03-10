package com.sniper.springmvc.utils;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.imageio.ImageIO;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.sniper.springmvc.model.Lottery;
import com.sniper.springmvc.mybatis.service.impl.LotteryService;

/**
 * 获取随机数
 * 
 * @author suzhen
 * 
 */
public class LotteryUtil {

	public static int lineHeight = 100;
	public static int width = lineHeight * 9;
	public static int height = lineHeight * 16;

	/**
	 * 获取随机数
	 * 
	 * @param min
	 * @param max
	 * @return
	 */
	public static int random(int min, int max) {
		Random random = new Random();
		int result = random.nextInt(max) % (max - min + 1) + min;
		return result;
	}

	/**
	 * 
	 * @param limit
	 * @param min
	 * @param max
	 * @return
	 */
	public static List<Integer> getList(int limit, int min, int max) {
		List<Integer> integers = new ArrayList<>();
		for (int i = 0; i < limit; i++) {
			int result = random(min, max);
			if (!integers.contains(result)) {
				integers.add(result);
			} else {
				i--;
			}
		}

		Integer[] a = new Integer[limit];
		integers.toArray(a);
		Arrays.sort(a);
		return Arrays.asList(a);
	}

	public static List<Lottery> dlt(int yLength) {
		// 输出数字
		List<Lottery> lists = new ArrayList<>();
		for (int i = 0; i < yLength; i++) {
			List<Integer> c = new ArrayList<>();
			List<Integer> a = getList(5, 1, 35);
			for (Integer integer : a) {
				c.add(integer);
			}

			List<Integer> b = getList(2, 1, 12);
			for (Integer integer : b) {
				c.add(integer);
			}
			Lottery lottery = new Lottery();
			lottery.setId(FilesUtil.getUUIDName("", false));
			lottery.setDate("111");
			lottery.setNum("12345");
			lottery.setNumOne(c.get(0));
			lottery.setNumTwo(c.get(1));
			lottery.setNumThree(c.get(2));
			lottery.setNumFour(c.get(3));
			lottery.setNumFive(c.get(4));
			lottery.setNumSix(c.get(5));
			lottery.setNumSeven(c.get(6));
			lists.add(lottery);

		}

		return lists;
	}

	/**
	 * 大乐透基本数据展示
	 * 
	 * @return
	 */
	public static ByteArrayInputStream dlt(List<Lottery> lists, int limit) {
		int size = lists.size();
		BufferedImage image = new BufferedImage(lineHeight * 9, (size + 2)
				* lineHeight, BufferedImage.TYPE_INT_RGB);

		Graphics2D g = image.createGraphics();
		g.setBackground(Color.WHITE);
		g.fillRect(0, 0, lineHeight * 9, (size + 2) * lineHeight);
		g.setStroke(new BasicStroke(1.0f, BasicStroke.CAP_SQUARE,
				BasicStroke.JOIN_ROUND));

		// 竖线
		// 高度横线数量
		int yLength = size + 1;
		// 宽度坑横线数量
		int xLength = 8;

		// 竖线输出
		// 竖线x一直在变动 y，开头和结束是不变的
		g.setColor(Color.GREEN);
		for (int x = 0; x < xLength; x++) {
			g.drawLine(x * lineHeight + lineHeight, lineHeight, x * lineHeight
					+ lineHeight, yLength * lineHeight);
		}
		// 横线
		// x开始结束不变，y一直变化
		for (int y = 0; y < yLength; y++) {
			g.drawLine(lineHeight, (y + 1) * lineHeight, xLength * lineHeight,
					(y + 1) * lineHeight);
		}

		for (int y = 0; y < size; y++) {

			if (y < limit) {
				g.setColor(Color.BLACK);
				g.setFont(new Font("华文楷书", Font.ITALIC, 20));
			} else {
				g.setColor(Color.RED);
				g.setFont(new Font("华文楷书", Font.BOLD, 20));
			}

			setData(g, lists.get(y).getNumOne(), 1, 0, 15, y);

			setData(g, lists.get(y).getNumTwo(), 2, 0, 15, y);

			setData(g, lists.get(y).getNumThree(), 3, 0, 15, y);

			setData(g, lists.get(y).getNumFour(), 4, 0, 15, y);

			setData(g, lists.get(y).getNumFive(), 5, 0, 15, y);
			g.setColor(Color.BLUE);
			setData(g, lists.get(y).getNumSix(), 6, 0, 15, y);

			setData(g, lists.get(y).getNumSeven(), 7, 0, 15, y);

		}

		g.dispose();
		ByteArrayInputStream inputStream = null;
		// 创建输出留
		ByteArrayOutputStream bos = new ByteArrayOutputStream();

		try {
			ImageIO.write(image, "jpeg", bos);
			byte[] bts = bos.toByteArray();
			inputStream = new ByteArrayInputStream(bts);
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		return inputStream;
	}

	/**
	 * 大乐透走势图
	 * 
	 * @return
	 */
	public static ByteArrayInputStream dltTrend(List<Lottery> lists, int limit) {
		int size = lists.size();
		lineHeight = 40;
		int lineOffset = 10;
		BufferedImage image = new BufferedImage(lineHeight * 49, lineHeight
				* (size + 2), BufferedImage.TYPE_INT_RGB);

		Graphics2D g = image.createGraphics();
		g.setBackground(Color.WHITE);
		g.fillRect(0, 0, lineHeight * 49, lineHeight * (size + 2));

		g.setStroke(new BasicStroke(1.0f, BasicStroke.CAP_SQUARE,
				BasicStroke.JOIN_ROUND));

		// 竖线
		// 高度横线数量
		int yLength = size + 1;
		// 宽度坑横线数量
		int xLength = 35 + 12 + 1;

		// 竖线输出
		// 竖线x一直在变动 y，开头和结束是不变的
		g.setColor(Color.GRAY);
		for (int x = 0; x < xLength; x++) {
			g.drawLine(x * lineHeight + lineHeight, lineHeight, x * lineHeight
					+ lineHeight, yLength * lineHeight);
		}
		// 横线
		// x开始结束不变，y一直变化
		for (int y = 0; y < yLength; y++) {
			g.drawLine(lineHeight, (y + 1) * lineHeight, xLength * lineHeight,
					(y + 1) * lineHeight);
		}

		for (int y = 0; y < size; y++) {

			if (lists.get(y) == null) {
				continue;
			}

			if (y < limit) {
				g.setColor(Color.GREEN);
				g.setFont(new Font("华文楷书", Font.BOLD, 18));
			} else {
				g.setColor(Color.RED);
				g.setFont(new Font("华文楷书", Font.BOLD, 18));
			}

			setData(g, lists.get(y).getNumOne(), lists.get(y).getNumOne(), 0,
					lineOffset, y);

			setData(g, lists.get(y).getNumTwo(), lists.get(y).getNumTwo(), 0,
					lineOffset, y);

			setData(g, lists.get(y).getNumThree(), lists.get(y).getNumThree(),
					0, lineOffset, y);

			setData(g, lists.get(y).getNumFour(), lists.get(y).getNumFour(), 0,
					lineOffset, y);

			setData(g, lists.get(y).getNumFive(), lists.get(y).getNumFive(), 0,
					lineOffset, y);

			g.setColor(Color.BLUE);
			setData(g, lists.get(y).getNumSix(), lists.get(y).getNumSix(), 35,
					lineOffset, y);

			setData(g, lists.get(y).getNumSeven(), lists.get(y).getNumSeven(),
					35, lineOffset, y);

		}

		g.dispose();
		ByteArrayInputStream inputStream = null;
		// 创建输出留
		ByteArrayOutputStream bos = new ByteArrayOutputStream();

		try {
			ImageIO.write(image, "jpeg", bos);
			byte[] bts = bos.toByteArray();
			inputStream = new ByteArrayInputStream(bts);
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		return inputStream;
	}

	/**
	 * 
	 * @param g
	 * @param data
	 *            要显示的数据
	 * @param limit
	 *            左侧偏移数量
	 * @param offset
	 * @param lineOffset
	 * @param y
	 */
	private static void setData(Graphics2D g, int data, int limit, int offset,
			int lineOffset, int y) {

		int xWidth = limit * lineHeight + (lineHeight / 2) + offset
				* lineHeight - lineOffset;
		int yHeight = lineHeight + (lineHeight / 2) + (y * lineHeight)
				+ lineOffset;
		g.drawString(String.valueOf(data), xWidth, yHeight);
	}

	/**
	 * 百度数据导入
	 * 
	 * @param filePath
	 * @param lotteryService
	 */
	public static void importDlt(String filePath, LotteryService lotteryService) {

		File file = new File(filePath);
		BufferedReader bis = null;

		String content;
		try {
			bis = new BufferedReader(new FileReader(file));
			while ((content = bis.readLine()) != null) {
				String[] a = content.split("    ");
				Lottery lottery = new Lottery();
				lottery.setId(FilesUtil.getUUIDName("", false));
				lottery.setNum(a[0]);
				lottery.setDate(a[2]);

				String[] b = a[1].split("\\|");
				String[] c = b[0].split(",");

				Map<String, Object> params = new HashMap<>();
				params.put("date", a[2]);
				if (lotteryService.find("find", params) != null) {
					continue;
				}
				lottery.setType(LotteryService.DLT);
				lottery.setNumOne(Integer.valueOf(c[0]).intValue());
				lottery.setNumTwo(Integer.valueOf(c[1]).intValue());
				lottery.setNumThree(Integer.valueOf(c[2]).intValue());
				lottery.setNumFour(Integer.valueOf(c[3]).intValue());
				lottery.setNumFive(Integer.valueOf(c[4]).intValue());
				String[] d = b[1].split(",");
				lottery.setNumSix(Integer.valueOf(d[0]).intValue());
				lottery.setNumSeven(Integer.valueOf(d[1]).intValue());
				lotteryService.insert(lottery);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (bis != null) {
					bis.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	/**
	 * 自定义道路
	 * 
	 * @param filePath
	 * @param lotteryService
	 */
	public static void importCustomDlt(String filePath,
			LotteryService lotteryService) {

		File file = new File(filePath);
		BufferedReader bis = null;

		String content;
		try {
			bis = new BufferedReader(new FileReader(file));
			while ((content = bis.readLine()) != null) {
				String[] a = content.split("\t");
				Lottery lottery = new Lottery();
				lottery.setId(FilesUtil.getUUIDName("", false));
				lottery.setNum(a[0]);
				lottery.setDate(a[1]);

				int length = a[2].length();
				String ab = a[2].trim();
				String[] bb = new String[5];
				for (int i = 0, j = 0; i < length;) {
					String aa = ab.substring(i, i + 2);
					bb[j] = aa;
					i += 2;
					j++;
				}
				Map<String, Object> params = new HashMap<>();
				params.put("date", a[2]);
				if (lotteryService.find("find", params) != null) {
					continue;
				}
				lottery.setType(LotteryService.DLT);
				lottery.setNumOne(Integer.valueOf(bb[0]).intValue());
				lottery.setNumTwo(Integer.valueOf(bb[1]).intValue());
				lottery.setNumThree(Integer.valueOf(bb[2]).intValue());
				lottery.setNumFour(Integer.valueOf(bb[3]).intValue());
				lottery.setNumFive(Integer.valueOf(bb[4]).intValue());
				String[] d = a[3].split(" ");
				lottery.setNumSix(Integer.valueOf(d[0]).intValue());
				lottery.setNumSeven(Integer.valueOf(d[1]).intValue());
				lotteryService.insert(lottery);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (bis != null) {
					bis.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	public static void main(String[] args) throws IOException {
		ClassPathXmlApplicationContext ac = new ClassPathXmlApplicationContext(
				"beans.xml");
		LotteryService lotteryService = ac.getBean(LotteryService.class);

		System.out.println(lotteryService);

		LotteryUtil.importCustomDlt("/Users/suzhen/Downloads/2016",
				lotteryService);

	}
}
