

import java.sql.SQLException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Octopart {

	/**
	 * @param args
	 * @throws SQLException 
	 */
	public static void main(String[] args) throws SQLException {
		Util.init();//初始化，网站首页，保存地址，线程数
		
		ExecutorService threadPool_Prepare = Executors.newFixedThreadPool(10);
		System.out.println("开始抓取属性列表和元器件详情URL...");
		for(String oneURL : Util_OV.List_10_URLlistOV) {
			ThreadTask_Prepare t1 = new ThreadTask_Prepare(oneURL);
			threadPool_Prepare.execute(t1);
		}
		
		while(true){
			threadPool_Prepare.shutdown();//启动一次顺序关闭，执行以前提交的任务，但不接受新任务
			if(threadPool_Prepare.isTerminated()){  
                System.out.println("所有元器件的详情URL和属性名获取完成！");  
                break;  
            } 
		} 
		
		try {
			Util_writer.save_URLlist_natureList();//将属性名列表和元器件详情URL保存到外部文件中（操作3个文本文件）
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		ExecutorService threadPool_Crawler = Executors.newFixedThreadPool(10);
		System.out.println("开始抓取元器件详细信息...");
		for(String oneURL : Util_OV.URLlistOV) {
			ThreadTask_Crawler oneCrawlerThread = new ThreadTask_Crawler(oneURL);
			threadPool_Crawler.execute(oneCrawlerThread);
		}
		
		while(true){
			threadPool_Crawler.shutdown();//启动一次顺序关闭，执行以前提交的任务，但不接受新任务
			if(threadPool_Crawler.isTerminated()){  
                System.out.println("元器件信息抓取完毕！");  
                break;  
            } 
		}
		
		Util.fina();//后续处理，txt转成csv，数据入库
	}

}
