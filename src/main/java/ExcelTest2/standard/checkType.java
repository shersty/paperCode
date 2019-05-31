package ExcelTest2.standard;

import org.junit.Test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 均是返回整体字符串 进行匹配的结果
 * 1.浮点型
 * 2.整形
 * 3.其他
 * auth:lht
 * time: 2018年11月23日17:44:14
 */
public class checkType {

    public static boolean checkIsInteger(String index){
        Matcher m = Pattern.compile("[+|-]?[0-9]+").matcher(index);
        return m.matches();
    }

    /**
     * 测试数据
     */
    @Test
    public void t1(){
        System.out.println(checkIsInteger("1d"));
        System.out.println(checkIsInteger("1"));
        System.out.println(checkIsInteger("-1"));
        System.out.println(checkIsInteger("+1"));
        System.out.println(checkIsInteger("1.1"));
        System.out.println(checkIsInteger(".1"));
        System.out.println(checkIsInteger(""));

    }


    public static boolean checkIsDouble(String index){
        Matcher m = Pattern.compile("[+|-]?[0-9]+\\.?[0-9]*[e]?[-|+]?[0-9]+").matcher(index);
        return m.matches();
    }

    /**
     * 测试数据
     */
    @Test
    public void t2(){
        System.out.println(checkIsDouble("1.1e-10"));
        System.out.println(checkIsDouble("-1.10"));
        System.out.println(checkIsDouble("+1.10"));
        System.out.println(checkIsDouble("1.1"));
        System.out.println(checkIsDouble("-1.1"));
        System.out.println(checkIsDouble("+1.1"));
        System.out.println(checkIsDouble(".1"));
        System.out.println(checkIsDouble("1"));
        System.out.println(checkIsDouble("1.1d"));
    }

    @Test
    public void t3(){
        System.out.println(1>0.99);
    }


    @Test
    public void t4(){
        double min = 10.5;
        double me  = 13.4;
        double dValue = 1.3;
        System.out.println((int)((me-min)/1.3));

        System.out.println(Float.parseFloat("1"));
    }
}
