package cesudu;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import DataSet_package.DataPreprocess;
import DataSet_package.IDS_THA;
import DataSet_package.IDS_THAV02;
import ExcelTest2.standard.entity.commonValue;

public class test_THAV02 {
	public static void doit(String fileName) {
//		long time[]=new long[8];
//		int t=0;//time下标
//		System.out.println("**********"+fileName+"上THA的运行时间"+"***********");
//		List<Integer> RED=null;
//		// 创建DataPreprocess对象data1
//       DataPreprocess data1=new DataPreprocess(fileName);
//       System.out.println(data1.get_origDSNum()+"##");
//       IDS_THAV02 im1=new IDS_THAV02(data1.get_origDS());
//       System.out.println("yuejian:"+im1.THA());


		long time[]=new long[8];
		int t=0;//time下标
		System.out.println("**********"+fileName+"上THAV02的运行时间"+"***********");

		List<Integer> RED=null;

		// 创建DataPreprocess对象data1
		DataPreprocess data1=new DataPreprocess(fileName);

		int k=0,m1=data1.get_origDSNum()/10,m=m1>0?m1:1;
		data1.dataRatioSelect(0.2);

		RED=new ArrayList<Integer>();

		ArrayList<ArrayList<Integer>> origData=data1.get_origDS();//得到初始数据
		ArrayList<ArrayList<Integer>> addData=data1.get_addDS();//得到增量数据

		data1=null;
		Iterator<ArrayList<Integer>> value = addData.iterator();

		long startTime = System.currentTimeMillis();   //获取开始时间
		IDS_THAV02 im1=new IDS_THAV02(origData);
		RED=im1.THA();//System.out.println("\n"+fileName+"上20%数据("+origData.size()+")THA的约简为："+RED+"\n");
		while (value.hasNext()) {
			origData.add(value.next());
			im1.setUn(origData);
			RED=im1.THA();	//System.out.println(RED);
			k++;
			if(k%m==0){
				long endTime=System.currentTimeMillis(); //获取结束时间
				time[t++]=endTime-startTime;//输出每添加10%数据求约简的时间
			}
		}


			for(int j=0;j<8;j++)
				System.out.print((double)time[j]/1000+"s ");
			//Collections.sort(RED);
			System.out.println("\n"+fileName+"上THA的约简为："+RED+"\n");
	}


}
