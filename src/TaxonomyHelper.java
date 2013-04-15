import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class TaxonomyHelper {

	/*<!-- Page Taxonomy -->
	<script pagespeed_no_defer="" type="text/javascript">//<![CDATA[
	var _pageTaxonomy={"Hashkey":"$","Channel":"General","SubChannel":"Homepage","Advertising":"Investing","SubAdvertising":"Homepage","AdTarget":"investopedia.com/homepage","DfpTarget":"Investing/Homepage","Tags":[],"Type":"Landing","Lucrativeness":null,"Timelessness":"Timeless","Feature":null,"Design":null,"InterestLevel":null,"Index":"True","NoIndexParams":"False","Follow":"True","Master":"True"};
	//]]></script>
	<!-- End Page Taxonomy -->*/
	public static String getPageTaxonomyBySingleURL(String httpurl) { // ��ȡ��ҳԴ���е�Taxonomy value
		String pageSource = HttpRequest.getPageSourceBySingleURL(httpurl)
				.toString();
		String startTag = "<!-- Page Taxonomy -->";
		String endTag = "<!-- End Page Taxonomy -->";
		int startIndex = pageSource.indexOf(startTag);
		int endIndex = pageSource.indexOf(endTag);
		String content = pageSource.substring(startIndex + startTag.length(),
				endIndex);
		int start = content.indexOf("{");
		int end = content.indexOf("}");
		return content.substring(start + 1, end);
	}

	public static void getPageTaxonomyByFile(String urlfile)
			throws IOException {
		ArrayList<String> urls = new ArrayList<String>();
		urls = ReadFromFile.getUrlList(urlfile);
		String pageTaxonomy = null;
		for (int i = 0; i < urls.size(); i++) {
			URL url = new URL(urls.get(i));
			pageTaxonomy = getPageTaxonomyBySingleURL(url.toString());
			System.out.println(urls.get(i) + pageTaxonomy);
		}
	}

	/* ��ȡPage Taxonomy����,��list��ʽ�洢,���������ֵһһ��Ӧ */
	public static List<TaxonomyEntity> getPageTaxonomyList(String pageTaxonomy) {
		String[] keyvalues = pageTaxonomy.split(",");
		List<TaxonomyEntity> list = new ArrayList<TaxonomyEntity>();
		for (int i = 0; i < keyvalues.length; i++) {
			String key = keyvalues[i].split(":")[0].replace("\"", "");
			String value = keyvalues[i].split(":")[1].replace("\"", "");
			list.add(new TaxonomyEntity(key, value));
		}
		return list;
	}
}
