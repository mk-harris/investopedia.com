import java.io.FileNotFoundException;
import java.lang.String;
import java.util.ArrayList;
import java.util.List;

public class CompareHelper {

	public static List<TaxonomyEntity> compareTaxonomy( // 比较两个list中的Taxonomy值是否相同
			String taxonomy1Str, String taxonomy2Str) {
		List<TaxonomyEntity> list1 = TaxonomyHelper
				.getPageTaxonomyList(taxonomy1Str);
		List<TaxonomyEntity> list2 = TaxonomyHelper
				.getPageTaxonomyList(taxonomy2Str);
		List<TaxonomyEntity> list = new ArrayList<TaxonomyEntity>();
		if (list1.size() == list2.size()) {
			for (int i = 0; i < list1.size(); i++) {
				if (!list1.get(i).value.equals(list2.get(i).value)) {
					list.add(new TaxonomyEntity(list1.get(i).key,
							list1.get(i).value, list2.get(i).value));
				}
			}
		}
		return list;
	}

	public static List<RssItemEntity> getDuplicateRssItems(
			List<RssItemEntity> items) { // 获取同一个rss中是否存在一样的item
		List<RssItemEntity> tempList = new ArrayList<RssItemEntity>();
		List<RssItemEntity> dupList = new ArrayList<RssItemEntity>();
		List<RssItemEntity> resultList = new ArrayList<RssItemEntity>();
		for (int i = 0; i < items.size() - 1; i++) {
			if (!tempList.contains(items.get(i))) {
				tempList.add(items.get(i));
			} else {
				dupList.add(items.get(i));
			}
		}
		for (int m = 0; m < dupList.size(); m++) {
			for (int n = 0; n < items.size(); n++) {
				if (dupList.get(m).equals(items.get(n))) {
					resultList.add(items.get(n));
				}
			}
		}
		return resultList;
	}

	public static List<MetaTagsEntity> compareMetaTags( // 比较两个list中的MetaTags值是否相同
			String testurl, String wwwurl) throws FileNotFoundException {
		List<MetaTagsEntity> list1 = PageAnalytics
				.getMetaTagesBySingleHttpURL(testurl);
		//System.out.println(list1.toString());
		List<MetaTagsEntity> list2 = PageAnalytics
				.getMetaTagesBySingleHttpURL(wwwurl);
		//System.out.println(list2.toString());
		List<MetaTagsEntity> list = new ArrayList<MetaTagsEntity>();
		if (list1.size() == list2.size()) { 
			for (int i = 0; i < list1.size(); i++) {
				if (!list1.get(i).content.equals(list2.get(i).content)) {
					list.add(new MetaTagsEntity(list1.get(i).name,
							list1.get(i).content, list2.get(i).content));
				}
			}
		} else {
			System.out.println(testurl + "\r\n"
					+ "The Meta Tags number is different");
		}
		return list;
	}	
	
}
