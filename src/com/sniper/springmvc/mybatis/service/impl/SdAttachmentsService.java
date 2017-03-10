package com.sniper.springmvc.mybatis.service.impl;

import java.util.List;

import com.sniper.springmvc.model.SdAttachments;
import com.sniper.springmvc.mybatis.service.BaseService;

public interface SdAttachmentsService extends BaseService<SdAttachments> {

	public void deleteByPath(String path);

	public SdAttachments getFileByUrl(String url);

	public List<SdAttachments> getFiles(String sid, Integer isimage, Integer main);

	public SdAttachments getOneImage(String sid);
}
