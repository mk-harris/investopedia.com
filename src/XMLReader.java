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
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance(); // 使用DocumentBuilderFactory类的静态方法newInstance()，来获取该类的一个对象dbf
			DocumentBuilder db = dbf.newDocumentBuilder(); // 使用dbf对象的newDocumentBuilder()方法，来获取DocumentBuilder类的对象															
			doc = db.parse(new ByteArrayInputStream(xmlContent.getBytes()));  
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
		}
		return analystXMLdoc(doc); 
	}

	public static List<RssItemEntity> getRssItemsByLocalFile(String xmlfile) {
		Document doc = null;
		try {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance(); // 使用DocumentBuilderFactory类的静态方法newInstance()，来获取该类的一个对象dbf
			DocumentBuilder db = dbf.newDocumentBuilder(); // 使用dbf对象的newDocumentBuilder()方法，来获取DocumentBuilder类的对象
			File file = new File(xmlfile); // 创建File对象，用来在内存中加载XML文件
			doc = db.parse(file); // 使用DocumentBuilder类对象的parse()方法，来解析XML文件，并返回Document对象
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
		}
		return analystXMLdoc(doc);
	}

	private static List<RssItemEntity> analystXMLdoc(Document doc) {
		List<RssItemEntity> RssItems = new ArrayList<RssItemEntity>();
		try {
			NodeList feeds = doc.getElementsByTagName("item"); //获得其子元素名为"item"的一个列表list
			if (feeds != null) {
				for (int i = 0; i < feeds.getLength(); i++) // 循环处理对象
				{
					Element el = (Element) feeds.item(i);
					String title = el.getElementsByTagName("title").item(0)
							.getFirstChild().getNodeValue();
					String link = el.getElementsByTagName("link").item(0)
							.getFirstChild().getNodeValue();
					String description = (el   //判断description是否为空，如是，赋值""
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
