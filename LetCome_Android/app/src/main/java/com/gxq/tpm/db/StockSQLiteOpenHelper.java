package com.gxq.tpm.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;

import com.letcome.App;
import com.gxq.tpm.mode.Stock;
import com.gxq.tpm.mode.StockList;
import com.gxq.tpm.tools.Print;
import com.gxq.tpm.tools.Util;
import com.letcome.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class StockSQLiteOpenHelper extends SQLiteOpenHelper {
	private final static String TAG 				= "StockSQLiteOpenHelper";

	public final static String DB_NAME 				= "stocklist.db";
	public final static String TABLE_NAME 			= "stocklist";
	public final static String COLUMN_ID 			= "id";
	public final static String COLUMN_STOCK_CODE 	= "stock_code";
	public final static String COLUMN_STOCK_NAME	= "stock_name";
	public final static String COLUMN_STOCK_ABBR	= "stock_abbr";
	public final static String COLUMN_CREATE_TIME	= "create_time";
	public final static String COLUMN_UPDATE_TIME	= "update_time";
	public final static String COLUMN_PLATE_TYPE	= "plate_type";
	public final static String COLUMN_CODE_SHSZ		= "code_shsz";
	
	private final static int DB_VERSION		= 1;
	private final static String DB_CACHE_PATH	= Environment.getExternalStorageDirectory().getAbsolutePath() + 
			App.instance().getString(R.string.cache_forever_path);
	
	private static final String DB_CREATE = "create table if not exists " +
			TABLE_NAME + "(" +
			"id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
			COLUMN_STOCK_CODE + " text not null, " +
			COLUMN_STOCK_NAME + " text not null, " +
			COLUMN_STOCK_ABBR + " text not null, "+
			COLUMN_CREATE_TIME + " text, " +
			COLUMN_UPDATE_TIME + " text, " +
			COLUMN_PLATE_TYPE + " text, " +
			COLUMN_CODE_SHSZ + " text)";
	
	private Object mUpdateDBLockStock = new Object();
	private Context mContext;
	
	public StockSQLiteOpenHelper(Context context) {
		this(context, DB_VERSION);
	}
	
	public StockSQLiteOpenHelper(Context context, int version) {
		super(context, DB_NAME, null, version, null);
		mContext = context;
	}

	@Override
	public void onCreate(final SQLiteDatabase db) {
		db.execSQL(DB_CREATE);
		
		new Thread(){
			public void run() {
				createStockDataBase(db);
			};
		}.start();
	}
	
	private boolean createStockDataBase(SQLiteDatabase stockDB) {
		synchronized (mUpdateDBLockStock) {
			String cacheFilePath = DB_CACHE_PATH + StockSQLiteOpenHelper.DB_NAME;
			File cacheDir = new File(cacheFilePath).getParentFile();
			
			if (!new File(cacheFilePath).exists()) {
				if (!cacheDir.exists()) {
					cacheDir.mkdirs();
				}
				copyDB(StockSQLiteOpenHelper.DB_NAME, cacheFilePath);
			}
			
			try {
				SQLiteDatabase db = mContext.openOrCreateDatabase(cacheFilePath, Context.MODE_PRIVATE, null);
				
				Cursor cursor = db.query(StockSQLiteOpenHelper.TABLE_NAME, 
						new String[]{StockSQLiteOpenHelper.COLUMN_STOCK_NAME, 
							StockSQLiteOpenHelper.COLUMN_STOCK_CODE, 
							StockSQLiteOpenHelper.COLUMN_STOCK_ABBR, 
							StockSQLiteOpenHelper.COLUMN_CREATE_TIME,
							StockSQLiteOpenHelper.COLUMN_UPDATE_TIME,
							StockSQLiteOpenHelper.COLUMN_PLATE_TYPE,
							StockSQLiteOpenHelper.COLUMN_CODE_SHSZ},
						null, null, null, null, StockSQLiteOpenHelper.COLUMN_ID);
				
				
				stockDB.beginTransaction();
				try {
					while (cursor.moveToNext()) {
						ContentValues values = new ContentValues();
						addColumn(StockSQLiteOpenHelper.COLUMN_STOCK_NAME, cursor, values);
						addColumn(StockSQLiteOpenHelper.COLUMN_STOCK_CODE, cursor, values);
						addColumn(StockSQLiteOpenHelper.COLUMN_STOCK_ABBR, cursor, values);
						addColumn(StockSQLiteOpenHelper.COLUMN_CREATE_TIME, cursor, values);
						addColumn(StockSQLiteOpenHelper.COLUMN_UPDATE_TIME, cursor, values);
						addColumn(StockSQLiteOpenHelper.COLUMN_PLATE_TYPE, cursor, values);
						addColumn(StockSQLiteOpenHelper.COLUMN_CODE_SHSZ, cursor, values);
						
						stockDB.insert(StockSQLiteOpenHelper.TABLE_NAME, null, values);
	//					Log.i(TAG, StockSQLiteOpenHelper.COLUMN_STOCK_NAME + 
	//							cursor.getString(cursor.getColumnIndex(StockSQLiteOpenHelper.COLUMN_STOCK_NAME)));
					}
					stockDB.setTransactionSuccessful();
					
					cursor.close();
					return true;
				} catch (SQLiteException e) {
					Print.i(TAG, e.getMessage());
				} finally {
					stockDB.endTransaction();
				}
				
			} catch (SQLiteException e) {
				Print.i(TAG, e.getMessage());
			}
		}
		return false;
	}
	
	private boolean copyDB(String dbName, String destFile) {
		InputStream inputStream = null;
		OutputStream outputStream = null;
		try {
			inputStream = mContext.getAssets().open(dbName);
			outputStream = new FileOutputStream(destFile);
			
			byte[] bytes = new byte[1024];
			int length = -1;
			while ((length = inputStream.read(bytes)) >= 0) {
				outputStream.write(bytes, 0, length);
			}
			return true;
		} catch (IOException e) {
			Print.i(TAG, e.getMessage());
		} finally {
			if (outputStream != null) {
				try {
					outputStream.close();
				} catch (IOException e) {
				}
			}
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e) {
				}
			}
		}
		return false;
	}
	
	private void addColumn(String columnName, Cursor cursor, ContentValues values) {
		values.put(columnName, getColumn(cursor, columnName));
	}
	
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	}
	
	protected List<Stock> stockQuery(String name) {
		String colName = "";
		if (Util.isChinese(name)) {
			colName = COLUMN_STOCK_NAME;
		} else if(Util.isNumeric(name)) {
			colName = COLUMN_STOCK_CODE;
		} else {
			colName = COLUMN_STOCK_ABBR;
		}
		
		String sql = "select * from " + TABLE_NAME + " where " + colName + " like '%" + name + "%'";
		
		List<Stock> stockList = new ArrayList<Stock>();
		synchronized (mUpdateDBLockStock) {
			SQLiteDatabase db = getReadableDatabase();
			Cursor cursor = null;
			try {
				cursor = db.rawQuery(sql, null);
				while (cursor.moveToNext()) {
					Stock stock = new Stock();
					stock.setStockName(getColumn(cursor, COLUMN_STOCK_NAME));
					stock.setStockCode(getColumn(cursor, COLUMN_STOCK_CODE));
					stock.setStockAbbr(getColumn(cursor, COLUMN_STOCK_ABBR));
					stock.setPlateType(getColumn(cursor, COLUMN_PLATE_TYPE));
					stock.setCreateTime(getColumn(cursor, COLUMN_CREATE_TIME));
					stockList.add(stock);
				}
			} catch (SQLException e) {
				
			} finally {
				if (cursor != null)
					cursor.close();
			}
		}
		return stockList;
	}
	
	private String getColumn(Cursor cursor, String columnName) {
		return cursor.getString(cursor.getColumnIndex(columnName));
	}
	
	protected Stock stockQueryByCode(String code) {
		String colName = COLUMN_STOCK_CODE;
				
		String sql = "select * from " + TABLE_NAME + " where " + colName + " like '%" + code + "%'";
		
		Stock stock = new Stock();
		synchronized (mUpdateDBLockStock) {
			
			SQLiteDatabase db = getReadableDatabase();
			Cursor cursor = null;
			try {
				cursor = db.rawQuery(sql, null);
				while (cursor.moveToNext()) {
					stock.setStockName(getColumn(cursor, COLUMN_STOCK_NAME));
					stock.setStockCode(getColumn(cursor, COLUMN_STOCK_CODE));
					stock.setStockAbbr(getColumn(cursor, COLUMN_STOCK_ABBR));
					stock.setCreateTime(getColumn(cursor, COLUMN_CREATE_TIME));
				}
			} catch (SQLException e) {
				
			} finally {
				if (cursor != null)
					cursor.close();
			}
		}
		return stock;
	}
	
	protected void updateDbStock(StockList stockList) {
		synchronized (mUpdateDBLockStock) {
			SQLiteDatabase stockDB = getWritableDatabase();
			try {
				stockDB.beginTransaction();
				
				stockDB.delete(TABLE_NAME, null, null);
				
				if (stockList.stockList != null) {
					for (Stock stock : stockList.stockList) {
						ContentValues values = new ContentValues();
						values.put(StockSQLiteOpenHelper.COLUMN_STOCK_NAME, stock.getStockName());
						values.put(StockSQLiteOpenHelper.COLUMN_STOCK_CODE, stock.getStockCode());
						values.put(StockSQLiteOpenHelper.COLUMN_STOCK_ABBR, stock.getStockAbbr());
						values.put(StockSQLiteOpenHelper.COLUMN_CREATE_TIME, stock.getCreateTime());
						values.put(StockSQLiteOpenHelper.COLUMN_UPDATE_TIME, stock.getUpdateTime());
						values.put(StockSQLiteOpenHelper.COLUMN_PLATE_TYPE, stock.getPlateType());
						values.put(StockSQLiteOpenHelper.COLUMN_CODE_SHSZ, stock.getCodeShsz());
						
						stockDB.insert(StockSQLiteOpenHelper.TABLE_NAME, null, values);
					}
				}
				stockDB.setTransactionSuccessful();
			} catch (Exception e) {
				
			} finally {
				stockDB.endTransaction();
			}
		}
	}

}
