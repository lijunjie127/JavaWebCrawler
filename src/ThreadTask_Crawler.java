

import java.util.concurrent.ConcurrentHashMap;

public class ThreadTask_Crawler implements Runnable{
	private String thisUrl;

	public ThreadTask_Crawler(String thisUrl) {
		this.thisUrl = thisUrl;
	}

	public void run() {
		
		String str_html = Util_get.getHTMLCodeString_OK(thisUrl);//获得html源码

		ConcurrentHashMap<String,String> map = Util_get.getMap(str_html);//根据源码解析出元器件属性和值的键值对
		
//		System.out.println("\n爬取第" + ++Util_OV.currentProgress + "条数据:=========" + thisUrl + "===========开始执行");
//		for (ConcurrentHashMap.Entry<String, String> entry : map.entrySet()) {
//			System.out.println(entry.getKey() + " = " +entry.getValue());
//		}
//		System.out.println("爬取第" + Util_OV.currentProgress + "条数据:=========" + thisUrl + "===========执行结束");
		
//		System.out.println("元器件详情URL：" + map.get("备用名字"));
//		System.out.println("datasheetDownLoadURL：" + map.get("datasheetDownLoadURL"));
		
		Util_writer.Write_To_Txt(map);
		
		System.out.println("共" + Util_OV.URLlistOV.size() + "个元器件信息需要爬取，已经爬取了" + ++Util_OV.currentProgress + "个");
	}
}
