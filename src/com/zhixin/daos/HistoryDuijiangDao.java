package com.zhixin.daos;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.zhixin.database.DbManager;
import com.zhixin.domain.HistoryDuijiang;

public class HistoryDuijiangDao {

	public void saveHistoryDuijiang(JSONObject jbo) throws JSONException,
			ParseException {
		JSONArray array = jbo.getJSONArray("list");
		JSONObject jboInA;
		HistoryDuijiang historyDuijiang;
		for (int i = 0; i < array.length(); i++) {
			jboInA = array.getJSONObject(i);
			historyDuijiang = DbManager.getDatabase().findUniqueByWhere(
					HistoryDuijiang.class, "duijiangId=" + jboInA.getInt("id"));
			boolean shouldSave;
			if (historyDuijiang == null) {
				shouldSave = true;
				historyDuijiang = new HistoryDuijiang();
				historyDuijiang.setDuijiangId(jboInA.getInt("id"));
			} else {
				shouldSave = false;
			}
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
			historyDuijiang.setDate(sdf.parse(jboInA.getString("date")));
			historyDuijiang.setStatus(jboInA.getInt("status"));
			historyDuijiang.setTitle(jboInA.getString("title"));
			historyDuijiang.setType(jboInA.getInt("type"));
			if (!StringUtils.isBlank(jboInA.getString("coin"))) {
				historyDuijiang.setCoin(jboInA.getInt("coin"));
			}
			if (!StringUtils.isBlank(jboInA.getString("credit"))) {
				historyDuijiang.setCredit(jboInA.getInt("credit"));
			}
			if (shouldSave) {
				DbManager.getDatabase().save(historyDuijiang);
			} else {
				DbManager.getDatabase().update(historyDuijiang);
			}
		}

	}


    public void changeStatus(int lotteryId,int status) {
        if (DbManager.getDatabase().tableExists(HistoryDuijiang.class)) {
            HistoryDuijiang historyDuijiang=DbManager.getDatabase().findUniqueBySql(HistoryDuijiang.class,"select * from history_duijiang where duijiangId="+lotteryId+";");
            historyDuijiang.setStatus(status);
            DbManager.getDatabase().update(historyDuijiang);
        }
    }
	// private void deleteAll() {
	// if (DbManager.getDatabase().tableExists(HistoryDuijiang.class)) {
	// String sql = "delete from history_duijiang";
	// DbManager.getDatabase().exeCustomerSql(sql);
	// }
	//
	// }

}
