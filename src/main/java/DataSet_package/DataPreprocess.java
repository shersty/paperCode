package DataSet_package;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class DataPreprocess {
	ArrayList<ArrayList<Integer>> origDS;//为了节省内存空间，该类对象生成时origDS是原始数据，调用方法dataRatioSelect(double percent)后变为初始数据。
	ArrayList<ArrayList<Integer>> addDS;
	
	public int get_origDSNum(){
		return origDS.size();
	}
	
	public DataPreprocess(String fileName){//构造函数
		origDS=readExcel(fileName);
		addDS=new ArrayList<ArrayList<Integer>>();
	}
	
	//返回增量数据addDS
	public void dataRatioSelect(double percent){
		int origNum=origDS.size();
		int num=(int)(origNum*percent);
		for(int i=num;i<origNum;i++){
			addDS.add(origDS.get(i));
		}
		
		for(int i=0;i<origNum-num;i++){
			origDS.remove(num);
		}		
	}
	
	
	public ArrayList<ArrayList<Integer>> get_origDS(){//得到初始数据origDS
		return origDS;
	}
	
	public ArrayList<ArrayList<Integer>> get_addDS(){//得到增量数据addDS
		return addDS;
	}
	// 读Excel的方法readExcel，该方法的入口参数为一个文件路径  
    public static ArrayList<ArrayList<Integer>> readExcel(String fileName) {
    	ArrayList<ArrayList<Integer>> arrList=new ArrayList<ArrayList<Integer>>();
    	Sheet sheet = null;
		Row row = null;
		Workbook wb = null;

		String extString = fileName.substring(fileName.lastIndexOf("."));
		InputStream is = null;
		
        try {  
            // 创建输入流，读取Excel  
           is = new FileInputStream(fileName);  
           if (".xls".equals(extString)) {
				wb = new HSSFWorkbook(is);
			} else if (".xlsx".equals(extString)) {
				wb = new XSSFWorkbook(is);
			} else {
				wb = null;
			}
           
           if (wb != null) {
        	// 获取第一个sheet
				sheet = wb.getSheetAt(0);// 考虑到效率，每个文件只放一个sheet，为其创建一个Sheet对象
				// 获取最大行数
				int rownum = sheet.getPhysicalNumberOfRows();
				
				row = sheet.getRow(0);
				// 获取最大列数
				int colnum = row.getLastCellNum();//System.out.println(colnum+"$$");
				for (int i = 0; i < rownum; i++) {
					ArrayList<Integer> temp_arrRow=new ArrayList<Integer>();
					row = sheet.getRow(i);
					if (row != null) {
						for (int j = 0; j < colnum; j++) {
							Cell cellinfo=row.getCell(j);//System.out.println(j);   
		                    if (cellinfo==null)
		                    	temp_arrRow.add(new Integer(-1));//用 -1 代表数据缺失
		                    else
		                    	temp_arrRow.add(new Integer((int) cellinfo.getNumericCellValue()));
						}
						
					} else {
						break;
					}
					arrList.add(temp_arrRow);
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();				
     }

        return arrList;
    } 
    
//    //将ArrayList数组转化为二维数组返回
//    public int[][] arrListToArr(String fileName){
//    	ArrayList<Integer> dataArrList=readExcel(fileName);
//    	int n=dataArrList.size(),m=get_col();
//    	int arr[][]=new int[get_row()][m];
//    	for(int i=0,t=0,j=0;i<n;i++){
//    		arr[t][j%m]=dataArrList.get(i).intValue();
//     	   j++;
//     	   if (j%m==0)
//     		   t++;
//        }
//    	return arr;
//    }

}
