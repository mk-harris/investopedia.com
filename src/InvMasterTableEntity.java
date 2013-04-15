public class InvMasterTableEntity {
	public InvMasterTableEntity(String pid, String title) {
		this.pid = pid;
		this.title = title;
	}

	public String pid;
	public String title;

	public boolean equals(Object other) {
		boolean result = false;
		if (other instanceof InvMasterTableEntity) {
			InvMasterTableEntity that = (InvMasterTableEntity) other;
			result = (this.pid.equals(that.pid));
		}
		return result;
	}

	@Override
	public int hashCode() {
		return this.pid.hashCode();
	}
}
