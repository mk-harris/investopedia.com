import java.io.ByteArrayInputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class XMLReader {

	public static List<RssItemEntity> getRssItemsByFileContent(String xmlContent) {  
		Document doc = null;
		try {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance(); // ʹ��DocumentBuilderFactory��ľ�̬����newInstance()������ȡ�����һ������dbf
			DocumentBuilder db = dbf.newDocumentBuilder(); // ʹ��dbf�����newDocumentBuilder()����������ȡDocumentBuilder��Ķ���															
			doc = db.parse(new ByteArrayInputStream(xmlContent.getBytes()));  
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
		}
		return analystXMLdoc(doc); 
	}

	public static List<RssItemEntity> getRssItemsByLocalFile(String xmlfile) {
		Document doc = null;
		try {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance(); // ʹ��DocumentBuilderFactory��ľ�̬����newInstance()������ȡ�����һ������dbf
			DocumentBuilder db = dbf.newDocumentBuilder(); // ʹ��dbf�����newDocumentBuilder()����������ȡDocumentBuilder��Ķ���
			File file = new File(xmlfile); // ����File�����������ڴ��м���XML�ļ�
			doc = db.parse(file); // ʹ��DocumentBuilder������parse()������������XML�ļ���������Document����
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
		}
		return analystXMLdoc(doc);
	}

	private static List<RssItemEntity> analystXMLdoc(Document doc) {
		List<RssItemEntity> RssItems = new ArrayList<RssItemEntity>();
		try {
			NodeList feeds = doc.getElementsByTagName("item"); //�������Ԫ����Ϊ"item"��һ���б�list
			if (feeds != null) {
				for (int i = 0; i < feeds.getLength(); i++) // ѭ���������
				{
					Element el = (Element) feeds.item(i);
					String title = el.getElementsByTagName("title").item(0)
							.getFirstChild().getNodeValue();
					String link = el.getElementsByTagName("link").item(0)
							.getFirstChild().getNodeValue();
					String description = (el   //�ж�description�Ƿ�Ϊ�գ����ǣ���ֵ""
							.getElementsByTagName("description").item(0)
							.getFirstChild() == null) ? "" : el
							.getElementsByTagName("description").item(0)
							.getFirstChild().getNodeValue();
					String guid = el.getElementsByTagName("guid").item(0)
							.getFirstChild().getNodeValue();
					String pubDate = el.getElementsByTagName("pubDate").item(0)
							.getFirstChild().getNodeValue();
					RssItemEntity item = new RssItemEntity(title, link,
							description, guid, pubDate);
					RssItems.add(item);
				}
			} else
				System.out.println("There is no item");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return RssItems;
	}
}
