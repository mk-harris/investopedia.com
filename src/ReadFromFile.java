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
			FileInputStream fis = new FileInputStream(urlfile); // ����FileInputStream����������ȡ�ַ���
			InputStreamReader isr = new InputStreamReader(fis, "UTF-8");
			BufferedReader br = new BufferedReader(isr); // ����ָ���ļ�������
			String url = null;
			while ((url = br.readLine()) != null) // һ��һ�ж�ȡ�ļ��������ȡ�����ַ�ʱ��������.
			{ // ���Ĺر�˳���ȴ򿪵ĺ�أ���򿪵��ȹأ������п��ܳ���java.io.IOException:Stream closed�쳣
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

	/* ���ֽ�Ϊ��λ��ȡ�ļ��������ڶ��������ļ�����ͼƬ��������Ӱ����ļ��� */
	public static String readFileByBytes(String srcFile,String desFile)
			throws FileNotFoundException {
		File file = new File(srcFile);
		FileInputStream fis = new FileInputStream(file); // ���ļ��л�ȡ������
		BufferedInputStream bis = new BufferedInputStream(fis); // ���ӻ���
		BufferedOutputStream bos =    
                new BufferedOutputStream(new FileOutputStream(desFile));
		String filecontent = null;

		/*int onebyte;
		try {
			while ((onebyte = fis.read()) != -1) { // һ�ζ�һ���ֽ�
				System.out.write(onebyte);
			}
			fis.close();
		} catch (IOException e) {
			e.printStackTrace();
		}*/

		/*
		 * BufferedInputStream�����ݳ�Աbuf��һ��λ���飬Ĭ��Ϊ2048�ֽڡ�����ȡ������Դʱ�������ļ���
		 * BufferedInputStream�ᾡ����buf������
		 * ��ʹ��read()����ʱ��ʵ�������ȶ�ȡbuf�е����ݣ�������ֱ�Ӷ�������Դ����ȡ��
		 * ��buf�е����ݲ���ʱ��BufferedInputStream�Ż���ʵ�ָ�����InputStream�����read
		 * ()��������ָ����װ������ȡ���ݡ�
		 */
		byte[] bytes = new byte[2048]; // һ�ζ�����ֽ�
		int bytereader = 0;
		try {
			while ((bytereader = bis.read(bytes)) != -1) { // �������ֽڵ��ֽ�������
				System.out.write(bytes, 0, bytereader);// ��ָ�����ֽ�д�������	
				 bos.write(bytes); 
			}
			//���������е�����ȫ��д��   
            bos.flush();   
  
            //�ر���   
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

	/* ����Ϊ��λ��ȡ�ļ��������ڶ������еĸ�ʽ���ļ� */
	public static String readFileByLines(String fileName)
			throws FileNotFoundException {

		String filecontent = "";
		FileInputStream fis = new FileInputStream(fileName); // ����FileInputStream����������ȡ�ַ���
		InputStreamReader isr = new InputStreamReader(fis);
		BufferedReader br = new BufferedReader(isr); // ����ָ���ļ�������
		try {
			String tempstring = br.readLine();
			while (tempstring != null) { // һ�ζ���һ�У�ֱ������nullΪ�ļ�����
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

	/* �����ȡ�ļ����� */
	public static void readFileByRandomAccess(String fileName) {
		RandomAccessFile randomFile = null;
		try {
			randomFile = new RandomAccessFile(fileName, "r"); // ��һ����������ļ�������ֻ����ʽ
			// System.out.println(randomFile.length());
			long fileLength = randomFile.length(); // �ļ����ȣ��ֽ���
			int beginIndex = (fileLength > 4) ? 4 : 0; // ���ļ�����ʼλ��
			randomFile.seek(beginIndex); // �����ļ��Ŀ�ʼλ���Ƶ�beginIndexλ�á�
			byte[] bytes = new byte[1024]; // һ�ζ�1024���ֽڣ�����ļ����ݲ���1024���ֽڣ����ʣ�µ��ֽڡ�
			int byteread = 0;
			while ((byteread = randomFile.read(bytes)) != -1) { // ��һ�ζ�ȡ���ֽ�������byteread
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

	/* ���ַ�Ϊ��λ��ȡ�ļ��������ڶ��ı������ֵ����͵��ļ� */
	public static void readFileByChars(String fileName) {
		File file = new File(fileName);
		Reader reader = null;
		try {
			System.out
					.println("In character units to read the file content, once read a byte: ");
			// һ�ζ�һ���ַ�
			reader = new InputStreamReader(new FileInputStream(file));
			int tempchar;
			while ((tempchar = reader.read()) != -1) {
				if (((char) tempchar) != '\r') { // ����windows�£�\r\n�������ַ���һ��ʱ����ʾһ�����С�������������ַ��ֿ���ʾʱ���ỻ�����С���ˣ����ε�\r����������\n�����򣬽������ܶ���С�
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
			char[] tempchars = new char[30]; // һ�ζ�����ַ�
			int charread = 0;
			reader = new InputStreamReader(new FileInputStream(fileName));
			while ((charread = reader.read(tempchars)) != -1) { // �������ַ����ַ������У�charreadΪһ�ζ�ȡ�ַ���
				if ((charread == tempchars.length)
						&& (tempchars[tempchars.length - 1] != '\r')) { // ͬ�����ε�\r����ʾ
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

	/* ��ʾ�������л�ʣ���ֽ��� */
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