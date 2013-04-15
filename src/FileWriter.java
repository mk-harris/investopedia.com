import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

//java���ļ���д�ļ�---<���ַ�����ʽ>
public class FileWriter {

	public static void writerFileContent(String outPath, String fileContent) {
		try {
			FileOutputStream fos = new FileOutputStream(outPath); // ����FileOutputStream������������ַ���
			OutputStreamWriter osw = new OutputStreamWriter(fos, "UTF-8"); // ����OutputStreamWriter��������д���ַ���
			BufferedWriter bw = new BufferedWriter(osw);
			bw.write(fileContent);
			bw.flush();
			bw.close();
		} catch (IOException ex) {
			System.out.println(ex.getMessage());
		}
	}

	
}