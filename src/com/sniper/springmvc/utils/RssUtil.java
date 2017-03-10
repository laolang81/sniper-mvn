package com.sniper.springmvc.utils;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import com.rometools.rome.feed.synd.SyndCategory;
import com.rometools.rome.feed.synd.SyndCategoryImpl;
import com.rometools.rome.feed.synd.SyndContent;
import com.rometools.rome.feed.synd.SyndContentImpl;
import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndEntryImpl;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.feed.synd.SyndFeedImpl;
import com.rometools.rome.io.FeedException;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.SyndFeedOutput;
import com.rometools.rome.io.XmlReader;

/**
 * 由于数据结构复杂，下面只是一些例子 feedType = RSS/Atom
 * 
 * @author suzhen
 * 
 */
public class RssUtil {

	private URL feedUrl = null;
	private XmlReader reader = null;

	SyndFeedInput input = new SyndFeedInput();

	/**
	 * rss读取,并写入一个文件
	 * 
	 * @param url
	 * @throws IOException
	 * @throws IllegalArgumentException
	 * @throws FeedException
	 */
	public void read(String url) throws IOException, IllegalArgumentException,
			FeedException {
		feedUrl = new URL(url);
		reader = new XmlReader(feedUrl);
		SyndFeed feed = input.build(reader);
		// 得到rss中的自相列表
		List<SyndEntry> entries = feed.getEntries();
		for (SyndEntry syndEntry : entries) {
			syndEntry.getTitle();
		}

		Writer writer = new FileWriter("a.xml");
		// feed类型
		feed.setFeedType("");
		SyndFeedOutput output = new SyndFeedOutput();
		output.output(feed, writer);
		writer.close();

	}

	/**
	 * 生成rss
	 * 
	 * @throws FeedException
	 * @throws IOException
	 */
	public void create(Writer writer) throws IOException, FeedException {
		SyndFeed feed = new SyndFeedImpl();
		// 设置类型

		feed.setFeedType("rss_2.0");
		feed.setTitle("title");
		feed.setLink("link");
		feed.setDescription("description");

		List<SyndEntry> entries = new ArrayList<>();

		SyndEntry entry;
		SyndContent content;
		SyndCategory category;
		// 添加一个节点
		entry = new SyndEntryImpl();
		entry.setTitle("测试节点");
		entry.setAuthor("111");
		entry.setComments("comments");
		category = new SyndCategoryImpl();
		category.setName("cate");

		List<SyndCategory> categories = new ArrayList<>();
		categories.add(category);

		entry.setCategories(categories);
		// 设置内容
		content = new SyndContentImpl();
		content.setType("text/html");
		content.setValue("第一个节点");
		entry.setDescription(content);
		entries.add(entry);

		// 添加第二个节点
		entry = new SyndEntryImpl();
		entry.setTitle("第二个节点");
		entries.add(entry);
		// 设置内容
		content = new SyndContentImpl();
		content.setType("text/html");
		content.setValue("内容展示");
		entry.setDescription(content);

		entries.add(entry);

		feed.setEntries(entries);

		SyndFeedOutput output = new SyndFeedOutput();
		output.output(feed, writer);

	}

	public static void main(String[] args) throws IOException, FeedException {
		RssUtil rssUtil = new RssUtil();
		PrintWriter writer = new PrintWriter("/Users/suzhen/Desktop/a");
		rssUtil.create(writer);
	}
}
