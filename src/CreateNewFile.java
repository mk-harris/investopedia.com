import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;

public class CreateNewFile{
 //����һ��������ɴ����ļ���Ŀ�ģ��ļ��ĵ�һ���������ļ�·�����ļ������ڶ����������ļ�����
 //�磺myfile.doc  HelloJava!
 public void createNewFile(String fileDirectoryAndName,String fileContent){
  try{
   String fileName = fileDirectoryAndName; 
   File myFile = new File(fileName); //����File���󣬲���ΪString���ͣ���ʾĿ¼��
   if(!myFile.exists()) //�ж��ļ��Ƿ���ڣ���������������createNewFile()����������Ŀ¼�����������쳣�������
    myFile.createNewFile();
   else  //����������ӳ��쳣
    throw new Exception("The new file already exists!");   
   FileWriter resultFile = new FileWriter(myFile); //���������д�봴�����ļ��������½��ļ���Ϊ��������FileWriter����   
   PrintWriter myNewFile = new PrintWriter(resultFile); //�Ѹö����װ��PrinterWriter���� 
   myNewFile.println(fileContent);  //��ͨ��PrinterWriter�����println()�������ַ�������д���½��ļ�
   resultFile.close();   //�ر��ļ�д����
  }catch(Exception ex){
   System.out.println("Unable to create new file!");
   ex.printStackTrace();
  }
 }
 /*public static void main(String[] args){
  CreateNewFile createFile = new CreateNewFile();  //������Ķ��󲢵��øö����createNewFile()�������������ļ���д������
  createFile.createNewFile("C:/eclipse/CreateNewFile.txt", "CreateNewFile");
 }*/
}