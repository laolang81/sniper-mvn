package com.sniper.springmvc.searchUtil;

public class ProjectScoreStandardUtil {

	private String id;
	private String question;
	private String standard;

	public String getQuestion() {
		return question;
	}

	public void setQuestion(String question) {
		this.question = question;
	}

	public String getStandard() {
		return standard;
	}

	public void setStandard(String standard) {
		this.standard = standard;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return "ProjectScoreStandardUtil [id=" + id + ", question=" + question
				+ ", standard=" + standard + "]";
	}

	public ProjectScoreStandardUtil(String id, String question, String standard) {
		super();
		this.id = id;
		this.question = question;
		this.standard = standard;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ProjectScoreStandardUtil other = (ProjectScoreStandardUtil) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
}
