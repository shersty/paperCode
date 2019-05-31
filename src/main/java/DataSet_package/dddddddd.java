package DataSet_package;

import java.util.ArrayList;
import java.util.List;

public class dddddddd {

	public static void main(String[] args) {
		ArrayList<ArrayList<Integer>> data=new ArrayList<ArrayList<Integer>>();
		//int data[][];
		
		int Un[][]={{1,-1,0,0,1,0},{0,0,1,-1,1,0},{0,1,-1,1,0,1},{-1,0,1,0,1,0},{0,1,1,1,0,1},{1,0,0,0,-1,1},{1,0,0,-1,0,1},{0,-1,1,0,1,0},{-1,1,1,0,1,1}};
		
		List<Integer> P=new ArrayList<Integer>();
		for(int i=0;i<5;i++)
			P.add(i);
		
		ArrayList<Integer>  data1=new ArrayList<Integer>();
		IDS_POSA posIDS=new IDS_POSA();
		ArrayList<Integer> original_reduct=posIDS.getPOS_Reduct(Un);
		ArrayList<Integer> original_POS=posIDS.getPOS(Un,P);
		ArrayList<Integer> additional_obj=new ArrayList<Integer>();
		additional_obj.add(0);additional_obj.add(-1);additional_obj.add(1);
		additional_obj.add(0);additional_obj.add(1);additional_obj.add(1);
		
		//data1=posIDS.getNewPOS(Un, original_reduct,original_POS, additional_obj);
		data=posIDS.SHU_IARS(Un, original_reduct,original_POS, additional_obj);
		System.out.println(original_reduct);
		System.out.println(original_POS);
		System.out.println(additional_obj);
		System.out.println();
		System.out.println(data.get(0));
		System.out.println(data.get(1));
		
//		ArrayList<Integer> data1=posIDS.getPOS_Reduct(Un,P);	
//		System.out.print(data1);
		
//		for(ArrayList<Integer> cells : data) {		   
//				System.out.print(cells+" ");			   
//		}
				
		
//		IDS_KGIRA posIDS=new IDS_KGIRA();data=posIDS.get_ToleranceMatrix(Un, P);
//		
//		for(int[] cells : data) {		 
//			for(int cell : cells) {   
//				System.out.print(cell+" ");			  
//			} 
//			System.out.println();  
//		}		
		
//		for(int i=0;i<data.size();i++) {		 
//			for(int j=0;j<data.get(i).size();j++) {   
//				System.out.print(data.get(i).get(j).intValue()+" ");			  
//			} 
//			System.out.println();  
//		}		

					  		
	}

}
