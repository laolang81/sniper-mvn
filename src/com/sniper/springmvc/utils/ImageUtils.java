package com.sniper.springmvc.utils;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.color.ColorSpace;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

import net.coobird.thumbnailator.Thumbnails;

import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageDecoder;

/**
 * 图片工具类， 图片水印，文字水印，缩放，补白等
 * 
 * @author Carl He
 */
public final class ImageUtils {
	/** 图片格式：JPG */
	private static final String PICTRUE_FORMATE_JPG = "jpg";

	private ImageUtils() {
	}

	/**
	 * 添加图片水印
	 * 
	 * @param targetImg
	 *            目标图片路径，如：C://myPictrue//1.jpg
	 * @param waterImg
	 *            水印图片路径，如：C://myPictrue//logo.png
	 * @param x
	 *            水印图片距离目标图片左侧的偏移量，如果x<0, 则在正中间
	 * @param y
	 *            水印图片距离目标图片上侧的偏移量，如果y<0, 则在正中间
	 * @param alpha
	 *            透明度(0.0 -- 1.0, 0.0为完全透明，1.0为完全不透明)
	 */
	public final static void pressImage(String targetImg, String waterImg,
			int x, int y, float alpha) {
		try {
			File file = new File(targetImg);
			Image image = ImageIO.read(file);
			int width = image.getWidth(null);
			int height = image.getHeight(null);
			BufferedImage bufferedImage = new BufferedImage(width, height,
					BufferedImage.TYPE_INT_RGB);
			Graphics2D g = bufferedImage.createGraphics();
			g.drawImage(image, 0, 0, width, height, null);

			Image waterImage = ImageIO.read(new File(waterImg)); // 水印文件
			int width_1 = waterImage.getWidth(null);
			int height_1 = waterImage.getHeight(null);
			g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP,
					alpha));

			int widthDiff = width - width_1;
			int heightDiff = height - height_1;
			if (x < 0) {
				x = widthDiff / 2;
			} else if (x > widthDiff) {
				x = widthDiff;
			}
			if (y < 0) {
				y = heightDiff / 2;
			} else if (y > heightDiff) {
				y = heightDiff;
			}
			g.drawImage(waterImage, x, y, width_1, height_1, null); // 水印文件结束
			g.dispose();
			ImageIO.write(bufferedImage, PICTRUE_FORMATE_JPG, file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 添加文字水印
	 * 
	 * @param targetImg
	 *            目标图片路径，如：C://myPictrue//1.jpg
	 * @param pressText
	 *            水印文字， 如：中国证券网
	 * @param fontName
	 *            字体名称， 如：宋体
	 * @param fontStyle
	 *            字体样式，如：粗体和斜体(Font.BOLD|Font.ITALIC)
	 * @param fontSize
	 *            字体大小，单位为像素
	 * @param color
	 *            字体颜色
	 * @param x
	 *            水印文字距离目标图片左侧的偏移量，如果x<0, 则在正中间
	 * @param y
	 *            水印文字距离目标图片上侧的偏移量，如果y<0, 则在正中间
	 * @param alpha
	 *            透明度(0.0 -- 1.0, 0.0为完全透明，1.0为完全不透明)
	 */
	public static void pressText(String targetImg, String pressText,
			String fontName, int fontStyle, int fontSize, Color color, int x,
			int y, float alpha) {
		try {
			File file = new File(targetImg);

			Image image = ImageIO.read(file);
			int width = image.getWidth(null);
			int height = image.getHeight(null);
			System.out.println(width);
			System.out.println(height);
			BufferedImage bufferedImage = new BufferedImage(width, height,
					BufferedImage.TYPE_INT_RGB);
			Graphics2D g = bufferedImage.createGraphics();
			g.drawImage(image, 0, 0, width, height, null);
			g.setFont(new Font(fontName, fontStyle, fontSize));
			g.setColor(color);
			g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP,
					alpha));

			int width_1 = fontSize * getLength(pressText);
			int height_1 = fontSize;
			int widthDiff = width - width_1;
			int heightDiff = height - height_1;
			if (x < 0) {
				x = widthDiff / 2;
			} else if (x > widthDiff) {
				x = widthDiff;
			}
			if (y < 0) {
				y = heightDiff / 2;
			} else if (y > heightDiff) {
				y = heightDiff;
			}

			g.drawString(pressText, x, y + height_1);
			g.dispose();
			ImageIO.write(bufferedImage, PICTRUE_FORMATE_JPG, file);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 获取字符长度，一个汉字作为 1 个字符, 一个英文字母作为 0.5 个字符
	 * 
	 * @param text
	 * @return 字符长度，如：text="中国",返回 2；text="test",返回 2；text="中国ABC",返回 4.
	 */
	public static int getLength(String text) {
		int textLength = text.length();
		int length = textLength;
		for (int i = 0; i < textLength; i++) {
			if (String.valueOf(text.charAt(i)).getBytes().length > 1) {
				length++;
			}
		}
		return (length % 2 == 0) ? length / 2 : length / 2 + 1;
	}

	/**
	 * 图片缩放
	 * 
	 * @param filePath
	 *            图片路径
	 * @param height
	 *            高度
	 * @param width
	 *            宽度
	 * @param bb
	 *            比例不对时是否需要补白
	 */
	public static void resize(String filePath, int height, int width, boolean bb) {
		try {
			double ratio = 0; // 缩放比例
			File f = new File(filePath);
			String suffix = filePath.substring(filePath.lastIndexOf(".") + 1);

			BufferedImage bi = ImageIO.read(f);
			Image itemp = bi.getScaledInstance(width, height,
					Image.SCALE_SMOOTH);
			// 计算比例
			if ((bi.getHeight() > height) || (bi.getWidth() > width)) {
				if (bi.getHeight() > bi.getWidth()) {
					ratio = (new Integer(height)).doubleValue()
							/ bi.getHeight();
				} else {
					ratio = (new Integer(width)).doubleValue() / bi.getWidth();
				}
				AffineTransformOp op = new AffineTransformOp(
						AffineTransform.getScaleInstance(ratio, ratio), null);
				itemp = op.filter(bi, null);
			}
			if (bb) {
				BufferedImage image = new BufferedImage(width, height,
						BufferedImage.TYPE_INT_RGB);
				Graphics2D g = image.createGraphics();
				g.setColor(Color.white);
				g.fillRect(0, 0, width, height);
				if (width == itemp.getWidth(null))
					g.drawImage(itemp, 0, (height - itemp.getHeight(null)) / 2,
							itemp.getWidth(null), itemp.getHeight(null),
							Color.white, null);
				else
					g.drawImage(itemp, (width - itemp.getWidth(null)) / 2, 0,
							itemp.getWidth(null), itemp.getHeight(null),
							Color.white, null);
				g.dispose();
				itemp = image;
			}
			ImageIO.write((BufferedImage) itemp, suffix, f);
		} catch (IOException e) {
			System.out.println(filePath);
			e.printStackTrace();
		}

	}

	/**
	 * 图片缩放
	 * 
	 * @param filePath
	 *            图片路径
	 * @param height
	 *            高度
	 * @param width
	 *            宽度
	 * @param bb
	 *            比例不对时是否需要补白
	 */
	public static void resizeCMYK(String filePath, int height, int width,
			boolean bb) {
		try {
			double ratio = 0; // 缩放比例
			File f = new File(filePath);
			String suffix = filePath.substring(filePath.lastIndexOf(".") + 1);
			JPEGImageDecoder decoder = JPEGCodec
					.createJPEGDecoder(new FileInputStream(f));
			BufferedImage bi = decoder.decodeAsBufferedImage();

			// BufferedImage bi = ImageIO.read(f);
			Image itemp = bi.getScaledInstance(width, height,
					Image.SCALE_SMOOTH);
			// 计算比例
			if ((bi.getHeight() > height) || (bi.getWidth() > width)) {
				if (bi.getHeight() > bi.getWidth()) {
					ratio = (new Integer(height)).doubleValue()
							/ bi.getHeight();
				} else {
					ratio = (new Integer(width)).doubleValue() / bi.getWidth();
				}
				AffineTransformOp op = new AffineTransformOp(
						AffineTransform.getScaleInstance(ratio, ratio), null);
				itemp = op.filter(bi, null);
			}
			if (bb) {
				BufferedImage image = new BufferedImage(width, height,
						BufferedImage.TYPE_INT_RGB);
				Graphics2D g = image.createGraphics();
				g.setColor(Color.white);
				g.fillRect(0, 0, width, height);
				if (width == itemp.getWidth(null))
					g.drawImage(itemp, 0, (height - itemp.getHeight(null)) / 2,
							itemp.getWidth(null), itemp.getHeight(null),
							Color.white, null);
				else
					g.drawImage(itemp, (width - itemp.getWidth(null)) / 2, 0,
							itemp.getWidth(null), itemp.getHeight(null),
							Color.white, null);
				g.dispose();
				itemp = image;
			}
			ImageIO.write((BufferedImage) itemp, suffix, f);
		} catch (IOException e) {
			System.out.println(filePath);
			e.printStackTrace();
		}

	}

	/**
	 * 获取文件类型
	 * 
	 * @param o
	 * @return
	 */
	public static String getFormatName(File f) {
		try {
			ImageInputStream iis = ImageIO.createImageInputStream(f);

			Iterator<ImageReader> iter = ImageIO.getImageReaders(iis);
			if (!iter.hasNext()) {
				return null;
			}

			ImageReader reader = iter.next();
			iis.close();

			return reader.getFormatName();
		} catch (IOException e) {
		}
		return null;
	}

	/**
	 * 获取图片的宽度和高度
	 * 
	 * @param imagePath
	 * @return
	 */
	public static List<Integer> getwidthAndHeight(String imagePath) {
		BufferedImage bufferedImage = null;
		List<Integer> integers = new ArrayList<>();
		try {

			bufferedImage = ImageIO.read(new File(imagePath));
			int width = bufferedImage.getWidth();
			int height = bufferedImage.getHeight();
			integers.add(width);
			integers.add(height);
		} catch (IOException e) {
			e.printStackTrace();
			integers.add(0);
			integers.add(0);
		}

		return integers;
	}
	
	public static boolean isCMYK(String filename) {
        boolean result = false;
        BufferedImage img = null;
        try {
            img = ImageIO.read(new File(filename));
        } catch (IOException e) {
            
        }
        if (img != null) {
            int colorSpaceType = img.getColorModel().getColorSpace().getType();
            result = colorSpaceType == ColorSpace.TYPE_CMYK;
        }

        return result;
    }

	public static void main(String[] args) throws IOException {

		Thumbnails.of("/home/sniper/图片/2015070618434646757.jpg").size(200, 200)
				.toFile("/home/sniper/图片/2015070618434646757-1.jpg");
		
	}
}