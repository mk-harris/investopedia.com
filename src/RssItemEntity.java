public class RssItemEntity extends Object {

	/* Rssitemʵ���б�,�洢items�ĸ������Լ�����ֵ */
	public RssItemEntity(String title, String link, String description,
			String guid, String pubDate) {
		this.title = title;
		this.link = link;
		this.description = description;
		this.guid = guid;
		this.pubDate = pubDate;
	}

	public String title;
	public String link;
	public String description;
	public String guid;
	public String pubDate;

	@Override
	public boolean equals(Object other) {
		boolean result = false;
		if (other instanceof RssItemEntity) {
			RssItemEntity that = (RssItemEntity) other;
			result = (this.guid.equals(that.guid));
		}
		return result;
	}

	@Override
	public int hashCode() {
		return this.guid.hashCode();
	}
}
