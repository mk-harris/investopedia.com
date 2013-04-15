
public class ImageSrcEntity {
	public ImageSrcEntity(String src) {

		this.src = src;
	}
	
	public String src;
	public String srcStatus;
	public boolean equals(Object other) { //
		boolean result = false;
		if (other instanceof ImageSrcEntity) {
			ImageSrcEntity that = (ImageSrcEntity) other;
			result = (this.src.equals(that.src));
		}
		return result;
	}

	@Override
	public int hashCode() {
		return this.src.hashCode();
	}
}

