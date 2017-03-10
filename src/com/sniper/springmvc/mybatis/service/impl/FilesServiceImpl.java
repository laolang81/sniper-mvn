package com.sniper.springmvc.mybatis.service.impl;

import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.sniper.springmvc.model.Files;
import com.sniper.springmvc.mybatis.dao.BaseDao;
import com.sniper.springmvc.utils.FilesUtil;
import com.sniper.springmvc.utils.ValidateUtil;

@Service("filesService")
public class FilesServiceImpl extends BaseServiceImpl<Files> implements
		FilesService {

	@Override
	@Resource(name = "filesDao")
	public void setDao(BaseDao<Files> dao) {
		super.setDao(dao);
	}

	@Override
	public Files getFileByUrl(String url) {
		Map<String, Object> params = new HashMap<>();
		params.put("newPath", url);
		List<Files> files = this.query("select", params);
		if (files.size() == 1) {
			return files.get(0);
		}
		return null;
	}

	@Override
	public Files getFileBySourcePath(String sourcePath) {
		Map<String, Object> params = new HashMap<>();
		params.put("sourcePath", sourcePath);
		List<Files> files = this.query("select", params);
		if (files.size() == 1) {
			return files.get(0);
		}
		return null;
	}

	@Override
	public boolean deleteByPath(String path) {
		Files files = this.getFileByUrl(path);
		if (files != null) {
			return true;
		}
		return false;
	}

	@Override
	public List<Files> lastLists(int limit, Date date) {
		Map<String, Object> params = new HashMap<>();
		params.put("stime", date);
		params.put("pageOffset", 0);
		params.put("pageSize", limit);
		List<Files> files = this.pageList(params);

		return files;
	}

	@Override
	public int delete(String id) {

		Files files = this.get(id);
		if (files != null) {
			if (!files.getNewPath().equals("")) {
				FilesUtil.delFileByPath(files.getNewPath());
			}
			return super.delete(id);
		}
		return 0;

	}

	@Override
	public void updateTags(String id, String tag) {
		if (ValidateUtil.isValid(id) && ValidateUtil.isValid(tag)) {
			Files files = this.get(id);

			if (files == null) {
				return;
			}

			String tagsFiles = files.getTags();
			if (tagsFiles == null) {
				tagsFiles = "";
			}

			if (!tagsFiles.contains(tag)) {
				String[] tags = files.getTags().split(",");
				List<String> strings = new ArrayList<>();
				for (int i = 0; i < tags.length; i++) {
					if (ValidateUtil.isValid(tags[i])) {
						strings.add(tags[i]);
					}
				}
				strings.add(tag);
				String tass = StringUtils.join(strings, ",");

				files.setTags(tass);
				this.update(files);
			}
		}

	}

	public static void main(String[] args) {
		String tag = "";
		String[] tags = tag.split(",");
		List<String> strings = new ArrayList<>();
		for (int i = 0; i < tags.length; i++) {
			if (ValidateUtil.isValid(tags[i])) {
				strings.add(tags[i]);
			}

		}
		strings.add("saasxa");
		System.out.println(strings);
		String tass = StringUtils.join(strings, ",");
		System.out.println(tass);
	}
}
