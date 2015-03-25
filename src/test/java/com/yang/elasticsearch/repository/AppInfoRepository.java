package com.yang.elasticsearch.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import com.yang.elasticsearch.model.AppInfo;

public interface AppInfoRepository extends ElasticsearchRepository<AppInfo, String> {

	Page<AppInfo> findByAppName(String appName, Pageable pageable);

	Page<AppInfo> findByPackageName(String packageName, Pageable pageable);

	Page<AppInfo> findByCountry(String country, Pageable pageable);
}
