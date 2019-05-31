//用静态二维数组的，即矩阵形式的
package cesudu;

import DataSet_package.DataPreprocess;
import DataSet_package.IDS_KGIRAV01;
import DataSet_package.IDS_KGIRAV02;
import DataSet_package.IDS_THAV02;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class test_KGIRAV02 {

    public static void doit(String fileName) {
        // TODO Auto-generated method stub
        System.out.println("****矩阵形式-知识粒度（改）******" + fileName + "上KGIRA的运行时间" + "***********");

        long time[] = new long[8];
        int t = 0;//time下标

        List<Integer> preRED = null;
        List<Integer> nowRED = null;

        // 创建DataPreprocess对象data1
        DataPreprocess data1 = new DataPreprocess(fileName);
        int k = 0, m1 = data1.get_origDSNum() / 10, m = m1 > 0 ? m1 : 1;
        data1.dataRatioSelect(0.2);

        preRED = new ArrayList<Integer>();
        nowRED = new ArrayList<Integer>();

        ArrayList<ArrayList<Integer>> origData = data1.get_origDS();//得到初始数据
        ArrayList<ArrayList<Integer>> addData = data1.get_addDS();//得到增量数据

        data1 = null;

        Iterator<ArrayList<Integer>> value = addData.iterator();

        IDS_THAV02 im1 = new IDS_THAV02(origData);
        preRED = im1.THA();

        IDS_KGIRAV02 im2 = new IDS_KGIRAV02();
        ArrayList<Integer> temp = new ArrayList<Integer>();
        long startTime = System.currentTimeMillis();   //获取开始时间
        while (value.hasNext()) {

            temp.addAll(value.next());
            im2.setUn(origData);
            im2.setUx(temp);
            nowRED = im2.KGIRA(im2.getUn(), preRED, im2.getUx());

            preRED.clear();
            preRED.addAll(nowRED);
            k++;

            if (k % m == 0) {
                long endTime = System.currentTimeMillis(); //获取结束时间
                time[t++] = endTime - startTime;//输出每添加10%数据求约简的时间
//                System.out.println("最近加的对象：" + temp);
                System.out.println("过程中的约简：" + nowRED);
            }
            origData.add(temp);
            temp = new ArrayList<Integer>();//.clear();
        }

        for (int i = 0; i < 8; i++)
            System.out.print((double) time[i] / 1000 + " ");

        System.out.println("\n" + fileName + "上KGIRA的约简为：" + nowRED + "\n");
    }
}

