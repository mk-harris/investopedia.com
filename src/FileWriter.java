import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

//java读文件、写文件---<以字符流方式>
public class FileWriter {

	public static void writerFileContent(String outPath, String fileContent) {
		try {
			FileOutputStream fos = new FileOutputStream(outPath); // 创建FileOutputStream对象，用来输出字符流
			OutputStreamWriter osw = new OutputStreamWriter(fos, "UTF-8"); // 创建OutputStreamWriter对象，用来写入字符流
			BufferedWriter bw = new BufferedWriter(osw);
			bw.write(fileContent);
			bw.flush();
			bw.close();
		} catch (IOException ex) {
			System.out.println(ex.getMessage());
		}
	}

	
}