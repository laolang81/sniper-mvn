package com.sniper.springmvc.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.oro.text.regex.MalformedPatternException;
import org.apache.oro.text.regex.MatchResult;
import org.apache.oro.text.regex.Pattern;
import org.apache.oro.text.regex.PatternCompiler;
import org.apache.oro.text.regex.Perl5Compiler;
import org.apache.oro.text.regex.Perl5Matcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 请设置好ffmpeg环境变量 参数参考
 * http://www.cuplayer.com/player/PlayerCode/FFmpeg/2014/1022/1629.html
 * 
 * @author sniper
 * 
 */
public class FfmpegUtils {
	private static Logger logger = LoggerFactory.getLogger(FfmpegUtils.class);
	private String ffmpegPath = "/usr/local/bin/ffmpeg";
	/**
	 * 截取时间 比如 00:02:06 20 表示20秒 默认120秒
	 */
	private String cutTime = "10";
	/**
	 * 720x480 截屏大小
	 */
	private String cutWidthAndHeight = "";
	private String thumbWidthAndHeight = "720x480";
	private FileInfo fileInfo = new FileInfo();

	private String videoPath = "";
	private String savePath = "";
	private String vedioData = "";
	/**
	 * 允许操作的视频格式
	 */
	private static HashMap<String, String> fileType;

	static {
		fileType = new HashMap<String, String>();
		fileType.put("avi", "true");
		fileType.put("mpg", "true");
		fileType.put("wmv", "true");
		fileType.put("3gp", "true");
		fileType.put("mov", "true");
		fileType.put("mp4", "true");
		fileType.put("asf", "true");
		fileType.put("asx", "true");
		fileType.put("flv", "true");
	}

	class FileInfo {
		// 提取出播放时间
		private String playTime;
		// 开始时间
		private String startTime;
		// bitrate 码率 单位
		private String unitKb;
		// 编码格式
		private String videoEncoding;
		// 视频格式
		private String videoFormat;
		// 分辨率
		private String videoResolution;
		private String audioEncoding;
		// 音频采样频率
		private String audioSample;

		public String getPlayTime() {
			return playTime;
		}

		public void setPlayTime(String playTime) {
			this.playTime = playTime;
		}

		public String getStartTime() {
			return startTime;
		}

		public void setStartTime(String startTime) {
			this.startTime = startTime;
		}

		public String getUnitKb() {
			return unitKb;
		}

		public void setUnitKb(String unitKb) {
			this.unitKb = unitKb;
		}

		public String getVideoEncoding() {
			return videoEncoding;
		}

		public void setVideoEncoding(String videoEncoding) {
			this.videoEncoding = videoEncoding;
		}

		public String getVideoFormat() {
			return videoFormat;
		}

		public void setVideoFormat(String videoFormat) {
			this.videoFormat = videoFormat;
		}

		public String getVideoResolution() {
			return videoResolution;
		}

		public void setVideoResolution(String videoResolution) {
			this.videoResolution = videoResolution;
		}

		public String getAudioEncoding() {
			return audioEncoding;
		}

		public void setAudioEncoding(String audioEncoding) {
			this.audioEncoding = audioEncoding;
		}

		public String getAudioSample() {
			return audioSample;
		}

		public void setAudioSample(String audioSample) {
			this.audioSample = audioSample;
		}

		@Override
		public String toString() {
			return "FileInfo [playTime=" + playTime + ", startTime="
					+ startTime + ", unitKb=" + unitKb + ", videoEncoding="
					+ videoEncoding + ", videoFormat=" + videoFormat
					+ ", videoResolution=" + videoResolution
					+ ", audioEncoding=" + audioEncoding + ", audioSample="
					+ audioSample + "]";
		}

	}

	public void setFfmpegPath(String ffmpegPath) {
		this.ffmpegPath = ffmpegPath;
	}

	public void setCutTime(String cutTime) {
		this.cutTime = cutTime;
	}

	public void setCutWidthAndHeight(String cutWidthAndHeight) {
		this.cutWidthAndHeight = cutWidthAndHeight;
	}

	public void setVideoPath(String videoPath) {
		this.videoPath = videoPath;
	}

	public String getThumbWidthAndHeight() {
		return thumbWidthAndHeight;
	}

	public void setThumbWidthAndHeight(String thumbWidthAndHeight) {
		this.thumbWidthAndHeight = thumbWidthAndHeight;
	}

	/**
	 * 设置文件保存地址
	 * 
	 * @param suffix
	 * @return
	 * @throws MalformedPatternException
	 */
	public String getSavePath(String suffix) {

		if (!ValidateUtil.isValid(savePath)) {
			// 设置图片保存路径
			savePath = videoPath.substring(0, videoPath.lastIndexOf(".")) + "_"
					+ getCutWidthAndHeight() + suffix;
		}
		return savePath;
	}

	public void setSavePath(String savePath) {
		this.savePath = savePath;
	}

	public String getVedioData() {
		return vedioData;
	}

	public FfmpegUtils(String videoPath) {
		this.videoPath = videoPath;
		File file = new File(this.videoPath);
		if (!file.exists()) {
			try {
				throw new FileNotFoundException(videoPath + "文件找不到");
			} catch (FileNotFoundException e1) {
				e1.printStackTrace();
				logger.error(videoPath + "文件找不到");
			}
		}
		// this.initFfmpegPath();
		this.initFileInfo();
		this.getFileInfo();

	}

	/**
	 * 检查格式
	 * 
	 * @return
	 */
	private boolean checkContentType() {

		String type = videoPath.substring(videoPath.lastIndexOf(".") + 1,
				videoPath.length()).toLowerCase();
		// ffmpeg能解析的格式：（asx，asf，mpg，wmv，3gp，mp4，mov，avi，flv等）
		return "true".equals(fileType.get(type));
	}

	/**
	 * 获取视频的时间长度秒计算
	 * 
	 * @return
	 * @throws MalformedPatternException
	 */
	public float getPlayTimesSeconds() throws MalformedPatternException {
		float seconds = 0f;
		String times = fileInfo.getPlayTime();
		String[] timess = times.split(":");
		float hoursSec = Float.valueOf(timess[0]) * 60 * 60;
		float minSec = Float.valueOf(timess[1]) * 60;
		float sec = Float.valueOf(timess[2]);
		seconds = Float.valueOf(hoursSec) + Float.valueOf(minSec)
				+ Float.valueOf(sec);
		return seconds;

	}

	/**
	 * 获取视频的宽度X高度
	 * 
	 * @return
	 * @throws MalformedPatternException
	 */
	public String getCutWidthAndHeight() {
		if (cutWidthAndHeight.equals("")) {
			cutWidthAndHeight = fileInfo.getVideoResolution();
		}
		return cutWidthAndHeight;
	}

	/**
	 * http://www.cnblogs.com/wangkangluo1/archive/2012/07/10/2585095.html 转换命令：
	 * ffmpeg -i "20090401010.mp4" -y -ab 32 -ar 22050 -qscale 10 -s 640*480 -r
	 * 15 /opt/a.flv
	 * 
	 * -i 是 要转换文件名 -y是 覆盖输出文件 -ab 是 音频数据流，大家在百度听歌的时候应该都可以看到 128 64 -ar 是 声音的频率
	 * 22050 基本都是这个。 -qscale 是视频输出质量，后边的值越小质量越高，但是输出文件就越“肥” -s 是输出 文件的尺寸大小！ -r 是
	 * 播放侦数。 ffmpeg能解析的格式：（asx，asf，mpg，wmv，3gp，mp4，mov，avi，flv等） -aspect aspect
	 * 设置横纵比 4:3 16:9 或 1.3333 1.7777 1280X720 720X480 800x480 1024x576
	 * 1920x1080 320x240
	 * 
	 * @return
	 * @throws MalformedPatternException
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public String vedioToFlv() throws MalformedPatternException, IOException,
			InterruptedException {
		if (!checkContentType()) {
			return "";
		}
		List<String> commands = new ArrayList<>();
		String flvPath = getSavePath(".flv");
		commands.add(ffmpegPath);
		commands.add("-i");
		commands.add(videoPath);
		// 音频bit，默认比较小 一般选择 32，64，96，128
		commands.add("-ab");
		commands.add("64K");
		// 使用的音频
		// commands.add("-acodec");
		// commands.add("mp4");
		// 设置声道数，1就是单声道，2就是立体声
		commands.add("-ac");
		commands.add("2");
		// 增大一倍音量
		commands.add("-vol");
		commands.add("512");
		// 音频采集率默认24000
		commands.add("-ar");
		commands.add("22050");
		// 指定比特率(bits/s)，似乎ffmpeg是自动VBR的，指定了就大概是平均比特率
		// bitrate 设置比特率，缺省200kb/s
		// commands.add("-b:v");
		// commands.add("200K");
		// 帧数，一般29.97 flv设置15,无论怎么设都出现past duration too large
		commands.add("-r");
		commands.add("12");
		// 以<数值>质量为基础的VBR，取值0.01-255，约小质量越好 == -qscale
		commands.add("-q");
		commands.add("4");
		// 设置格式
		commands.add("-f");
		commands.add("flv");
		// 设置生成的宽高
		// commands.add("-s");
		// commands.add(getCutWidthAndHeight());
		// 设置文件输出
		commands.add("-y");
		commands.add(flvPath);
		System.out.println(commands.toString().replace(",", ""));
		logger.debug(commands.toString());
		BufferedReader reader = null;

		try {
			ProcessBuilder builder = new ProcessBuilder();
			builder.command(commands);
			Process process = builder.start();
			StringBuffer result = new StringBuffer();
			reader = new BufferedReader(new InputStreamReader(
					process.getInputStream()));
			String line;
			while ((line = reader.readLine()) != null) {
				result.append(line);
				System.out
						.println(builder.command().toString() + "--->" + line);
			}
			process.waitFor();
			int exit = process.exitValue();
			if (exit != 0) {
				throw new IOException("failed to execute:" + builder.command()
						+ " with result:" + result);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				reader.close();
			}
		}
		return flvPath;
	}

	/**
	 * 转mp4 / cuplayer.com ffmpeg能解析的格式：（asx，asf，mpg，wmv，3gp，mp4，mov，avi，flv等）
	 * http://ferreousbox.iteye.com/blog/163865
	 * 
	 * @return
	 * @throws MalformedPatternException
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public String vedioToMp4() throws MalformedPatternException, IOException,
			InterruptedException {
		List<String> commands = new ArrayList<>();
		String flvPath = getSavePath(".mp4");
		commands.add(ffmpegPath);
		commands.add("-i");
		commands.add(videoPath);
		// 音频bit，默认比较小 一般选择 32，64，96，128
		commands.add("-ab");
		commands.add("64K");
		// 音频采集率默认24000
		commands.add("-ar");
		commands.add("24000");
		commands.add("-b:v");
		commands.add("300k");
		// 使用的音频
		commands.add("-acodec");
		commands.add("mp3");
		// 增大一倍音量
		commands.add("-vol");
		commands.add("512");
		// 以<数值>质量为基础的VBR，取值0.01-255，约小质量越好 == -qscale
		commands.add("-q");
		commands.add("4");
		commands.add("-vcodec");
		commands.add("copy");

		// 设置生成的宽高
		// commands.add("-s");
		// commands.add(getCutWidthAndHeight());
		// 设置文件输出
		commands.add("-y");
		commands.add(flvPath);
		System.out.println(commands.toString().replace(",", ""));
		logger.debug(commands.toString());
		BufferedReader reader = null;

		try {
			ProcessBuilder builder = new ProcessBuilder();
			builder.command(commands);
			Process process = builder.start();
			StringBuffer result = new StringBuffer();
			reader = new BufferedReader(new InputStreamReader(
					process.getInputStream()));
			String line;
			while ((line = reader.readLine()) != null) {
				result.append(line);
				System.out
						.println(builder.command().toString() + "--->" + line);
			}
			process.waitFor();
			int exit = process.exitValue();
			if (exit != 0) {
				throw new IOException("failed to execute:" + builder.command()
						+ " with result:" + result);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				reader.close();
			}
		}
		return flvPath;
	}

	/**
	 * 视频截图
	 * 
	 * @return
	 * @throws MalformedPatternException
	 */
	public String videoScreenshot() throws MalformedPatternException {

		String savePath = getSavePath(".jpg");
		List<String> commands = new ArrayList<>();
		commands.add(ffmpegPath);
		commands.add("-ss");
		if (ValidateUtil.isValid(this.cutTime)) {
			commands.add(this.cutTime);// 这个参数是设置截取视频多少秒时的画面
		} else {
			commands.add("10");
		}
		commands.add("-i");
		commands.add(videoPath);

		// 一般配合 -t表示每次的范围
		// commands.add("-t");
		// commands.add("0.001");
		// 配置表表达式生成多张,文件名字后面加上%03d表示生成1000张，0-999
		// commands.add("-r");
		// commands.add("1");
		commands.add("-s");
		commands.add(getThumbWidthAndHeight());
		commands.add("-f");
		commands.add("image2");
		commands.add("-y");
		commands.add(savePath);
		logger.debug(commands.toString());
		System.out.println(commands.toString().replace(",", ""));

		try {
			ProcessBuilder builder = new ProcessBuilder();
			builder.command(commands);
			builder.start();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return savePath;
	}

	/**
	 * 获取文件信息
	 */
	public void getFileInfo() {
		PatternCompiler compiler = new Perl5Compiler();
		try {
			String regexDuration = "Duration: (.*?), start: (.*?), bitrate: (\\d*) kb\\/s";
			String regexVideo = "Video: (.*?), (.*?), (.*?)[,\\s]";
			String regexAudio = "Audio: (\\w*), (\\d*) Hz";

			Pattern patternDuration = compiler.compile(regexDuration,
					Perl5Compiler.CASE_INSENSITIVE_MASK);
			Perl5Matcher matcherDuration = new Perl5Matcher();
			if (matcherDuration.contains(getVedioData(), patternDuration)) {
				MatchResult re = matcherDuration.getMatch();
				fileInfo.setPlayTime(re.group(1));
				fileInfo.setStartTime(re.group(2));
				fileInfo.setUnitKb(re.group(3));
			}

			Pattern patternVideo = compiler.compile(regexVideo,
					Perl5Compiler.CASE_INSENSITIVE_MASK);
			Perl5Matcher matcherVideo = new Perl5Matcher();

			if (matcherVideo.contains(getVedioData(), patternVideo)) {
				MatchResult re = matcherVideo.getMatch();
				fileInfo.setVideoEncoding(re.group(1));
				fileInfo.setVideoFormat(re.group(2));
				fileInfo.setVideoResolution(re.group(3));

			}

			Pattern patternAudio = compiler.compile(regexAudio,
					Perl5Compiler.CASE_INSENSITIVE_MASK);
			Perl5Matcher matcherAudio = new Perl5Matcher();

			if (matcherAudio.contains(getVedioData(), patternAudio)) {
				MatchResult re = matcherAudio.getMatch();
				fileInfo.setAudioEncoding(re.group(1));
				fileInfo.setAudioSample(re.group(2));
			}

		} catch (MalformedPatternException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 获取ffmpg的执行目录
	 */
	public void initFfmpegPath() {
		try {
			Map<String, String> config = SystemConfigUtil.getSystemConfig();
			if (config.containsKey("ffmpeg")) {
				this.ffmpegPath = config.get("ffmpeg");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * ffmpeg能解析的格式：（asx，asf，mpg，wmv，3gp，mp4，mov，avi，flv等） 获取视频文件信息
	 * 
	 * @param inputPath
	 * @return
	 */
	public void initFileInfo() {

		List<String> commend = new ArrayList<>();

		commend.add(ffmpegPath);
		commend.add("-i");
		commend.add(videoPath);

		ProcessBuilder builder = new ProcessBuilder();
		builder.command(commend);
		builder.redirectErrorStream(true);
		Process p;
		try {
			p = builder.start();
			int ret = p.waitFor();// 这里线程阻塞，将等待外部转换进程运行成功运行结束后，才往下执行
			// 1. start
			BufferedReader buf = null; // 保存ffmpeg的输出结果流
			String line = null;
			StringBuffer sb = new StringBuffer();
			// read the standard output
			// ret == 1
			if (ret == 1) {
				buf = new BufferedReader(new InputStreamReader(
						p.getInputStream()));
				sb = new StringBuffer();
				while ((line = buf.readLine()) != null) {
					sb.append(line);
					continue;
				}
			}
			vedioData = sb.toString();
			// 1. end
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 下面是原理子 已经被拆的不能使用了
	 * 
	 * @param args
	 */
	public void demo(String[] args) {

		PatternCompiler compiler = new Perl5Compiler();
		try {
			String regexDuration = "Duration: (.*?), start: (.*?), bitrate: (\\d*) kb\\/s";
			String regexVideo = "Video: (.*?), (.*?), (.*?)[,\\s]";
			String regexAudio = "Audio: (\\w*), (\\d*) Hz";

			Pattern patternDuration = compiler.compile(regexDuration,
					Perl5Compiler.CASE_INSENSITIVE_MASK);
			Perl5Matcher matcherDuration = new Perl5Matcher();
			// System.out.println(result);
			if (matcherDuration.contains(getVedioData(), patternDuration)) {
				MatchResult re = matcherDuration.getMatch();

				System.out.println("提取出播放时间  ===" + re.group(1));
				System.out.println("开始时间        =====" + re.group(2));
				System.out.println("bitrate 码率 单位 kb==" + re.group(3));
			}

			Pattern patternVideo = compiler.compile(regexVideo,
					Perl5Compiler.CASE_INSENSITIVE_MASK);
			Perl5Matcher matcherVideo = new Perl5Matcher();

			if (matcherVideo.contains(getVedioData(), patternVideo)) {
				MatchResult re = matcherVideo.getMatch();
				System.out.println("编码格式  ===" + re.group(1));
				System.out.println("视频格式 ===" + re.group(2));
				System.out.println(" 分辨率  == =" + re.group(3));
			}

			Pattern patternAudio = compiler.compile(regexAudio,
					Perl5Compiler.CASE_INSENSITIVE_MASK);
			Perl5Matcher matcherAudio = new Perl5Matcher();

			if (matcherAudio.contains(getVedioData(), patternAudio)) {
				MatchResult re = matcherAudio.getMatch();
				System.out.println("音频编码             ===" + re.group(1));
				System.out.println("音频采样频率  ===" + re.group(2));
			}

		} catch (MalformedPatternException e) {
			e.printStackTrace();
		}

	}

	public boolean processImg(String veido_path, String ffmpeg_path) {
		File file = new File(veido_path);
		if (!file.exists()) {
			logger.error("路径[" + veido_path + "]对应的视频文件不存在!");
			return false;
		}

		List<String> commands = new ArrayList<>();
		commands.add(ffmpeg_path);
		if (ValidateUtil.isValid(cutTime)) {
			commands.add("-ss");
			commands.add(cutTime);// 这个参数是设置截取视频多少秒时的画面
		}
		commands.add("-i");
		commands.add(veido_path);
		commands.add("-y");
		commands.add("-f");
		commands.add("image2");
		commands.add("-t");
		commands.add("0.0001");
		commands.add("-s");
		commands.add(getCutWidthAndHeight());
		commands.add(getSavePath(".jpg"));
		try {
			ProcessBuilder builder = new ProcessBuilder();
			builder.command(commands);
			builder.start();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public static void main(String[] args) throws MalformedPatternException,
			IOException, InterruptedException {
		// /Users/suzhen/approot/Tomcat/apache-tomcat-8.0.28/webapps/vedio/attachments/image
		String mp4path = "/Users/suzhen/approot/Tomcat/634e3a98-925c-4e1c-b49d-6827a2a03482.mp4";
		// String flvpath =
		// "/Users/suzhen/approot/Tomcat/apache-tomcat-8.0.28/webapps/video/attachments/image/20160311/0d974103-8a67-4070-a2d5-1c713f269222.mp4";

		FfmpegUtils ffmpegUtils = new FfmpegUtils(mp4path);

		// ffmpegUtils.getCutWidthAndHeight();
		// String string = ffmpegUtils.getPlayTimes();
		// ffmpegUtils.videoScreenshot();
		// System.out.println(ffmpegUtils.getPlayTimesSeconds());
		// System.out.println(string);
		String flvPath = ffmpegUtils.videoScreenshot();

		System.out.println(flvPath);
	}

}
