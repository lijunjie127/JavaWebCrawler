

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Map;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class Util_get {
	
	public static String getHTMLCodeString(String urlString){//得到网页源码这个大的字符串  
        URL url;  
        StringBuffer sb = new StringBuffer();//sb为爬到的网页内容  
        try {
	        url = new URL(urlString);  //爬网页sUrl
	//	              HttpURLConnection urlconnection = (HttpURLConnection)url.openConnection();   
	        URLConnection urlconnection = url.openConnection();  
	//	                urlconnection.addRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.0)");  
	        urlconnection.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64; rv:48.0) Gecko/20100101 Firefox/48.0");
	        InputStream is = url.openStream();  
	        BufferedReader bReader = new BufferedReader(new InputStreamReader(is));  
	        String rLine = null;  
			while((rLine=bReader.readLine()) != null){  
			    sb.append(rLine);//换行需要另加
			    sb.append("\r\n");
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return sb.toString();
	}
	
	private static class TrustAnyTrustManager implements X509TrustManager {
	       
        public void checkClientTrusted(X509Certificate[] chain, String authType)
                throws CertificateException {
        }
 
        public void checkServerTrusted(X509Certificate[] chain, String authType)
                throws CertificateException {
        }
 
        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[] {};
        }
    }
     
     public static String getHTMLCodeString_OK(String urlString) {
		String resultData = null;
		try {
			SSLContext sc = SSLContext.getInstance("SSL");
			sc.init(null, new TrustManager[] { new TrustAnyTrustManager() },
					new java.security.SecureRandom());

			SSLSocketFactory ssf = sc.getSocketFactory();
			URL url = new URL(urlString);

			if ((urlString.startsWith("https")) || (urlString.startsWith("HTTPS"))) {
				HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
				conn.addRequestProperty("User-Agent","Mozilla/5.0 (Windows NT 10.0; WOW64; rv:48.0) Gecko/20100101 Firefox/48.0");// 伪装成已登录用户
				conn.setConnectTimeout(60 * 1000);
				conn.setSSLSocketFactory(ssf);
				conn.connect();
				if (conn.getResponseCode() == 200) {
					InputStream inputStream = conn.getInputStream();// HttpsURLConnection
					ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
					byte[] data = new byte[1024];
					int len = 0;
					try {
						while ((len = inputStream.read(data)) != -1) {
							byteArrayOutputStream.write(data, 0, len);
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
					resultData = new String(byteArrayOutputStream.toByteArray());
				}
			} else {
				StringBuffer sb = new StringBuffer();// sb为爬到的网页内容
				URLConnection urlconnection = url.openConnection();
				urlconnection
						.addRequestProperty("User-Agent",
								"Mozilla/5.0 (Windows NT 10.0; WOW64; rv:48.0) Gecko/20100101 Firefox/48.0");
				InputStream is = url.openStream();
				BufferedReader bReader = new BufferedReader(
						new InputStreamReader(is));
				String rLine = null;
				while ((rLine = bReader.readLine()) != null) {
					sb.append(rLine);// 换行需要另加
					sb.append("\r\n");
				}
				resultData = sb.toString();
			}
		} catch (Throwable e) {
			e.printStackTrace();
		}
		return resultData;
	}
	
	public static void getPropertieList(String str_html){//得到元器件属性和详情的URL
		Document doc = Jsoup.parse(str_html);
		Elements divContent = doc.getElementsByAttributeValue("class", "part-item");//分别获取每个型号的div
		Vector<String> list_properties = new Vector<String>();
		for(int div_i = 0; div_i < divContent.size(); div_i ++)
		{
			doc = Jsoup.parse(divContent.get(div_i).toString());//处理这里面的内容
			
			Elements oneURL = doc.getElementsByAttributeValue("class", "media-left");//一个元器件的元器件详情URL
			String as = oneURL.get(0).getElementsByTag("a").toString();//将Div里面的a标签的内容取出来<a href="/t6522-alb-coilcraft-6747742">
			String [] asA = as.split("\"");//asA[1]=/t6522-alb-coilcraft-6747742
			
			if(!Util_OV.URLlistOV.contains("https://octopart.com" + asA[1])){
				Util_OV.URLlistOV.add("https://octopart.com" + asA[1]);
			}
			
			//元器件名称，元器件详情URL，备用名字，规格型号，datasheetDownLoadURL都已经写入表头了
			Elements td1 = doc.getElementsByAttributeValue("class", "spec-label");
			for(int i = 0; i < td1.size(); i ++)
			{
				if("Manufacturer".equals(td1.get(i).text()))
				{
					list_properties.add("生产厂商");
				}
				else if("Mounting Type".equals(td1.get(i).text()))
				{
					list_properties.add("封装形式");
				}
				else
				{
					list_properties.add(td1.get(i).text());
				}
			}
						
			try {
				Util_writer.writePropertiesToSave(list_properties);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public synchronized static ConcurrentHashMap<String, String> getMap(String resultData){
		ConcurrentHashMap<String, String> map = new ConcurrentHashMap<String, String>();
		Document doc = Jsoup.parse(resultData);
		Elements alternateNames = doc.getElementsByAttributeValue("class", "col-md-4 small text-muted");
		
		String resultData2 = alternateNames.get(0).text();
		for(int alternateNames_i = 1 ; alternateNames_i < alternateNames.size() ; alternateNames_i ++)
		{
			resultData2 = resultData2 + ";;" + alternateNames.get(alternateNames_i).text();
		}
		map.put("备用名字",resultData2);
		
		Elements pdp = doc.getElementsByAttributeValue("class", "pdp-wrapper");
		if(pdp.size() > 0){
			String as = pdp.get(0).getElementsByTag("div").toString();
			String[] tempA =as.split("\"");
				map.put("生产厂商",tempA[3]);
				map.put("规格型号",tempA[5]);
			}
		
		Elements DisName = doc.getElementsByAttributeValue("class", "text-muted small");
		if(DisName.size() > 0){
			map.put("元器件名称", Util.stripNonCharCodepoints(DisName.text()));
		}
		
		Elements td = doc.getElementsByAttributeValue("class", "datasheet-group-row clickable");
		if(td.size() > 0) {
			String as = td.get(0).getElementsByTag("a").toString();
			String [] asA = as.split("\"");
			map.put("datasheetDownLoadURL", asA[1]);
		}
			
		Elements lis = doc.getElementsByAttributeValue("class", "table table-condensed table-striped");
		if(lis.size() > 0) {
			doc = Jsoup.parse(lis.toString());
			Elements trs = doc.select("table").select("tr");
			for(int i = 0 ; i < trs.size() ; i ++){
			    Elements tds = trs.get(i).select("td");
			    Elements tdsb=tds.get(1).select("b");
			    if("Mounting Type".equals(tds.get(0).text()))
				{
					map.put("封装形式", tdsb.text());
				}
			    else
			    {
			    	map.put(tds.get(0).text(), tdsb.text());//列表
			    }
			}
		}
		
		Vector<String> vector1 = new Vector<String>();
		Vector<String> vector2 = new Vector<String>();
		for (Map.Entry<String, String> entry : map.entrySet()) {
			vector1.add(entry.getKey());
			vector2.add(entry.getValue());
       }
		vector1.clear();
		vector2.clear();

		return map;
	}
}
