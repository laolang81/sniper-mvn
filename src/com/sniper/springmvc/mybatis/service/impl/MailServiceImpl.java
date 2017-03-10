package com.sniper.springmvc.mybatis.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.sniper.springmvc.model.Files;
import com.sniper.springmvc.model.Mail;
import com.sniper.springmvc.model.MailFiles;
import com.sniper.springmvc.mybatis.dao.BaseDao;

@Service("mailService")
public class MailServiceImpl extends BaseServiceImpl<Mail> implements
		MailService {

	@Resource
	MailFilesService mailFilesService;

	@Override
	@Resource(name = "mailDao")
	public void setDao(BaseDao<Mail> dao) {
		super.setDao(dao);
	}

	@Override
	public int insert(Mail t) {
		List<Files> files = t.getFiles();
		for (Files files2 : files) {
			MailFiles files3 = new MailFiles();
			files3.setFile_id(files2.getId());
			files3.setMail_id(t.getId());
			mailFilesService.insert(files3);
		}
		return super.insert(t);
	}

	@Override
	public int update(Mail t) {
		if (t.getFiles().size() > 0) {
			mailFilesService.delete(t.getId());
			List<Files> files = t.getFiles();
			for (Files files2 : files) {
				MailFiles files3 = new MailFiles();
				files3.setFile_id(files2.getId());
				files3.setMail_id(t.getId());
				mailFilesService.insert(files3);
			}
		}

		return super.update(t);
	}

	@Override
	public int delete(String id) {
		mailFilesService.delete(id);
		return super.delete(id);
	}

}
