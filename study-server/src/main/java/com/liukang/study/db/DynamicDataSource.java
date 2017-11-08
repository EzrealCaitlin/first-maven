package com.liukang.study.db;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * TODO: 增加描述
 * 
 * @author liukang
 * @date 2015年9月11日 下午9:35:23
 * @version 0.1.0 
 * @copyright liukang.com
 */
public class DynamicDataSource extends AbstractRoutingDataSource {

	@Override
	protected Object determineCurrentLookupKey() {
		return DataSourceHolder.getDataSource();
	}

}
