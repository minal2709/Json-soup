package core.scraper.model;

import java.util.Date;

public class JobPost {

	String url;
	String designation;
	String location;
	String jobFamily;
	String jobDescription;
	String experiance;
	String qualification;
	Integer noOfPositions;
	String skill;
	Date removalDate;
	
	public JobPost(String url) {
		this.url = url;
	}
	
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getDesignation() {
		return designation;
	}
	public void setDesignation(String designation) {
		this.designation = designation;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public String getJobFamily() {
		return jobFamily;
	}
	public void setJobFamily(String jobFamily) {
		this.jobFamily = jobFamily;
	}
	public String getJobDescription() {
		return jobDescription;
	}
	public void setJobDescription(String jobDescription) {
		this.jobDescription = jobDescription;
	}
	public String getExperiance() {
		return experiance;
	}
	public void setExperiance(String experiance) {
		this.experiance = experiance;
	}
	public String getQualification() {
		return qualification;
	}
	public void setQualification(String qualification) {
		this.qualification = qualification;
	}
	public Integer getNoOfPositions() {
		return noOfPositions;
	}
	public void setNoOfPositions(Integer noOfPositions) {
		this.noOfPositions = noOfPositions;
	}
	public String getSkill() {
		return skill;
	}
	public void setSkill(String skill) {
		this.skill = skill;
	}
	public Date getRemovalDate() {
		return removalDate;
	}
	public void setRemovalDate(Date removalDate) {
		this.removalDate = removalDate;
	}
	
}
