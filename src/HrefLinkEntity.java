
public class HrefLinkEntity {

		public HrefLinkEntity(String href) {

			this.href = href;
		}
		
		public String href;
		public String hrefStatus;

		public boolean equals(Object other) { //
			boolean result = false;
			if (other instanceof HrefLinkEntity) {
				HrefLinkEntity that = (HrefLinkEntity) other;
				result = (this.href.equals(that.href));
			}
			return result;
		}

		@Override
		public int hashCode() {
			return this.href.hashCode();
		}
	}
