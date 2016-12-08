

public class ThreadTask_Prepare implements Runnable {
	private String thisUrl;

	public ThreadTask_Prepare(String thisUrl) {
		this.thisUrl = thisUrl;
	}

	public void run() {
		System.out.println("\nThreadTask_Prepare:=========" + thisUrl + "===========开始执行");
		
//		String str_html = Util_get.getHTMLCodeString(thisUrl);//获得html源码
		
		String str_html = Util_get.getHTMLCodeString_OK(thisUrl);

		Util_get.getPropertieList(str_html);//根据源码解析出元器件详情URL（写入内存Util_OV.URLlistOV）和所有元器件重复的属性值（写入内存Util_OV.natureList）
		
		System.out.println("\nThreadTask_Prepare:=========" + thisUrl + "===========执行结束");
	}
}