package com.zhixin.utils;

import org.apache.commons.lang3.StringUtils;

public class MatcherUtil {
	public static boolean validateEmail(String email) {
		if (StringUtils.isEmpty(email)) {
			return false;
		} else {
			return email
					.matches("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");
		}
		// String check = "\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*";
		// Pattern regex = Pattern.compile(check);
		// Matcher matcher = regex.matcher(email);
		// return matcher.matches();
	}

	/**
	 * 判断是否为合法的手机号码
	 */
	public static boolean validateMobile(String mobile) {
		String aa = "^1[3458][0-9]{9}$";

		if (StringUtils.isEmpty(mobile)) {
			return false;
		} else {
			return mobile.matches(aa);
		}
	}

    public static boolean validateMobile86(String mobile) {
        String aa = "^\\+861[3458][0-9]{9}$";

        if (StringUtils.isEmpty(mobile)) {
            return false;
        } else {
            return mobile.matches(aa);
        }
    }

	/**
	 * 判断是否符合标准密码
	 */
	public static boolean validatePassword(String password) {

		if (StringUtils.isEmpty(password)) {
			return false;
		} else {
			return password
					.matches("^(?=.*[A-Za-z])(?=.*[0-9])(?=\\S+$).{8,30}$");
		}
	}

	/**
	 * 判断是否符合标准身份证
	 */
	public static boolean validateId(String id) {

		if (StringUtils.isEmpty(id)) {
			return false;
		} else {
			return id.matches("^\\d{17}([0-9xX])$");
		}
	}

	/**
	 * 判断是否为合法的电话号码
	 */
	public static boolean validateTelephone(String tel) {
		if (StringUtils.isEmpty(tel)) {
			return false;
		} else {
			return tel
					.matches("^((\\+{0,1}\\d{2,3}){0,1})(\\d{3}-\\d{8}|\\d{4}-\\d{8}|\\d{4}-\\d{7})");
		}
	}

	/**
	 * 检查URL是否合法
	 * 
	 * @param url
	 * @return
	 */
	public static boolean validateURL(String url) {
		if (StringUtils.isEmpty(url)) {
			return false;
		} else {
			return url.matches("[a-zA-z]+://[^\\s]*");
		}
	}

	/**
	 * 检查邮编是否合法
	 * 
	 * @param
	 * @return
	 */
	public static boolean validatePostCode(String postcode) {
		if (StringUtils.isEmpty(postcode)) {
			return false;
		} else {
			return postcode.matches("[1-9]\\d{5}(?!\\d)");
		}
	}

	/**
	 * 在公安局（PSB）上验证身份证
	 * 
	 * @param id
	 * @return
	 */
	/*
	 * public static boolean validateIdOnPSB(String id){
	 * System.setProperty("javax.net.ssl.trustStore","CheckID.keystore");
	 * QueryValidatorServicesProxy proxy = new QueryValidatorServicesProxy();
	 * proxy
	 * .setEndpoint("http://gboss.id5.cn/services/QueryValidatorServices?wsdl");
	 * QueryValidatorServices service = proxy.getQueryValidatorServices();
	 * String userName = "username";//用户名 String password = "password";//密码
	 * System.setProperty("javax.net.ssl.trustStore", "CheckID.keystore");
	 * String resultXML = ""; String datasource = "1A020201";//数据类型 //单条 String
	 * param = "刘丽萍,210204196501015829";//输入参数 resultXML =
	 * service.querySingle(userName, password, datasource, param); //批量 String
	 * params =
	 * " 王茜,150202198302101248; 吴晨晨,36252519821201061x; 王鹏,110108197412255477";
	 * resultXML = service.queryBatch(userName, password, datasource, params);
	 * 
	 * return true; }
	 */

	/**
	 * 检验身份证是否合法
	 * 
	 * @param arrIdCard
	 * @return
	 */
	public static boolean isIdCard(String arrIdCard) {
		int sigma = 0;
		Integer[] a = { 7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2 };
		String[] w = { "1", "0", "X", "9", "8", "7", "6", "5", "4", "3", "2" };
		for (int i = 0; i < 17; i++) {
			int ai = Integer.parseInt(arrIdCard.substring(i, i + 1));
			int wi = a[i];
			sigma += ai * wi;
		}
		int number = sigma % 11;
		String check_number = w[number];
		// return check_number;
		
		if (!arrIdCard.substring(17).equalsIgnoreCase(check_number)) {
			return false;
		} else {
			return true;
		}
	}


    public static boolean validateNormalString(String string) {
        if (StringUtils.isEmpty(string)) {
            return false;
        } else {
            return string.matches("^[\\u4E00-\\u9FA5\\uF900-\\uFA2D\\w]+$");
        }
    }

    public static boolean validatePwd(String pwd) {
        return pwd.matches("^[a-zA-Z0-9_]{8,30}$");
    }

    public static boolean validateNumber(String number) {
        return number.matches("^\\d+$");
    }

}
