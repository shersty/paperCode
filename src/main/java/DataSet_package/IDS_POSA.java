package DataSet_package;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class IDS_POSA {
	int Un[][]=null;
	int Ux[]=null;
	
	public IDS_POSA(int a[][],int b[]){
		Un=a;Ux=b;
	}
	
	public IDS_POSA(){ }
	
	//返回实例变量Un
	public int[][] getUn(){	return Un;	}	
	
	//返回实例变量Ux
	public int[] getUx(){	return Ux;	}	
	
	//设置实例变量Un
	public void setUn(ArrayList<ArrayList<Integer>> nData){
		int row=nData.size(),col=nData.get(0).size();
		Un=new int[row][col];
		for(int i=0;i<row;i++)
			for(int j=0;j<col;j++)
				Un[i][j]=nData.get(i).get(j);
	}
	
	//设置实例变量Ux
	public void setUx(ArrayList<Integer> xData){
		int col=xData.size();
		Ux=new int[col];
		for(int i=0;i<col;i++)
				Ux[i]=xData.get(i);
	}	
	
	//构造函数，给两个实例变量赋值
	public IDS_POSA(ArrayList<ArrayList<Integer>> orgData,ArrayList<Integer> addData){
		setUn(orgData);
		setUx(addData);		
	}
	
	//构造函数，给一个实例变量赋值
	public IDS_POSA(ArrayList<ArrayList<Integer>> orgData){
		setUn(orgData);	
	}
		
	/**
	 * 功能：计算单个对象obj在属性集P下的容差类
	 * 输入：决策系统DS,属性集P，单个对象obj,用数值表示某个对象，例如第i个对象，obj=i.
	 * 输出：obj在属性集P下的容差类
	 */		
	public ArrayList<Integer> get_SingleToleranceClass(int[][] Un,List<Integer> P,int obj){
		int k=0,n=Un.length,m=P.size();
		ArrayList<Integer> SingleToleranceClass=new ArrayList<Integer>();
		
		int attribute[]=new int[m];
		//利用Iterator实现遍历
		int s=0;
		Iterator<Integer> value = P.iterator();
		while (value.hasNext()) {
			attribute[s]= value.next();			
		    s++;
		}   			    
		
		for(int i=0;i<n;i++)
		{
			k=0;
			for(int v=0;v<m;v++)            
				if( (Un[i][attribute[v]]!=Un[obj][attribute[v]]) && (Un[i][attribute[v]]!=-1) && (Un[obj][attribute[v]]!=-1))
					break;
				else
					k=k+1;
               
			if(k==m)
				SingleToleranceClass.add(i);								
		}

		return SingleToleranceClass;
	}		

	/**
	 * 功能：计算决策系统属性集P下的容差类集合
	 * 输入：决策系统DS，属性集P
	 * 输出：决策系统属性集P下的容差类集合；
	 *     容差类集合用ArrayList<ArrayList<Integer>>二维动态数组ToleranceClassSet表示，其中每个元素表示一个相容类。
	 */		
	public ArrayList<ArrayList<Integer>> get_ToleranceClass(int[][] Un,List<Integer> P){
		int k=0,n=Un.length,m=P.size();
		ArrayList<ArrayList<Integer>> ToleranceClassSet=new ArrayList<ArrayList<Integer>>();
		
		for(int i=0;i<n;i++){
			ArrayList<Integer> temp=new ArrayList<Integer>();
			ToleranceClassSet.add(temp);
		}
		
		int attribute[]=new int[m];
		for (int s=0;s<m;s++) {
			attribute[s]= P.get(s);			
		}   			    
		
		for(int i=0;i<n;i++)
			for(int j=i;j<n;j++){
				k=0;
				for(int v=0;v<m;v++)            
					if( (Un[i][attribute[v]]!=Un[j][attribute[v]]) && (Un[i][attribute[v]]!=-1) && (Un[j][attribute[v]]!=-1))
						break;
					else
						k=k+1;
               
				if(k==m){
					if(i==j){
						ToleranceClassSet.get(i).add(j);
					}else{
						ToleranceClassSet.get(i).add(j);
						ToleranceClassSet.get(j).add(i);
					}					
				}
			}

		return ToleranceClassSet;
	}	
	
	/**
	 * 功能：计算决策系统DS的决策类集合
	 * 输入：决策系统DS
	 * 输出：决策系统DS的决策等价类集合DecisionClassSet
	 */		
	public ArrayList<ArrayList<Integer>> get_DecisionClassSet(int[][] Un){
		int n1=Un.length,n2=Un[0].length-1;
		ArrayList<ArrayList<Integer>> DecisionClassSet=new ArrayList<ArrayList<Integer>>();
		
		HashSet set1=new HashSet();//利用集合获得决策值的个数		
		for(int i=0;i<n1;i++){
			set1.add(Un[i][n2]);
		}		
		
		Iterator it = set1.iterator();		  
		while(it.hasNext()){
			ArrayList<Integer> temp=new ArrayList<Integer>();
			int k=(int) it.next();
			for(int i=0;i<n1;i++)
				if(k==Un[i][n2])
					temp.add(i);
			DecisionClassSet.add(temp);
		}
		
		return DecisionClassSet;
	}
	
	/**
	 * 功能：计算决策系统属性集P下的正域
	 * 输入：决策系统DS，属性集P
	 * 输出：决策系统属性集P下的正域POS
	 */		
	
	public ArrayList<Integer> getPOS(int[][] Un,List<Integer> P){
		ArrayList<Integer> POS=new ArrayList<Integer>();
		ArrayList<ArrayList<Integer>> DecisionClassSet=new ArrayList<ArrayList<Integer>>();
		ArrayList<ArrayList<Integer>> ToleranceClassSet=new ArrayList<ArrayList<Integer>>();
		
		DecisionClassSet=get_DecisionClassSet(Un);		
		ToleranceClassSet=get_ToleranceClass(Un,P);
//		for(int i=0;i<ToleranceClassSet.size();i++)
//			System.out.print(ToleranceClassSet.get(i));
		
		for(int i=0;i<ToleranceClassSet.size();i++)
			for(ArrayList<Integer> celld : DecisionClassSet)
				if(celld.containsAll(ToleranceClassSet.get(i)))
					POS.add(i);		
		
		return POS;
	}
	
	/**
	 * 功能：计算基于正域约简的传统算法
	 * 输入：决策系统DS
	 * 输出：决策系统的正域约简POS_Reduct
	 */		
	public ArrayList<Integer> getPOS_Reduct(int[][] Un){
		ArrayList<Integer> POS_Reduct=new ArrayList<Integer>();
		ArrayList<Integer> POS_C=new ArrayList<Integer>();
		ArrayList<Integer> Core=new ArrayList<Integer>();
		int m=Un[0].length;//m个属性，包含决策属性
		List<Integer> C=new ArrayList<Integer>();
		for(int i=0;i<m-1;i++)
			C.add(i);
		
		POS_C=getPOS(Un,C);//计算全集C上的正域
		
		//计算核
		for(int i=0;i<m-1;i++){
			ArrayList<Integer> Q= new ArrayList<Integer>();
			Q.addAll(C);				
			Q.remove(i);
			ArrayList<Integer> POS_Q=getPOS(Un,Q);//计算属性集C-i上的正域
			
			ArrayList<Integer> temp=new ArrayList<Integer>();
			temp.addAll(POS_C);
			temp.removeAll(POS_Q);
			if(temp.size()!=0)
				Core.add(i);
		}

		//从核出发，获得满足第一个条件的属性集
		POS_Reduct.addAll(Core);
		ArrayList<Integer> POS_R=getPOS(Un,POS_Reduct);
		int lengthR=POS_R.size();
		int lengthC=POS_C.size();
		while(lengthR!=lengthC){
			ArrayList<Integer> Q= new ArrayList<Integer>();
			Q.addAll(C);				
			Q.removeAll(POS_Reduct);
			
			int lengthQ=Q.size();
			int MaxSignificance=0;
			int InAttribute=-1;
			
			for(int j=0;j<lengthQ;j++){
				ArrayList<Integer> Temp=new ArrayList<Integer>();
				ArrayList<Integer> POS_T=new ArrayList<Integer>();
				Temp.addAll(POS_Reduct);
				Temp.add(Q.get(j));
			    POS_T=getPOS(Un,Temp);
			    POS_T.removeAll(POS_R);
			    int InSignificance=POS_T.size();
			    if (InSignificance>MaxSignificance){
			            MaxSignificance=InSignificance;
			            InAttribute=Q.get(j);
			    }			    
			}
			
			if (InAttribute==-1)
//				InAttribute=Q.get((int)(Math.random()*lengthQ));
				InAttribute = Q.get(Q.size() - 1);
		    POS_Reduct.add(InAttribute);
		    POS_R=getPOS(Un,POS_Reduct);
		    lengthR=POS_R.size();
		}
		
		//检验属性集中是否还有多余的属性。
		int v=0;
		while(v<POS_Reduct.size()){
			ArrayList<Integer> T=new ArrayList<Integer>();
			ArrayList<Integer> POS_T=new ArrayList<Integer>();
			T.addAll(POS_Reduct);
			T.remove(POS_Reduct.get(v));
			POS_T=getPOS(Un,T);
			
			ArrayList<Integer> temp=new ArrayList<Integer>();
			temp.addAll(POS_R);
			temp.removeAll(POS_T);
			int OutSignificance=temp.size();
			if(OutSignificance==0){
				POS_Reduct.remove(POS_Reduct.get(v));
				v=0;
	        }
			else
				v=v+1;
		}		
		
		//POS_Reduct=sort(POS_Reduct);    
		return POS_Reduct;
	}
	
	/**
	 * 功能：舒文豪提出的算法中的正域更新方法
	 * 输入：原始决策系统Un，原始正域约简original_reduct，原始正域original_POS， 单个增量对象additional_obj
	 * 输出：新决策系统的正域new_POS
	 */		
	public ArrayList<Integer> getNewPOS(int[][] Un,ArrayList<Integer> original_reduct,ArrayList<Integer> original_POS,ArrayList<Integer> additional_obj){
		ArrayList<Integer> new_POS=new ArrayList<Integer>();
		ArrayList<Integer> P=new ArrayList<Integer>();
		ArrayList<Integer> P_ToleranceClass=new ArrayList<Integer>();
		
		int n=Un.length;//原决策系统Un包含n个对象
		int m=Un[0].length;//m个属性，包含决策属性
		P.addAll(original_reduct);
				
		int[][] new_sys=new int[n+1][m];
		for(int i=0;i<n;i++)
			for(int j=0;j<m;j++)
				new_sys[i][j]=Un[i][j];
		for(int i=0;i<m;i++)
			new_sys[n][i]=additional_obj.get(i);
	    	
		P_ToleranceClass=get_SingleToleranceClass(new_sys,P,n);//求新加第n号对象(从0开始)在原约简上的容忍类
		HashSet GeneralDecisionP=new HashSet();	
		//第n+1号对象在原约简上的容忍类里每一个对象的决策值放入GeneralDecisionP
		int LP=P_ToleranceClass.size();
		for (int i=0;i<LP;i++)
			GeneralDecisionP.add(new_sys[P_ToleranceClass.get(i)][m-1]);//利用集合获得容忍类中对象的决策值的集合,不包含重复的			

		//判定新增对象的P-容差类中是否造成原有正域中的对象出正域。
		ArrayList<Integer> out_POS=new ArrayList<Integer>();
		for (int j=0;j<LP;j++){
			ArrayList<Integer> Q_ToleranceClass=new ArrayList<Integer>();
			Q_ToleranceClass=get_SingleToleranceClass(new_sys,P,P_ToleranceClass.get(j));
			int LQ=Q_ToleranceClass.size();	
			HashSet GeneralDecisionQ=new HashSet();
			for (int i=0;i<LQ;i++)
				GeneralDecisionQ.add(new_sys[Q_ToleranceClass.get(i)][m-1]);
			if(GeneralDecisionQ.size()!=1)
				out_POS.add(P_ToleranceClass.get(j));
		}
		
		new_POS.addAll(original_POS);
		new_POS.removeAll(out_POS);
		
		//判定新增加的第n+1号对象是否属于正域，如果|lengthDecisionP|==1，即n+1号对象的容忍类中所有元素的决策值相同，则n+1号对象属于正域		
		if(GeneralDecisionP.size()==1)
			new_POS.add(n+1);
		
		return new_POS;
	}
	
	/**
	 * 功能：舒文豪提出的正域增量式方法计算约简,增量的对象为一个
	 * 输入：原始决策系统original_sys，原始正域约简original_reduct，原始正域original_POS， 单一增量对象additional_obj
	 * 输出：新决策系统的正域约简incre_POS_Reduct，新决策系统的正域newC_POS，分别放在二维动态数组result_POS的第0维和第1维
	 */
	public ArrayList<ArrayList<Integer>> SHU_IARS(int[][] Un,ArrayList<Integer> original_reduct,ArrayList<Integer> original_POS,ArrayList<Integer> additional_obj){
		ArrayList<ArrayList<Integer>> result_POS=new ArrayList<ArrayList<Integer>>();
		ArrayList<Integer> incre_POS_Reduct=new ArrayList<Integer>();
		ArrayList<Integer> newC_POS=new ArrayList<Integer>();
		ArrayList<Integer> newP_POS=new ArrayList<Integer>();
		ArrayList<Integer> P_ToleranceClass=new ArrayList<Integer>();
		ArrayList<Integer> C_ToleranceClass=new ArrayList<Integer>();
		ArrayList<Integer> P=new ArrayList<Integer>();
		ArrayList<Integer> C=new ArrayList<Integer>();
		int n=Un.length;//原决策系统Un包含n个对象
		int m=Un[0].length;//m个属性，包含决策属性		
		for(int i=0;i<m-1;i++)
			C.add(i);
		P.addAll(original_reduct);
		//把单个增量对象加入原决策系统得到新决策系统new_sys		
		int[][] new_sys=new int[n+1][m];
		for(int i=0;i<n;i++)
			for(int j=0;j<m;j++)
				new_sys[i][j]=Un[i][j];
		for(int i=0;i<m;i++)
			new_sys[n][i]=additional_obj.get(i);
		
		P_ToleranceClass=get_SingleToleranceClass(new_sys,P,n);
		C_ToleranceClass=get_SingleToleranceClass(new_sys,C,n);
		
		int LP=P_ToleranceClass.size();
		int LC=C_ToleranceClass.size();
		HashSet GeneralDecisionP=new HashSet();	
		//第n+1号对象在原约简上的容忍类里每一个对象的决策值放入GeneralDecisionP
		for (int i=0;i<LP;i++)
			GeneralDecisionP.add(new_sys[P_ToleranceClass.get(i)][m-1]);//利用集合获得容忍类中对象的决策值的集合,不包含重复的		

		int lengthDecisionP=GeneralDecisionP.size();

		//第一种情况
		if (LC==1&&lengthDecisionP==1)
			    incre_POS_Reduct.addAll(P);
		else{//第二种情况						
			newP_POS=getNewPOS(Un,P,original_POS,additional_obj);
		    newC_POS=getNewPOS(Un,C,original_POS,additional_obj);
		    int numberP_POS=newP_POS.size();
		    int numberC_POS=newC_POS.size();
		    while(numberP_POS!=numberC_POS){
		    	ArrayList<Integer> Q= new ArrayList<Integer>();
				Q.addAll(C);				
				Q.removeAll(P);
				
				int lengthQ=Q.size();
				int MaxSignificance=0;
				int InAttribute=-1;
				
				for(int i=0;i<lengthQ;i++){
					ArrayList<Integer> Temp=new ArrayList<Integer>();
					ArrayList<Integer> POS_T=new ArrayList<Integer>();
					Temp.addAll(P);
					Temp.add(Q.get(i));
				    POS_T=getPOS(new_sys,Temp);
				    POS_T.removeAll(newP_POS);
				    int InSignificance=POS_T.size();
				    if (InSignificance>MaxSignificance){
				            MaxSignificance=InSignificance;
				            InAttribute=Q.get(i);
				    }			    
				}
				
				if (InAttribute==-1)
					InAttribute=Q.get(Q.size()-1);
			    P.add(InAttribute);
			    newP_POS=getPOS(new_sys,P);
			    numberP_POS=newP_POS.size();
		    }		    
		    incre_POS_Reduct.addAll(P);
		    
		    //去除incre_POS_Reduct中多余的属性
		    int v=0;
			while(v<incre_POS_Reduct.size()){
				ArrayList<Integer> T=new ArrayList<Integer>();
				ArrayList<Integer> POS_T=new ArrayList<Integer>();
				T.addAll(incre_POS_Reduct);
				T.remove(incre_POS_Reduct.get(v));
				POS_T=getPOS(new_sys,T);
				
				ArrayList<Integer> temp=new ArrayList<Integer>();
				temp.addAll(newC_POS);
				temp.removeAll(POS_T);
				int OutSignificance=temp.size();
				if(OutSignificance==0){
					incre_POS_Reduct.remove(incre_POS_Reduct.get(v));
					v=0;
		        }
				else
					v=v+1;
			}	
		}

		newC_POS=getPOS(new_sys,incre_POS_Reduct);
		result_POS.add(incre_POS_Reduct);
		result_POS.add(newC_POS);
		return result_POS;
	}
}
