package com.liukang.study.tools;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class RandomBatchNumUtils {

	/**
	 * 批次号生成 生成规则：当天日期[8位]+序列号[3至24位]，如：201008010000001
	 * 
	 * @return
	 */
	public String RandomBatchNum() {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
		String format = dateFormat.format(new Date());
		int max = 24;
		int min = 3;
		Random random = new Random();
		int s = random.nextInt(max) % (max - min + 1) + min;
		StringBuffer buffer = new StringBuffer();
		for (int i = 0; i < s; i++) {
			Integer val = (int) (Math.random() * 9 + 1);
			buffer.append(val.toString());
		}
		return format + buffer.toString();
	}
}
