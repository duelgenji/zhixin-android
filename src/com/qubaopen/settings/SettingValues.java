package com.qubaopen.settings;

import android.os.Environment;

public class SettingValues {
	// Settings Url prefix

	// public static final String URL_PREFIX = "http://qubaopen.com.cn/demo/";
//	public static final String URL_PREFIX = "http://10.0.0.88:8080/know-heart/";
	// public static final String URL_PREFIX = "http://10.0.0.16:8081/";
	 public static final String URL_PREFIX =
	 "http://115.28.176.74:8080/know-heart/";
	// Setttings in quceshi
	public static final int QU_LIST_BY_DEFAULT_NEXT_ROUND_NUM = 10;

	// Current memberId settings,it
	// save the key of the current active memberId after you log on
	public static final String FILE_NAME_SETTINGS = "fileNameSettings";

	public static final String KEY_TEMP_USER_PHONE_FOR_REGIST_USE = "keyTempUserPhoneForRegistUse";

	public static final String KEY_CURRENT_ADDRESS_SAVED = "keyCurrentAddressSaved";

	public static final String KEY_CURRENT_ACTIVE_USER_ID = "keyCurrentActiveUserId";

	public static final String KEY_CURRENT_ACTIVE_USER_PHONE = "keyCurrentActiveUserPhone";

	public static final String KEY_CURRENT_ACTIVE_USER_NICKNAME = "keyCurrentActiveUserNickname";

	public static final String KEY_PHONE_HEIGHT = "keyPhoneHeight";

	public static final String KEY_PHONE_WIDTH = "keyPhoneWidth";

	public static final String KEY_PHONE_DENSITY = "keyPhoneDensity";

	public static final int COUNT_DOWN_TIME_FOR_VALIDATE_MESSAGE = 10;

	public static final int MAX_ONCE_GET_MESSAGE = 15;

	public static final int CHAT_HEIGHT_IN_DP = 60;

	public static final String COUNTDOWN_TIMER_RECEIVE_ACTION = "countdownTimerReveiveAction";

	// Fragment name
	public static final String FRAGMENT_ROOT_FRAGMENT = "rootFragment";
	public static final String FRAGMENT_QUDUIJIANG_FRAGMENT = "quduijiangFragment";
	public static final String FRAGMENT_QUCESHI_FRAGMENT = "quceshiFragment";
	public static final String FRAGMENT_MORE_FRAGMENT = "moreFragment";
	public static final String FRAGMENT_QUSHEJIAO_FRAGMENT = "qushejiaoFragment";

	// save path
	public static final String PATH_USER_PREFIX = "/qubaopen/headerIcon/";
	public static final String PATH_SHAREPIC_PREFIX = "/qubaopen/sharePic/";

	// ranklist cate
	public static final int RANK_ALL = 0;
	public static final int RANK_MONTH = 1;
	public static final int RANK_WEEK = 2;

	public static final String TEMP_PHOTO_FILE_NAME = "temp.jpg";
	public static final String TEMP_SHAREPIC_PHOTO_FILE_NAME = "sharepic.png";

	public static final String TEMP_PHOTO_FILE_PATH = Environment
			.getExternalStorageDirectory()
			+ PATH_USER_PREFIX
			+ TEMP_PHOTO_FILE_NAME;

	public static final String SHAREPIC_PHOTO_FILE_PATH = Environment
			.getExternalStorageDirectory()
			+ PATH_SHAREPIC_PREFIX
			+ TEMP_SHAREPIC_PHOTO_FILE_NAME;

	public static final String JPUSH_ALIAS_PREFIX = "qbp_";

	public static final int MAX_REQUEST_TIME = 12;

	public static final int MAX_TIME_OUT = 25;

	public static final String APP_FIRSTTIME_IN = "isFirstIn";

	public static final String INSTRUCTION_FRIEND_ANSWER = "instruction_friend_answer";

	public static final String INSTRUCTION_FRIEND_RECOMMEND = "instruction_friend_recommend";

	public static final String INSTRUCTION_QUCESHI_ANSWER = "instruction_quceshi_answer";

	public static final String INSTRUCTION_QUCESHI_LIST1 = "instruction_quceshi_list1";

	public static final String INSTRUCTION_QUCESHI_LIST2 = "instruction_quceshi_list2";

	public static final String INSTRUCTION_MAIN1 = "instruction_main1";

	public static final String INSTRUCTION_MAIN2 = "instruction_main2";

	public static final String INSTRUCTION_SELF_LIST = "instruction_self_list";

	public static final String INSTRUCTION_SELF_AGE_SEX = "instruction_self_age_sex";

}
