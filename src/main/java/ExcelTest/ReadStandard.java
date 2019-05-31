package ExcelTest;

import java.io.*;
import java.util.ArrayList;
import java.util.List;


/**
 * auth:lht
 * time: 2018年11月23日17:44:14
 * 1. 根据.txt文件内容生成标准数组 用来对数据分类
 * 2. 检查文件是否存在不存在则关闭程序
 */
public class ReadStandard {

    public static void dispalyArray(String[] args){
        for(String s :args){
            System.out.print(s+"|");
        }
        System.out.println();
    }

    /**
     * 检查传入的参数是否存在不存在则停止程序
     * @param f
     */
    public static void checkFileIsExist(File f){
        if(!f.exists()){
            System.err.println("文件不存在！！");
            System.exit(0);                 //结束程序
        }
    }



    /**
     * 从传入的部分截取标准存储到数组里
     * @param content
     * @return
     */
    public static  String[] buildStandardList(String content){

        int start = content.indexOf(":");               //截取 ：后面的部分
        content = content.substring(start+1);
        String[] standards = content.split(",");
        for(int i =0;i<standards.length;i++){
            standards[i]=standards[i].trim();           //每个元素去掉空格
        }


        return standards;
    }

    /**
     * 读取文件调用方法生成标准 然后放入容器
     * @param filePath
     * @return
     * @throws IOException
     */
    public static  List<String[]> getContents(String filePath) throws IOException {
        List<String[]> standardsList = new ArrayList<String[]>();        //存放所有标准的有序容器

        File f = new File(filePath);//存有标准信息的文件
        checkFileIsExist(f);            //检查文件是否存在
        BufferedReader br = new BufferedReader(new FileReader(f));
        String flage ="";
        int counts = 0;                     //记录当前标准行数
        while(flage!=null){

            flage = br.readLine();          //一行一行读取
            if(flage==null)break;
            standardsList.add(buildStandardList(flage));

        }
//      //检验list
//        for(String[] ddd:standardsList){
//            dispalyArray(ddd);
//        }
//        System.out.println(standardsList.size());

        br.close();
        return standardsList;
    }

//    public static void main(String[] args) throws IOException {
//
//        getContents("C:\\Users\\Hi\\Desktop\\standard.txt");
//
//    }
}
