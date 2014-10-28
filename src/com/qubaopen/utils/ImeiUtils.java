package com.qubaopen.utils;

import org.apache.commons.lang3.StringUtils;

import android.content.Context;
import android.telephony.TelephonyManager;

public class ImeiUtils {

	public static String getImeiCode(Context context) {
		TelephonyManager mngr = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);

		String code = mngr.getDeviceId();
		if (StringUtils.isNotBlank(code)) {
			return code;
		} else {
			return null;
		}
	}

}
