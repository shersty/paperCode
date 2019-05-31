package DataSet_package;

import java.util.*;

import cesudu.resultEntity;

public class IDS_KGIRA {
	int Un[][]=null;
	int Ux[]=null;
	
	public IDS_KGIRA(int a[][],int b[]){
		Un=a;Ux=b;
	}
	
	public IDS_KGIRA(){
		
	}
	
	//返回实例变量Un
	public int[][] getUn(){
		return Un;
	}	
	
	//返回实例变量Ux
	public int[] getUx(){
		return Ux;
	}		
	
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
	public IDS_KGIRA(ArrayList<ArrayList<Integer>> orgData,ArrayList<Integer> addData){
		setUn(orgData);
		setUx(addData);		
	}
	
	//求属性集P下的相容矩阵
	public int[][] get_ToleranceMatrix(int[][] Un,List<Integer> P){
		int k=0,n=Un.length,m=P.size();
		int ToleranceMatrix[][]=new int[n][n];
		int attribute[]=new int[m];
		//利用Iterator实现遍历
		int s=0;
		Iterator<Integer> value = P.iterator();
		while (value.hasNext()) {
			attribute[s]= value.next();			
		    //System.out.print(attribute[s]+" ");
		    s++;
		}   
		for(int i=0;i<n;i++)
			ToleranceMatrix[i][i]=1;
		
	//	System.out.println("n:"+n);
		for(int i=0;i<n;i++)
			for(int j=i+1;j<n;j++){
				k=0;
				for(int v=0;v<m;v++)            
			 
					if( (Un[i][attribute[v]]!=Un[j][attribute[v]]) && (Un[i][attribute[v]]!=-1) && (Un[j][attribute[v]]!=-1))
						break;
					else
						k=k+1;
				 
				if(k==m){
					ToleranceMatrix[i][j]=1;
					ToleranceMatrix[j][i]=1;
				}
			}

		return ToleranceMatrix;
	}
	
	//求决策系统DS在属性集P下的知识粒度GP
	public double get_GP(int[][] Un,List<Integer> P){
		int n=Un.length;
  
		int ToleranceMatrix[][]=get_ToleranceMatrix(Un,P);
		int sum=0;
		for (int i = 0; i < ToleranceMatrix.length; i++) {
			for (int j = 0; j < ToleranceMatrix[i].length; j++) {
          	
          	sum+=ToleranceMatrix[i][j];
          	
			}
		}		
		//System.out.println("sum= "+sum);
		return sum;///Math.pow(n,2);
	}
	
	//求决策系统DS在属性集P下的相对知识粒度GP(D|P)
	public double get_GPRelative(int[][] Un,List<Integer> P){
			int m=Un[0].length;//
			List<Integer> PUD= new ArrayList<Integer>();
			Iterator<Integer> value = P.iterator();
			while (value.hasNext()) {
			    PUD.add(value.next());
			}   

			PUD.add(m-1);	
			 
//			double k1=get_GP(P);System.out.println("k1="+k1);
//			double k2=get_GP(PUD);System.out.println("k2="+k2);
			double GPRelative=get_GP(Un,P)-get_GP(Un,PUD);
			return GPRelative;//k1-k2;
	}
	
	//计算决策系统DS属性a对属性集P的内部重要性Sig_U_SigInner(a,P,D)=GP(D|P-{a})-GP(D|P)
	public double get_SigInner(int[][] Un,int a,List<Integer> P){
		List<Integer> P_D= new ArrayList<Integer>();
		
		Iterator<Integer> value = P.iterator();
		while (value.hasNext()) {
			int num=value.next();
			if(num!=a)
				P_D.add(num);
		}
		double k=get_GPRelative(Un,P_D);
		double s=get_GPRelative(Un,P);
		return(k-s);
	}
	
	//计算决策系统DS属性a对属性集P的外部重要性Sig_U_Outer(a,P,D)=GP(D|P)-GP(D|PU{a})
	public double get_SigOuter(int[][] Un,int a,List<Integer> P){
		double k=get_GPRelative(Un,P);
		
		List<Integer> PUa= new ArrayList<Integer>();
		PUa.addAll(P);			
		PUa.add(a);
		
		double s=get_GPRelative(Un,PUa);
		return (k-s);
	}
	
	//计算决策系统DS增加对象Ux后，在属性集P下，新增对象Ux和DS中元素的容差矩阵。譬如|Ux|=1,|DS|=n,则所求容差矩阵维数为1xn,用一维数组存放
	public int[] get_PTC(int Un[][],int Ux[],List<Integer> P){
		int k=0,n=Un.length,m=P.size();
		int val_Ux=0,val_Un=0;
		int ToleranceMatrix[]=new int[n];
		int attribute[]=new int[m];
		//利用Iterator实现遍历
		int w=0;
		Iterator<Integer> value = P.iterator();
		while (value.hasNext()) {
			attribute[w]= value.next();			
		    w++;
		}   
		
		for(int j=0;j<n;j++){
			k=0;				
			for(int v=0;v<m;v++){
				val_Ux=Ux[attribute[v]];
				val_Un=Un[j][attribute[v]];
				if( (val_Ux!=val_Un) && (val_Ux!=-1) && (val_Un!=-1))
					break;
				else
					k=k+1;
			}
			if(k==m){
				ToleranceMatrix[j]=1;
			}
		}
		return ToleranceMatrix;
	}	

	public static List<Integer> get_UUUxRelativeP;
	public static double get_GPRelativeUnPResult;
	
	public boolean checkPandP(List<Integer> P){
		
	//	System.out.println(P.equals(get_UUUxRelativeP)+"  "+P+"  "+get_UUUxRelativeP);
		return P.equals(get_UUUxRelativeP);
	}
	
	//计算决策系统DS和新增对象Ux属性集C相对于决策属性D的知识粒度GP_UUUx(D|C)
	public double get_UUUxRelative(int Un[][],int Ux[],List<Integer> P,int flage){			
		int n=Un.length, m=Un[0].length;
		List<Integer> D=new ArrayList<Integer>();
		D.add(m-1);//决策属性序号
		
		int PTC[]=get_PTC(Un,Ux,P);//计算DS和新增对象Ux在属性集P下的相容矩阵
		int PTD[]=get_PTC(Un,Ux,D);//计算DS和新增对象Ux在决策属性D下的相容矩阵
		int PTCUD[]=new int[n];//计算DS和新增对象Ux在属性集CUD下的相容矩阵

		for(int j=0;j<n;j++)
			PTCUD[j]=PTC[j]&PTD[j];		
		
		double get_GPRelativeUnP ;
		
		if(flage == 1){		//如果是全集的
			if(!checkPandP(P)){
				get_UUUxRelativeP = new ArrayList<>();
				get_UUUxRelativeP.addAll(P);
				get_GPRelativeUnPResult = get_GPRelative(Un,P);
				
				get_GPRelativeUnP = get_GPRelativeUnPResult;
			}else{
				get_GPRelativeUnP = get_GPRelativeUnPResult;
			}
			
		}else if(flage==3){ 
			get_UUUxRelativeP = new ArrayList<>();
			get_UUUxRelativeP.addAll(P);
			get_GPRelativeUnPResult = get_GPRelative(Un,P);
			
			get_GPRelativeUnP = get_GPRelativeUnPResult;
			return  (Math.pow(n,2)*get_GPRelativeUnP+2*(sum(PTC)-sum(PTCUD)));
		}else{//否则照常
			return  (Math.pow(n,2)*get_GPRelative(Un,P)+2*(sum(PTC)-sum(PTCUD)));
		}
		 
		// System.out.println("now:"+get_GPRelativeUnP);
		//System.out.println("2*(sum(PTC)-sum(PTCUD))="+2*(sum(PTC)-sum(PTCUD)));
		double UUUxRelative=(Math.pow(n,2)*get_GPRelativeUnP+2*(sum(PTC)-sum(PTCUD)));///Math.pow(n+1,2);
	//	return  (Math.pow(n,2)*get_GPRelative(Un,P)+2*(sum(PTC)-sum(PTCUD)));
		//double UUUxRelative=(Math.pow(n,2)*(get_GP(Un,P)-get_GP(Un,PUD))+2*(sum(PTC)-sum(PTCUD)));///Math.pow(n+1,2);
	//	double UUUxRelative=(Math.pow(n,2)*get_GPRelative(Un,P)+2*(sum(PTC)-sum(PTCUD)));///Math.pow(n+1,2);
		return UUUxRelative;
	}
	
	double sum(int a[]){
		double sum1=0;
		for(int i=0;i<a.length;i++)
				sum1+=a[i];		
		return sum1;
	}
	
	//计算决策系统DS和新增对象Ux的属性a对属性集P的外部重要性Sig_UUUx_Outer(a,P,D)=GP(D|P)-GP(D|PU{a})
	public double get_UUUxSigOuter(int Un[][],int Ux[],int a,List<Integer> P){
		int n=Un.length, m=Un[0].length;  
		List<Integer> D=new ArrayList<Integer>();
		D.add(m-1);//决策属性序号
		
		int PTC[]=get_PTC(Un,Ux,P);//计算DS和新增对象集Ux在属性集P下的相容矩阵
		int PTD[]=get_PTC(Un,Ux,D);//计算DS和新增对象集Ux在决策属性D下的相容矩阵
		int PTCUD[]=new int[n];//计算DS和新增对象集Ux在属性集CUD下的相容矩阵
		for(int j=0;j<n;j++)
			PTCUD[j]=PTC[j]&PTD[j];
		
		List<Integer> PUa= new ArrayList<Integer>();
		PUa.addAll(P);			
		PUa.add(a);
		
		int PTPUa[]=get_PTC(Un,Ux,PUa);//计算DS和新增对象集Ux在属性集PUa下的相容矩阵
		int PTPUaUD[]=new int[n];//计算DS和新增对象集Ux在属性集PUaUD下的相容矩阵
		for(int j=0;j<n;j++)
			PTPUaUD[j]=PTPUa[j]&PTD[j];			

		double UUUxSigOuter=(Math.pow(n,2)*get_SigOuter(Un,a,P)+2*(sum(PTC)-sum(PTCUD))-2*(sum(PTPUa)-sum(PTPUaUD)));///((n+1)^2);

		return UUUxSigOuter;
	}
	
	 public static List<Integer> C =null;
	
	//增量式计算求约简KGIRA
	public List<Integer> KGIRA(int Un[][],List<Integer> SReduct,int Ux[]){
//		List<Integer> B= new ArrayList<Integer>();
		List<Integer> B= new ArrayList<Integer>();
		B.addAll(SReduct);
		
		if(C==null){
		int m=Un[0].length;
		C= new ArrayList<Integer>();
		for(int i=0;i<m-1;i++)
			C.add(i);
		}
		double new_GPRelativeC=get_UUUxRelative(Un,Ux,C,1);	//计算GP_UUUx(D|C)
		double new_GPRelativeB=get_UUUxRelative(Un,Ux,B,2);	//计算GP_UUUx(D|B)
	 //System.out.println(new_GPRelativeC+"   "+new_GPRelativeB);
	//	System.out.println("stop1:"+new_GPRelativeC+"\n\n");
		if(new_GPRelativeC==new_GPRelativeB){//比较GP_UUUx(D|C)和GP_UUUx(D|B)

			return B;
		}
		
		List<Integer> temp= new ArrayList<Integer>();
//		while (new_GPRelativeC!=get_UUUxRelative(Un,Ux,B)){
//		System.out.println("stop1:"+new_GPRelativeC+"\n\n");
		new_GPRelativeC = get_UUUxRelative(Un,Ux,C,3);
//		System.out.println("stop3:"+new_GPRelativeC+"\n\n");
//		
//		System.exit(0);
		while (new_GPRelativeC!=get_UUUxRelative(Un,Ux,B,2)){

//			temp.addAll(C);
//			temp.removeAll(B);
			temp = setDiff(C, B);

			Iterator<Integer> v1 = temp.iterator();
			double preSigOuter=Double.NEGATIVE_INFINITY;
		    int prenum=-1;//属性编号从0开始，所有属性编号都会大于-1
			while (v1.hasNext()) {
				int num=v1.next();
				double gm=get_UUUxSigOuter(Un,Ux,num,B);//System.out.println(num+"HH"+prenum+"*"+gm+"///");
				if(preSigOuter < gm){
					prenum=num;
					preSigOuter=gm;
				}
			}
			B.add(prenum);//System.out.println(B);
			//System.out.println("阶段3-"+(i++)+"完成");
		    temp.clear();
			//System.out.println(new_GPRelativeC+"$$$$"+get_UUUxRelative(Un,Ux,B));
//		    if(get_UUUxRelative(Un,Ux,C,2)!=get_UUUxRelative(Un,Ux,B,3))break;
		    
		  //  System.out.println("我在里面");
		}
		
		//Collections.sort(B);
		//System.out.println("我出来了");

		//约简最小化
		List<Integer> temp2= new ArrayList<Integer>(); 
		temp2.addAll(B);
		Iterator<Integer> v2 = temp2.iterator();
		List<Integer> temp3= new ArrayList<Integer>();
		while(v2.hasNext()){
			Object objnum=v2.next();
			temp3.addAll(B);
			temp3.remove(objnum);			
			if(get_UUUxRelative(Un,Ux,temp3,2)==new_GPRelativeC)
				B.remove(objnum);
			temp3.clear();	
			//System.out.println(B);
		}
		return B;
	}

	private static List<Integer> setDiff(List<Integer> A, List<Integer> B){
		List<Integer> diff = new ArrayList<>();
		for (int a:A
			 ) {
			int num = 0;
			for (int b : B
					) {
				if (a != b)
					num++;
			}
			if (num == B.size())
				diff.add(a);
		}
		return diff;
	}
}