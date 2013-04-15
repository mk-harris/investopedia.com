import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.io.InputStream;

public class HttpRequest {
	public static String getPageSourceBySingleURL(String httpurl) { // ����http���󣬻�ȡ����ҳ��htmlԴ��
		URL url = null;
		StringBuffer pagesource = new StringBuffer();
		HttpURLConnection con = null;
		try {
			url = new URL(httpurl);
			con = (HttpURLConnection) url.openConnection();
			int statuscode = HttpRequest.getStatusCodeBySingleURL(url
					.toString());
			if (statuscode!=200) { // �������ӻ�������ʧ��
				System.out.println(url.toString() + " :  " + statuscode);
				con.disconnect();
			} else {
				InputStream inStr = con.getInputStream();
				InputStreamReader istreamReader = new InputStreamReader(inStr);
				BufferedReader buffStr = new BufferedReader(istreamReader);
				String str = null;
				while ((str = buffStr.readLine()) != null)
					pagesource.append(str).append("\r\n");
				//System.out.println(pagesource);
				inStr.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
			pagesource = null;
			System.out.println("Request Failed:  " + url.toString());
		} finally {
			con.disconnect();
		}
		return pagesource.toString();

	}

	public static String getPageSourceByFile(String urlfile) // ͨ��urls�б�����ø���url��ҳ��Դ��
			throws IOException {
		ArrayList<String> urls = new ArrayList<String>();
		urls = ReadFromFile.getUrlList(urlfile);
		StringBuffer pagesource = new StringBuffer();
		//int count = 0;
		for (int i = 0; i < urls.size(); i++) {
			URL url = new URL(urls.get(i));
			//count++;
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			int statuscode = HttpRequest.getStatusCodeBySingleURL(url
					.toString()); // ��ȡ��ҳ״̬��
			try {
				if (statuscode!=200) {
					System.out.println(url.toString() + "   " + statuscode);
					con.disconnect();
				} else {
					InputStream inStr = con.getInputStream();
					InputStreamReader istreamReader = new InputStreamReader(
							inStr);
					BufferedReader buffStr = new BufferedReader(istreamReader);
					String str = null;
					while ((str = buffStr.readLine()) != null)
						pagesource.append(str).append("\r\n"); // ������е�pagesource
					//System.out.println(pagesource);
					inStr.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
				pagesource = null;
				System.out.println("Request Failed:  " + url.toString());
			} finally {
				con.disconnect();
			}
		}
		//System.out.println("Total urls number is:  " + count); // ��ȡ�ܵ�url����
		return pagesource.toString();
	}

	public static int getStatusCodeBySingleURL(String httpurl) // ����http���󣬻�ȡ��ҳ������Ϣ��
			throws MalformedURLException {
		URL url = new URL(httpurl);
		HttpURLConnection con = null;
		int statuscode = -1;
		try {
			con = (HttpURLConnection) url.openConnection();
			con.connect();
			con.setConnectTimeout(60000);
			con.setReadTimeout(300000);
			statuscode = con.getResponseCode(); // �����ҳ������Ϣ��
			/*
			 * String message = con.getResponseMessage();
			 * System.out.println(url.toString() + "   " + statuscode + "   " +
			 * message);
			 */
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			con.disconnect();
		}
		return statuscode;
	}

	public static String getStatusCodeByFile(String urlfile) // ��ȡ�ļ��е�urls������ø���url����Ӧ״̬��
			throws MalformedURLException {
		ArrayList<String> urls = new ArrayList<String>();
		urls = ReadFromFile.getUrlList(urlfile);
		String statuscode = "";
		HttpURLConnection con = null;
		for (int i = 0; i < urls.size(); i++) {
			URL url = new URL(urls.get(i));
			try {
				con = (HttpURLConnection) url.openConnection();
				con.connect();
				con.setRequestProperty("User-Agent",
						"Mozilla/5.0 (compatible; MSIE 9.0; Windows NT 6.1; Trident/5.0))");// IE�����������
				con.setConnectTimeout(60000);
				con.setReadTimeout(300000);
				con.setUseCaches(false);
				statuscode = new Integer(con.getResponseCode()).toString(); // �����ҳ������Ϣ��
				String message = con.getResponseMessage();
				System.out.println(urls.toString() + "   " + statuscode + "   "
						+ message);
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				con.disconnect();
			}
		}
		return statuscode;
	}
}
