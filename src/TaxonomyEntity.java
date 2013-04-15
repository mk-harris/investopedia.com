/* Taxonomy实例列表,存储Taxonomy的各个参数及参数值 */
public class TaxonomyEntity {
	public TaxonomyEntity(String k, String v) {
		key = k;
		value = v;
	}

	public TaxonomyEntity(String k, String v1, String v2) {
		key = k;
		value = v1;
		value2 = v2;
	}

	public String key;
	public String value;
	public String value2;
}
