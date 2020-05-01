package utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.codec.binary.Base64;

/**
 * 	文件和字符串互相转换
 *  文件转Base64字符串，Base64字符串转文件
 *  适用于通过json方式传输小文件
 *  
 *  @author Angilin
 *
 */
public class FileStrConvertUtil {

	public static void main(String[] args) {
				
		//System.out.println(getFileStr("E:\\20150409_162132.jpg"));
		//generateFile(getFileStr("E:\\20150409_162132.jpg"),"E:\\","test123.jpg");
		
		System.out.println(getFileStr("D:\\eclipse.rar"));
		
	}

	/**
	 * Java使用Base64编码处理文件转String
	 * 将文件转化为字节数组字符串，并对其进行Base64编码处理
	 * 过大的文件会导致内存溢出
	 *  
	 * @param filePath
	 * @return
	 */
	public static String getFileStr(String filePath) {
		InputStream in;
		try {
			in = new FileInputStream(filePath);
			return getFileStr(in);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return "";
	}
	
	/**
	 * Java使用Base64编码处理文件转String
	 * 将文件转化为字节数组字符串，并对其进行Base64编码处理
	 * 过大的文件会导致内存溢出
	 *  
	 * @param filePath
	 * @return
	 */
	public static String getFileStr(InputStream is) {
		ByteArrayOutputStream outSteam = new ByteArrayOutputStream();  
	    byte[] buffer = new byte[1024];  
	    int len = -1;  
	    try {
			while ((len = is.read(buffer)) != -1) {  
			    outSteam.write(buffer, 0, len);  
			}
		    outSteam.close();  
		    is.close(); 
	    } catch (IOException e) {
			e.printStackTrace();
			return "";
		}  
		Base64 encoder = new Base64();
		return encoder.encodeToString(outSteam.toByteArray());
	}

	/**
	 * 对字节数组字符串进行Base64解码并生成文件
	 * 
	 * @param fileStr
	 * @param filePath
	 * @param fileName
	 * @return
	 */
	public static boolean generateFile(String fileStr, String filePath, String fileName) {
		if (fileStr == null) { 
			return false;
		}
		Base64 decoder = new Base64();
		try {
			// Base64解码
			byte[] bytes = decoder.decode(fileStr);
			String fileFullPath = getFilePath(filePath, fileName); 
			OutputStream out = new FileOutputStream(fileFullPath);
			out.write(bytes);
			out.flush();
			out.close();
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * 获取文件路径
	 * 
	 * @param dir
	 * @param fileName
	 * @return
	 */
	public static String getFilePath(String dir, String fileName) {
		String fileSeparator = System.getProperty("file.separator");
		if (!dir.endsWith(fileSeparator)) {
			dir += fileSeparator;
		}
		File file = new File(dir);
		if (!file.isDirectory()) {
			// 如果文件夹不存在就新建
			file.mkdirs();
		}
		return dir + fileName;
	}
}
