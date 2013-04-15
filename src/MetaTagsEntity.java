public class MetaTagsEntity {
	public MetaTagsEntity(String id, String name, String content) {
		this.id = id;
		this.name = name;
		this.content = content;
	}

	public String id;
	public String name;
	public String content;

	public boolean equals(Object other) { 
		boolean result = false;
		if (other instanceof MetaTagsEntity) {
			MetaTagsEntity that = (MetaTagsEntity) other;
			result = (this.name.equals(that.name));
		}
		return result;
	}

	@Override
	public int hashCode() {
		return this.name.hashCode();
	}
}
