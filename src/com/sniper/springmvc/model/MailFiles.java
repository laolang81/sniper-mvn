package com.sniper.springmvc.model;

public class MailFiles extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String mail_id;
	private String file_id;

	public String getMail_id() {
		return mail_id;
	}

	public void setMail_id(String mail_id) {
		this.mail_id = mail_id;
	}

	public void setFile_id(String file_id) {
		this.file_id = file_id;
	}

	public String getFile_id() {
		return file_id;
	}

}
