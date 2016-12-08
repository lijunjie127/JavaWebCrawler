


import java.util.ArrayList;
//import java.util.HashSet;
import java.util.List;

public class Util_OV {

//	static HashSet<String> URLsetOV = new HashSet<String>();
	static ArrayList<String> URLlistOV = new ArrayList<String>();//元器件详情URL
	static ArrayList<String> List_10_URLlistOV = new ArrayList<String>();//元器件列表的URL
	static List<String> natureList = new ArrayList<String>();//相当于内存
	static String[] origPropertiesA = {"物资ID","唯一识别码","大类","小类","元器件名称","元器件描述","规格型号","质量等级","工作温度范围","生产厂商","国产/进口","封装形式","封装尺寸","规范号","认证源","质保可行性",
										"应用验证状态","SPL状态","datasheet","数据包","应用指南（技术文件）","质量预警信息","优品号","目录内","替代型号","元器件详情URL","datasheetDownLoadURL","备用名字",
										};//初始表头
	static int currentProgress = 0;//当前进度，写在excel的第几行
	static String pathOV = null;//文件夹的路径，处理数据信息都在这个文件夹下处理
	static String excelFilePathOV = null;//省得选择文件干啥的 了，只要不人为改名，以后的文件位置和文件名就确定了
	static String txtFilePathOV = null;
	
	static boolean refreshDataFlag = false;//是否需要添加元器件是不是新添加进来的，默认false
	
}
