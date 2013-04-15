import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PageAnalytics {

	public static boolean matchStaticText(String source, String staticText) { // 匹配静态文本，返回true或false
		//System.out.println(staticText);
		return source.contains(staticText);
	}

	/*
	 * 获取如：<meta id="Robots" name="robots" content="index,follow"/>
	 */
	public static String getRobotsValueByFile(String urlfile)
			throws IOException {
		ArrayList<String> urls = new ArrayList<String>();
		urls = ReadFromFile.getUrlList(urlfile);
		String RobotsValue = null;
		for (int i = 0; i < urls.size(); i++) {
			URL url = new URL(urls.get(i));
			RobotsValue = PageAnalytics.getRobotsValueBySingleURL(url
					.toString());
			if (RobotsValue != null) {
				System.out.println((i + 1) + ". " + url.toString() + "\r\n"
						+ RobotsValue + "\r\n");
			} else {
				System.out.println(url.toString() + "\r\n" + "No Robots found");
			}
		}
		return RobotsValue;
	}

	public static String getRobotsValueBySingleURL(String httpurl)
			throws IOException {
		String pageSource = HttpRequest.getPageSourceBySingleURL(httpurl)
				.replaceAll("\r\n", ""); // 替换换行
		String regEx = "<meta\\s*id=\"Robots\"\\s*name=\"robots\"\\s*content=\"([\\S]*)\"\\s*/>";
		Pattern pattern = Pattern.compile(regEx, Pattern.CASE_INSENSITIVE); // 这个用来设置要匹配的规则(字符串或者正则表达式)
		Matcher matcher = pattern.matcher(pageSource); // 通过规则进行字符串匹配
		String RobotsValue = null;
		while (matcher.find()) {
			RobotsValue = matcher.group();
			// System.out.println(RobotsValue);
		}
		return RobotsValue;
	}

	/*
	 * 以下方法匹配形如： <meta name="description" content=
	 * "Investopedia is a premiere resource for investing education, personal finance."
	 * /> <meta id="Robots" name="robots" content="index,follow"/> <meta
	 * id="MetaOgUrl" name="og:url" content="http://www.investopedia.com/"/>
	 * <meta id="MetaOgSiteName" name="og:site_name" content="Investopedia"/>
	 * <meta id="MetaOgType" name="og:type" content="Landing"/> <meta
	 * id="MetaOgTitle" name="og:title"
	 * ontent="Investopedia - Educating the world about finance"/> <meta
	 * id="MetaCategory" name="category" content="General/Homepage"/> <meta
	 * id="MetaPublishDate" name="publish-date"
	 * content="Sat, 23 Feb 2013 10:11:20 GMT"/> <meta name="keywords"
	 * content="dictionary,glossary,education,investment,tutorials,investing,invest"
	 * />
	 */
	public static ArrayList<String> getMetaTagsBySingleURL(String httpurl)
			throws IOException {
		String pageSource = HttpRequest.getPageSourceBySingleURL(httpurl)
				.replaceAll("\r\n", "");
		ArrayList<String> metaTags = new ArrayList<String>();

		Pattern description = Pattern
				.compile("<meta\\s*name=\"description\"\\s*content=\"[^<]*\"\\s*/>"); // 这个用来设置要匹配的规则(字符串或者正则表达式)
		Pattern Robots = Pattern
				.compile("<meta\\s*id=\"Robots\"\\s*name=\"robots\"\\s*content=\"([\\S]*)\"\\s*/>");
		Pattern MetaOgUrl = Pattern
				.compile("<meta\\s*id=\"MetaOgUrl\"\\s*name=\"og:url\"\\s*content=\"[\\S]*\"\\s*/>");
		Pattern MetaOgSiteName = Pattern
				.compile("<meta\\s*id=\"MetaOgSiteName\"\\s*name=\"og:site_name\"\\s*content=\"Investopedia\"[\\s]*/>");
		Pattern MetaOgType = Pattern
				.compile("<meta\\s*id=\"MetaOgType\"\\s*name=\"og:type\"\\s*content=\"[^<]*\"\\s*/>");
		Pattern MetaOgTitle = Pattern
				.compile("<meta\\s*id=\"MetaOgTitle\"\\s*name=\"og:title\"\\s*content=\"[^<]*\"[\\s]*/>");
		Pattern MetaOgImage = Pattern
				.compile("<meta\\s*id=\"MetaOgImage\"\\s*name=\"og:image\"\\s*content=\"[\\S]*\"[\\s]*/>");
		Pattern MetaCategory = Pattern
				.compile("<meta\\s*id=\"MetaCategory\"\\s*name=\"category\"\\s*content=\"[^<]*\"[\\s]*/>");
		Pattern MetaPublishDate = Pattern
				.compile("<meta\\s*id=\"MetaPublishDate\"\\s*name=\"publish-date\"\\s*content=\"[^<]*\"[\\s]*/>");
		Pattern keywords = Pattern
				.compile("<meta\\s*name=\"keywords\"\\s*content=\"[^<]*\"[\\s]*/>");

		Matcher Desc = description.matcher(pageSource); // 通过规则进行字符串匹配
		Matcher Robot = Robots.matcher(pageSource);
		Matcher Url = MetaOgUrl.matcher(pageSource);
		Matcher SiteName = MetaOgSiteName.matcher(pageSource);
		Matcher Type = MetaOgType.matcher(pageSource);
		Matcher Title = MetaOgTitle.matcher(pageSource);
		Matcher Image = MetaOgImage.matcher(pageSource);
		Matcher Category = MetaCategory.matcher(pageSource);
		Matcher PublishDate = MetaPublishDate.matcher(pageSource);
		Matcher Keyword = keywords.matcher(pageSource);

		if (Desc.find()) {
			// System.out.println(Desc.group());
			metaTags.add(Desc.group());
		} else {
			// System.out.println("Not found Description, please check!");
			metaTags.add("Not found Description");
		}
		if (Robot.find()) {
			// System.out.println(Robot.group());
			metaTags.add(Robot.group());
		} else {
			// System.out.println("Not found Robots, please check!");
			metaTags.add("Not found Robots");
		}
		if (Url.find()) {
			// System.out.println(Url.group());
			metaTags.add(Url.group());
		} else {
			// System.out.println("Not found MetaOgUrl, please check!");
			metaTags.add("Not found MetaOgUrl");
		}
		if (SiteName.find()) {
			// System.out.println(SiteName.group());
			metaTags.add(SiteName.group());
		} else {
			// System.out.println("Not found MetaOgSiteName, please check!");
			metaTags.add("Not found MetaOgSiteName");
		}
		if (Type.find()) {
			// System.out.println(Type.group());
			metaTags.add(Type.group());
		} else {
			// System.out.println("Not found MetaOgType, please check!");
			metaTags.add("Not found MetaOgType");
		}
		if (Title.find()) {
			// System.out.println(Title.group());
			metaTags.add(Title.group());
		} else {
			// System.out.println("Not found MetaOgTitle, please check!");
			metaTags.add("Not found MetaOgTitle");
		}
		if (Image.find()) {
			// System.out.println(Image.group());
			metaTags.add(Image.group());
		} else {
			// System.out.println("Not found MetaOgImage, please check!");
			metaTags.add("Not found MetaOgImage");
		}
		if (Category.find()) {
			// System.out.println(Category.group());
			metaTags.add(Category.group());
		} else {
			// System.out.println("Not found MetaCategory, please check!");
			metaTags.add("Not found MetaCategory");
		}
		if (PublishDate.find()) {
			// System.out.println(PublishDate.group());
			metaTags.add(PublishDate.group());
		} else {
			// System.out.println("Not found PublishDate, please check!");
			metaTags.add("Not found PublishDate");
		}
		if (Keyword.find()) {
			// System.out.println(Keyword.group());
			metaTags.add(Keyword.group());
		} else {
			// System.out.println("Not found Keywords, please check!");
			metaTags.add("Not found Keywords");
		}
		return metaTags;
	}

	/* 获取Meta Tags的Entity list值，用于与数据库做对比 */
	public static List<MetaTagsEntity> getMetaTagesBySingleHttpURL(String url)
			throws FileNotFoundException {
		String pageSource = HttpRequest.getPageSourceBySingleURL(url).replaceAll("\r\n", "");
		//System.out.println(pageSource);
		List<MetaTagsEntity> metaTags = new ArrayList<MetaTagsEntity>();
		String reg = "<meta([^<]*)name=\"(\\S*)\"(\\s*)content=\"([^<]*)\"(\\s*)/>";

		Pattern regExpression = Pattern.compile(reg);
		Matcher match = regExpression.matcher(pageSource); // 通过规则进行字符串匹配

		while (match.find()) {
			String id = null;
			//System.out.println(match.group());
			if (match.group(1) != "") {
				id = match.group(1);
			}
			String name = match.group(2);
			String content = match.group(4);
			metaTags.add(new MetaTagsEntity(id, name, content));
		}
		return metaTags;
	}

	public static ArrayList<String> getMetaTagsByFile(String urlfile)
			throws IOException {
		ArrayList<String> urls = new ArrayList<String>();
		ArrayList<String> metaTag = new ArrayList<String>();
		ArrayList<String> metaTags = new ArrayList<String>();
		urls = ReadFromFile.getUrlList(urlfile);

		for (int i = 0; i < urls.size(); i++) {
			URL url = new URL(urls.get(i));
			metaTag = PageAnalytics.getMetaTagsBySingleURL(url.toString());
			String metaValue = "";
			for (int j = 0; j < metaTag.size(); j++) {
				metaValue = metaValue + metaTag.get(j) + "\r\n";
			}
			metaTags.add(url.toString() + "\r\n" + metaValue);
		}
		for (int m = 0; m < metaTags.size(); m++) {
			System.out.println(metaTags.get(m));
		}
		return metaTags;
	}

	/*
	 * 获取网页源码中的page title标签内容。如：<title>Biggest Stock Scams - Slideshow |
	 * Investopedia</title>
	 */
	public static void getPageTitleByFile(String urlfile) throws IOException {
		ArrayList<String> urls = new ArrayList<String>();
		urls = ReadFromFile.getUrlList(urlfile);
		for (int i = 0; i < urls.size(); i++) {
			URL url = new URL(urls.get(i));
			String pageSource = HttpRequest.getPageSourceBySingleURL(url
					.toString());
			Pattern pattern = Pattern.compile(
					"<title>[^.]*[\\s]*.*[^.]*[\\s]*</title>",
					Pattern.CASE_INSENSITIVE); // 这个用来设置要匹配的规则(字符串或者正则表达式)，忽略大小写
			Matcher matcher = pattern.matcher(pageSource); // 通过规则进行字符串匹配
			if (matcher.find()) {
				System.out.println((i + 1) + ". " + url.toString() + "\r\n"
						+ matcher.group() + "\r\n");
			} else
				System.out.println((i + 1) + ". " + url.toString() + "\r\n"
						+ "	No Result Found" + "\r\n");
		}
	}

	public static String getPageTitleBySingleURl(String url) throws IOException {
		String pageSource = HttpRequest.getPageSourceBySingleURL(url);
		String pageTitle = "";
		Pattern pattern = Pattern.compile(
				"<title>[^.]*[\\s]*.*[^.]*[\\s]*</title>",
				Pattern.CASE_INSENSITIVE); // 这个用来设置要匹配的规则(字符串或者正则表达式)，忽略大小写
		Matcher matcher = pattern.matcher(pageSource); // 通过规则进行字符串匹配
		if (matcher.find()) {
			pageTitle = matcher.group();
			// System.out.println(pageTitle);
		} else {
			System.out.println("No Result Found");
		}
		return pageTitle;
	}

	/* 获取网站404网页 */
	public static void getNotFoundPageByFile(String urlfile) throws IOException {
		String title = "";
		ArrayList<String> urls = new ArrayList<String>();
		urls = ReadFromFile.getUrlList(urlfile);
		for (int i = 0; i < urls.size(); i++) {
			URL url = new URL(urls.get(i));
			int statusCode = HttpRequest.getStatusCodeBySingleURL(url
					.toString());
			if (statusCode == 404) {
				System.out.println(urls.get(i) + "    " + statusCode + "\r\n");
			} else {
				if (statusCode == 200) {
					title = PageAnalytics.getPageTitleBySingleURl(url
							.toString());
					if (title.contains("Page not found")) {
						System.out.println(urls.get(i) + "    " + statusCode
								+ "  Page not found" + "\r\n");
					}
				}
			}
		}
	}

	/* 获取网站404网页 */
	public static String getNotFoundPageByURL(String httpURL)
			throws IOException {
		String pageStatus = "";
		URL url = new URL(httpURL);
		int statusCode = HttpRequest.getStatusCodeBySingleURL(url.toString());
		//System.out.println(statusCode);
		if (statusCode == 404) {
			pageStatus = "Page not found:404";
		} else if (statusCode == 200) {
			String title = PageAnalytics
					.getPageTitleBySingleURl(url.toString());
			if (title.contains("Page not found")) {
				pageStatus = "Page not found:200";
			} else {
				pageStatus = "Accessable";
			}
		}
		return pageStatus;
	}

	/* 获取no result found module */
	public static void getBlockNoResultPage(String urlfile) throws IOException {
		String pagesource = "";
		ArrayList<String> urls = new ArrayList<String>();
		urls = ReadFromFile.getUrlList(urlfile);
		for (int i = 0; i < urls.size(); i++) {
			URL url = new URL(urls.get(i));
			pagesource = HttpRequest.getPageSourceBySingleURL(url.toString());
			String pageContent = pagesource.substring(pagesource.toLowerCase() // 获取<div
																				// id="Header"...>...</div>的源码
					.indexOf("<div id=\"header\""), pagesource.toLowerCase()
					.indexOf("<div id=\"content\""));
			while (pageContent.contains("No results found.")) {
				System.out.println(urls.get(i) + "\r\n"
						+ "The source line is:  " + ("No results found.")
						+ "\r\n");
			}
		}
	}

	/* 获取网页源码中div id="content"中所有的<a>标签的href属性指向的地址 */
	public static List<HrefLinkEntity> getContentHrefLinkBySingleUrl(String url) {
		String pageSource = HttpRequest.getPageSourceBySingleURL(url);
		List<HrefLinkEntity> hrefLinks = new ArrayList<HrefLinkEntity>();
		Pattern pattern = Pattern.compile(
				"<a(\\s)*href=\"(\\S*)\"([^>]*)>([^<]*)</a>",
				Pattern.CASE_INSENSITIVE);
		String pageContent = pageSource.substring(pageSource.toLowerCase() // 获取<div
																			// id="content"...>....</div>的源码
				.indexOf("<div id=\"content\""), pageSource.toLowerCase()
				.indexOf("<div id=\"footer\""));
		Matcher matcher = pattern.matcher(pageContent);
		while (matcher.find()) {
			String hrefLink = matcher.group(2);
			if (!hrefLink.equals("'+playlistItems[i].Page+'")) {
				hrefLinks.add(new HrefLinkEntity(hrefLink));
				// System.out.println(hrefLink);
			}
		}
		// System.out.println(hrefLinks.size());
		return hrefLinks;
	}

	/* 获取网页源码中div id="Header"中所有的<a>标签的href属性指向的地址 */
	public static List<HrefLinkEntity> getHeaderHrefLinkBySingleUrl(String url) {
		String pageSource = HttpRequest.getPageSourceBySingleURL(url);
		List<HrefLinkEntity> hrefLinks = new ArrayList<HrefLinkEntity>();
		Pattern pattern = Pattern.compile(
				"<a(\\s)*href=\"(\\S*)\"([^>]*)>([^<]*)</a>",
				Pattern.CASE_INSENSITIVE);
		String pageHeader = pageSource.substring(pageSource.toLowerCase() // 获取<div
																			// id="Header"...>...</div>的源码
				.indexOf("<div id=\"header\""), pageSource.toLowerCase()
				.indexOf("<div id=\"content\""));
		Matcher matcher = pattern.matcher(pageHeader);
		while (matcher.find()) {
			String hrefLink = matcher.group(2);
			hrefLinks.add(new HrefLinkEntity(hrefLink));
			System.out.println(hrefLink);
		}
		// System.out.println(hrefLinks.size());
		return hrefLinks;
	}

	/* 获取网页源码中div id="Footer"中所有的<a>标签的href属性指向的地址 */
	public static List<HrefLinkEntity> getFooterHrefLinkBySingleUrl(String url) {
		String pageSource = HttpRequest.getPageSourceBySingleURL(url);
		List<HrefLinkEntity> hrefLinks = new ArrayList<HrefLinkEntity>();
		Pattern pattern = Pattern.compile(
				"<a(\\s)*href=\"(\\S*)\"([^>]*)>([^<]*)</a>",
				Pattern.CASE_INSENSITIVE);
		String pageFooter = pageSource.substring(pageSource.toLowerCase()
				.indexOf("<div id=\"footer\""), pageSource.toLowerCase()
				.indexOf("</html>"));
		Matcher matcher = pattern.matcher(pageFooter);
		while (matcher.find()) {
			String hrefLink = matcher.group(2);
			hrefLinks.add(new HrefLinkEntity(hrefLink));
			// System.out.println(hrefLink);
		}
		// System.out.println(hrefLinks.size());
		return hrefLinks;
	}

	/* 获取网页源码中div id="Header"的img标签的src属性指向的地址 */
	public static List<ImageSrcEntity> getHeadertItemImageSrcBySingleUrl(String url) {
		String pagesource = HttpRequest.getPageSourceBySingleURL(url)
				.replaceAll("\r\n", "");
		String pageHeader = pagesource.substring(pagesource.toLowerCase()
				.indexOf("<div id=\"header\""), pagesource.toLowerCase()
				.indexOf("<div id=\"content\""));
		List<ImageSrcEntity> imageSrc = new ArrayList<ImageSrcEntity>();

		Pattern itemImage = Pattern
				.compile(
						"<div(\\s)*class=\"item-image-src\"(\\s)*style=\"(.*)\">([^>]*)</div>",
						Pattern.CASE_INSENSITIVE);
		Matcher itemImage_src = itemImage.matcher(pageHeader);

		while (itemImage_src.find()) {
			System.out.println(itemImage_src.group());
			String itemLink = itemImage_src.group(3);
			// System.out.println(itemLink);
			int beginIndex = itemLink.indexOf("(");
			int endIndex = itemLink.indexOf(")");
			String itemImageLink = itemLink.substring(beginIndex + 1, endIndex);
			imageSrc.add(new ImageSrcEntity(itemImageLink));
			// System.out.println(itemImageLink);
		}
		// System.out.println(imageSrc.size());
		return imageSrc;
	}

	/* 获取网页源码中div id="content"的img标签的src属性指向的地址 */
	public static List<ImageSrcEntity> getContentItemImageSrcBySingleUrl(
			String url) {
		String pagesource = HttpRequest.getPageSourceBySingleURL(url);
		String pageContent = pagesource.substring(pagesource.toLowerCase() // 获取<div
																			// id="content"...>....</div>的源码
				.indexOf("<div id=\"content\""), pagesource.toLowerCase()
				.indexOf("<div id=\"footer\""));
		List<ImageSrcEntity> imageSrc = new ArrayList<ImageSrcEntity>();

		Pattern itemImage = Pattern
				.compile(
						"<div(\\s)*class=\"item-image-src\"(\\s)*style=\"(\\S*)(.*)\">([^>]*)</div>",
						Pattern.CASE_INSENSITIVE);
		Matcher itemImage_src = itemImage.matcher(pageContent);

		while (itemImage_src.find()) {
			// System.out.println(itemImage_src.group());
			String itemLink = itemImage_src.group(3);
			// System.out.println(itemLink);
			int beginIndex = itemLink.indexOf("(");
			int endIndex = itemLink.indexOf(")");
			String itemImageLink = itemLink.substring(beginIndex + 1, endIndex);
			imageSrc.add(new ImageSrcEntity(itemImageLink));
			//System.out.println(itemImageLink);
		}
		//System.out.println(imageSrc.size());
		return imageSrc;
	}

	/*
	 * 匹配如：<img src="http://i.investopedia.com/assets_v2/img/icon_book.png"
	 * style="width:60px; vertical-align:middle; margin-right:10px;">
	 */
	public static List<ImageSrcEntity> getContentEditorImageSrcBySingleUrl(
			String url) {
		String pagesource = HttpRequest.getPageSourceBySingleURL(url);
		List<ImageSrcEntity> imageSrc = new ArrayList<ImageSrcEntity>();

		Pattern bodyImage = Pattern.compile(
				"<img([^_]*)src=\"([\\S]*)\"([^>]*)>", Pattern.CASE_INSENSITIVE
						| Pattern.MULTILINE);
		String pageContent = pagesource.substring(pagesource.toLowerCase()
				.indexOf("<div id=\"content\""), pagesource.toLowerCase()
				.indexOf("<div id=\"footer\""));
		Matcher bodyImage_src = bodyImage.matcher(pageContent);

		while (bodyImage_src.find()) {
			// System.out.println(bodyImage_src.group());
			String image_Link = bodyImage_src.group(2);
			if (image_Link.contains("http://i.investopedia.com")) {
				imageSrc.add(new ImageSrcEntity(image_Link));
				//System.out.println(image_Link);
			}
		}
		//System.out.println(imageSrc.size());
		return imageSrc;
	}

	/*
	 * 匹配如：<img pagespeed_lazy_src=
	 * "http://i.investopedia.com/vcb/assets_v2/img/xlogo_investopedia_black.png.pagespeed.ic.LgMBDiJIKN.png"
	 * alt="Investopedia" src=
	 * "data:image/gif;base64,R0lGODlhAQABAAAAACH5BAEKAAEALAAAAAABAAEAAAICTAEAOw=="
	 * onload="pagespeed.lazyLoadImages.loadIfVisible(this);"/>
	 */
	public static List<ImageSrcEntity> getPageSpeedImageSrcBySingleUrl(
			String url) {
		String pagesource = HttpRequest.getPageSourceBySingleURL(url);
		List<ImageSrcEntity> imageSrc = new ArrayList<ImageSrcEntity>();

		Pattern pagespeedimage = Pattern.compile(
				"<img(.*)pagespeed_lazy_src=\"([\\S]*)\"([^>]*)>",
				Pattern.CASE_INSENSITIVE | Pattern.MULTILINE);
		Matcher pagespeed_src = pagespeedimage.matcher(pagesource);

		while (pagespeed_src.find()) {
			// System.out.println(pagespeed_src.group());
			String imageLink = pagespeed_src.group(2);
			// System.out.println(pagespeed_src.groupCount());

			if (imageLink.contains("http://i.investopedia.com")) {
				imageSrc.add(new ImageSrcEntity(imageLink));
				System.out.println(imageLink);
			}
		}
		System.out.println(imageSrc.size());
		return imageSrc;
	}

	// 匹配如：<input type="image" name="ctl00$ctl00$BBCPH$SearchBar$SearchButton"
	// id="BBCPH_SearchBar_SearchButton" class="SearchButton icon"
	// src="http://i.investopedia.com/assets_v2/img/search_button.png">
	public static List<ImageSrcEntity> getInputImageSrcBySingleUrl(String url) {
		String pagesource = HttpRequest.getPageSourceBySingleURL(url);
		List<ImageSrcEntity> imageSrc = new ArrayList<ImageSrcEntity>();

		Pattern inputImage = Pattern.compile(
				"<input([^>]*)src=\"([\\S]*)\"(\\s)*>",
				Pattern.CASE_INSENSITIVE);
		Matcher inputImage_src = inputImage.matcher(pagesource.replaceAll(
				"\r\n", ""));

		while (inputImage_src.find()) {
			//System.out.println(inputImage_src.group());
			String inputImageLink = inputImage_src.group(2);
			System.out.println(inputImageLink);
			imageSrc.add(new ImageSrcEntity(inputImageLink));
		}
		// System.out.println(imageSrc.size());
		return imageSrc;
	}

}
