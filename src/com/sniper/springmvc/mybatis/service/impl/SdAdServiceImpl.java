package com.sniper.springmvc.mybatis.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.sniper.springmvc.model.SdAd;
import com.sniper.springmvc.mybatis.dao.BaseDao;
import com.sniper.springmvc.utils.ValidateUtil;

@Service("sdAdService")
public class SdAdServiceImpl extends BaseServiceImpl<SdAd> implements
		SdAdService {

	@Resource
	SdAttachmentsService attachmentsService;

	@Resource(name = "sdAdDao")
	@Override
	public void setDao(BaseDao<SdAd> dao) {
		super.setDao(dao);
	}

	@Override
	public int update(SdAd t) {
		return super.update(t);
	}

	@Override
	public int delete(String id) {
		SdAd ad = this.get(id);
		if (ad != null) {
			if (ValidateUtil.isValid(ad.getPath())) {
				attachmentsService.deleteByPath(ad.getPath());
			}
			return super.delete(id);
		}
		return 0;
	}
}
