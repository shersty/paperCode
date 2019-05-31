package cesudu;

import DataSet_package.DataPreprocess;
import DataSet_package.IDS_POSD;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class test_POSD {
	public static void doit(String fileName) {
		// TODO Auto-generated method stub
		System.out.println("****矩阵形式-正域（减）******"+fileName+"上POSD的运行时间"+"***********");
		long time[]=new long[8];
		int t=0;//time下标
		
		List<Integer> preRED=null;
	    List<Integer> nowRED = null;

	 // 创建DataPreprocess对象data1        
	       DataPreprocess data1=new DataPreprocess(fileName);
	       
	       int k=0,sn=data1.get_origDSNum();
	       int m1=sn/10,m=m1>0?m1:1;
	       int count=sn-(int)(0.2*sn);//保持与增加对象的约简结果一致
	       data1.dataRatioSelect(0.2);
	       
	       preRED=new ArrayList<Integer>();
	       nowRED=new ArrayList<Integer>();
	             
	       ArrayList<ArrayList<Integer>> addData=data1.get_origDS();//交换位置，把原数据前面20%放置到后面，为了保持与对象增加一致
	       ArrayList<ArrayList<Integer>> origData=data1.get_addDS();
	       origData.addAll(addData);	       	       
	       
	       data1=null;
		       
	       Iterator<ArrayList<Integer>> value = origData.iterator();
	       
	       ArrayList<ArrayList<Integer>> result_POS=new ArrayList<ArrayList<Integer>>();
	       ArrayList<Integer> POS_Reduct=new ArrayList<Integer>();
	       ArrayList<Integer> original_POS=new ArrayList<Integer>();
	       ArrayList<Integer> original_reduct=new ArrayList<Integer>();
	       int[][] original_sys;
	       ArrayList<Integer> P=new ArrayList<Integer>();
	       IDS_POSD im1=new IDS_POSD(origData);
	       
	       POS_Reduct=im1.getPOS_Reduct(im1.getUn());	       
	       original_POS=im1.getPOS(im1.getUn(),POS_Reduct);
	       
	       original_reduct.addAll(POS_Reduct);
	       	       
	       //original_sys=im1.getUn();
	       int n1=im1.getUn().length;//原决策系统Un包含n个对象
	       int s1=im1.getUn()[0].length;//m个属性，包含决策属性	
	       original_sys=new int[n1][s1];
	       for(int i=0;i<n1;i++)
	    	   for(int j=0;j<s1;j++)
	    		   original_sys[i][j]=im1.getUn()[i][j];

	       IDS_POSD im2=new IDS_POSD();
	       
	       ArrayList<Integer> temp=new ArrayList<Integer>();
	       long startTime = System.currentTimeMillis();   //获取开始时间
		   while (value.hasNext()) {
			 	
			    temp.addAll(value.next());//System.out.println(temp);
				im2.setUn(origData);
				im2.setUx(temp);	
				result_POS=im2.SHU_IARS(im2.getUn(),original_reduct,original_POS,im2.getUx());	
								
			    original_reduct=result_POS.get(0);
			    original_POS=result_POS.get(1);
			     
				k++;
				count--;
				if(k%m==0){
					
					long endTime=System.currentTimeMillis(); //获取结束时间             
					time[t++]=endTime-startTime;//输出每添加10%数据求约简的时间
					if(t==8)
						t--;
				}
				value.remove();
				temp=new ArrayList<Integer>();//.clear();
				if (count==0)
					break;
			}
		   
		   for(int i=0;i<8;i++)				
			   System.out.print((double)time[i]/1000+" ");   

		   System.out.println("\n"+fileName+"上POSD的约简为："+result_POS.get(0)+"\n");		
	}

}
