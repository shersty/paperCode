package cesudu;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import DataSet_package.DataPreprocess;
import DataSet_package.IDS_THA;
import ExcelTest2.standard.entity.commonValue;

public class test_THA {
	public static void doit(String fileName,timeCounter timer) {
		//开启一个后台进程 进行计时
		Thread timerListen = new Thread(new timerThread(timer));
		timerListen.setDaemon(true);
		timerListen.start();


		long time[]=new long[8];
		int t=0;//time下标
		System.out.println("**********"+fileName+"上THA的运行时间"+"***********");

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
       IDS_THA im1=new IDS_THA(origData);
       RED=im1.THA();//System.out.println("\n"+fileName+"上20%数据("+origData.size()+")THA的约简为："+RED+"\n");
	   while (value.hasNext()) {
		   if(!timer.isNowRun()){	//timer.isNowRun=false 意味着超时了
				break;
				}
			origData.add(value.next());
			im1.setUn(origData);
			RED=im1.THA();	//System.out.println(RED);
			   if(!timer.isNowRun()){	//timer.isNowRun=false 意味着超时了
					break;
					}
			k++;
			if(k%m==0){
				long endTime=System.currentTimeMillis(); //获取结束时间
				time[t++]=endTime-startTime;//输出每添加10%数据求约简的时间
			}
		}

	   if(!timer.isNowRun()){
		   System.out.println("程序运行已经过了至少 "+(commonValue.maxRunTime/1000)+"秒,已经超时 即将进行下一个程序！");
	   }else{
	   	for(int j=0;j<8;j++)
				System.out.print((double)time[j]/1000+"s ");
			//Collections.sort(RED);
		System.out.println("\n"+fileName+"上THA的约简为："+RED+"\n");
	   }
	}
}
