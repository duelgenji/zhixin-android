package com.zhixin.datasynservice;

import java.text.ParseException;

import net.tsz.afinal.http.AjaxParams;

import org.json.JSONException;
import org.json.JSONObject;

import com.zhixin.R;
import com.zhixin.daos.InterestListDao;
import com.zhixin.daos.QuListDao;
import com.zhixin.settings.SettingValues;
import com.zhixin.utils.HttpClient;

import android.content.Context;

public class InterestListService {
	public static class QuceshiSqlMaker {
//		public static final int ALL_TEST = 0;
//		public static final int XINGE_TEST = 1;
//		public static final int QINGAN_TEST = 2;
//		public static final int ZHICHANG_TEST = 3;
//		public static final int XINGZUO_TEST = 4;
//		public static final int ZHILI_TEST = 5;
//		public static final int QITA_TEST = 6;

		public static final int DEFAULT_ORDER = 0;
		public static final int TUIJIAN_ORDER = 0;
		public static final int POP_ORDER = 1;
		public static final int TIME_ORDER = 2;
		public static final int CREDIT_ORDER = 3;

		public static String makeSql() {
			StringBuffer sqlBuffer = new StringBuffer();
			sqlBuffer.append("select * from interest_list");
//			if (type != ALL_TEST) {
//				sqlBuffer.append(" where type=");
//				sqlBuffer.append(type);
//				sqlBuffer.append(" and wjorder=");
//				sqlBuffer.append(order);
//				sqlBuffer.append(" and controlFlag=0");
//			} else {
				//sqlBuffer.append(" where wjorder=");
				//sqlBuffer.append(order);
				//.append(" and controlFlag=0");
//			}
			//sqlBuffer.append(" order by _id asc");
			return sqlBuffer.toString();

		}
	}
	private Context context;
	private InterestListDao interestListDao;

	public InterestListService(Context context) {
		this.context = context;
		this.interestListDao = new InterestListDao();
	}

	
	
	public JSONObject getInterestList(Integer type, Integer order,Integer page)
			throws JSONException, ParseException {
		String url = SettingValues.URL_PREFIX
				+ context.getString(R.string.URL_INTEREST_GET_LIST);
		AjaxParams requestParams  = new AjaxParams();
		if (type!=null) {
			requestParams.put("interestTypeId",type.toString());
		}
		if (order!=null) {
			requestParams.put("sortTypeId",order.toString());
		}
		if (page!=null) {
			requestParams.put("page",page.toString());
		}
		JSONObject result = HttpClient.requestSync(url, requestParams,HttpClient.TYPE_POST_NORMAL);
		if (result != null && result.getString("success").equals("1")) {

			requestParams.put("page","1");
			interestListDao.saveInterestList(result);

		}
		return result;

	}

}
