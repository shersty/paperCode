package cesudu;

import DataSet_package.DataPreprocess;
import DataSet_package.IDS_KGIRAV2;
import DataSet_package.IDS_THA;
import ExcelTest2.standard.entity.commonValue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class test_KGIRAV2 {

	public static void doit(String fileName,timeCounter timer) {
		//开启一个后台进程 进行计时
		Thread timerListen = new Thread(new timerThread(timer));
		timerListen.setDaemon(true);
		timerListen.start();


		// TODO Auto-generated method stub
		System.out.println("**********"+fileName+"上KGIRA的运行时间"+"***********");

		long time[]=new long[8];
		int t=0;//time下标

		List<Integer> preRED=null;
	    List<Integer> nowRED = null;

		// 创建DataPreprocess对象data1
	       DataPreprocess data1=new DataPreprocess(fileName);
	       int k=0,m1=data1.get_origDSNum()/10,m=m1>0?m1:1;
	       data1.dataRatioSelect(0.2);

	       preRED=new ArrayList<Integer>();
	       nowRED=new ArrayList<Integer>();

	       ArrayList<ArrayList<Integer>> origData=data1.get_origDS();//得到初始数据
	       ArrayList<ArrayList<Integer>> addData=data1.get_addDS();//得到增量数据

	       data1=null;

	       Iterator<ArrayList<Integer>> value = addData.iterator();

	       IDS_THA im1=new IDS_THA(origData);
	       preRED=im1.THA();
	       System.out.println("初始约简："+preRED);

	       IDS_KGIRAV2 im2=new IDS_KGIRAV2();
	       ArrayList<Integer> temp=new ArrayList<Integer>();
	       long startTime = System.currentTimeMillis();   //获取开始时间

		   IDS_KGIRAV2.C = null; //全集初始化
		  int idd = 0;
		   IDS_KGIRAV2.get_UUUxRelativeP = new ArrayList<>();		//约简结果初始化
	       	while (value.hasNext()) {
//
			   if(!timer.isNowRun()){	//timer.isNowRun=false 意味着超时了
				break;
				}
			   idd++;
//			   System.out.println("第:"+idd);
			    temp.addAll(value.next());
			  //  System.out.println(temp);
				im2.setUn(origData);
				im2.setUx(temp);
				nowRED=im2.KGIRA(im2.getUn(), preRED, im2.getUx());
				preRED.clear();
				preRED.addAll(nowRED);
				k++;
				if(k%m==0){
					long endTime=System.currentTimeMillis(); //获取结束时间
					time[t++]=endTime-startTime;//输出每添加 10% 数据求约简的时间
				}
				origData.add(temp);
				temp = new ArrayList<Integer>();
				if(!timer.isNowRun()){	//timer.isNowRun=false 意味着超时了
					break;
				}

			}
		   if(!timer.isNowRun()){
			   System.out.println("程序运行已经过了至少 "+(commonValue.maxRunTime/1000)+"秒,已经超时 即将进行下一个程序！");
		   }else{
		   for(int i=0;i<8;i++)
		   	System.out.print((double)time[i]/1000+"s ");
		   Collections.sort(nowRED);
		   System.out.println("\n"+fileName+"上KGIRA的约简为："+nowRED+"\n");
		   }
	}
}
