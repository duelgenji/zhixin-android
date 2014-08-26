package com.zhixin.utils;

import com.zhixin.database.DbManager;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.content.CursorLoader;

public class SqlCursorLoader extends CursorLoader {
	// someone think the following code should be included but I donn't know the
	// actual reason,just leave it there
	private final ForceLoadContentObserver mObserver = new ForceLoadContentObserver();

	private String sql;
	private Class clazz[];
    private boolean isPublic;

	public SqlCursorLoader(Context context, String sql, Class... clazz) {
		super(context);
		this.sql = sql;
		this.clazz = clazz;
        this.isPublic=false;
	}

    public SqlCursorLoader(Context context, String sql,boolean isPublic, Class... clazz) {
        super(context);
        this.sql = sql;
        this.isPublic = isPublic;
        this.clazz = clazz;
    }

	@Override
	public Cursor loadInBackground() {
        if(isPublic){
            for (Class aClazz:clazz){
                DbManager.getPublicDatabase().tableExists(aClazz);

            }
            Cursor cursor = DbManager.getPublicDatabase().findAllBySqlReturnCursor(clazz[0],
                    sql);

            if (cursor != null) {
                // Ensure the cursor window is filled
                cursor.getCount();
                cursor.registerContentObserver(mObserver);
            }
            return cursor;

        }else{
            for (Class aClazz:clazz){
                DbManager.getDatabase().tableExists(aClazz);

            }
            Cursor cursor = DbManager.getDatabase().findAllBySqlReturnCursor(clazz[0],
                    sql);

            if (cursor != null) {
                // Ensure the cursor window is filled
                cursor.getCount();
                cursor.registerContentObserver(mObserver);
            }

            return cursor;
        }
    }


}
