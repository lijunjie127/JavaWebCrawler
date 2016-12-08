

import java.awt.Component;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Vector;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileSystemView;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

public class Util {
	public static void init(){
		int allPageNum = 5;//列表页有多少页
		Util_OV.pathOV = "E:\\test_20161128";
		String firstPageUrl = "https://octopart.com/parts/category/4195/chokes/";
		String ppp = "&s=create_ts_asc";//网址后缀
		
		String[] tempA = firstPageUrl.split("/");
		Util_OV.excelFilePathOV = Util_OV.pathOV + "\\" + tempA[tempA.length - 1] + ".csv";//输出文件
		Util_OV.txtFilePathOV = Util_OV.pathOV + "\\" + tempA[tempA.length - 1] + ".txt";
		
		for(String v : Util_OV.origPropertiesA)
			Util_OV.natureList.add(v);
		
		for(int count = 0 ; count < 3 ; count ++) {//循环爬取多少次
			for(int i = 1 ; i <= allPageNum ; i ++){//将所有的链接加入链接列表
				Util_OV.List_10_URLlistOV.add(firstPageUrl + "?p=" + i + ppp);
		    }
		}
	}
	
	public static String stripNonCharCodepoints(String input) {  //过滤掉无效的UTF-8特殊字符
		StringBuilder retval = new StringBuilder();  
		char ch;  
		  
		for (int i = 0; i < input.length(); i++) {  
			ch = input.charAt(i);  
			  
			// Strip all non-characters http://unicode.org/cldr/utility/list-unicodeset.jsp?a=[:Noncharacter_Code_Point=True:]  
			// and non-printable control characters except tabulator, new line and carriage return  
			if (ch % 0x10000 != 0xffff && // 0xffff - 0x10ffff range step 0x10000  
				ch % 0x10000 != 0xfffe && // 0xfffe - 0x10fffe range  
				(ch <= 0xfdd0 || ch >= 0xfdef) && // 0xfdd0 - 0xfdef  
				(ch > 0x1F || ch == 0x9 || ch == 0xa || ch == 0xd)) {  
				  
					retval.append(ch);  
				}  
			}  
	  
		return retval.toString();  
	}
	
	public static void writeToooExcelCell(String filePath,int x,int y,String value) {//x,y第几行第几列都是从0开始计数
		try {
			// 创建Excel的工作书册 Workbook,对应到一个excel文档
			HSSFWorkbook wb = new HSSFWorkbook(new FileInputStream(filePath));
			HSSFSheet sheet=wb.getSheetAt(0);
			HSSFRow row=sheet.getRow(x);
			HSSFCell cell = row.createCell(y);
			cell.setCellValue(value);
			FileOutputStream os;
			os = new FileOutputStream(filePath);
			wb.write(os);
			os.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static void writeToExcelCell(String filePath,int y,Vector<Integer> vector1,Vector<String> vector2) {//x,y第几行第几列都是从0开始计数
		try {
			// 创建Excel的工作书册 Workbook,对应到一个excel文档
			HSSFWorkbook wb = new HSSFWorkbook(new FileInputStream(filePath));
			HSSFSheet sheet=wb.getSheetAt(0);
			for(int i=0;i<vector1.size();i++){
				HSSFRow row=sheet.getRow(vector1.get(i));
				HSSFCell cell = row.createCell(y);
				cell.setCellValue(vector2.get(i));
			}
			FileOutputStream os;
			os = new FileOutputStream(filePath);
			wb.write(os);
			os.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static String dealDatasheetURLReturnSimpleFileName (String stra)//"https://datasheet.octopart.com/RN112-4-02-Schaffner-datasheet-10855108.pdf"
	{
		String [] tempa1 = stra.split("/");
		stra = tempa1[tempa1.length-1];
		String [] tempa2 = stra.split("-datasheet");
		stra = tempa2[0];
		return stra;//RN112-4-02-Schaffner
	}
	
	public static String dealDatasheetFileReturnSimpleFileName(String strb)//例如："Schaffner-RN112-4-02-datasheet.pdf"
	{
		String [] tempb1 = strb.split("-datasheet");
		strb = tempb1[0];
		strb = strb.trim();
		return strb;//例如："Schaffner-RN112-4-02"
	}
	
	public static Map<Character,Integer> dealReturnMap(String str)//RN112-4-02-Schaffner
	{
		Map<Character,Integer> hsmap = new HashMap<Character,Integer>();
		str = str.replaceAll("[^A-Za-z0-9]", "");
		for (int i = 0; i < str.length(); i++) 
		{
			 char c = str.charAt(i);
			 if(hsmap.containsKey(c))
			 {
				 int val = hsmap.get(c)+1; 
				 hsmap.put(c, val);
			 }
			 else
			 {
				 hsmap.put(c, 1);
			 }
		}
		
		return hsmap;
	}
	
	public static String[] getFileORpathList(String path)
	{
		File file= new File(path);
        File[] fileORpathList = file.listFiles();
        String[] simpleList = new String[fileORpathList.length];
//        System. out.println("该目录下对象个数：" +fileORpathList.length);
        for (int i = 0; i < fileORpathList.length; i++) {
        	String[] tempa = fileORpathList[i].toString().split("\\\\");
        	simpleList[i] = tempa[tempa.length-1];
//        	System.out.println(simpleList[i]);
        }
		return simpleList;
	}
	
	public static String chooseFile()//选择文件
	{
		String filePath = null;
		int result_JFC = 0;
		JFileChooser fileChooser = new JFileChooser();
		FileSystemView fsv = FileSystemView.getFileSystemView();  //注意了，这里重要的一句
//		System.out.println("请在小窗口中选择待处理的数据文件，当前默认为桌面路径：" + fsv.getHomeDirectory());//得到桌面路径
		fileChooser.setCurrentDirectory(fsv.createFileObject(Util_OV.pathOV));//弹出窗口为程序刚开始运行时选择的那个路径
		fileChooser.setDialogTitle("请选择文件...");
		fileChooser.setApproveButtonText("确定");
		fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		Component chatFrame = null;
		result_JFC = fileChooser.showOpenDialog(chatFrame);//弹出选择文件的界面
		if (JFileChooser.APPROVE_OPTION == result_JFC) 
		{
			filePath = fileChooser.getSelectedFile().getPath();
		}
		return filePath;
	}
	
	public static String chooseFilePath()//选择文件目录,不是第一次选择路径，也就是静态变量中存路径信息了，静态变量中的路径也是从文件中读取的
	{
		JFileChooser chooser = new JFileChooser();
		chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);//设置只能选择目录
		chooser.setCurrentDirectory(new File(Util_OV.pathOV));//若new File(".")是以当前目录生成一个文件目录实例，然后设置为当前的工作目录
		chooser.setDialogTitle("请选择路径...");
		chooser.setApproveButtonText("确定");
		int returnVal = chooser.showOpenDialog(chooser);
		if(returnVal == JFileChooser.APPROVE_OPTION) 
		{
			String selectPath =chooser.getSelectedFile().getPath() ;
//			System.out.println ( "你选择的文件输出目录是：" + selectPath );
			return selectPath;
		}
		else
		{
			return "选择目录不合法";
		}
	}
	
	public static String chooseFilePath1()//选择文件目录，第一次选择文件目录，默认桌面吧
	{
		File desktopDir = FileSystemView.getFileSystemView()
    			.getHomeDirectory();
    			String desktopPath = desktopDir.getAbsolutePath();
		JFileChooser chooser = new JFileChooser();
		chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);//设置只能选择目录
		chooser.setCurrentDirectory(new File(desktopPath));//若new File(".")是以当前目录生成一个文件目录实例，然后设置为当前的工作目录
		chooser.setDialogTitle("请选择路径...");
		chooser.setApproveButtonText("确定");
		int returnVal = chooser.showOpenDialog(chooser);
		if(returnVal == JFileChooser.APPROVE_OPTION) 
		{
			String selectPath =chooser.getSelectedFile().getPath() ;
//			System.out.println ( "你选择的文件输出目录是：" + selectPath );
			return selectPath;
		}
		else
		{
			return "选择目录不合法";
		}
	}
	
	
	
	
	
	public static HashMap<String, Integer> sortOnValueRiseAndOutput(HashMap<String, Integer> hsmap,PrintStream p)
	{//key为字符串，value为该字符串出现的次数，这里按照value升序排序
		List<Map.Entry< String,Integer>> list = new ArrayList<Map.Entry<String, Integer>>(hsmap.entrySet());
		
		Collections. sort (
		     list , new Comparator<Map.Entry<String, Integer >>() {//升序排序
		           public int compare(Entry<String , Integer > o1,Entry<String, Integer > o2) {
		               return o1.getValue().compareTo(o2.getValue());
		          }
		     }
		);
		for(Map.Entry<String, Integer> mapping:list ){
//			     System.out.println(mapping.getKey()+ " : " +mapping.getValue());
		     p.println(mapping.getKey()+ " : " +mapping.getValue());
		}
		return hsmap;
	}
	
	public static HashMap<String, StringBuilder> sortOnValueRiseStringAndOutput(HashMap<String, StringBuilder> hsmap,BufferedWriter p) throws IOException
	{//key为字符串，value为该字符串所对应的赛思编码，这里按照value升序排序
		List<Map.Entry< String,StringBuilder>> list = new ArrayList<Map.Entry<String,StringBuilder>>(hsmap.entrySet());
		
		Collections. sort (
		     list , new Comparator<Map.Entry<String, StringBuilder>>() {
		           public int compare(Entry<String , StringBuilder> o1,Entry<String, StringBuilder> o2) {
		        	   int a1 = o1.getValue().toString().split("##").length;
		        	   int a2 = o2.getValue().toString().split("##").length;
		        	   return (a2 - a1);//降序排序
		          }
		     }
		);
		for(Map.Entry<String, StringBuilder> mapping:list ){
			 if(mapping.getValue().indexOf("##") != -1)
			 {
				 p.write(mapping.getKey()+ " : " +mapping.getValue());
				 p.write("\r\n");
			 }
		}
		return hsmap;
	}
	
	public static HashMap<Character,Integer> findOutSpecialChar(BufferedReader br,int v) throws IOException//处理哪个文件，第几列
	{//找出某字段的全部特殊字符
		String str = "";
		String [] tempa = br.readLine().split("\t");
		HashMap<Character,Integer> hsmap = new HashMap<Character,Integer>();//存储特殊字符
		while ((str = br.readLine()) != null) 
		{
			String [] a = str.split("\t");
			
			if(a.length == tempa.length)
			{
				String orig = a[v];
				String regex = "[a-zA-Z0-9\u4e00-\u9fa5]+";//字母数字汉字
				String newValue = orig.replaceAll(regex, "");
				for (int i = 0; i < newValue.length(); i++) 
				{
					 char c = newValue.charAt(i);
					 if(hsmap.containsKey(c))
					 {
						 int val = hsmap.get(c)+1; 
						 hsmap.put(c, val);
					 }
					 else
					 {
						 hsmap.put(c, 1);
					 }
				}
			}
		}
		return hsmap;
	}
	
//	public static String csvToTxt(String csvFilePath) throws IOException {
//		List<String[]> list = null;
//		String txtFilePath = csvFilePath.substring(0, csvFilePath.length() - 4) + ".txt";//输入文件和输出文件在同一个目录下，名称相同，只有后缀名不同
//		BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(txtFilePath), "UTF-8")); 
//		ExcelReader er = new ExcelReader(csvFilePath);
//		list = er.getAllData(0);//先获取所有数据
//
//		for(String[] v : list)
//		{
//			for(String vv : v)
//			{
////				System.out.print(vv + "\t");
//				writer.write(vv + "\t");
//			}
////			System.out.println("");
//			writer.write("\r\n");
//		}
//		writer.close();
//		return txtFilePath;
//    }
	
	public static String txtToCsv(String txtFilePath) throws IOException
	{
		String csvFilePath = txtFilePath.substring(0, txtFilePath.length() - 4) + ".csv";//输入文件和输出文件在同一个目录下，名称相同，只有后缀名不同
		BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(txtFilePath),"utf-8"));
		String str = null;
		WritableWorkbook wwb = null;   
        try {   
            //首先要使用Workbook类的工厂方法创建一个可写入的工作薄(Workbook)对象   
            wwb = Workbook.createWorkbook(new File(csvFilePath));   
        } catch (IOException e) {   
            e.printStackTrace();   
        }   
        if(wwb!=null){   
            //创建一个可写入的工作表   
            //Workbook的createSheet方法有两个参数，第一个是工作表的名称，第二个是工作表在工作薄中的位置   
            WritableSheet ws = wwb.createSheet("sheet1", 0);   
            
            int j = 0;
            while((str = br.readLine()) != null){
            	String[] strA = str.split("\t");
	            //下面开始添加单元格   
	            for(int i = 0 ; i < strA.length ; i ++){   
	                //这里需要注意的是，在Excel中，第一个参数表示列，第二个表示行   
	                Label labelC = new Label(i, j,strA[i]);   
	                try {   
	                    //将生成的单元格添加到工作表中   
	                    ws.addCell(labelC);   
	                } catch (RowsExceededException e) {   
	                    e.printStackTrace();   
	                } catch (WriteException e) {   
	                    e.printStackTrace();   
	                }   
	            } 
	            j ++;
            }
            
  
            try {   
                //从内存中写入文件中   
                wwb.write();   
                //关闭资源，释放内存   
                wwb.close();   
            } catch (IOException e) {   
                e.printStackTrace();   
            } catch (WriteException e) {   
                e.printStackTrace();   
            }   
        } 
		return csvFilePath;
	}

	public static void fina() {
		try {
			System.out.print("正在将文本格式转换为excel格式...");
			Util.txtToCsv(Util_OV.txtFilePathOV);
			System.out.println("...完成");
			
//			System.out.println(Util_OV.txtFilePathOV);
//			System.out.println(Util_OV.currentProgress);
//			MysqlConn mysqlConn = new MysqlConn();
//			Connection conn = mysqlConn.getConn();
//			Statement st = conn.createStatement();
//			String str = null;
//			BufferedReader br = new BufferedReader( new InputStreamReader(new FileInputStream(Util_OV.txtFilePathOV)));
//			br.readLine ();
//			while ((str = br.readLine ()) != null){
//				String[] a = str.split("\t");
//				st.executeUpdate("INSERT INTO student values(" + Util_OV.currentProgress-- + "," + a[26] + "," + a[27] + ")");
//			}
//			st.close();
//			conn.close();
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
