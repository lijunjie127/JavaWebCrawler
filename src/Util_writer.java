

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;


public class Util_writer {
	static BufferedWriter writer ;
	static synchronized void save_URLlist_natureList() throws Exception{
		System.out.print("正在将元器件详情URL保存到外部文件中，请稍后···");
		writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(Util_OV.pathOV + "\\URLlistOV.txt"), "UTF-8"));
		for(String v : Util_OV.URLlistOV)
			writer.write(v + "\r\n");
		writer.close();
		System.out.println("完成！");
		
		System.out.print("正在将元器件属性名列表保存到外部文件中，请稍后···");
		writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(Util_OV.pathOV + "\\natureList.txt"), "UTF-8"));
		for(String v : Util_OV.natureList)
			writer.write(v + "\r\n");
		writer.close();
		System.out.println("完成！");
		
		System.out.print("正在将元器件属性名列表写到文本文件第一行，请稍后···");
		writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(Util_OV.txtFilePathOV), "UTF-8"));
		for(String v : Util_OV.natureList)
			writer.write(v + "\t");
		writer.write("\r\n");
		writer.close();
		System.out.println("完成！");
	}
	
	static void writeData_txt(String filePath, BufferedWriter writer, String content) 
	{
		try {
			writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filePath,true), "UTF-8"));
			writer.write(content);
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	static void writePropertiesToSave(Vector<String> vector) throws IOException//更新表头
	{
//		String TempFilePath = Util_OV.pathOV + "\\natureList.txt";//存储临时表头
//		BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(TempFilePath,true), "UTF-8"));
		
        //对全局变量的natureList进行处理，若有新的参数（元器件是属性）则加进来，这个for循环是去掉list_properties中在natureList中已经存在的参数
        for(int i = 0 ,j; i < Util_OV.natureList.size(); i ++)//定义循环变量i和j，对i赋值，对j不赋值
        {
        	for(j = 0 ; j < vector.size() ; j ++)
        	{
        		if(Util_OV.natureList.get(i).equals(vector.get(j)))
        		{
        			vector.remove(j);
        			break;
        		}
        	}
        }
        //在natureList中加上：list_properties中有，natureList中没有的参数
        while(vector.size() > 0)
    	{
        	Util_OV.natureList.add(vector.get(0));
//        	writer.write(vector.get(0) + "\r\n");
        	vector.remove(0);
    	}
//        writer.close();
	}
	
	public static void Write_To_Txt(Vector<String> list_properties , Vector<String> list_value){//
		try 
		{
			String txtFilePath = Util_OV.txtFilePathOV;
			writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(txtFilePath,true), "UTF-8"));
			for(int i = 0 ,j ; i < Util_OV.natureList.size(); i ++)//定义循环变量i和j，对i赋值，对j不赋值
		    {
		    	for(j = 0 ; j < list_properties.size() ; j ++)
		    	{
		    		if(Util_OV.natureList.get(i).equals(list_properties.get(j)))
		    		{
		    			writer.write(list_value.get(j)+"\t");//设置第(i+1)个（从0开始）单元格的数据为list_value.get(j)
		    			break;
		    		}
		    	}
		    	if(list_properties.size() == j){
		    		writer.write("-\t");
		    	}
		    }
			writer.write("\r\n");
			writer.close();
//			writer.close();
		}
		 catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
	   }
	}

	public synchronized static void Write_To_Txt(ConcurrentHashMap<String, String> map) {
		
		Vector<String> list_properties = new Vector<String>();//属性列表
		Vector<String> list_value = new Vector<String>();//值列表
		for (ConcurrentHashMap.Entry<String, String> entry : map.entrySet()) {
			list_properties.add(entry.getKey());
			list_value.add(entry.getValue());
		}
		try 
		{
			String txtFilePath = Util_OV.txtFilePathOV;
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(txtFilePath,true), "UTF-8"));
			for(int i = 0 ,j ; i < Util_OV.natureList.size(); i ++)//定义循环变量i和j，对i赋值，对j不赋值
		    {
		    	for(j = 0 ; j < list_properties.size() ; j ++)
		    	{
		    		if(Util_OV.natureList.get(i).equals(list_properties.get(j)))
		    		{
		    			writer.write(list_value.get(j)+"\t");//设置第(i+1)个（从0开始）单元格的数据为list_value.get(j)
		    			break;
		    		}
		    	}
		    	if(list_properties.size() == j){
		    		writer.write("-\t");
		    	}
		    }
			writer.write("\r\n");
			writer.close();
//			writer.close();
		}
		 catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
	   }
	}
}
