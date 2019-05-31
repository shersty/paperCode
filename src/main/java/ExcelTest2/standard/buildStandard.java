package ExcelTest2.standard;

import ExcelTest2.standard.entity.TypeInfo;
import ExcelTest2.standard.entity.commonValue;
import com.alibaba.druid.sql.ast.statement.SQLIfStatement;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * auth:lht
 * time: 2018年11月23日17:44:14
 */
public class buildStandard {

    public static List<TypeInfo> standardList = null;
    public static List<String> meansNll = null ;



    @Test
    public void t1(){

        System.out.println(Double.parseDouble("1.2"));
    }

    /**
     * 将content 进行数组化 然后比对每个元素的类型来确定 bean的type属性;
     * @param content
     * @return
     */
    public static List<TypeInfo> confirmDataType(String content){

        List<TypeInfo> results = new ArrayList<TypeInfo>();  //保存总标准信息的容器

        String[] strs = content.split(commonValue.splitFlage);
        int length = strs.length;                       //获取列数

//        for(String s:strs) System.out.print(s+"|");
//        System.out.println();

        for(int i=0;i<strs.length;i++){
        String  current = strs[i];

        if(checkType.checkIsInteger(current)){          //判断是否是整型
            results.add(new TypeInfo("Integer",new ArrayList<String>()));

        }else if(checkType.checkIsDouble(current)){     //判断是否是浮点型
            results.add(new TypeInfo("Double",new ArrayList<String>()));

        }else{
            results.add(new TypeInfo("Other",new ArrayList<String>()));
        }
        }
        return results;
    }

    /**
     * 输出所有结果
     */
    public static void displayList(){
        int i =1;
        for(TypeInfo p : standardList){
            if(p.getTypes()!=null){

                System.out.println(i+" "+p+"  "+p.getTypes().size());
            }else{
                System.out.println(i+" "+p+"  ");
            }
            i++;
        }
    }

    /**
     * 进行越界检查生产最终结果
     * @param MaxLength
     * @param myLength
     */
    public static List<TypeInfo> vertifiedResult(int MaxLength,int myLength){

            for(TypeInfo p : standardList){
                if(!p.getType().equals("Other")){
            	int size = p.getTypes().size();
                if(size>MaxLength){
                    p.setOver("yes");               //表示超出了
                    Double Dvalue = (p.getMax()-p.getMin())/myLength;   //得到每一段的大小
                    p.setDvalue(Dvalue);            //设置区间的间隔值 然后用当前数据减去最小的除区间数就行了
                    p.setTypes(null);               //容器里的东西没意义了把内存交给垃圾处理机
                }
                }
            }
            return standardList;
    }

    /**
     * 遍历所有数据进行统计
     * @param content
     */
    public static  void dealWithData(String content,int maxLength){
        String[] strs = content.split(commonValue.splitFlage);
        int length = strs.length;                       //获取列数

        for(int i =0;i<strs.length;i++){
            String current = strs[i];                   //当前处理的数据

            if(current.equals(commonValue.lossFlage))continue;     //空缺值不进行处理
            	
            TypeInfo currentP = standardList.get(i);    //获取对应的类型收集bean

            if(currentP.getType().equals("Integer")){
                if(checkType.checkIsDouble(current)) {
                    Double nowInt = Double.parseDouble(current);         //转为Double
                    if (currentP.getTypes().size() == 0) {              //容器为0 说明是第一列
                        currentP.getTypes().add(nowInt + "");         //直接以String放入容器
                        currentP.setMax(nowInt);
                        currentP.setMin(nowInt);
                    } else {
                        if (!currentP.getTypes().contains(current)) { //没有放进来过说明需要进行处理
                            if (currentP.getTypes().size() < maxLength + 1)
                                currentP.getTypes().add(current); //如果存储的个数超出了规定值 数据就不继续放入了
                            if (nowInt < currentP.getMin()) currentP.setMin(nowInt);
                            if (nowInt > currentP.getMax()) currentP.setMax(nowInt);
                        }
                    }
                }else {
                    int nowInt = Integer.parseInt(current);         //转为int
                    if (currentP.getTypes().size() == 0) {              //容器为0 说明是第一列
                        currentP.getTypes().add(nowInt + "");         //直接以String放入容器
                        currentP.setMax(nowInt);
                        currentP.setMin(nowInt);
                    } else {
                        if (!currentP.getTypes().contains(current)) { //没有放进来过说明需要进行处理
                            if (currentP.getTypes().size() < maxLength + 1)
                                currentP.getTypes().add(current); //如果存储的个数超出了规定值 数据就不继续放入了
                            if (nowInt < currentP.getMin()) currentP.setMin(nowInt);
                            if (nowInt > currentP.getMax()) currentP.setMax(nowInt);
                        }
                    }
                }
            }else if(currentP.getType().equals("Double")){
                if(checkType.checkIsInteger(current)){
                    int nowDouble = Integer.parseInt(current);      //转为Int
                    if (currentP.getTypes().size() == 0) {              //容器为0 说明是第一列
                        currentP.getTypes().add(nowDouble + "");         //直接以String放入容器
                        currentP.setMax(nowDouble);
                        currentP.setMin(nowDouble);
                    } else {
                        if (!currentP.getTypes().contains(current)) { //没有放进来过说明需要进行处理
                            if (currentP.getTypes().size() < maxLength + 1)
                                currentP.getTypes().add(current);//如果存储的个数超出了规定值 数据就不继续放入了
                            if (nowDouble < currentP.getMin()) currentP.setMin(nowDouble);
                            if (nowDouble > currentP.getMax()) currentP.setMax(nowDouble);
                        }
                    }

                }else{
                    Double nowDouble = Double.parseDouble(current);      //转为double
                    if (currentP.getTypes().size() == 0) {              //容器为0 说明是第一列
                        currentP.getTypes().add(nowDouble + "");         //直接以String放入容器
                        currentP.setMax(nowDouble);
                        currentP.setMin(nowDouble);
                    } else {
                        if (!currentP.getTypes().contains(current)) { //没有放进来过说明需要进行处理
                            if (currentP.getTypes().size() < maxLength + 1)
                                currentP.getTypes().add(current);//如果存储的个数超出了规定值 数据就不继续放入了
                            if (nowDouble < currentP.getMin()) currentP.setMin(nowDouble);
                            if (nowDouble > currentP.getMax()) currentP.setMax(nowDouble);
                        }
                    }
                }
            }else{      //说明是其他类型
                if(!currentP.getTypes().contains(current))currentP.getTypes().add(current);

            }

        }

    }

    /**
     * 进行信息处理
     * @param f
     * @param maxlength
     * @throws IOException
     */
    public static void buildStandardList(File f,int maxlength) throws IOException {

        BufferedReader br = new BufferedReader(new FileReader(f));

        String content = br.readLine();
//        System.out.println(content.trim());
        int counts = 0;

        if(content!=null){          //进行确认各列的标准信息
            standardList =  confirmDataType(content.trim());        //全局的静态容器 在容器里面填入data基本的类型信息
            dealWithData(content.trim(),maxlength);           //处理第一行
        }
//        for(TypeInfo p :standardList){
//            System.out.println(p.getType());
//        }

        while(content!=null){
            content = br.readLine();
            if(content ==null )break;
//            System.out.println(content.trim());
            dealWithData(content.trim(),maxlength);           //处理每一行
        }

        br.close();

    }

    public static void checkFile(File f){
        if(!f.exists()){
            System.err.println(f.getPath()+"  文件不存在！\n 程序结束 请检查！");
            System.exit(0);
        }
    }

    public static  List<TypeInfo> pointCut( String filePath,List<String> meansNull) throws IOException {

        System.out.println("努力生成标准信息中！请稍后！\n ……\n ……");
        File f = new File(filePath);
        checkFile(f);

        meansNll  = meansNull;  //表示的是为空的值或者不进行处理的数据

        buildStandardList(f, commonValue.maxLength);
        List<TypeInfo> resultss =  vertifiedResult(commonValue.maxLength,commonValue.myLength);
        System.out.println("成功生成标准信息 ：\n");
        displayList();

        return resultss;

    }

    public static void main(String[] args) throws IOException {
//

//        //标识为空的符号
//        //meansNll = new ArrayList<String>();
//        //meansNll.add("?");//意味着 为？代表缺失
//        buildStandardList(f,7);
//        displayList();
//        vertifiedResult(7,5);
//        displayList();
        pointCut("C:\\Users\\Hi\\Desktop\\data2.txt",null);
    }

}
