package com.sniper.springmvc.mybatis.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.sniper.springmvc.model.SdAttachments;
import com.sniper.springmvc.mybatis.dao.BaseDao;
import com.sniper.springmvc.utils.FilesUtil;
import com.sniper.springmvc.utils.SmbUtils;
import com.sniper.springmvc.utils.ValidateUtil;

@Service("sdAttachmentsService")
public class SdAttachmentsServiceImpl extends BaseServiceImpl<SdAttachments>
		implements SdAttachmentsService {

	@Resource(name = "sdAttachmentsDao")
	@Override
	public void setDao(BaseDao<SdAttachments> dao) {
		super.setDao(dao);
	}

	@Override
	public void deleteByPath(String path) {
		Map<String, Object> params = new HashMap<>();
		params.put("path", path);
		SdAttachments attachments = (SdAttachments) this.find("find", params);
		if (attachments != null) {
			if (this.delete(attachments.getAid() + "") == 1) {
				FilesUtil.delFileByPath(attachments.getFilename());
			}
		}
	}

	@Override
	public SdAttachments getFileByUrl(String url) {
		Map<String, Object> params = new HashMap<>();
		params.put("path", url);
		SdAttachments attachments = (SdAttachments) this.find("find", params);
		return attachments;
	}

	/**
	 * 读取附件，是否是只读取图片
	 */
	@Override
	public List<SdAttachments> getFiles(String sid, Integer isimage,
			Integer main) {
		Map<String, Object> params = new HashMap<>();
		params.put("sid", sid);
		if (isimage != null) {
			params.put("isimage", isimage);
		}
		if (main != null) {
			params.put("mainsite", main);
		}
		return this.query("select", params);
	}

	@Override
	public SdAttachments getOneImage(String sid) {
		Map<String, Object> params = new HashMap<>();
		params.put("sid", sid);
		params.put("isprimeimage", 1);
		List<SdAttachments> attachments = this.query("select", params);
		if (ValidateUtil.isValid(attachments) && attachments.size() > 0) {
			attachments.get(0);
		}
		return null;
	}

	/**
	 * 这里只能删除本系统上传的图片，原来老系统的不能删除
	 */
	@Override
	public int delete(String id) {
		SdAttachments attachments = super.get(id);
		// 先删除数据库的
		int result = super.delete(id);
		if (result == 1
				&& ValidateUtil.isValid(attachments.getFilename())
				&& attachments.getFilename().startsWith(
						"/public/attachment/three/")) {
			// 删除本地
			FilesUtil.delFileByPath(attachments.getFilename());
			// 删除可能存在的原图
			if (ValidateUtil.isValid(attachments.getDescription())
					&& attachments.getDescription().startsWith("/public")) {
				// 删除原图
				FilesUtil.delFileByPath(attachments.getDescription());
			}
			SmbUtils smb = SmbUtils.getInstance();
			if (smb.isEnabled()) {
				// 删除缩略图,删除public这一级目录
				smb.smbDelFile(attachments.getFilename());
				if (ValidateUtil.isValid(attachments.getDescription())) {
					// 删除原图
					smb.smbDelFile(attachments.getDescription());
				}
				if (ValidateUtil.isValid(attachments.getDescription())
						&& attachments.getDescription().startsWith("/public")) {
					// 删除原图
					smb.smbDelFile(attachments.getDescription());
				}
			}
		}
		return result;
	}
}
