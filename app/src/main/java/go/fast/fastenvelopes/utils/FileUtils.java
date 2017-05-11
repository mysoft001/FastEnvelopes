package go.fast.fastenvelopes.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

import go.fast.fastenvelopes.http.Constant;


public class FileUtils {

    private static String rootDirectory = Environment
	    .getExternalStorageDirectory().getAbsolutePath();
    public static String SDPATH = rootDirectory + Constant.CACHE_DIR
	    + "photo/thumb/";
    public static String SDPATH1 = rootDirectory + Constant.CACHE_DIR
	    + "photo/";

    private static String fileFloder = rootDirectory + Constant.CACHE_DIR
	    + "chapter/";

    private static String cacheChapterFileFloder = rootDirectory
	    + Constant.CACHE_LOCALCHAPTER_DIR + "chapter/";

    /**
     * 如果文件不存在，就创建文件
     * 
     * @param path
     *            文件路径
     * @return
     */
    public static String createIfNotExist(String path) {
	File file = new File(path);
	if (!file.exists()) {
	    try {
		file.createNewFile();
	    } catch (Exception e) {
		System.out.println(e.getMessage());
	    }
	}
	return path;
    }

    // 向SD卡写入数据
    public static boolean writeCacheChapter2SDcard(String str,
	    String articleId, String chapterName, Context context) {

	return writeSDcard(str, getPathByCacheChapter(articleId, chapterName),
		context);
    }

    // 向SD卡写入数据并返回路径
    public static String writeCacheChapter2SDcard(String str, Context context) {

	String path = getCacheChapterPath();
	writeSDcardByCharSet(str, path, context);
	return path;
    }

    // 向SD卡写入数据
    public static boolean writeSDcard(String str, String articleId,
	    int chapterPosition, Context context) {
	return writeSDcard(str, getPathByChapter(articleId, chapterPosition),
		context);
    }

    // 向SD卡写入数据
    public static boolean writeSDcard(String str, String filePath,
	    Context context) {
	try {
	    // 判断是否存在SD卡
	    if (Environment.getExternalStorageState().equals(
		    Environment.MEDIA_MOUNTED)) {

		File file = new File(filePath);
		if (!file.exists()) {
		    File dirFile = file.getParentFile();
		    if (!dirFile.exists()) {
			if (dirFile.mkdirs()) {
			}
		    } else {
		    }
		} else {
		    file.delete();
		}

		// 获取SD卡的目录
		FileOutputStream outFileStream = new FileOutputStream(
			file.getPath());
		outFileStream.write(str.getBytes());
		outFileStream.close();
		return true;
	    }
	    return false;
	} catch (Exception e) {

	    e.printStackTrace();
	    return false;
	}
    }

    // 向SD卡写入数据
    public static boolean writeSDcardByCharSet(String str, String filePath,
	    Context context) {
	try {
	    // 判断是否存在SD卡
	    if (Environment.getExternalStorageState().equals(
		    Environment.MEDIA_MOUNTED)) {

		File file = new File(filePath);
		if (!file.exists()) {
		    File dirFile = file.getParentFile();
		    if (!dirFile.exists()) {
			if (dirFile.mkdirs()) {
			}
		    } else {
		    }
		} else {
		    file.delete();
		}

		// 获取SD卡的目录
		FileOutputStream outFileStream = new FileOutputStream(
			file.getPath());
		outFileStream.write(str.getBytes("utf8"));
		outFileStream.close();
		return true;
	    }
	    return false;
	} catch (Exception e) {

	    e.printStackTrace();
	    return false;
	}
    }

    /**
     * 根据作品id和章节位置获取txt文件的位置
     * 
     * @param articleId
     * @param chapterPosition
     * @return
     */
    public static String getPathByChapter(int articleId, int chapterPosition) {
	return fileFloder + articleId + "_" + chapterPosition + ".txt";
    }

    /**
     * 根据作品id和章节位置获取txt文件的位置
     * 
     * @param articleId
     * @param chapterPosition
     * @return
     */
    public static String getPathByChapter(String articleId, int chapterPosition) {
	return fileFloder + articleId + "_" + chapterPosition + ".txt";
    }

    /**
     * 根据作品id和章节名获取txt文件的位置
     * 
     * @param articleId

     * @return
     */
    public static String getPathByCacheChapter(String articleId,
	    String chapterName) {
	return cacheChapterFileFloder + articleId + "_" + chapterName + ".txt";
    }

    /**
     * 根据作品id和章节名获取txt文件的位置
     * 

     * @return
     */
    public static String getCacheChapterPath() {
	return cacheChapterFileFloder + "previewChapter" + ".txt";
    }

    /**
     * 向文件中写入数据
     * 
     * @param filePath
     *            目标文件全路径
     * @param data
     *            要写入的数据
     * @return true表示写入成功 false表示写入失败
     */
    public static boolean writeBytes(String filePath, byte[] data) {
	try {
	    FileOutputStream fos = new FileOutputStream(filePath);
	    fos.write(data);
	    fos.close();
	    return true;
	} catch (Exception e) {
	    System.out.println(e.getMessage());
	}
	return false;
    }

    /**
     * 从文件中读取数据
     * 
     * @param file
     * @return
     */
    public static byte[] readBytes(String file) {
	try {
	    FileInputStream fis = new FileInputStream(file);
	    int len = fis.available();
	    byte[] buffer = new byte[len];
	    fis.read(buffer);
	    fis.close();
	    return buffer;
	} catch (Exception e) {
	    System.out.println(e.getMessage());
	}

	return null;

    }

    /**
     * 从文件中读取数据
     * 
     * @param file
     * @return
     */
    public static String readContentformFile(String file) {
	try {
	    FileInputStream fis = new FileInputStream(file);
	    int len = fis.available();
	    byte[] buffer = new byte[len];
	    fis.read(buffer);
	    fis.close();
	    return buffer.toString();
	} catch (Exception e) {
	    System.out.println(e.getMessage());
	}

	return null;

    }

    /**
     * 向文件中写入字符串String类型的内容
     * 
     * @param file
     *            文件路径
     * @param content
     *            文件内容
     * @param charset
     *            写入时候所使用的字符集
     */
    public static void writeString(String file, String content, String charset) {
	try {
	    byte[] data = content.getBytes(charset);
	    writeBytes(file, data);
	} catch (Exception e) {
	    System.out.println(e.getMessage());
	}

    }

    /**
     * 从文件中读取数据，返回类型是字符串String类型
     * 
     * @param file
     *            文件路径
     * @param charset
     *            读取文件时使用的字符集，如utf-8、GBK等
     * @return
     */
    public static String readString(String file, String charset) {
	byte[] data = readBytes(file);
	String ret = null;

	try {
	    ret = new String(data, charset);
	} catch (Exception e) {
	    System.out.println(e.getMessage());
	}
	return ret;
    }

    public static void saveBitmap(Bitmap bm, String picName) {
	Log.e("", "保存图片");
	try {

	    File filerootDir = new File(FileUtils.SDPATH);
	    if (!filerootDir.exists()) {
		filerootDir.mkdirs();
	    }
	    // if (!isFileExist("")) {
	    // File tempf = createSDDir("");
	    // }
	    File f = new File(SDPATH, picName + ".jpg");
	    if (f.exists()) {
		f.delete();
	    }
	    FileOutputStream out = new FileOutputStream(f);
	    bm.compress(Bitmap.CompressFormat.JPEG, 90, out);
	    out.flush();
	    out.close();
	    Log.e("", "已经保存");
	} catch (FileNotFoundException e) {
	    e.printStackTrace();
	} catch (IOException e) {
	    e.printStackTrace();
	}
    }

    public static File createSDDir(String dirName) throws IOException {
	File dir = new File(SDPATH + dirName);
	if (Environment.getExternalStorageState().equals(
		Environment.MEDIA_MOUNTED)) {

	    dir.getParentFile().mkdirs();
	    // System.out.println("createSDDir:" + dir.getAbsolutePath());
	    // System.out.println("createSDDir:" + dir.mkdir());
	}
	return dir;
    }

    // public static boolean isFileExist(String fileName) {
    // File file = new File(SDPATH + fileName);
    // file.isFile();
    // return file.exists();
    // }

    public static void delFile(String fileName) {
	File file = new File(SDPATH + fileName);
	if (file.isFile()) {
	    file.delete();
	}
	file.exists();
    }

    public static void deleteDir(String path) {
	File dir = new File(path);
	if (dir == null || !dir.exists() || !dir.isDirectory())
	    return;

	for (File file : dir.listFiles()) {
	    if (file.isFile())
		file.delete(); // 删除所有文件
	    else if (file.isDirectory())
		deleteDir(path); // 递规的方式删除文件夹
	}
	dir.delete();// 删除目录本身
    }

    public static boolean fileIsExists(String path) {
	try {
	    File f = new File(path);
	    if (!f.exists()) {
		return false;
	    }
	} catch (Exception e) {

	    return false;
	}
	return true;
    }

    // 下载具体操作
    public static void download(final String downloadUrl,final Handler handler) {

	new Thread(new Runnable() {

	    @Override
	    public void run() {
		try {
		    URL url = new URL(downloadUrl);
		    // 打开连接
		    URLConnection conn = url.openConnection();
		    // 打开输入流
		    InputStream is = conn.getInputStream();
		    // 获得长度
		    int contentLength = conn.getContentLength();
		    // 创建文件夹 MyDownLoad，在存储卡下
		    String dirName = SDPATH1;
		    File file = new File(dirName);
		    // 不存在创建
		    if (!file.exists()) {
			file.mkdir();
		    }
		    // 下载后的文件名
		    String fileName = dirName + "cacheheadurl.jpg";
		    File file1 = new File(fileName);
		    if (file1.exists()) {
			file1.delete();
		    }
		    // 创建字节流
		    byte[] bs = new byte[1024];
		    int len;
		    OutputStream os = new FileOutputStream(fileName);
		    // 写数据
		    while ((len = is.read(bs)) != -1) {
			os.write(bs, 0, len);
		    }
		    
		    handler.sendEmptyMessage(CommonUtils.DOWNLOAD_SUC);
		    // 完成后关闭流
		    os.close();
		    is.close();
		} catch (Exception e) {
		    handler.sendEmptyMessage(CommonUtils.DOWNLOAD_ERROR);
		    e.printStackTrace();
		}
	    }
	}).start();
    }
    
    public static String getCacheHeadUrlPath()
    {
	return SDPATH1+"cacheheadurl.jpg";
    }

}
