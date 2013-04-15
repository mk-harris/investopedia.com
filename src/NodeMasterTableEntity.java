
public class NodeMasterTableEntity {

	public NodeMasterTableEntity(String title,String nid, String vid, String type) {
		this.title = title;
		this.nid = nid;
		this.vid = vid;
		this.type = type;
	}

	public String title;
	public String nid;
	public String vid;
	public String type;
	public String summary;
	
	public boolean equals(Object other) { 
		boolean result = false;
		if (other instanceof NodeMasterTableEntity) {
			NodeMasterTableEntity that = (NodeMasterTableEntity) other;
			result = (this.nid.equals(that.nid));
		}
		return result;
	}

	@Override
	public int hashCode() {
		return this.nid.hashCode();
	}
}

