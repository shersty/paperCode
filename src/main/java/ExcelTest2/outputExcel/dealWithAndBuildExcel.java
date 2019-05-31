package ExcelTest2.outputExcel;

import ExcelTest2.standard.buildStandard;
import ExcelTest2.standard.entity.TypeInfo;
import ExcelTest2.standard.entity.commonValue;
import com.avalon.holygrail.excel.exception.ExcelException;
import com.avalon.holygrail.excel.norm.ExcelWorkBookExport;
import com.avalon.holygrail.util.Export;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * auth:lht
 * time: 2018年11月23日17:44:14
 */
public class dealWithAndBuildExcel {



    public static List<TypeInfo> standardsList = null;
    public static List<String> dontUse = null;
    public static List<Map<String,Integer>> excelDatas;

    public static int width = 0;
    /**
     * 对于当前数据进行分类
     * @param data
     * @param index
     * @return
     */
    public static int findAndDeal(String data,int index){
        TypeInfo p = standardsList.get(index);              //获取标准


        if(p.getOver()!=null && p.getOver().equals("yes")){                      //说明是个数超了的 则进行离散化
            double dataD = Double.parseDouble(data);         //转成double
            double min = p.getMin();
            double dValue = p.getDvalue();
            int result = (int)((dataD-min)/dValue)+1;
            
            if(result>commonValue.myLength)result=commonValue.myLength;
            
            return result;
        }else{
            int indexof = p.getTypes().indexOf(data);
            indexof++;

            return indexof;
        }

    }

    public static boolean lossDeal(){
        int  a = (int)(Math.random()*100)+1; //1-100
        return a<commonValue.lossPercent?true:false;        //true代表丢失
    }

    /**
     * 将一行数据进行处理
     * @param content
     * @return
     */
    public static Map<String,Integer> dealWith(String content){
        Map<String,Integer> mp = new HashMap<String,Integer>();//保存一行的结果

        String[] strs = content.split(commonValue.splitFlage);
        int length = strs.length;
        if(width == 0){
            width = length;
        }
//        System.out.println(length);
//        for(String s :strs) System.out.print(s+"|");
//        System.out.println();

        for(int i =0;i<length;i++){
//            if(lossDeal())continue;             //丢失化处理
            String current = strs[i];
            if(dontUse!=null && dontUse.contains(current)) continue;

            int result = findAndDeal(current.trim(),i);
            mp.put(i+1+"",result);
        }
        return mp;
    }

    /**
     * 读取数据
     * @param f
     * @throws IOException
     */
    public static List<Map<String,Integer>> inputDatas(File f) throws IOException {
        excelDatas = new ArrayList<Map<String,Integer>>();

        BufferedReader br = new BufferedReader(new FileReader(f));

        String content = "";
        int n =1;
        while(content!=null){

            content = br.readLine();
            if(content == null)break;

//            System.out.println(content.trim());

            excelDatas.add(dealWith(content.trim()));
        }

        br.close();
        return excelDatas;

    }

    public static void display(List<Map<String,Integer>> mp){
        int i =0;
        for(Map<String,Integer> p :mp){
            System.out.println(p);
            i++;
        }
        System.out.println("总行数："+i);

    }

    public static void buildExcelFile(String filePath,List<Map<String, Integer>> results) throws ExcelException, IOException {

        int size = width;
        System.out.println("lines is :"+results.size());
        System.out.println("width is :"+width);
        String[] titles = new String[size];
        //生成列
        for(int i =0;i<size;i++){
            titles[i]=(i+1)+"";
        }

        System.out.println("努力生成中……");

        Export.buildSXSSFExportExcelWorkBook()
        .createSheet()
        .setColumnFields(titles)
        .importData(results, (value, record, cellHandler, field, rowCursor, index) -> {

          
   
                //将值格式化成数值类型
                return Integer.parseInt(value == null ? "" : value.toString());
         
          
        })//导入数据,支持多次导入
        .export(filePath); //导出成Excel

        System.out.println("生成完毕");

    }
    
    public static void pointCut(String origFila,String overPath) throws IOException, ExcelException{
    	// buildStandard.
        //生成标准 filePath是数据文件路径（用来生成标准）
        standardsList = buildStandard.pointCut(origFila,null);

        //进行匹配 filapath是数据文件路径（用来生成Excel)
        //filepath是指要处理的数据所在的文件
        //内容应该是纯数据
        File f = new File(origFila);
        System.out.println("\n\n");

        List<Map<String,Integer>> results = inputDatas(f);  //获取最终结果集合
        display(results);
        //进行excel输出  filepath 是保存excel文件的路径
        buildExcelFile(overPath,results);
    	
    }
    
	/**
	 * * pointCut(要处理的文件位置，处理完成后生成的文件存放的位置)
	 * @param args
	 * @throws IOException
	 * @throws ExcelException
	 */
    public static void main(String[] args) throws IOException, ExcelException {

        
        pointCut("data\\continousAdult.txt","saveResult\\continousAdult.xlsx");
    }
}
