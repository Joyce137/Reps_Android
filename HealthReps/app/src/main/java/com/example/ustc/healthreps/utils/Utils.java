package com.example.ustc.healthreps.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import com.example.ustc.healthreps.R;
import com.example.ustc.healthreps.serverInterface.ControlMsg;
import com.example.ustc.healthreps.serverInterface.Types;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {
	// 大小端转化问题
	// short转化成byte数组
	public static byte[] shortToLH(int n) {
		byte[] b = new byte[2];
		b[0] = (byte) (n & 0xff);
		b[1] = (byte) (n >> 8 & 0xff);
		return b;
	}

	// int转化成byte数组
	public static byte[] toLH(int n) {
		byte[] b = new byte[4];
		b[0] = (byte) (n & 0xff);
		b[1] = (byte) (n >> 8 & 0xff);
		b[2] = (byte) (n >> 16 & 0xff);
		b[3] = (byte) (n >> 24 & 0xff);
		return b;
	}

	// byte数组转化为int
	public static int vtolh(byte[] bArr) {
		byte begin = bArr[0];
		if (begin < 0) {
			short n = 0;
			for (int i = 0; i < bArr.length && i < 4; i++) {
				int left = i * 8;
				n += (bArr[i] << left);
			}
			return n + 256;
		} else {
			int n = 0;
			for (int i = 0; i < bArr.length && i < 4; i++) {
				int left = i * 8;
				n += (bArr[i] << left);
			}
			if(n<0){
				n = n + 65536;
			}
			return n;
		}
	}

	// 交换对象
	public static void SWAP(Object a, Object b) {
		Object c = a;
		a = b;
		b = c;
	}

	// 生成随机字符串
	public static String getRandomStr() {
		int SIZE = 18;
		String CCH = "_0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ_";
		char[] ch = new char[SIZE];
		for (int i = 0; i < SIZE - 1; ++i) {
			int x = (int) (Math.random() * 100 % (CCH.length() - 1));
			ch[i] = CCH.charAt(x);
		}
		String str = new String(ch);

		return str;
	}

	// 使用正则表达式过滤非数字的字符串
	public static String filterUnNumber(String str) {
		// 只允数字
		String regEx = "[^0-9]";
		Pattern p = Pattern.compile(regEx);
		Matcher m = p.matcher(str);
		// 替换与模式匹配的所有字符（即非数字的字符将被""替换）
		return m.replaceAll("").trim();
	}

	// 获取当前日期
	public static String getDate() {
		// 日期
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
		return formatter.format(new Date());
	}

	public static int getYear(){
		return new Date().getYear();
	}

	// 获取当前时间
	public static String getTime() {
		// 时间
		SimpleDateFormat formatter = new SimpleDateFormat("HHmmss");
		return formatter.format(new Date());

	}

	// 获取当前日期和时间作为文件名
	public static String getDataAndTime(){
		return getDate()+"-"+getTime();
	}
	
	//判断是否仅包括数字、下划线和字母
	public static boolean onlyIncludingNumber_Letter(String str){
		for(int i = 0;i<str.length();i++){
			char ch = str.charAt(i);
			if((ch>='0'&&ch<='9')||(ch>='a'&&ch<='z')||(ch>='a'&&ch<='z')||ch == '_'){
				continue;
			}
			else{
				return false;
			}
		}
		return true;
	}

	//密码加密
	public static byte[] encryptPwd(String pwd){
		String str = pwd;
		byte crcPwd[] = str.getBytes();
		for (int i = 0; i < crcPwd.length; i++)
			crcPwd[i] = 0;
		byte strb[] = str.getBytes();
		for (int i = 0; i < str.length(); i++) {
			crcPwd[i] = strb[i];
		}
		CRC4 crc = new CRC4();
		byte b[] = Types.AES_KEY.getBytes();
		crc.Encrypt(crcPwd, b);

		return crcPwd;
	}

	//获取当前时间
	public static String getCurrentTime()
	{
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date curDate = new Date(System.currentTimeMillis());
		String str = format.format(curDate);
		System.out.println(str);
		return str;
	}

	//获取现在时间
	public static String getFileNameDate(String filename) {
		//拆分
		String[] fileNameArray = filename.split("-");
		String dataStr = fileNameArray[4];

		//判断是否为时间串
		if(Utils.filterUnNumber(dataStr)== dataStr && dataStr.length() == 8){
			return dataStr;
		}
		//如果不是，则使用现在日期
		return Utils.getDate();
	}

	//type(String)->int
	public static int changeTypeToInt(String type){
		if(type.equals("患者")){
			return Types.USER_TYPE_PATIENT;
		}
		else if(type == "医生"){
			return Types.USER_TYPE_DOCTOR;
		}
		else if(type == "药师"){
			return Types.USER_TYPE_PHA;
		}
		else if(type == "药监局"){
			//药监局;
		}
		return Types.USER_TYPE_STORE;
	}

	//type(int)->String
	public static String changeTypeToString(int type) {
		if (type == Types.USER_TYPE_PATIENT) {
			return "患者";
		} else if (type == Types.USER_TYPE_DOCTOR) {
			return "医生";
		} else if (type == Types.USER_TYPE_PHA) {
			return "药师";
		} else if (type == Types.USER_TYPE_STORE) {
			return "药店";
		} else {
			return "药监局";
		}
	}

	//判断某日期与当前日期的差值(是否一个月以内）
	public static boolean checkValid(String date){
		String now = getDate();
		for(int i = 0;i<6;i++){
			if(now.charAt(i)>date.charAt(i))
				return false;
		}
		return true;
	}

	//Q   判断用户名是否有效
	public static boolean checkUsername(String NumberWord)
	{
		int[] anArray;
		int[] bnArray;
		int i,m;
		int j=0,n=0,k=0;
		while (true) {
			String s = NumberWord;
			bnArray=new int[s.length()];
			anArray=new int[s.length()];

			for(i=0;i<s.length();i++)
			{
				anArray[i]=(int) s.charAt(i);
			}
			//检查用户名中是否包含A~Z，a~z的字
			for(i=0;i<s.length();i++)
				if(anArray[i]<65||anArray[i]>90&anArray[i]<97
						||anArray[i]>122)
				{
					bnArray[j]=anArray[i];
					j++;

				}
			for(m=0;m<bnArray.length;m++)
			{
				if(bnArray[m]!=0&(bnArray[m]>47&bnArray[m]<58))
					n++;
				else if(bnArray[m]!=0&(bnArray[m]<48||bnArray[m]>57))
					k++;
			}
			if(k>0)
			{
				//System.out.println("用户名非法，必须只含有数字和字母");
				return false;
			}
			if(n==s.length())
			{
				//System.out.println("用户名非法，用户名不能全为数字");
				return false;
			}
			if(j==0)
			{
				//System.out.println("用户名非法，用户名不能全为字母");
				return false;
			}
			return true;
		}
	}
	public static boolean checkEmail(String email)
	{
		String s=email;
		int i,m;
		//检测Email的长度
		if(s.length()==0||s.length()>40)
			return false;
		else{
			//检测"@"在字符串中的位置
			for(i=0;i<s.length();i++)
			{
				System.out.println(s.charAt(i));
				if(s.charAt(i)=='@')
					break;
			}
			//检测‘.’在字符串中的位置
			for(m=0;m<s.length();m++)
			{
				if(s.charAt(m)=='.')
					break;
			}
			//判断Email地址中的'@'和'.'是否合法
			if(i==0||i==s.length()-1||m-i<=1||m==s.length()-1)
			{
				System.out.println("Email地址不合法");
				return false;
			}
			else
				return true;
		}

	}
	public static boolean checkNumber(String Number)
	{
		String s=Number;
		int i=0,j=0;
		//检查是否有除数字以外的字符
		for(i=0;i<s.length();i++)
		{
			if(s.charAt(i)>='0'&&s.charAt(i)<='9')
				j++;
			else
			{
				System.out.println("账号名不合法");
				return false;
			}
		}
		//若字符串中不全是数字，则结果为false
		if(j!=s.length())
			return false;
		return true;
	}

	//copy默认头像到手机headphoto路径下
	public static String copyDefaultHeadPhoto(Context context){
		int BUFFER_SIZE = 20000;
		String imagePath = AppPath.getPathByFileType("headphoto");
		AppPath.CheckAndMkdirPathExist(imagePath);
		String imageFile = imagePath+"/defaultHeadPhoto.jpg";
		try {
			File f = new File(imageFile);
			if (!f.exists()) {
				//判断头像文件是否存在，若不存在则执行导入，否则直接返回
				InputStream is = context.getResources().openRawResource(R.raw.ic_perimg); //欲导入的数据库
				FileOutputStream fos = new FileOutputStream(imageFile);
				byte[] buffer = new byte[BUFFER_SIZE];
				int count;
				while ((count = is.read(buffer)) > 0) {
					fos.write(buffer, 0, count);
				}
				fos.close();
				is.close();
			}
			return imageFile;
		} catch (FileNotFoundException e) {
			Log.e("Database", "File not found");
			e.printStackTrace();
		} catch (IOException e) {
			Log.e("Database", "IO exception");
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Try to return the absolute file path from the given Uri
	 *
	 * @param context
	 * @param uri
	 * @return the file path or null
	 */
	public static String getRealFilePath( final Context context, final Uri uri ) {
		if ( null == uri ) return null;
		final String scheme = uri.getScheme();
		String data = null;
		if ( scheme == null )
			data = uri.getPath();
		else if ( ContentResolver.SCHEME_FILE.equals( scheme ) ) {
			data = uri.getPath();
		} else if ( ContentResolver.SCHEME_CONTENT.equals( scheme ) ) {
			Cursor cursor = context.getContentResolver().query( uri, new String[] { MediaStore.Images.ImageColumns.DATA }, null, null, null );
			if ( null != cursor ) {
				if ( cursor.moveToFirst() ) {
					int index = cursor.getColumnIndex( MediaStore.Images.ImageColumns.DATA );
					if ( index > -1 ) {
						data = cursor.getString( index );
					}
				}
				cursor.close();
			}
		}
		return data;
	}
}
