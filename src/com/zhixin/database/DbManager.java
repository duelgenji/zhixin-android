package com.zhixin.database;

import com.zhixin.settings.MyApplication;

import net.tsz.afinal.FinalDb;

public final class DbManager {

	private static final int DB_VERSION_CODE = 3;

	private static final String PUBLIC_DATABASE_NAME = "qubaopen.db";

	private static FinalDb publicDatabase = null;

	private static FinalDb privateDatabase = null;

	private static boolean databaseSwitch = false;

	public static void setPublicDatabase(FinalDb database) {
		publicDatabase = database;
	}

	public static void setPrivateDatabase(FinalDb database) {
		privateDatabase = database;
	}

	public static FinalDb getPublicDatabase() {
		if (publicDatabase == null) {
			publicDatabase = FinalDb.create(MyApplication.getAppContext(),
					PUBLIC_DATABASE_NAME, true, DB_VERSION_CODE, null);
		}
		return DbManager.publicDatabase;
	}

	public static FinalDb getDatabase() {
		// String phone = CurrentUserHelper.getCurrentPhone();
		// if (phone == null) {
		// while (true) {
		// if (CurrentUserHelper.getCurrentPhone() != null) {
		// phone = CurrentUserHelper.getCurrentPhone();
		// break;
		// }
		// try {
		// Thread.sleep(200);
		// } catch (InterruptedException e) {
		// e.printStackTrace();
		// }
		// }
		// }
		// if (privatedatabase == null) {
		// privatedatabase = crateDatabase(phone);
		// } else {
		// Log.i("private db error",
		// "dbname: "+privatedatabase.getConfig().getDbName());
		// Log.i("private db error", "current phone: "+phone);
		// if (!privatedatabase.getConfig().getDbName().equals(phone)) {
		//
		// privatedatabase = crateDatabase(phone);
		//
		// }
		// }
		// return privatedatabase;

		if (privateDatabase == null) {

			privateDatabase = FinalDb.create(MyApplication.getAppContext(),
					"privatequbaopen", true, DB_VERSION_CODE, null);

		}
		return privateDatabase;
	}

	public static void releseDatabase() {
		databaseSwitch = false;
		privateDatabase = null;
	}

	public static void initPrivateDatabase() {
		databaseSwitch = true;
		privateDatabase = FinalDb.create(MyApplication.getAppContext(),
				"privatequbaopen", true, DB_VERSION_CODE, null);
	}

}
