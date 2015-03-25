package com.yang.elasticsearch.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import com.yang.elasticsearch.model.AppInfo;

public interface AppInfoRepository extends ElasticsearchRepository<AppInfo, String> {

	// findByAppName根据app名称获取信息列表,这里的"AppName"是和索引属性对应的命名,不可随意修改，之前改成了findByName后报错
	Page<AppInfo> findByAppName(String appName, Pageable pageable);

	Page<AppInfo> findByPackageName(String packageName, Pageable pageable);

	Page<AppInfo> findByCountry(String country, Pageable pageable);
}
