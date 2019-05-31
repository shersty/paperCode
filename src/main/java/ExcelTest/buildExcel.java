package ExcelTest;

import com.avalon.holygrail.excel.exception.ExcelException;
import com.avalon.holygrail.excel.exception.ExportException;
import com.avalon.holygrail.util.Export;

import ExcelTest2.standard.entity.commonValue;

import org.junit.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * auth:lht
 * time: 2018年11月23日17:44:14
 */
public class buildExcel {

    //保存所有标准的容器
    public static List<String[]> standards;
    public static List<String> noExitst;

    /**
     * 正则表达式判断是不是数字 形如+1、 1、 1.1、 -1、 -1.1等
     * @param indexS
     * @return
     */
     public static boolean checkIsNumber(String indexS){
         Matcher m = Pattern.compile("[-|+]?[0-9]+\\.?[0-9]*").matcher(indexS);
        return m.matches();             //返回整体匹配的结果
     }

        @Test
        public void t12(){
            System.out.println(checkIsNumber("+1"));
        }

    /**
     * 返回匹配结果
     * @param aim
     * @param arg
     * @return
     */
    public static int findIndex(String[] aim,String arg){
        int result =-1;
        int shut = 0;
        for(int i=0;i<aim.length;i++){

            if(noExitst.contains(aim[i])){
                shut++;
                continue;
            }

            //如果是连续类型则检查是不是数字类型
            if(aim[i].equals("continuous")){
                if(checkIsNumber(arg))return i+1;       //是数字则返回
                else continue;                          //不是数字则什么都不做
            }

            if(arg.equals(aim[i])){
                result = i+1-shut;               //从1开始所以下标加一
                break;
            }
        }
        return result;
    }


    public static Map<String,Integer> buildMap(String dataContent){

//        System.out.println(standards.size());
        String[] datas = dataContent.split(commonValue.conSplitFlage);
//        for(String s :datas){
//            System.out.print(s+"|");
//        }

        //如果数据和检查标准数量不匹配则退出程序
        if(standards.size()!=datas.length){
            System.err.println("数据的列数是："+datas.length+" 不等于 检查标准的数量："+standards.size()+" 请检查输入文件是否正确");
            System.exit(0);
        }

        Map<String,Integer> m = new HashMap<String,Integer>();      //记录结果的map
        String[] indexs;
        int result ;
        for(int i=0;i<datas.length;i++){
            if(datas[i].trim().equals("?"))continue;  //如果遇到 ,就跳过默认是空

            indexs = standards.get(i);                  //获取当前列数的标准 列数从0开始

            result = findIndex(indexs,datas[i].trim());

            if(result!=-1){                 //不是-1则表示找到了

                m.put((i+1)+"",result);   //将结果保存
            }

        }
        return m;
        //System.out.println("\n"+m);

    }



    public static List<Map<String, Integer>> getDataContent(String filePath) throws IOException {
        List<Map<String, Integer>> results = new ArrayList<>();    //集合用来存储比对信息

        File f = new File(filePath);                            //存有标准信息的文件
        ReadStandard.checkFileIsExist(f);                       //检查文件是否存在
        BufferedReader br = new BufferedReader(new FileReader(f));
        String flage ="";
//        int counts = 1;                     //记录当前标准行数
        while(flage!=null){

            flage = br.readLine();          //一行一行读取
            if(flage==null)break;
//            System.out.println(flage);
            results.add(buildMap(flage));            //放到总容器里
//            counts++;
        }

        br.close();
        System.out.println(results.size());
        return results;
    }

    public static void buildExcelFile(String filePath,List<Map<String, Integer>> results) throws ExcelException, IOException {

        int size = standards.size();
        String[] titles = new String[size];
        //生成列
        for(int i =0;i<size;i++){
            titles[i]=(i+1)+"";
        }

        System.out.println("努力生成中……");

        Export.buildSXSSFExportExcelWorkBook()//构建查询对象
                .createSheet("SheetTest")//创建一个Sheet,可以传参设置Sheet名
                .setColumnFields(titles)//设置列字段,对应Map集合的key
                .importData(results, (value, record, cellHandler, field, rowCursor, index) -> {

                    
                	   
                    //将值格式化成数值类型
                    return Integer.parseInt(value == null ? "" : value.toString());
             
              
                })//导入数据,支持多次导入
                .export(filePath);//导出成Excel

        System.out.println("生成完毕");

    }
    
    static void pointCut(String satandard,String datas,String savePath) throws IOException, ExcelException{
    	 noExitst = new ArrayList<String>();
       //  noExitst.add("-");
       //  noExitst.add("--");

         //filepath是指标准分类信息所在的文件 内容形如3. steel:	-,R,A,U,K,M,S,W,V
         //一定要有 : 这个符号 然后后面用,分隔每个类别
         standards = ReadStandard.getContents(satandard);   //获取标准

         //filepath是指要处理的数据所在的文件
         //内容应该是纯数据
         List<Map<String, Integer>> results =getDataContent(datas);    //内容对比分类生成Map集合

         //filepath 是指excel文件保存的位置
         buildExcelFile(savePath,results);
    	
    }

    public static void main(String[] args) throws ExcelException, IOException {
    	pointCut("standards\\connect-4Standards.txt","data\\connect-4data.txt","saveResult\\connect-4data.xlsx");
    }

}
