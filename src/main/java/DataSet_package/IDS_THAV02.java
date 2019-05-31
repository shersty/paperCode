package DataSet_package;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class IDS_THAV02 {
	ArrayList<ArrayList<Integer>> Un;

	//返回实例变量Un
	public ArrayList<ArrayList<Integer>> getUn(){
		return Un;
	}

	//设置实例变量Un
	public void setUn(ArrayList<ArrayList<Integer>> nData){
		Un=new ArrayList<ArrayList<Integer>>();
		Un.addAll(nData);
	}

	//构造函数，给两个实例变量赋值
	public IDS_THAV02(ArrayList<ArrayList<Integer>> orgData){
		setUn(orgData);
	}

	//求Un在属性集P下各对象的相容类集合
	public ArrayList<ArrayList<Integer>> get_ToleranceClassSet(ArrayList<ArrayList<Integer>> Un,List<Integer> P){
		int k=0,n=Un.size(),m=P.size();
		ArrayList<ArrayList<Integer>> ToleranceClassSet=new ArrayList<ArrayList<Integer>>();

		for(int i=0;i<n;i++){
			ArrayList<Integer> temp=new ArrayList<Integer>();
			ToleranceClassSet.add(temp);
		}

		for(int i=0;i<n;i++)
			for(int j=i;j<n;j++){
				k=0;
				for(int v=0;v<P.size();v++)
					if( (!Un.get(i).get(P.get(v)).equals(Un.get(j).get(P.get(v)))) && (Un.get(i).get(P.get(v))!=-1) && (Un.get(j).get(P.get(v))!=-1))
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

//	@Test
//	public void testGetToleranceClass(){
//		double[][] testArr = {{1.0, 1.0, 1.0, 0.0, 1.0}, {0.0, Double.NaN, 1.0, 0.0, 1.0}, {Double.NaN, Double.NaN, 0, 0, 0},
//				{1.0, Double.NaN, 1, 1, 1}, {Double.NaN, Double.NaN, 1, 1, 2}, {0, 1, 1, Double.NaN, 1}};
//		ArrayList<Integer> a1 = new ArrayList<>();
//		ArrayList<Integer> a2 = new ArrayList<>();
//		ArrayList<Integer> a3 = new ArrayList<>();
//		ArrayList<Integer> a4 = new ArrayList<>();
//		ArrayList<Integer> a5 = new ArrayList<>();
//		ArrayList<Integer> a6 = new ArrayList<>();
//		a1.add(1);a1.add(1);a1.add(1);a1.add(0);a1.add(1);
//		a2.add(0);a2.add(-1);a2.add(1);a2.add(0);a2.add(1);
//		a3.add(-1);a3.add(-1);a3.add(0);a3.add(0);a3.add(0);
//		a4.add(1);a4.add(-1);a4.add(1);a4.add(1);a4.add(1);
//		a5.add(-1);a5.add(-1);a5.add(1);a5.add(1);a5.add(2);
//		a6.add(0);a6.add(1);a6.add(1);a6.add(-1);a6.add(1);
//		ArrayList<ArrayList<Integer>> testDS = new ArrayList<>();
//		testDS.add(a1);testDS.add(a2);testDS.add(a3);testDS.add(a4);testDS.add(a5);testDS.add(a6);
//		List<Integer> p = new ArrayList<>();
//		for(int i = 0; i < 4; i++){
//			p.add(i);
//		}
//		ArrayList<ArrayList<Integer>> testTolerance = get_ToleranceClassSet(testDS,p);
//		for (ArrayList<Integer> a:testTolerance
//			 ) {
//			System.out.println(a);
//		}
//	}

	//求决策系统DS在属性集P下的知识粒度GP
	public double get_GP(ArrayList<ArrayList<Integer>> Un,List<Integer> P){
		ArrayList<ArrayList<Integer>> ToleranceMatrixSet=get_ToleranceClassSet(Un,P);
		int sum = 0;
		for (ArrayList<Integer> aToleranceMatrixSet : ToleranceMatrixSet) {
			sum += aToleranceMatrixSet.size();
		}
		return sum/Math.pow(Un.size(),2);
//		return sum;
	}
//	@Test
//	public void testGetGP(){
//		ArrayList<Integer> a1 = new ArrayList<>();
//		ArrayList<Integer> a2 = new ArrayList<>();
//		ArrayList<Integer> a3 = new ArrayList<>();
//		ArrayList<Integer> a4 = new ArrayList<>();
//		ArrayList<Integer> a5 = new ArrayList<>();
//		ArrayList<Integer> a6 = new ArrayList<>();
//		a1.add(1);a1.add(1);a1.add(1);a1.add(0);a1.add(1);
//		a2.add(0);a2.add(-1);a2.add(1);a2.add(0);a2.add(1);
//		a3.add(-1);a3.add(-1);a3.add(0);a3.add(0);a3.add(0);
//		a4.add(1);a4.add(-1);a4.add(1);a4.add(1);a4.add(1);
//		a5.add(-1);a5.add(-1);a5.add(1);a5.add(1);a5.add(2);
//		a6.add(0);a6.add(1);a6.add(1);a6.add(-1);a6.add(1);
//		ArrayList<ArrayList<Integer>> testDS = new ArrayList<>();
//		testDS.add(a1);testDS.add(a2);testDS.add(a3);testDS.add(a4);testDS.add(a5);testDS.add(a6);
//		List<Integer> p = new ArrayList<>();
//		for(int i = 0; i < 4; i++){
//			p.add(i);
//		}
//
//		double testGP = get_GP(testDS, p);
//		System.out.println(testGP);
//	}

	//求决策系统DS在属性集P下的相对知识粒度GP(D|P)
	public double get_GPRelative(ArrayList<ArrayList<Integer>> Un,List<Integer> P){
		int m=Un.get(0).size();
		List<Integer> PUD= new ArrayList<Integer>();
		PUD.addAll(P);
		PUD.add(m-1);

//		double k=get_GP(Un,P),s=get_GP(Un,PUD);
		return get_GP(Un,P) - get_GP(Un,PUD);
	}

//	@Test
//	public void testGPR(){
//		ArrayList<Integer> a1 = new ArrayList<>();
//		ArrayList<Integer> a2 = new ArrayList<>();
//		ArrayList<Integer> a3 = new ArrayList<>();
//		ArrayList<Integer> a4 = new ArrayList<>();
//		ArrayList<Integer> a5 = new ArrayList<>();
//		ArrayList<Integer> a6 = new ArrayList<>();
//		a1.add(1);a1.add(1);a1.add(1);a1.add(0);a1.add(1);
//		a2.add(0);a2.add(-1);a2.add(1);a2.add(0);a2.add(1);
//		a3.add(-1);a3.add(-1);a3.add(0);a3.add(0);a3.add(0);
//		a4.add(1);a4.add(-1);a4.add(1);a4.add(1);a4.add(1);
//		a5.add(-1);a5.add(-1);a5.add(1);a5.add(1);a5.add(2);
//		a6.add(0);a6.add(1);a6.add(1);a6.add(-1);a6.add(1);
//		ArrayList<ArrayList<Integer>> testDS = new ArrayList<>();
//		testDS.add(a1);testDS.add(a2);testDS.add(a3);testDS.add(a4);testDS.add(a5);testDS.add(a6);
//		List<Integer> p = new ArrayList<>();
//		for(int i = 0; i < 4; i++){
//			p.add(i);
//		}
//
//		double gpr = get_GPRelative(testDS, p);
//		System.out.println(gpr);
//	}

	//计算决策系统DS属性a对属性集P的内部重要性Sig_U_SigInner(a,P,D)=GP(D|P-{a})-GP(D|P)
	public double get_SigInner(ArrayList<ArrayList<Integer>> Un,int a,List<Integer> P){
		List<Integer> P_D= new ArrayList<Integer>();

		Iterator<Integer> value = P.iterator();
		while (value.hasNext()) {
			Integer num=value.next();
			if(!num.equals(a))
				P_D.add(num);

//			int num=value.next();
//			if(num!=a)
//				P_D.add(num);
		}
		double k=get_GPRelative(Un,P_D);
		double s=get_GPRelative(Un,P);
		return(k-s);
	}
//	@Test
//	public void testGetSigIn(){
//		ArrayList<Integer> a1 = new ArrayList<>();
//		ArrayList<Integer> a2 = new ArrayList<>();
//		ArrayList<Integer> a3 = new ArrayList<>();
//		ArrayList<Integer> a4 = new ArrayList<>();
//		ArrayList<Integer> a5 = new ArrayList<>();
//		ArrayList<Integer> a6 = new ArrayList<>();
//		a1.add(1);a1.add(1);a1.add(1);a1.add(0);a1.add(1);
//		a2.add(0);a2.add(-1);a2.add(1);a2.add(0);a2.add(1);
//		a3.add(-1);a3.add(-1);a3.add(0);a3.add(0);a3.add(0);
//		a4.add(1);a4.add(-1);a4.add(1);a4.add(1);a4.add(1);
//		a5.add(-1);a5.add(-1);a5.add(1);a5.add(1);a5.add(2);
//		a6.add(0);a6.add(1);a6.add(1);a6.add(-1);a6.add(1);
//		ArrayList<ArrayList<Integer>> testDS = new ArrayList<>();
//		testDS.add(a1);testDS.add(a2);testDS.add(a3);testDS.add(a4);testDS.add(a5);testDS.add(a6);
//		List<Integer> p = new ArrayList<>();
//		for(int i = 0; i < 4; i++){
//			p.add(i);
//		}
//		double sigIn = get_SigInner(testDS, 2, p);
//		System.out.println(sigIn);
//	}


	//计算决策系统DS属性a对属性集P的外部重要性Sig_U_Outer(a,P,D)=GP(D|P)-GP(D|PU{a})
	public double get_SigOuter(ArrayList<ArrayList<Integer>> Un,int a,List<Integer> P){
		double k=get_GPRelative(Un,P);

		List<Integer> PUa= new ArrayList<Integer>();
		PUa.addAll(P);
		PUa.add(a);

		double s=get_GPRelative(Un,PUa);
		return (k-s);
	}

	//计算决策系统DS约简的传统方法THA
	public List<Integer> THA( ){
		List<Integer> RED=new ArrayList<Integer>();
		int m=Un.get(0).size();
		List<Integer> P= new ArrayList<Integer>();
		for(int i=0;i<m-1;i++)//不包含决策属性
			P.add(i);

		double gp_relativeC=get_GPRelative(Un,P);//GP(D|C)

		//System.out.println("阶段1完成");
		Iterator<Integer> value = P.iterator();
		while (value.hasNext()) {
			Integer num=value.next();
			double x=get_SigInner(Un,num,P);//
			if(x>0)
				RED.add(num);
		}
		List<Integer> B= new ArrayList<Integer>();
		B.addAll(RED);
		List<Integer> temp= new ArrayList<Integer>();
		while (get_GPRelative(Un,B)!=gp_relativeC) {
			temp.addAll(P);
			temp.removeAll(B);
			Iterator<Integer> v1 = temp.iterator();
			double preSigOuter = Double.NEGATIVE_INFINITY;
			int prenum = -1;//属性编号从0开始，所有属性编号都会大于-1
			while (v1.hasNext()) {
				int num = v1.next();
				double gm = get_SigOuter(Un, num, B);
				if(preSigOuter < gm){
					prenum = num;
					preSigOuter = gm;
				}
			}
			B.add(prenum);//System.out.println(B);
			//System.out.println("阶段3-"+(i++)+"完成");
			temp.clear();
		}
	//Collections.sort(B);

	//约简最小化
	List<Integer> temp2= new ArrayList<Integer>();
	temp2.addAll(B);
	Iterator<Integer> v2 = temp2.iterator();
	List<Integer> temp3= new ArrayList<Integer>();
	while(v2.hasNext()){
		Object objnum=v2.next();
		temp3.addAll(B);
		temp3.remove(objnum);
		if(get_GPRelative(Un,temp3)==gp_relativeC)
			B.remove(objnum);
		temp3.clear();
	}

	return B;
}
}