import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Automation {

	public static void main(String[] args) throws IOException,
			ClassNotFoundException, SQLException {
		/*
		 * VerifyTaxonomyEntityByURLFile(
		 * "D:\\Program Files (x86)\\Java test\\urls.txt",
		 * "D:\\Program Files (x86)\\Java test\\");
		 */
		// VerifyImageLinkValidity("D:\\Program Files (x86)\\Java test\\testURLs.txt");

		// VerifyTaxonomyEntityByEnvironment(
		// "C:/eclipse/urls.txt",
		// "C:/eclipse/test.txt",
		// "C:/eclipse/");

		// TaxonomyHelper.getPageTaxonomyByFile("C:/eclipse/staticpageurls.txt");

		// PageAnalytics.getMetaTagsBySingleURL("http://www.investopedia.com/articles/investing/032613/approved-paying-online-sales-tax.asp");

		// PageAnalytics.getMetaTagesBySingleHttpURL("D:\\Program Files (x86)\\Java test\\pagesource.txt");
		/*
		 * VerifyRssItemByRssList("D:\\Program Files (x86)\\Java test\\xmlUrl.txt"
		 * );
		 */

//		VerifyMetaTagsEntityByEnvironment("C:/eclipse/new  4.txt",
//				"C:/eclipse/new  5.txt", "C:/eclipse/");
		
		/*String httpURL = "http://www.investopedia.com/articles/investing/032613/";
		httpURL = httpURL.toLowerCase().replace("http://", "")
				.replace("https://", "");
		httpURL = httpURL.substring(httpURL.indexOf("/"), httpURL.length());
		
		if (httpURL.endsWith("/")) {
			httpURL = httpURL.substring(0, httpURL.length() - 1);
			System.out.println(httpURL);
		} */
		// List<MetaTagsEntity> list = new ArrayList<MetaTagsEntity>();
		// //PageAnalytics.getMetaTagsByFile("C:/eclipse/testurls.txt");
		// CompareHelper.compareMetaTags("http://dev2.investopedia.com/exam-guide/series-7/securities-transactions/executing-trades.asp",
		// "http://www.investopedia.com/exam-guide/series-7/securities-transactions/executing-trades.asp");
		// FileWriter.writerFileContent("C:/eclipse/Results.txt",list.toString());

		// PageAnalytics.getMetaTagesBySingleHttpURL("http://dev2.investopedia.com/exam-guide/series-7/securities-transactions/executing-trades.asp");
		/*
		 * VerifyStaticTextmatcher("C:/eclipse/urls.txt" ,
		 * "C:/eclipse/result.txt");
		 */

		// GetDBData.getPageTitle("articles/optioninvestor/08/invest-in-commodities.asp");
	}

	/* 匹配静态文本 */
	public static void VerifyStaticTextmatcher(String urlfile, String textfile)
			throws FileNotFoundException, UnsupportedEncodingException {
		ArrayList<String> urls = new ArrayList<String>();
		urls = ReadFromFile.getUrlList(urlfile);
		for (int i = 0; i < urls.size(); i++) {
			System.out.println(urls.get(i));
			String pagesource = (HttpRequest.getPageSourceBySingleURL(urls
					.get(i))).replaceAll("\r\n", "");
			String staticText = ReadFromFile.readFileByLines(textfile)
					.replaceAll("\r\n", "");
			System.out.println("matcher: "
					+ PageAnalytics.matchStaticText(pagesource, staticText));
		}
	}

	/* 获取urllist,输出各个url的Pagetaxonomy值 */
	public static void VerifyTaxonomyEntityByURLFile(String urlFile,
			String resultFilePath) {
		List<String> urls = ReadFromFile.getUrlList(urlFile);
		String result = "";
		for (int i = 0; i < urls.size(); i++) {
			String taxonomy = TaxonomyHelper.getPageTaxonomyBySingleURL(urls
					.get(i));
			result = result + urls.get(i) + "\r\n" + taxonomy + "\r\n";
		}
		FileWriter.writerFileContent( // 将获取到的所有Pagetaxonomy值写入一个文件中
				resultFilePath
						+ new SimpleDateFormat("MM-dd hh-mm")
								.format(new Date()) + "Taxonomy.txt", result);
	}

	/* 验证同一个url，在不同的环境上，其Page Taxonomy value是否相同 */
	public static void VerifyTaxonomyEntityByEnvironment(String testURLFile,
			String wwwURLFile, String resultFilePath) {
		List<String> testUrls = ReadFromFile.getUrlList(testURLFile);
		List<String> wwwUrls = ReadFromFile.getUrlList(wwwURLFile);
		String outPut = "";
		String diff = "";
		for (int i = 0; i < testUrls.size(); i++) {
			String testTaxonomy = TaxonomyHelper
					.getPageTaxonomyBySingleURL(testUrls.get(i));
			String wwwTaxonomy = TaxonomyHelper
					.getPageTaxonomyBySingleURL(wwwUrls.get(i));
			List<TaxonomyEntity> diffTaxonomy = CompareHelper.compareTaxonomy(
					testTaxonomy, wwwTaxonomy);

			for (int j = 0; j < diffTaxonomy.size(); j++) {
				diff = diff + "" + diffTaxonomy.get(j).key + ": "
						+ diffTaxonomy.get(j).value + ";"
						+ diffTaxonomy.get(j).value2 + "\r\n";
			}
			outPut = outPut + testUrls.get(i) + "\r\n" + diff + "\r\n";
		}
		if (!diff.replaceAll("\r\n", "").equals("")) {
			FileWriter.writerFileContent(resultFilePath
					+ new SimpleDateFormat("MM-dd hh-mm").format(new Date())
					+ "diffTaxonomy.txt", outPut);
		} else {
			System.out.println("All the Page Taxonomy value is the same.");
		}
	}

	/* 验证同一个url，在不同的环境上，其Meta Tags value是否相同 */
	public static void VerifyMetaTagsEntityByEnvironment(String testURLFile,
			String wwwURLFile, String resultFilePath)
			throws FileNotFoundException {
		List<String> testUrls = ReadFromFile.getUrlList(testURLFile);
		List<String> wwwUrls = ReadFromFile.getUrlList(wwwURLFile);
		String outPut = "";
		for (int i = 0; i < testUrls.size(); i++) {
			String diff = "";
			List<MetaTagsEntity> diffMetaTags = CompareHelper.compareMetaTags(
					testUrls.get(i), wwwUrls.get(i));

			for (int j = 0; j < diffMetaTags.size(); j++) {
				diff = diff + diffMetaTags.get(j).id + ":   "
						+ diffMetaTags.get(j).name + ";    "
						+ diffMetaTags.get(j).content + "\r\n";
			}
			if (!diff.replaceAll("\r\n", "").equals("")) {
				outPut = outPut + testUrls.get(i) + "\r\n" + diff + "\r\n";
			} else {
				System.out.println("");
			}

		}
		FileWriter.writerFileContent(resultFilePath
				+ new SimpleDateFormat("MM-dd hh-mm").format(new Date())
				+ "diffMeta.txt", outPut);
	}

	/* 验证同一个rss中，是否存在相同的item */
	public static void VerifyRssItemByRssList(String rssFile) {
		List<String> rssUrls = ReadFromFile.getUrlList(rssFile);
		for (int i = 0; i < rssUrls.size(); i++) {
			String xmlDoc = HttpRequest
					.getPageSourceBySingleURL(rssUrls.get(i)); // 获取xmlcontent
			// System.out.println(xmlDoc);
			List<RssItemEntity> rssItems = XMLReader
					.getRssItemsByFileContent(xmlDoc); // 获取item实例
			System.out.println(rssUrls.get(i) + "     ItemsCount: "
					+ rssItems.size());
			List<RssItemEntity> dupRssItems = CompareHelper
					.getDuplicateRssItems(rssItems);
			if (dupRssItems.size() > 0) { // 如果存在相同的item
				for (int j = 0; j < dupRssItems.size(); j++) {
					System.out.println(dupRssItems.get(j).title);
					System.out.println(dupRssItems.get(j).guid);
				}
				System.out.println("\r\n");
			} else {
				System.out.println("There is no duplicate RSS items!" + "\r\n");
			}
		}
	}

	/* 验证网页的a标签链接是否为空，以及能否正确跳转。 */
	public static void VerifyHrefLinkValidity(String domainName, String urlFile)
			throws IOException {
		List<HrefLinkEntity> hrefLinks = new ArrayList<HrefLinkEntity>();
		ArrayList<String> urls = new ArrayList<String>();
		urls = ReadFromFile.getUrlList(urlFile);
		String badLinkResults = "";

		for (int i = 0; i < urls.size(); i++) {
			URL url = new URL(urls.get(i));
			hrefLinks = PageAnalytics.getContentHrefLinkBySingleUrl(url
					.toString());

			for (HrefLinkEntity hrefLink : hrefLinks) { // 判断链接属性值
				if (hrefLink.href.equals("")) {
					hrefLink.hrefStatus = "link is null";
				}
				if (hrefLink.href.equals("#") || hrefLink.href.equals("/")) {
					hrefLink.hrefStatus = PageAnalytics
							.getNotFoundPageByURL(domainName);
				}
				if (hrefLink.href.toLowerCase().startsWith("http")) {
					hrefLink.hrefStatus = PageAnalytics
							.getNotFoundPageByURL(hrefLink.href);
				} else {
					hrefLink.hrefStatus = PageAnalytics
							.getNotFoundPageByURL(domainName + hrefLink.href);
				}
				// System.out.println(hrefLink.href);
			}

			String badLinks = "";
			for (HrefLinkEntity badHref : hrefLinks) {
				if (badHref.hrefStatus != "Accessable") {
					badLinks += badHref.href + "      " + badHref.hrefStatus
							+ "\r\n";
					// System.out.println(urls.get(i) + badLinks);
				}
			}
			if (badLinks != "") { // 如果存在Broken Href Links
				badLinkResults = badLinkResults + urls.get(i) + "\r\n"
						+ badLinks + "\r\n";
			}
		}
		System.out.println(badLinkResults);
	}

	/* 验证网页的img标签链接是否为空，以及能否正确跳转。 */
	public static void VerifyImageLinkValidity(String urlFile)
			throws IOException {
		List<ImageSrcEntity> imageLinks = new ArrayList<ImageSrcEntity>();

		ArrayList<String> urls = new ArrayList<String>();
		urls = ReadFromFile.getUrlList(urlFile);
		int statusCode = -1;
		String brokenResults = "";

		for (int i = 0; i < urls.size(); i++) {
			URL url = new URL(urls.get(i));
			imageLinks = PageAnalytics.getContentItemImageSrcBySingleUrl(url
					.toString());
			imageLinks.addAll(PageAnalytics
					.getContentEditorImageSrcBySingleUrl(url.toString())); // 合并list

			for (ImageSrcEntity imageLink : imageLinks) {
				// System.out.println(imageLink.src);
				if (imageLink.src.equals("")) {
					imageLink.srcStatus = "image link is null.";
				} else {
					statusCode = HttpRequest
							.getStatusCodeBySingleURL(imageLink.src);
					if (statusCode != 200) {
						imageLink.srcStatus = "Image not found";
					} else {
						imageLink.srcStatus = "Image is exits";
					}
				}
			}
			// System.out.println(urls.get(i) + "  :Broken Image Links");
			String urlImageStatus = "";
			for (ImageSrcEntity badImage : imageLinks) {
				if (badImage.srcStatus != "Image is exits") {
					urlImageStatus += badImage.src + "      "
							+ badImage.srcStatus + "\r\n";
					// System.out.println(urls.get(i) + urlImageStauts);
				}
			}
			if (urlImageStatus != "") { // 如果存在Broken Image Links
				brokenResults = brokenResults + urls.get(i) + "\r\n"
						+ urlImageStatus + "\r\n";
			}
		}
		System.out.println(brokenResults);
		// FileWriter.writerFileContent( // 将结果写入文件
		// "C:/eclipse/badImage.txt", outPut);

	}
}
