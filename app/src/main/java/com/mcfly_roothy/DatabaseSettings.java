package com.mcfly_roothy;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DatabaseSettings extends SQLiteOpenHelper
{
	private final Context context;
	public DatabaseSettings(Context context)
	{
		super(context, "Roothy", null, 1, null);
		this.context=context;
	}
	@Override
	public void onCreate(SQLiteDatabase db)
	{//Authorization
		db.execSQL("CREATE TABLE Users(ID INTEGER PRIMARY KEY AUTOINCREMENT, ARACKODU TEXT, ARACPLAKA TEXT, Password TEXT, Session INTEGER)");
		//db.execSQL("CREATE TABLE Bills(ID INTEGER PRIMARY KEY AUTOINCREMENT, FISTIPI TEXT, FISID TEXT, FISNO TEXT, IRSTARIH TEXT, IPTAL TEXT, TARIH TEXT, MUSKODU TEXT, MUSADI TEXT, VERGIDAIRESI TEXT, VERGINO TEXT, DEPOKODU TEXT, ODEMETIPI TEXT, TOPLAMBRUT TEXT, TOPLAMISKONTO TEXT, TOPLAMKDV TEXT, TOPLAMNET TEXT, SATISTEMSILCISIKODU TEXT, DAGITICIKODU TEXT, ARACKODU TEXT, ARACPLAKA TEXT, SEVKNO TEXT, VADETARIHI TEXT)");
		db.execSQL("CREATE TABLE Bills(ID INTEGER PRIMARY KEY AUTOINCREMENT, FISNO TEXT, MUSKODU TEXT, MUSADI TEXT, ADRESI TEXT, ODEMETIPI1 TEXT, ODEMETUTARI1 TEXT, ODEMETIPI2 TEXT, ODEMETUTARI2 TEXT, TOPLAMNET TEXT, SATISTEMSILCISIKODU TEXT, ARACKODU TEXT, ARACPLAKA TEXT, SEVKNO TEXT, Sent INTEGER)");
		//db.execSQL("CREATE TABLE Bills(ID INTEGER PRIMARY KEY AUTOINCREMENT, FISNO TEXT, MUSADI TEXT, SEVKNO TEXT, ODEMETIPI1 TEXT, TUTAR1 TEXT, ODEMETIPI2 TEXT, TUTAR2 TEXT)");
		db.execSQL("CREATE TABLE SafeIade(ID INTEGER PRIMARY KEY AUTOINCREMENT, SEVKNO TEXT, Iade TEXT)");
		db.execSQL("CREATE TABLE SafePremix(ID INTEGER PRIMARY KEY AUTOINCREMENT, SEVKNO TEXT, Premix TEXT)");
	}
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
	{
	}
	public String GetLoginARACKODU()
	{
		SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
		Cursor cursor = sqLiteDatabase.rawQuery("SELECT ARACKODU, Session FROM Users WHERE Session=1", null);
		if(cursor.moveToFirst())
		{
			do
			{
				if (new String(cursor.getString(1)).equals("1"))
				{
					return cursor.getString(0);
				}
			}
			while (cursor.moveToNext());
		}
		sqLiteDatabase.close();
		return "";
	}
	public String SetLoginSession(CharSequence ARACKODU, CharSequence Password)
	{
		SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
		Cursor cursor = sqLiteDatabase.rawQuery("SELECT ID, ARACPLAKA, Session FROM Users WHERE ARACKODU='"+ARACKODU+"' AND Password='"+Password+"'", null);
		if(cursor.moveToFirst())
		{
			do
			{
				ContentValues contentValues=new ContentValues();
				contentValues.put("Session", 1);
				sqLiteDatabase.update("Users", contentValues, "ID="+cursor.getString(0), null);
				sqLiteDatabase.close();
				return cursor.getString(0);
			}
			while (cursor.moveToNext());
		}
		sqLiteDatabase.close();
		return "";
	}
	public void LoginInsert(String ARACKODU, String ARACPLAKA, String Password)
	{
		SQLiteDatabase sqLiteDatabase=this.getReadableDatabase();
		ContentValues contentValues=new ContentValues();
		contentValues.put("ARACKODU", ARACKODU);
		contentValues.put("ARACPLAKA", ARACPLAKA);
		contentValues.put("Password", Password);
		contentValues.put("Session", 1);
		sqLiteDatabase.insert("Users", null, contentValues);
		sqLiteDatabase.close();
	}
	public List<String> GetLogistics(String ARACKODU)
	{
		SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
		Cursor cursor = sqLiteDatabase.rawQuery("SELECT SEVKNO FROM Bills WHERE ODEMETIPI1='0' AND ARACKODU='"+ARACKODU+"' GROUP BY SEVKNO", null);
		List<String> logistics=new ArrayList<String>();
		if(cursor.moveToFirst())
		{
			do
			{
				logistics.add(cursor.getString(0));
			}
			while (cursor.moveToNext());
		}
		sqLiteDatabase.close();
		return logistics;
	}
	public void DataInsert(String[][] billlist, String[][] safeiadelist, String[][] safepremixlist)
	{
		SQLiteDatabase sqLiteDatabase=this.getReadableDatabase();
		for (int i = 0; i < billlist.length; i++)
		{
			ContentValues contentValues=new ContentValues();
			contentValues.put("FISNO", billlist[i][0]);
			contentValues.put("MUSKODU", billlist[i][1]);
			contentValues.put("MUSADI", billlist[i][2]);
			contentValues.put("ADRESI", billlist[i][3]);
			contentValues.put("ODEMETIPI1", billlist[i][4]);
			contentValues.put("ODEMETUTARI1", billlist[i][5]);
			contentValues.put("ODEMETIPI2", billlist[i][6]);
			contentValues.put("ODEMETUTARI2", billlist[i][7]);
			contentValues.put("TOPLAMNET", billlist[i][8]);
			contentValues.put("SATISTEMSILCISIKODU", billlist[i][9]);
			contentValues.put("ARACKODU", billlist[i][10]);
			contentValues.put("ARACPLAKA", billlist[i][11]);
			contentValues.put("SEVKNO", billlist[i][12]);
			if (billlist[i][4].equals("0"))
			{
				contentValues.put("Sent", 0);
			}
			else
			{
				contentValues.put("Sent", 1);
			}
			sqLiteDatabase.insert("Bills", null, contentValues);
		}
		for (int i = 0; i < safeiadelist.length; i++)
		{
			ContentValues contentValues=new ContentValues();
			contentValues.put("SEVKNO", safeiadelist[i][0]);
			contentValues.put("Iade", safeiadelist[i][1]);
			sqLiteDatabase.insert("SafeIade", null, contentValues);
		}
		for (int i = 0; i < safepremixlist.length; i++)
		{
			ContentValues contentValues=new ContentValues();
			contentValues.put("SEVKNO", safepremixlist[i][0]);
			contentValues.put("Premix", safepremixlist[i][1]);
			sqLiteDatabase.insert("SafePremix", null, contentValues);
		}
		sqLiteDatabase.close();
	}
	public List<Bill> GetBills(String logistic)
	{
		SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
		Cursor cursor = sqLiteDatabase.rawQuery("SELECT FISNO, MUSKODU, MUSADI, ADRESI FROM Bills WHERE SEVKNO='"+logistic+"' AND ODEMETIPI1='0'", null);
		List<Bill> billlist=new ArrayList<Bill>();
		if(cursor.moveToFirst())
		{
			do
			{
				billlist.add(new Bill(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getString(3)));
			}
			while (cursor.moveToNext());
		}
		sqLiteDatabase.close();
		return billlist;
	}
	public List<String> GetSalesDetail(String FISNO)
	{//FISNO TEXT, MUSKODU TEXT, MUSADI TEXT, ADRESI TEXT, ODEMETIPI1 TEXT, ODEMETUTARI1 TEXT, ODEMETIPI2 TEXT, ODEMETUTARI2 TEXT, TOPLAMNET TEXT, SATISTEMSILCISIKODU TEXT, ARACKODU TEXT, ARACPLAKA TEXT, SEVKNO TEXT
		SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
		Cursor cursor = sqLiteDatabase.rawQuery("SELECT FISNO, MUSKODU, MUSADI, ADRESI, TOPLAMNET, SATISTEMSILCISIKODU, ODEMETIPI1, ODEMETUTARI1, ODEMETIPI2, ODEMETUTARI2 FROM Bills WHERE FISNO='"+FISNO+"'", null);
		List<String> salesdetail=new ArrayList<String>();
		if(cursor.moveToFirst())
		{
			for (int i = 0; i < cursor.getColumnCount(); i++)
			{
				salesdetail.add(cursor.getString(i));
			}
		}
		sqLiteDatabase.close();
		return salesdetail;
	}
	public void SetPayment(List<String> payment)
	{
		SQLiteDatabase sqLiteDatabase=this.getReadableDatabase();
		ContentValues contentValues=new ContentValues();
		//FISNO, MUSKODU, MUSADI, ADRESI, ODEMETUTARI1, ODEMETUTARI2, TOPLAMNET, SATISTEMSILCISIKODU
		contentValues.put("ODEMETIPI1", payment.get(6));
		contentValues.put("ODEMETUTARI1", payment.get(7));
		contentValues.put("ODEMETIPI2", payment.get(8));
		contentValues.put("ODEMETUTARI2", payment.get(9));
		contentValues.put("Sent", 0);
		sqLiteDatabase.update("Bills", contentValues, "FISNO='"+payment.get(0).toString()+"'", null);
		sqLiteDatabase.close();
	}
	public List<String> GetPaymentsLogistics(String ARACKODU)
	{
		SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
		Cursor cursor = sqLiteDatabase.rawQuery("SELECT SEVKNO FROM Bills WHERE ODEMETIPI1<>'0' AND ARACKODU='"+ARACKODU+"' GROUP BY SEVKNO", null);
		List<String> logistics=new ArrayList<String>();
		if(cursor.moveToFirst())
		{
			do
			{
				logistics.add(cursor.getString(0));
			}
			while (cursor.moveToNext());
		}
		sqLiteDatabase.close();
		return logistics;
	}
	public List<Bill> GetPaymentsToLogistic(String logistic)
	{
		SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
		Cursor cursor = sqLiteDatabase.rawQuery("SELECT FISNO, MUSKODU, MUSADI, ADRESI FROM Bills WHERE SEVKNO='"+logistic+"' AND ODEMETIPI1<>'0'", null);
		List<Bill> billlist=new ArrayList<Bill>();
		if(cursor.moveToFirst())
		{
			do
			{
				billlist.add(new Bill(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getString(3)));
			}
			while (cursor.moveToNext());
		}
		sqLiteDatabase.close();
		return billlist;
	}
	public List<NameValuePair> GetPayments()
	{
		SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
		Cursor cursor = sqLiteDatabase.rawQuery("SELECT FISNO, ODEMETIPI1, ODEMETUTARI1, ODEMETIPI2, ODEMETUTARI2 FROM Bills WHERE ODEMETIPI1<>'0' AND Sent='0'", null);
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		if(cursor.moveToFirst())
		{
			do
			{
				nameValuePairs.add(new BasicNameValuePair("FISNO", cursor.getString(0)));
				nameValuePairs.add(new BasicNameValuePair("ODEMETIPI1", cursor.getString(1)));
				nameValuePairs.add(new BasicNameValuePair("ODEMETUTAR1", cursor.getString(2)));
				nameValuePairs.add(new BasicNameValuePair("ODEMETIPI2", cursor.getString(3)));
				nameValuePairs.add(new BasicNameValuePair("ODEMETUTAR2", cursor.getString(4)));
			}
			while (cursor.moveToNext());
		}
		sqLiteDatabase.close();
		return nameValuePairs;
	}
	public void SetSent(List<NameValuePair> nameValuePairs)
	{
		SQLiteDatabase sqLiteDatabase=this.getReadableDatabase();
		ContentValues contentValues=new ContentValues();
		contentValues.put("Sent", 1);
		for (int i = 0; i < nameValuePairs.size(); i+=5)
		{
			sqLiteDatabase.update("Bills", contentValues, nameValuePairs.get(i).getName()+"='"+nameValuePairs.get(i).getValue()+"'", null);
		}
		sqLiteDatabase.close();
	}
	public HashMap<String, String> LogisticDetails(String logistic)
	{
		SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
		//FISNO, MUSKODU, MUSADI, ADRESI, ODEMETIPI1, ODEMETUTARI1, ODEMETIPI2, ODEMETUTARI2, TOPLAMNET, SATISTEMSILCISIKODU, ARACKODU, ARACPLAKA, SEVKNO, Sent
		HashMap<String, String> logisticdetails=new HashMap<String, String>();
		String[] totaldetails=new String[] {"NakitAdedi", "KrediKartiAdedi", "VeresiAcikHesapAdedi", "SenetAdedi", "IptalAdedi"};
		String[] sumdetails=new String[] {"NakitToplam", "KrediKartToplam", "VeresiAcikHesapToplam", "SenetToplam", "IptalToplam"};
		for (int i = 0; i < totaldetails.length; i++)
		{
			Cursor cursor1 = sqLiteDatabase.rawQuery("SELECT COUNT(ODEMETIPI1), SUM(ODEMETUTARI1) FROM Bills WHERE SEVKNO LIKE '"+logistic+"' AND ODEMETIPI1='"+String.valueOf(i+1)+"'", null);
			Cursor cursor2 = sqLiteDatabase.rawQuery("SELECT COUNT(ODEMETIPI2), SUM(ODEMETUTARI2) FROM Bills WHERE SEVKNO LIKE '"+logistic+"' AND ODEMETIPI2='"+String.valueOf(i+1)+"'", null);
			if(cursor1.moveToFirst() && cursor2.moveToFirst())
			{
				logisticdetails.put(totaldetails[i], String.valueOf(Integer.valueOf(cursor1.getString(0))+Integer.valueOf(cursor2.getString(0))));
				if (cursor1.getString(1)!=null)
				{
					if (cursor2.getString(1)!=null)
					{
						logisticdetails.put(sumdetails[i], new DecimalFormat("0.00").format(Float.valueOf(cursor1.getString(1))+Float.valueOf(cursor2.getString(1))));
					}
					else
					{
						logisticdetails.put(sumdetails[i], new DecimalFormat("0.00").format(Float.valueOf(cursor1.getString(1))));
					}
				}
				else
				{
					logisticdetails.put(sumdetails[i], "0,00");
				}
			}
		}

		Cursor bills = sqLiteDatabase.rawQuery("SELECT COUNT(ODEMETIPI1), SUM(TOPLAMNET) FROM Bills WHERE SEVKNO LIKE '"+logistic+"' AND ODEMETIPI1='0'", null);
		if (bills.moveToFirst())
		{
			logisticdetails.put("YapilmayanTahsilatAdedi", bills.getString(0));
			if (bills.getString(1)!=null)
			{
				logisticdetails.put("YapilmayanTahsilatToplami", new DecimalFormat("0.00").format(Float.valueOf(bills.getString(1))));
			}
			else
			{
				logisticdetails.put("YapilmayanTahsilatToplami", "0,00");
			}
		}

		Cursor payment = sqLiteDatabase.rawQuery("SELECT COUNT(TOPLAMNET), SUM(TOPLAMNET) FROM Bills WHERE SEVKNO LIKE '"+logistic+"' AND ODEMETIPI1<>'0'", null);
		if (payment.moveToFirst())
		{
			logisticdetails.put("Yap�lanTahsilatAdedi", payment.getString(0));
			if (payment.getString(1)!=null)
			{
				logisticdetails.put("Yap�lanTahsilatToplam", new DecimalFormat("0.00").format(Float.valueOf(payment.getString(1))));
			}
			else
			{
				logisticdetails.put("Yap�lanTahsilatToplam", "0,00");
			}
		}

		Cursor billssum = sqLiteDatabase.rawQuery("SELECT COUNT(TOPLAMNET), SUM(TOPLAMNET) FROM Bills WHERE SEVKNO LIKE '"+logistic+"'", null);
		if (billssum.moveToFirst())
		{
			logisticdetails.put("FaturaAdedi", billssum.getString(0));
			if (billssum.getString(1)!=null)
			{
				logisticdetails.put("FaturaToplam", new DecimalFormat("0.00").format(Float.valueOf(billssum.getString(1))));
			}
			else
			{
				logisticdetails.put("FaturaToplam", "0,00");
			}
		}

		Cursor safeiadesum = sqLiteDatabase.rawQuery("SELECT SUM(Iade) FROM SafeIade WHERE SEVKNO LIKE '"+logistic+"'", null);
		if (safeiadesum.moveToFirst())
		{
			if (safeiadesum.getString(0)!=null)
			{
				logisticdetails.put("SafeIade", safeiadesum.getString(0));
			}
			else
			{
				logisticdetails.put("SafeIade", "0");
			}
		}
		Cursor safepremixsum = sqLiteDatabase.rawQuery("SELECT SUM(Premix) FROM SafePremix WHERE SEVKNO LIKE '"+logistic+"'", null);
		if (safepremixsum.moveToFirst())
		{
			if (safepremixsum.getString(0)!=null)
			{
				logisticdetails.put("SafePremix", safepremixsum.getString(0));
			}
			else
			{
				logisticdetails.put("SafePremix", "0");
			}
		}

		sqLiteDatabase.close();
		return logisticdetails;
	}
	public void Delete()
	{
		SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
		sqLiteDatabase.delete("Bills", null, null);
		sqLiteDatabase.delete("SafeIade", null, null);
		sqLiteDatabase.delete("SafePremix", null, null);
		sqLiteDatabase.close();
	}
	public void Logout()
	{
		SQLiteDatabase sqLiteDatabase=this.getReadableDatabase();
		ContentValues contentValues=new ContentValues();
		contentValues.put("Session", 0);
		sqLiteDatabase.update("Users", contentValues, "Session=1", null);
		sqLiteDatabase.close();
	}
}