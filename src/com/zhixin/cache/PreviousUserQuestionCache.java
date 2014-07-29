package com.zhixin.cache;

import java.util.List;

import com.zhixin.domain.UserQuestionAnswer;

public class PreviousUserQuestionCache {
	
	private static List<? extends UserQuestionAnswer> cacheAnswer;
	
	public static List<? extends UserQuestionAnswer> getInstance(){
		return cacheAnswer;
	}
	
	public static void saveCache(List<? extends UserQuestionAnswer> cache){		
		cacheAnswer = cache;
	}
	
	public static void clearCache(){
		cacheAnswer = null;
		
	}

}
