package com.liukang.study.tools;

import java.util.Random;

public class RandomUtil {
	public static String getRendomStr(int len)
	{
		String fields = "0123456789asdfghjklzcvbnmqwertyuiop";
        String validateCode = "";
        Random random = new Random();
        for (int i = 0; i < len; i++)
        {
            validateCode = fields.charAt(random.nextInt(fields.length())) + validateCode;
        }
        return validateCode;
	}
	
	public static String getRendomStr()
	{
		return getRendomStr(6);
	}
	
}
