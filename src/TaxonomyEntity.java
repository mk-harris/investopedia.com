/* Taxonomyʵ���б�,�洢Taxonomy�ĸ�������������ֵ */
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
