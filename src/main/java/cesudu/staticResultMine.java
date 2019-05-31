package cesudu;

import java.util.ArrayList;
import java.util.Map;

public class staticResultMine {
	public static int percent20;								//所有数据的百分之二十是多少行
	public static int nowRows;								//现在处理的是第几行
	public static Map<Integer,ArrayList<Integer>> resultMap;	//存放每一行的结果矩阵
	
	
	
	
	@Override
	public String toString() {
		return "staticResult [percent20=" + percent20 + ", nowRows=" + nowRows + ", resultMap=" + resultMap + "]";
	}
 
	
	

}
