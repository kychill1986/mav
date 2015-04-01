package com.yang.elasticsearch.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

@Document(indexName = "app_index", type = "app_type")
public class AppInfo implements java.io.Serializable {
	
	private static final long serialVersionUID = 5864021808204747002L;
	
	@Id
	private Integer id;
	private Integer rank;
	private String cate;
	private String country;
	private String period;
	private String type;
	private String appCate;
	private String qiiCate;
	private String appStars;
	private String appName;
	private String packageName;
	private String logoUrl;
	private String versionCode;
	private String versionName;
	private String appSize;
	private Integer downloads;
	private int minTerminalVersion;
	private boolean status;
	private String releaseTime;

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getRank() {
		return this.rank;
	}

	public void setRank(Integer rank) {
		this.rank = rank;
	}

	public String getCate() {
		return this.cate;
	}

	public void setCate(String cate) {
		this.cate = cate;
	}

	public String getCountry() {
		return this.country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getPeriod() {
		return this.period;
	}

	public void setPeriod(String period) {
		this.period = period;
	}

	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getAppCate() {
		return this.appCate;
	}

	public void setAppCate(String appCate) {
		this.appCate = appCate;
	}

	public String getQiiCate() {
		return this.qiiCate;
	}

	public void setQiiCate(String qiiCate) {
		this.qiiCate = qiiCate;
	}

	public String getAppStars() {
		return this.appStars;
	}

	public void setAppStars(String appStars) {
		this.appStars = appStars;
	}

	public String getAppName() {
		return this.appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public String getPackageName() {
		return this.packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public String getLogoUrl() {
		return this.logoUrl;
	}

	public void setLogoUrl(String logoUrl) {
		this.logoUrl = logoUrl;
	}

	public String getVersionCode() {
		return this.versionCode;
	}

	public void setVersionCode(String versionCode) {
		this.versionCode = versionCode;
	}

	public String getVersionName() {
		return this.versionName;
	}

	public void setVersionName(String versionName) {
		this.versionName = versionName;
	}

	public String getAppSize() {
		return this.appSize;
	}

	public void setAppSize(String appSize) {
		this.appSize = appSize;
	}

	public Integer getDownloads() {
		return downloads;
	}

	public void setDownloads(Integer downloads) {
		this.downloads = downloads;
	}

	public int getMinTerminalVersion() {
		return this.minTerminalVersion;
	}

	public void setMinTerminalVersion(int minTerminalVersion) {
		this.minTerminalVersion = minTerminalVersion;
	}

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	public String getReleaseTime() {
		return this.releaseTime;
	}

	public void setReleaseTime(String releaseTime) {
		this.releaseTime = releaseTime;
	}

}