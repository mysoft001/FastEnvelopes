package go.fast.fastenvelopes.http;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DecimalFormat;

import android.graphics.Bitmap;
import android.os.Environment;
import android.text.TextUtils;

public class FileHelper {
    private static String mSdCardPath="not_init";

    /**
	 * 创建单个文件
	 * 
	 * @param destFileName
	 *            文件名
	 * @return 创建成功返回true，否则返回false
	 * @throws IOException
	 */
	public static File createFile(String destFileName) throws IOException {
		File file = new File(destFileName);
		if (file.exists()) {
			return file;
		}
		if (!file.getParentFile().exists()) {
			if (!file.getParentFile().mkdirs()) {
				return file;
			}
		}

		// 创建目标文件
		try {
			if (file.createNewFile()) {
				return file;
			} else {
				return file;
			}
		} catch (IOException e) {
			throw e;
		}
	}

	/**
	 * 创建目录
	 * 
	 * @param destDirName
	 *            目标目录名
	 * @return 目录创建成功返回true，否则返回false
	 */
	public static boolean createDir(String destDirName) {
		File dir = new File(destDirName);
		if (dir.exists()) {
			return true;
		}
		if (!destDirName.endsWith(File.separator))
			destDirName = destDirName + File.separator;
		// 创建单个目录
		return dir.mkdirs();
	}

	/**
	 * 创建临时文件
	 * 
	 * @param prefix
	 *            临时文件的前缀
	 * @param suffix
	 *            临时文件的后缀
	 * @param dirName
	 *            临时文件所在的目录，如果输入null，则在用户的文档目录下创建临时文件
	 * @return 临时文件创建成功返回抽象路径名的规范路径名字符串，否则返回null
	 */
	public static String createTempFile(String prefix, String suffix,
			String dirName) {
		File tempFile = null;
		try {
			if (dirName == null) {
				// 在默认文件夹下创建临时文件
				tempFile = File.createTempFile(prefix, suffix);
				return tempFile.getCanonicalPath();
			} else {
				File dir = new File(dirName);
				// 如果临时文件所在目录不存在，首先创建
				if (!dir.exists()) {
					if (!createDir(dirName)) {
						return null;
					}
				}
				tempFile = File.createTempFile(prefix, suffix, dir);
				return tempFile.getCanonicalPath();
			}
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static boolean deleteFile(String filename){
		File file=new File(filename);
		if(file.exists()){
			return file.delete();
		}
		return false;
	}
	
	public static File getFile(String filePath) throws IOException {
		File file = new File(filePath);

		if (!file.exists()) {
			createDir(file.getParent());
			file.createNewFile();
		}
		return file;
	}

	public static byte[] readFile(String path) throws IOException {
		if (TextUtils.isEmpty(path)) {
			throw new FileNotFoundException("file path not found");
		}
		ByteArrayOutputStream out = null;

		BufferedInputStream bis = null;
		try {
			// 创建文件输入流对象
			InputStream is = new FileInputStream(createFile(path));
			bis = new BufferedInputStream(is);
			out = new ByteArrayOutputStream();
			// 设定读取的字节数
			int n = 512;
			byte buffer[] = new byte[n];
			// 读取输入流
			while ((bis.read(buffer) != -1)) {
				out.write(buffer);
			}
			// 关闭输入流
			bis.close();
			out.flush();
			out.close();
			return out.toByteArray();
		} catch (IOException ioe) {
			throw ioe;
		} finally {
			if (bis != null) {
				bis.close();
			}
			if (out != null) {
				out.close();
			}
		}
	}

	public static void writeFile(Bitmap bitmap, File path) throws IOException {
		if (!path.exists()) {
			path.createNewFile();
		}
		FileOutputStream fOut = null;
		try {
			fOut = new FileOutputStream(path);
			writeFile(bitmap,fOut);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
	}

	public static void writeFile(Bitmap bitmap, FileOutputStream fOut) throws IOException {
		bitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
		try {
			fOut.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			fOut.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static boolean writeFile(InputStream stream, String path)
			throws IOException {
		if (TextUtils.isEmpty(path)) {
			throw new FileNotFoundException("file path not found");
		}
		return writeFile(stream,new FileOutputStream(
					createFile(path)));
	}
	
	public static boolean writeFile(InputStream stream, FileOutputStream fos)
			throws IOException {
		BufferedOutputStream bos = null;
		try {
			bos = new BufferedOutputStream(fos);
			// 设定读取的字节数
			byte buffer[] = new byte[512];
			int length;  
            while((length=(stream.read(buffer))) >0){  
            	bos.write(buffer,0,length);  
            }  
			bos.flush();
			return true;
		} catch (FileNotFoundException e) {
			throw e;
		} catch (IOException e) {
			throw e;
		} finally {
			if (bos != null) {
				bos.close();
			}
			if (stream != null) {
				stream.close();
			}
		}
	}

	public static boolean writeFile(byte[] bytes, String path)
			throws IOException {
		if (TextUtils.isEmpty(path)) {
			throw new FileNotFoundException("file path not found");
		}
		BufferedOutputStream bos = null;
		try {
			bos = new BufferedOutputStream(new FileOutputStream(
					createFile(path)));
			bos.write(bytes);
			bos.flush();
			return true;
		} catch (FileNotFoundException e) {
			throw e;
		} catch (IOException e) {
			throw e;
		} finally {
			if (bos != null) {
				bos.close();
			}
		}
	}

	public static String getSDPath() throws FileNotFoundException {
        boolean sdCardExist = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);

        if(sdCardExist){
            if("not_found".equals(mSdCardPath))
                mSdCardPath="not_init";
        }else{
            throw new FileNotFoundException("sdcard not found");
        }

        if("not_init".equals(mSdCardPath)){
            mSdCardPath=getSDCardPath();
        }
        return mSdCardPath;
	}

    /**
     * 获取外置SD卡路径
     *
     * @return
     */
    public static String getSDCardPath() {
        String cmd = "cat /proc/mounts";
        Runtime run = Runtime.getRuntime();// 返回与当前 Java 应用程序相关的运行时对象
        try {
            Process p = run.exec(cmd);// 启动另一个进程来执行命令
            BufferedInputStream in = new BufferedInputStream(p.getInputStream());
            BufferedReader inBr = new BufferedReader(new InputStreamReader(in));

            String lineStr;
            while ((lineStr = inBr.readLine()) != null) {
                // 获得命令执行后在控制台的输出信息
                LoggerHelper.i("CommonUtil:getSDCardPath", lineStr);
                if (lineStr.contains("sdcard")
                        && lineStr.contains(".android_secure")) {
                    String[] strArray = lineStr.split(" ");
                    if (strArray != null && strArray.length >= 5) {
                        String result = strArray[1].replace("/.android_secure",
                                "");
                        return result;
                    }
                }
                // 检查命令是否执行失败。
                if (p.waitFor() != 0 && p.exitValue() == 1) {
                    // p.exitValue()==0表示正常结束，1：非正常结束
                    LoggerHelper.e("CommonUtil:getSDCardPath", "命令执行失败!");
                }
            }
            inBr.close();
            in.close();
        } catch (Exception e) {
            LoggerHelper.e("CommonUtil:getSDCardPath", e.toString());
            return Environment.getExternalStorageDirectory().getPath();
        }

        return Environment.getExternalStorageDirectory().getPath();
    }

	public static String getDataPath() throws FileNotFoundException {
		File sdDir = null;
		boolean sdCardExist = Environment.getDataDirectory().equals(
				Environment.MEDIA_MOUNTED); // 判断目录是否存在
		if (sdCardExist) {
			sdDir = Environment.getDataDirectory();// 获取跟目录
			return sdDir.toString();
		}else{
			throw new FileNotFoundException("sdcard not found");
		}
	}
    /** size 如果 小于1024 * 1024,以KB单位返回,反则以MB单位返回 */
    private static DecimalFormat df = new DecimalFormat("###.##");

	public static String convert2KB(float size) {
		float f;
		if (size < 1024 * 1024) {
			f = size / (float) 1024;
			return (df.format(new Float(f).doubleValue()) + "KB");
		} else if(size<1024*1024*1024){
			f = size / (float) (1024 * 1024);
			return (df.format(new Float(f).doubleValue()) + "MB");
		}else{
			f = size / (float) (1024 * 1024 * 1024);
			return (df.format(new Float(f).doubleValue()) + "GB");
		}
	}
	
}