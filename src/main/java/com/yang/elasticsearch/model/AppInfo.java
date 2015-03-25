package com.yang.elasticsearch.model;

import java.io.Serializable;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldIndex;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Document(indexName = "app_index", type = "app_type")
public class AppInfo implements Serializable {

	private static final long serialVersionUID = -5873691974645631359L;

	@Id
	private String id;

	@Field(type = FieldType.String, store = true)
	private String cate;

	@Field(type = FieldType.String, store = true, index = FieldIndex.analyzed)
	private String country;

	@Field(type = FieldType.String, store = true)
	private String appName;

	@Field(type = FieldType.String, store = true)
	private String packageName;

	@Field(type = FieldType.Integer, store = true)
	private Integer minTerminalVersion;

	public AppInfo() {
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCate() {
		return cate;
	}

	public void setCate(String cate) {
		this.cate = cate;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public Integer getMinTerminalVersion() {
		return minTerminalVersion;
	}

	public void setMinTerminalVersion(Integer minTerminalVersion) {
		this.minTerminalVersion = minTerminalVersion;
	}

}