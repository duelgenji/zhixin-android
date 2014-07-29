package com.zhixin.database;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;

import com.zhixin.daos.UserLevelDao;
import com.zhixin.domain.UserLevel;

import net.tsz.afinal.FinalDb;
import android.app.Activity;
import android.os.Environment;

public class Initial {

	private Activity context;

	public Initial(Activity context) {
		this.context = context;

	}

	public void initDb() throws IOException {
        if (!DbManager.getPublicDatabase().tableExists(UserLevel.class)){
//				&& !DbManager.getPublicDatabase()
//						.tableExists(BaoBaoMarketLevel.class)) {
			BufferedReader reader = new BufferedReader(new InputStreamReader(
//				用户等级数据库
					context.getAssets().open("yhdjsql.txt")));
			String sql = reader.readLine();
			new UserLevelDao().saveAllInfo(sql);

//			reader = new BufferedReader(new InputStreamReader(context
////					宝宝等级数据库
//					.getAssets().open("baobaoDjsql.txt")));
//			sql = reader.readLine();
			DbManager.getPublicDatabase().exeCustomerSql(sql);
		}

	}

}
