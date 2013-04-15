import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class GetDBData {

	public static String getPageURLSource(String httpURL)
			throws ClassNotFoundException, SQLException {
		Connection con = ConnectDB.ConnectMYSQL();
		String source = null;
		ResultSet queryValues = null;
		PreparedStatement sta = null;
		String URL = removeDomain(httpURL);
		try {
			String sql_source = "SELECT source FROM devel_url_alias WHERE alias = '?' AND language = 'und' ORDER BY pid DESC LIMIT 1"; // 根据URL获取Drupal内部路径(PATH),找不到结果,则认为URL就是内部路径
			System.out.println(sql_source);
			sta = con.prepareStatement(sql_source);
			sta.setString(1, URL);
			queryValues = sta.executeQuery();
			if (queryValues.next()) {
				source = queryValues.getString("source");
				queryValues.close();
			} else {
				System.out.println("The source result is empty");
			}
		} finally {
			queryValues.close();
			con.close();
		}
		return source;
	}

	public static String getPageTitle(String httpURL)
			throws ClassNotFoundException, SQLException {
		String pageTitle = null;
		NodeMasterTableEntity nodeMasterValue = null;
		InvMasterTableEntity invMasterValue = null;
		String URL = removeDomain(httpURL);
		try {
			String sourceData = getPageURLSource(URL);
			if (sourceData != null) {
				if (sourceData.contains("/invpage/")) {
					String PID = sourceData.replace("/invpage/", ""); // 获得PID
					invMasterValue = getInvMasterTableEntity(PID);
					if (invMasterValue != null) {
						pageTitle = invMasterValue.title;
					}
				}
				if (sourceData.contains("/node/")) {
					String NID = sourceData.replace("/node/", "");
					nodeMasterValue = getNodeMasterTableEntity(NID);
					if (nodeMasterValue != null) {
						pageTitle = nodeMasterValue.title;
					}
				}
			} else {
				System.out.println("The source query result is empty");
			}
		} catch (Exception e) {
			System.out.println("Exception:  " + e.toString());
		}
		return pageTitle;
	}

	private static NodeMasterTableEntity getNodeMasterTableEntity(String NID)
			throws ClassNotFoundException, SQLException {
		Connection con = ConnectDB.ConnectMYSQL();
		PreparedStatement psta = null;
		NodeMasterTableEntity nodeMasterValue = null;
		try { // 获得NID
			String Sql = "SELECT nid, vid, type,title FROM devel_node WHERE nid = ?";
			// String Sql =
			// "SELECT nid, vid, type,title FROM devel_node WHERE nid = '?'";
			psta = con.prepareStatement(Sql);
			psta.setInt(1, Integer.parseInt(NID));
			// psta.setString(1, NID);
			ResultSet rs = psta.executeQuery();
			if (rs.next()) {
				String nid = rs.getString("nid");
				String vid = rs.getString("vid");
				String type = rs.getString("type");
				String title = rs.getString("title");
				nodeMasterValue = new NodeMasterTableEntity(nid, title, vid,
						type);
				rs.close();
			} else {
				System.out.println("The query result is empty");
			}
		} finally {
			con.close();
		}
		return nodeMasterValue;

	}

	private static InvMasterTableEntity getInvMasterTableEntity(String PID)
			throws ClassNotFoundException, SQLException {
		Connection con = ConnectDB.ConnectMYSQL();
		String pageTitle = null;
		String created =null;
		PreparedStatement psta = null;
		InvMasterTableEntity invMasterValue = null;
		try {
			String getTitleSql = "SELECT pid, title, created FROM devel_invpage WHERE pid = '?'";
			psta = con.prepareStatement(getTitleSql);
			psta.setString(1, PID);
			ResultSet rs = psta.executeQuery();
			if (rs.next()) {
				String pid = rs.getString("pid");
				pageTitle = rs.getString("title");
				created=rs.getString("created");							
				invMasterValue = new InvMasterTableEntity(pid, pageTitle);
				rs.close();
			} else {
				System.out.println("The sourceData is null");
			}
		} finally {
			con.close();
		}
		return invMasterValue;
	}

	/* 去domain，去/ */
	private static String removeDomain(String httpURL) {
		httpURL = httpURL.toLowerCase().replace("http://", "")
				.replace("https://", "");
		httpURL = httpURL.substring(httpURL.indexOf("/"), httpURL.length());

		if (httpURL.endsWith("/")) {
			httpURL = httpURL.substring(0, httpURL.length() - 1);
			System.out.println(httpURL);
		}
		return httpURL;
	}

	public static List<MetaTagsEntity> getMetaTags(String httpurl) {
		String MetaOgTitle = null;
		String VID = null;
		String type = null;
		List<MetaTagsEntity> nodeMetaTags = new ArrayList<MetaTagsEntity>();
		InvMasterTableEntity invMasterValue = null;
		NodeMasterTableEntity nodeMasterValue = null;
		try {
			String sourceData = getPageURLSource(httpurl);

			if (sourceData != null) {
				// if (sourceData.contains("/invpage/")) {
				// String PID = sourceData.replace("/invpage/", ""); //
				// 获得PID
				// invMasterValue = getInvMasterTableEntity(PID);
				// if (invMasterValue != null) {
				// MetaOgTitle = invMasterValue.title;
				// }
				// }
				if (sourceData.contains("/node/")) {
					String NID = sourceData.replace("/node/", "");
					nodeMasterValue = getNodeMasterTableEntity(NID);
					if (nodeMasterValue != null) {
						MetaOgTitle = nodeMasterValue.title;
						VID = nodeMasterValue.vid;
						type = nodeMasterValue.type;

						MetaTagsEntity descMetaTag = getDescMetaTag(VID, NID);
						if (descMetaTag != null) {
							nodeMetaTags.add(descMetaTag);
						}
						nodeMetaTags.add(new MetaTagsEntity("Robots", "robots",
								""));
						nodeMetaTags.add(new MetaTagsEntity("MetaOgUrl",
								"og:url", httpurl));
						nodeMetaTags.add(new MetaTagsEntity("MetaOgSiteName",
								"og:site_name", "Investopedia"));

						MetaTagsEntity ogTypeMetaTag = getMetaOgTypeMetaTag(
								VID, NID);
						if (ogTypeMetaTag != null) {
							nodeMetaTags.add(ogTypeMetaTag);
						}
						nodeMetaTags.add(new MetaTagsEntity("MetaOgTitle",
								"og:title", MetaOgTitle));
					}
					/* 获取image */
					MetaTagsEntity imageMetaTag = getImageMetaTag(VID, NID);
					if (imageMetaTag != null) {
						nodeMetaTags.add(imageMetaTag);
					}
					/* 获取category */
					MetaTagsEntity categoryMetaTag = getCategoryMetaTag(VID,
							NID);
					if (categoryMetaTag != null) {
						nodeMetaTags.add(categoryMetaTag);
					}
					/* get publishDate Meta tag */
					MetaTagsEntity publishDateMetaTag = getPublishDateMetaTag(VID);
					if (publishDateMetaTag != null) {
						nodeMetaTags.add(publishDateMetaTag);
					}
					/* get keywords Meta tag */
					MetaTagsEntity keywordsMetaTag = getKeywordsMetaTag(VID,
							NID);
					if (keywordsMetaTag != null) {
						nodeMetaTags.add(keywordsMetaTag);
					}
				}

			} else {
				System.out.println("The source query result is empty");
			}
		} catch (Exception e) {
			System.out.print("getMetaTags : Exception:");
			e.printStackTrace();
		}
		return nodeMetaTags;
	}

	private static MetaTagsEntity getKeywordsMetaTag(String VID, String NID) {
		MetaTagsEntity keywordsMetaTag = null;
		Connection con;
		PreparedStatement psta;
		try {
			String keywords_sql = "SELECT field_keywords_value FROM devel_field_revision_field_keywords WHERE entity_id = NID AND revision_id = '?'";
			con = ConnectDB.ConnectMYSQL();
			psta = con.prepareStatement(keywords_sql);
			psta.setString(1, VID);
			ResultSet keywords_value = psta.executeQuery();
			String keywords = "";
			if (keywords_value.next()) {
				keywords = keywords_value.getString("field_keywords_value");
				keywords_value.close();
			}

			String tickers_sql = "SELECT field_tickers_value FROM devel_field_revision_field_tickers WHERE entity_id = NID AND revision_id = '?'";
			psta = con.prepareStatement(tickers_sql);
			psta.clearParameters();
			psta.setString(1, VID);
			ResultSet tickers_value = psta.executeQuery();
			String tickers = "";
			if (tickers_value.next()) {
				tickers = tickers_value.getString("field_keywords_value");
				tickers_value.close();
			}

			String tags_sql = "SELECT t.name AS tag FROM devel_field_revision_field_tags AS tags JOIN devel_taxonomy_term_data AS t ON t.tid = tags.field_tags_tid WHERE tags.entity_id = NID AND tags.revision_id = VID";
			psta = con.prepareStatement(tags_sql);
			psta.clearParameters();
			psta.setString(1, NID);
			psta.setString(2, VID);
			ResultSet tags_value = psta.executeQuery();
			String tags = "";
			if (tags_value.next()) {
				tags = tickers_value.getString("t.name AS tag");
				tags_value.close();
			}
			keywordsMetaTag = new MetaTagsEntity("", "keywords", keywords
					+ tags + tickers);
			if (psta != null) {
				psta.close();
			}
			if (con != null) {
				con.close();
			}
		} catch (ClassNotFoundException e) {
			System.out.print("getKeywordsMetaTag : ClassNotFoundException:");
			e.printStackTrace();
		} catch (SQLException ex) {
			System.out.print("getKeywordsMetaTag : SQLException:");
			ex.printStackTrace();
		}

		return keywordsMetaTag;
	}

	private static MetaTagsEntity getPublishDateMetaTag(String VID) {
		MetaTagsEntity publishDateMetaTag = null;
		String publish_date_sql = "SELECT field_sitedate_value FROM devel_field_revision_field_sitedate WHERE entity_id = NID AND revision_id = '?'";
		Connection con;
		PreparedStatement psta;
		try {
			con = ConnectDB.ConnectMYSQL();
			psta = con.prepareStatement(publish_date_sql);
			psta.setString(1, VID);
			ResultSet dateValue = psta.executeQuery();
			String publish_date = "";
			if (dateValue.next()) {
				publish_date = dateValue.getString("field_sitedate_value");
				dateValue.close();
			}
			publishDateMetaTag = new MetaTagsEntity("MetaCategory", "category",
					publish_date);
			if (psta != null) {
				psta.close();
			}
			if (con != null) {
				con.close();
			}
		} catch (ClassNotFoundException e) {
			System.out.print("getPublishDateMetaTag : ClassNotFoundException:");
			e.printStackTrace();
		} catch (SQLException ex) {
			System.out.print("getPublishDateMetaTag : SQLException:");
			ex.printStackTrace();
		}
		return publishDateMetaTag;
	}

	private static MetaTagsEntity getCategoryMetaTag(String VID, String NID) {
		MetaTagsEntity categoryMetaTag = null;
		PreparedStatement psta;
		String tid = null;
		String tid_sql = "SELECT field_channel_tid AS TID FROM devel_field_revision_field_channel WHERE entity_id = '?' AND revision_id = '?'";
		Connection con;
		try {
			con = ConnectDB.ConnectMYSQL();
			psta = con.prepareStatement(tid_sql);
			psta.setString(1, NID);
			psta.setString(2, VID);
			ResultSet tidValue = psta.executeQuery();
			if (tidValue.next()) {
				tid = tidValue.getString("field_channel_tid");
				tidValue.close();

				String parent_tid_sql = "SELECT parent AS PARENT_TID FROM devel_taxonomy_term_hierarchy AS h WHERE h.tid = '?' AND parent <> 0";
				psta.clearParameters();
				psta = con.prepareStatement(parent_tid_sql);
				psta.setString(1, tid);
				ResultSet parent_tid_value = psta.executeQuery();
				String parentCategory = "";
				String childCatetgory = "";
				if (parent_tid_value.next()) {
					parentCategory = parent_tid_value.getString("parent");
					parent_tid_value.close();
				}
				String channel_sql = "SELECT name FROM devel_taxonomy_term_data WHERE tid = '?'";
				psta.clearParameters();
				psta = con.prepareStatement(channel_sql);
				psta.setString(1, tid);
				ResultSet channelValue = psta.executeQuery();
				if (channelValue.next()) {
					childCatetgory = channelValue.getString("name");
					channelValue.close();
				}
				if (parentCategory != "") {
					categoryMetaTag = new MetaTagsEntity("MetaCategory",
							"category", parentCategory + "/" + childCatetgory);
				} else {
					categoryMetaTag = new MetaTagsEntity("MetaCategory",
							"category", childCatetgory + "/");
				}
			}
			if (psta != null) {
				psta.close();
			}
			if (con != null) {
				con.close();
			}
		} catch (ClassNotFoundException e) {
			System.out.print("getCategoryMetaTag : ClassNotFoundException:");
			e.printStackTrace();
		} catch (SQLException ex) {
			System.out.print("getCategoryMetaTag : SQLException:");
			ex.printStackTrace();
		}
		return categoryMetaTag;
	}

	private static MetaTagsEntity getImageMetaTag(String VID, String NID) {
		MetaTagsEntity imageMetaTag = null;
		String type_sql = "SELECT t.name FROM devel_field_revision_field_content_type AS ct JOIN devel_taxonomy_term_data AS t ON t.tid = ct.field_content_type_tid WHERE ct.entity_id = '?' AND ct.revision_id = '?'";
		Connection con;
		PreparedStatement psta;
		ResultSet imageResult;
		try {
			con = ConnectDB.ConnectMYSQL();
			psta = con.prepareStatement(type_sql);
			psta.setString(1, NID);
			psta.setString(2, VID);
			imageResult = psta.executeQuery();
			if (imageResult.next()) {
				String image = imageResult.getString("f.uri");
				if (image.equals("")) {
					System.out.println("The image is inexistence");
				} else {// 替换 public://为 http://

					image = image.replace("public://", "http://");

					imageMetaTag = new MetaTagsEntity("MetaOgImage",
							"og:image", image);
				}
				imageResult.close();
			} else {
				imageMetaTag = new MetaTagsEntity("MetaOgImage", "og:image",
						"http://i.investopedia.com/facebook/investopedia-facebook-image.gif");
			}
			if (psta != null) {
				psta.close();
			}
			if (con != null) {
				con.close();
			}
		} catch (ClassNotFoundException e) {
			System.out.print("getImageMetaTag : ClassNotFoundException:");
			e.printStackTrace();
		} catch (SQLException ex) {
			System.out.print("getImageMetaTag : SQLException:");
			ex.printStackTrace();
		}
		return imageMetaTag;
	}

	private static MetaTagsEntity getMetaOgTypeMetaTag(String VID, String NID) {

		MetaTagsEntity ogTypeMetaTag = null;
		String pageType = null;
		String type_sql = "SELECT t.name FROM devel_field_revision_field_content_type AS ct JOIN devel_taxonomy_term_data AS t ON t.tid = ct.field_content_type_tid WHERE ct.entity_id = '?' AND ct.revision_id = '?'";

		Connection con;
		try {
			PreparedStatement psta;
			con = ConnectDB.ConnectMYSQL();
			psta = con.prepareStatement(type_sql);
			psta.setString(1, NID);
			psta.setString(2, VID);
			ResultSet typeRS = psta.executeQuery();
			if (typeRS.next()) {
				pageType = typeRS.getString("field_summary_value");
				ogTypeMetaTag = new MetaTagsEntity("MetaOgType", "og:type",
						pageType);
			}
			typeRS.close();
			if (psta != null) {
				psta.close();
			}
			if (con != null) {
				con.close();
			}
		} catch (ClassNotFoundException e) {
			System.out.print("getMetaOgTypeMetaTag : ClassNotFoundException:");
			e.printStackTrace();
		} catch (SQLException ex) {
			System.out.print("getMetaOgTypeMetaTag : SQLException:");
			ex.printStackTrace();
		}
		return ogTypeMetaTag;
	}

	private static MetaTagsEntity getDescMetaTag(String VID, String NID) {

		String Description = null;
		MetaTagsEntity descMeta = null;
		String Des_sql = "SELECT field_summary_value FROM devel_field_revision_field_summary WHERE entity_id = '?' AND revision_id = '?'";
		Connection con;
		try {
			PreparedStatement psta;
			con = ConnectDB.ConnectMYSQL();
			psta = con.prepareStatement(Des_sql);
			psta.setString(1, NID);
			psta.setString(2, VID);
			ResultSet Desc = psta.executeQuery();
			if (Desc.next()) {
				Description = Desc.getString("field_summary_value");
				descMeta = new MetaTagsEntity("", "description", Description);
			}			
			Desc.close();
			if (psta != null) {
				psta.close();
			}
			if (con != null) {
				con.close();
			}
		} catch (ClassNotFoundException e) {
			System.out.print("getDescMetaTag : ClassNotFoundException:");
			e.printStackTrace();
		} catch (SQLException ex) {
			System.out.print("getDescMetaTag : SQLException:");
			ex.printStackTrace();
		}
		return descMeta;
	}
}
