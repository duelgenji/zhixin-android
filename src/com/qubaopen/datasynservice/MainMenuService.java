package com.qubaopen.datasynservice;

import java.text.ParseException;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.qubaopen.R;
import com.qubaopen.settings.MyApplication;
import com.qubaopen.settings.SettingValues;
import com.qubaopen.utils.HttpClient;


public class MainMenuService {
//	public static class MainMenuServiceSqlMaker {
//		public static String makeSql(int cate) {
//			String sql;
//			switch (cate) {
//			case MainmenuFragment.LOADER_ROLLING_INFO:
//				sql = "select * from rolling_information";
//				break;
//			case MainmenuFragment.LOADER_QU_GONGGAO:
//				sql = "select * from qu_gong_gao";
//				break;
//			default:
//				sql = null;
//				break;
//			}
//			return sql;
//		}
//
//	}

	private Context context;

//	private RollingInformationDao rollingInformationDao;
//
//	private QuGongGaoDao qugonggaoDao;
//
	public MainMenuService(Context context) {
		this.context = context;
//		rollingInformationDao = new RollingInformationDao();
//		qugonggaoDao = new QuGongGaoDao();
	}

//	public String saveRollingInfo() throws JSONException {
//		String requestUrl = SettingValues.URL_PREFIX
//				+ context.getString(R.string.URL_ROLLING_LIST_INFO);
//		JSONObject result = HttpClient.requestSync(requestUrl, null);
//		if (result != null && result.getInt("success") == 1) {
//			try {
//				rollingInformationDao.saveRollingInfomation(result);
//			} catch (JSONException e) {
//				e.printStackTrace();
//			}
//			return null;
//		} else if (result != null && result.getInt("success") == 0) {
//			return result.getString("message");
//		}
//		return null;
//	}

//	public String saveGonggaoInfo() throws JSONException, ParseException {
//		String requestUrl = SettingValues.URL_PREFIX
//				+ context.getString(R.string.URL_QU_GONG_GAO_LIST);
//		JSONObject result = HttpClient.requestSync(requestUrl, null);
//		if (result != null && result.getInt("success") == 1) {
//			try {
//				qugonggaoDao.saveQuGongGao(result);
//			} catch (JSONException e) {
//				e.printStackTrace();
//			}
//			return null;
//		} else if (result != null && result.getInt("success") == 0) {
//			return result.getString("message");
//		}
//		return null;
//	}


    public String newVersion() throws JSONException, ParseException {
        try {
            String requestUrl = SettingValues.URL_PREFIX
                    + context.getString(R.string.URL_NEW_VERSION);


            PackageManager packageManager =  MyApplication.getAppContext().getPackageManager();
            // getPackageName()是你当前类的包名，0代表是获取版本信息
            PackageInfo packInfo = packageManager.getPackageInfo(MyApplication.getAppContext().getPackageName(),0);
            String version = packInfo.versionCode+"";
            JSONObject jsonParams = new JSONObject();
            jsonParams.put("iLx", "0");
            jsonParams.put("iVersion", version);
            JSONObject result = HttpClient.requestSync(requestUrl, jsonParams);
            if (result != null && result.getInt("success") == 1) {
                return result.getString("path");
            }else{
                if(result.has("message")){
                    return "isNew";
                }else{
                    return "";
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

}