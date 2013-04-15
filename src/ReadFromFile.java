import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Scanner;

public class ReadFromFile {

	public static ArrayList<String> getUrlList(String urlfile) {
		ArrayList<String> urls = new ArrayList<String>();
		try {
			FileInputStream fis = new FileInputStream(urlfile); // 创建FileInputStream对象，用来读取字符流
			InputStreamReader isr = new InputStreamReader(fis, "UTF-8");
			BufferedReader br = new BufferedReader(isr); // 缓冲指定文件的输入
			String url = null;
			while ((url = br.readLine()) != null) // 一行一行读取文件，解决读取中文字符时出现乱码.
			{ // 流的关闭顺序：先打开的后关，后打开的先关，否则有可能出现java.io.IOException:Stream closed异常
				urls.add(url);
			}
			br.close();
			isr.close();
			fis.close();
		} catch (IOException e) {
			System.out.println("Get URL failure.");
			e.printStackTrace();
		}
		return urls;
	}

	/* 以字节为单位读取文件，常用于读二进制文件，如图片、声音、影像等文件。 */
	public static String readFileByBytes(String srcFile,String desFile)
			throws FileNotFoundException {
		File file = new File(srcFile);
		FileInputStream fis = new FileInputStream(file); // 从文件中获取输入流
		BufferedInputStream bis = new BufferedInputStream(fis); // 增加缓冲
		BufferedOutputStream bos =    
                new BufferedOutputStream(new FileOutputStream(desFile));
		String filecontent = null;

		/*int onebyte;
		try {
			while ((onebyte = fis.read()) != -1) { // 一次读一个字节
				System.out.write(onebyte);
			}
			fis.close();
		} catch (IOException e) {
			e.printStackTrace();
		}*/

		/*
		 * BufferedInputStream的数据成员buf是一个位数组，默认为2048字节。当读取数据来源时，例如文件，
		 * BufferedInputStream会尽量将buf填满。
		 * 当使用read()方法时，实际上是先读取buf中的数据，而不是直接对数据来源作读取。
		 * 当buf中的数据不足时，BufferedInputStream才会再实现给定的InputStream对象的read
		 * ()方法，从指定的装置中提取数据。
		 */
		byte[] bytes = new byte[2048]; // 一次读多个字节
		int bytereader = 0;
		try {
			while ((bytereader = bis.read(bytes)) != -1) { // 读入多个字节到字节数组中
				System.out.write(bytes, 0, bytereader);// 将指定的字节写入输出流	
				 bos.write(bytes); 
			}
			//将缓冲区中的数据全部写出   
            bos.flush();   
  
            //关闭流   
            bis.close();   
            bos.close();   
		} catch (Exception e1) {
			e1.printStackTrace();
		} finally {
			if (bis != null)
				try {
					bis.close();
				} catch (IOException e1) {
				}
		}
		return filecontent;
	}

	/* 以行为单位读取文件，常用于读面向行的格式化文件 */
	public static String readFileByLines(String fileName)
			throws FileNotFoundException {

		String filecontent = "";
		FileInputStream fis = new FileInputStream(fileName); // 创建FileInputStream对象，用来读取字符流
		InputStreamReader isr = new InputStreamReader(fis);
		BufferedReader br = new BufferedReader(isr); // 缓冲指定文件的输入
		try {
			String tempstring = br.readLine();
			while (tempstring != null) { // 一次读入一行，直到读入null为文件结束
				filecontent += tempstring;
				tempstring = br.readLine();
				if (tempstring != null) {
					filecontent += "\r\n";
				}
			}
			// System.out.println(filecontent);
			br.close();
			isr.close();
			fis.close();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e1) {
				}
			}
		}
		return filecontent;
	}

	/* 随机读取文件内容 */
	public static void readFileByRandomAccess(String fileName) {
		RandomAccessFile randomFile = null;
		try {
			randomFile = new RandomAccessFile(fileName, "r"); // 打开一个随机访问文件流，按只读方式
			// System.out.println(randomFile.length());
			long fileLength = randomFile.length(); // 文件长度，字节数
			int beginIndex = (fileLength > 4) ? 4 : 0; // 读文件的起始位置
			randomFile.seek(beginIndex); // 将读文件的开始位置移到beginIndex位置。
			byte[] bytes = new byte[1024]; // 一次读1024个字节，如果文件内容不足1024个字节，则读剩下的字节。
			int byteread = 0;
			while ((byteread = randomFile.read(bytes)) != -1) { // 将一次读取的字节数赋给byteread
				System.out.write(bytes, 0, byteread);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (randomFile != null) {
				try {
					randomFile.close();
				} catch (IOException e1) {
				}
			}
		}
	}

	/* 以字符为单位读取文件，常用于读文本，数字等类型的文件 */
	public static void readFileByChars(String fileName) {
		File file = new File(fileName);
		Reader reader = null;
		try {
			System.out
					.println("In character units to read the file content, once read a byte: ");
			// 一次读一个字符
			reader = new InputStreamReader(new FileInputStream(file));
			int tempchar;
			while ((tempchar = reader.read()) != -1) {
				if (((char) tempchar) != '\r') { // 对于windows下，\r\n这两个字符在一起时，表示一个换行。但如果这两个字符分开显示时，会换两次行。因此，屏蔽掉\r，或者屏蔽\n。否则，将会多出很多空行。
					System.out.print((char) tempchar);
				}
			}
			reader.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			System.out
					.println("In character units to read the file content, once read multiple bytes: ");
			char[] tempchars = new char[30]; // 一次读多个字符
			int charread = 0;
			reader = new InputStreamReader(new FileInputStream(fileName));
			while ((charread = reader.read(tempchars)) != -1) { // 读入多个字符到字符数组中，charread为一次读取字符数
				if ((charread == tempchars.length)
						&& (tempchars[tempchars.length - 1] != '\r')) { // 同样屏蔽掉\r不显示
					System.out.print(tempchars);
				} else {
					for (int i = 0; i < charread; i++) {
						if (tempchars[i] == 'r') {
							continue;
						} else {
							System.out.print(tempchars[i]);
						}
					}
				}
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e1) {
				}
			}
		}
	}

	/* 显示输入流中还剩的字节数 */
	private static void showAvailableBytes(BufferedInputStream bis) {
		try {
			System.out
					.println("The remaining number of current input stream of bytes : "
							+ bis.available());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}