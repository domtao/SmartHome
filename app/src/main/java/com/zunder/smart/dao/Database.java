package com.zunder.smart.dao;

import java.io.File;
import com.zunder.smart.MyApplication;
import com.zunder.smart.setting.ProjectUtils;
import com.zunder.smart.MyApplication;

import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class Database {
	private String db_name = "homedatabases.db";

	private SQLiteDatabase db;

	public Database() {
		open();
	}
	/**
	 */
	public void close() {
		if (db != null) {
			db.close();
			db = null;
		}
	}
	/**
	 */
	private void open() throws SQLException {
		try {
			String databaseFilename = ProjectUtils.getRootPath().getRootPath();
			if (new File(databaseFilename).exists()) {
				db = SQLiteDatabase
						.openOrCreateDatabase(databaseFilename, null);
			} else {
				databaseFilename = MyApplication.getInstance().getRootPath()
						+ File.separator + "Root" + File.separator + db_name;
				db = SQLiteDatabase
						.openOrCreateDatabase(databaseFilename, null);
			}

		} catch (Exception e) {
			System.out.println("Database_Exception:" + e);
		}
	}
	public SQLiteDatabase getSQLiteDatabase() {
		return this.db;
	}
}
