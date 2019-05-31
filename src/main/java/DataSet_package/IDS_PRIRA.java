package DataSet_package;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.junit.Test;

import java.util.*;

public class IDS_PRIRA {
    static double[][] testArr = {{1.0, 1.0, 1.0, 0.0, 1.0}, {0.0, Double.NaN, 1.0, 0.0, 1.0}, {Double.NaN, Double.NaN, 0, 0, 0},
            {1.0, Double.NaN, 1, 1, 1}, {Double.NaN, Double.NaN, 1, 1, 2}, {0, 1, 1, Double.NaN, 1}};
    static RealMatrix DS = new Array2DRowRealMatrix(testArr);

    public static ArrayList<Integer> shuExperiment1(RealMatrix origDS , RealMatrix addDS){
        int numberAdd = addDS.getRowDimension();
        ArrayList<Integer> posReduct = getPosReduct(origDS);
        System.out.println("初始约简:" + posReduct);
        ArrayList<Integer> originalReduct = new ArrayList<>(posReduct);
        ArrayList<Integer> originalPos = getPos(origDS,posReduct);
        RealMatrix originalSys = origDS;
        long[] time = new long[8];
        int K = 0;
        int k1 = (origDS.getRowDimension()+numberAdd)/10;
//        k1 = k1==0?1:k1;
        int timeCount = 0;
        int counts = 0;
        ShuIarsParam shuIarsParam = null;
        long timeStart = System.currentTimeMillis();
        for(int i = 0; i< numberAdd; i++){
            RealMatrix addtionalObj = addDS.getRowMatrix(i);
            shuIarsParam = shuIARS(originalSys,originalReduct,originalPos,addtionalObj);
            K = K + shuIarsParam.NM;
            originalSys = addARow(originalSys,addtionalObj);
            originalReduct = shuIarsParam.increPosReduct;
            originalPos = shuIarsParam.newCPos;
            counts++;
            if(counts % k1 == 0){
                long timeTemp = System.currentTimeMillis();
                time[timeCount] = timeTemp - timeStart;
                timeCount++;
            }
        }
        for (long t:time)
            System.out.print((double) t/1000+"s ");
        return shuIarsParam.increPosReduct;
    }

    /**
     * 正域增量式方法计算约简,增量的对象为一个，
     * @param originalSys   初始决策系统
     * @param originalReduct    初始约简
     * @param originalPos   初始正域
     * @param addtionalObj  添加的对象
     * @return 一次添加所得到的响应 约简和正域和重复计算次数所包装的对象
     */
    public static ShuIarsParam shuIARS(RealMatrix originalSys, ArrayList<Integer> originalReduct, ArrayList<Integer> originalPos, RealMatrix addtionalObj){

        ArrayList<Integer> increPosReduct;
        ArrayList<Integer> newCPos;
        int NM;

        int n = originalSys.getRowDimension();
        int m = originalSys.getColumnDimension();
        ArrayList<Integer> c = new ArrayList<>();
        for(int i = 0; i < m - 1; i++){
            c.add(i);
        }
        ArrayList<Integer> p = new ArrayList<>(originalReduct);

        RealMatrix newSys = addARow(originalSys,addtionalObj);

        ArrayList cToleranceClass = IRISSingleTolerance(newSys, c, n);//求新增的对象 在属性集c下的 容差类
        ArrayList<Integer> pToleranceClass = IRISSingleTolerance(newSys, p, n);//求新增对象 在原始约简下的 容差类

        int LC = cToleranceClass.size();
        int LP = pToleranceClass.size();
        ArrayList<Integer> genaralDecisionP = new ArrayList<>();
        for(int i = 0; i < LP; i++){
            genaralDecisionP.add((int)newSys.getEntry(pToleranceClass.get(i),m -1));
        }
        int lengthDecision = getUnique(genaralDecisionP).size();

        //第一种情况
        if(LC == 1 && lengthDecision == 1){
            increPosReduct = p;
            NM = 0;
        }

        //第二种情况
        else {
            NM = 1;
            ArrayList<Integer> newPPos = getNewPos(originalSys, p, originalPos, addtionalObj);
            newCPos = getNewPos(originalSys, c, originalPos, addtionalObj);
            int numberPPos = newPPos.size();
            int numberCPos = newCPos.size();
            //***********************************************************************************************
            while (numberPPos != numberCPos){
                ArrayList<Integer> temp = setDiff(c, p);
                int lengthT = temp.size();
                int maxSig = 0;
                int inAttribute = -1;
                for(int i = 0; i < lengthT; i++){
                    ArrayList<Integer> currAtt = new ArrayList<>(p);
                    currAtt.add(temp.get(i));
                    ArrayList<Integer> currPos = getPos(newSys, currAtt);
                    int insignificance = setDiff(currPos, newPPos).size();
                    if(insignificance > maxSig){
                        inAttribute = temp.get(i);
                        maxSig = insignificance;
                    }
                }
                if(inAttribute == -1){
                    inAttribute = temp.get(lengthT - 1);
                }
                p.add(inAttribute);

                newPPos = getPos(newSys, p);
                numberPPos = newPPos.size();
            }
            //*************************************************************************************************
            increPosReduct = p;
            //去除reduct中多余的属性
            int v = 0;
            while (v < increPosReduct.size()){
                ArrayList<Integer> t = new ArrayList<>(increPosReduct);
                t.remove(v);
                ArrayList<Integer> posT = getPos(newSys, t);
                int outSignificance = setDiff(newCPos, posT).size();
                if(outSignificance == 0){
                    increPosReduct = t;
                    v = 0;
                }else {
                    v++;
                }
            }
//            Collections.sort(increPosReduct);
        }
        newCPos = getPos(newSys, increPosReduct);
        return new ShuIarsParam(increPosReduct, newCPos, NM);
    }

    /**
     * 使用属性重要度计算基于正域约简的传统算法
     * @param DS 决策系统DS
     * @return 决策系统的正域约简POS_Reduct
     */
    public static ArrayList<Integer> getPosReduct(RealMatrix DS){
        int m = DS.getColumnDimension();
        //get core of C
        ArrayList<Integer> c = new ArrayList<>();
        for(int i = 0; i < m - 1; i++){
            c.add(i);
        }
        ArrayList<Integer> posC = getPos(DS, c);
        int lengthC = posC.size();
        ArrayList<Integer> coreSet = new ArrayList<>();
        for(int i = 0; i < m - 1; i++){
            ArrayList<Integer> t = new ArrayList<>(c);
            t.remove(i);
            ArrayList<Integer> posQ = getPos(DS, t);
            ArrayList diffCQ = setDiff(posC, posQ);
            int outSignificance = diffCQ.size();
            if(outSignificance != 0){
                coreSet.add(i);
            }
        }
        //从核出发，获得满足第一个条件的属性集
        ArrayList<Integer> posReduct = new ArrayList<>(coreSet);
        ArrayList<Integer> posR = getPos(DS, posReduct);
        int lengthR = posR.size();
//        System.out.println(lengthC+"=="+lengthR);
        //***********************************************************************************************
        while(lengthR != lengthC){
            ArrayList<Integer> t = setDiff(c, posReduct);
            int lengthT = t.size();
            int maxSignificance = 0;
            int inAttribute = -1;
            for(int j = 0; j < lengthT; j++){
                ArrayList<Integer> temp = new ArrayList<>(posReduct);
                temp.add(t.get(j));
                ArrayList<Integer> posT = getPos(DS, temp);
                int inSignificance = setDiff(posT, posR).size();
                if(inSignificance > maxSignificance){
                    maxSignificance = inSignificance;
                    inAttribute = t.get(j);
                }
            }
            if(inAttribute == -1)
                inAttribute = t.get(lengthT - 1);
            posReduct.add(inAttribute);
            posR = getPos(DS, posReduct);
            lengthR = posR.size();
        }
        //***********************************************************************************************
        //检验属性集中是否还有多余的属性
        int v = 0;
        while (v < posReduct.size()){
            ArrayList<Integer> temp = new ArrayList<>(posReduct);
            temp.remove(v);
            ArrayList<Integer> posT = getPos(DS, temp);
            int outSignificance  = setDiff(posR, posT).size();
            if(outSignificance == 0){
                posReduct = temp;
                v = 0;
            }else {
                v++;
            }
        }
//        Collections.sort(posReduct);
        return posReduct;
    }

    /**
     * 计算决策系统 DS 在属性集P 下的正域
     * @param DS 决策系统DS
     * @param p 属性集P
     * @return 决策系统在属性集P下的正域POS
     */
    public static ArrayList<Integer> getPos(RealMatrix DS , ArrayList<Integer> p){
        ArrayList<Integer> posList = new ArrayList<>();
        if(p.size() == 0)
            return posList;
        else {
            ArrayList<ArrayList<Integer>> decisionClass = findDecisionClass(DS);
            ArrayList<ArrayList<Integer>> toleranceClass = IRISSingleTolerance(DS, p);
            int n = DS.getRowDimension();
            for (int i = 0; i < n; i++) {
                int sum = 0;
                for (int obj:
                     toleranceClass.get(i)) {
                    if(!decisionClass.get(i).contains(obj))//如果 decisionClass 没有该元素就 +1 ， 遍历 toleranceClass 遍历完的sum就是 t中元素在d中不存在个数，如果sum为0,则包含其中
                        sum++;
                }
                if (sum == 0){
                    posList.add(i);
                }
            }
        }
        return posList;
    }

    @Test
    public void testGetPos(){
        RealMatrix testDs = DS;
        ArrayList<Integer> p = new ArrayList<>();
//        for(int i = 0; i < 4; i++){
//            p.add(i);
//        }
//        p.add(2);
        p.add(3);
        ArrayList<Integer> pos = getPos(testDs, p);
        System.out.println(pos);
    }

    /**
     * 计算具体对象obj属性集P下的容差类
     * @param DS 决策系统
     * @param p 属性集
     * @param obj 指定的对象
     * @return 该对象在该属性集下的容差类
     */
    private static ArrayList<Integer> IRISSingleTolerance(RealMatrix DS, ArrayList<Integer> p, int obj){
        int n = DS.getRowDimension();
        int m = p.size();
        ArrayList<Integer> ToleranceClass = new ArrayList<Integer>();


        for (int j = 0; j < n; j++) {
            int k = 0;
            for (int v = 0; v < p.size(); v++) {
                if (DS.getEntry(j, p.get(v)) != DS.getEntry(obj, p.get(v)) && !Double.isNaN(DS.getEntry(j, p.get(v)))
                        && !Double.isNaN(DS.getEntry(obj, p.get(v))))
                    break;
                else
                    k = k + 1;

                //如果完全一致，那么是一个容差类》
                if (k == m) {
                    ToleranceClass.add(j);
                }
            }
        }
        return ToleranceClass;
    }

    /**
     * 计算决策系统属性集P下的容差类
     * @param DS 决策系统
     * @param p 属性集
     * @return 所有对象在该属性集下的容差类的链表
     */
    private static ArrayList<ArrayList<Integer>> IRISSingleTolerance(RealMatrix DS, ArrayList<Integer> p){
        int n = DS.getRowDimension();
        int m = p.size();
        ArrayList<ArrayList<Integer>> ToleranceClass = new ArrayList<>();
        for(int i = 0; i < n; i++){
            ArrayList<Integer> temp = new ArrayList<>();
            temp.add(i);
            ToleranceClass.add(i,temp);
        }
        for(int i = 0 ; i < n ; i++) {
            for (int j = i + 1; j < n; j++) {
                int k = 0;
                for (int v = 0; v < m; v++) {
                    if (DS.getEntry(j, p.get(v)) != DS.getEntry(i, p.get(v)) && !Double.isNaN(DS.getEntry(j, p.get(v)))
                            && !Double.isNaN(DS.getEntry(i, p.get(v))))
                        break;
                    else
                        k = k + 1;

                    //如果完全一致，那么是一个容差类》
                    if (k == m) {
                        ToleranceClass.get(i).add(j);
                        ToleranceClass.get(j).add(i);
                    }
                }
            }
        }
        return ToleranceClass;
    }

    @Test
    public void tesGetToleranceClass(){
        ArrayList<Integer> p = new ArrayList<>();
        for(int i = 0; i < 4; i++){
            p.add(i);
        }
        ArrayList<ArrayList<Integer>> test = IRISSingleTolerance(DS,p);
        for (ArrayList<Integer> a:test
             ) {
            System.out.println(a);
        }
    }


    /**
     * 舒文豪提出的算法中的正域更新方法
     * @param originalSys 原始决策系统original_sys
     * @param originalReduct 原始正域约简original_reduct
     * @param originalPos 原始正域original_POS
     * @param addtionalObj 单一增量对象additional_obj
     * @return 新决策系统的正域 new_POS
     */
    public static ArrayList<Integer> getNewPos(RealMatrix originalSys, ArrayList<Integer> originalReduct, ArrayList<Integer> originalPos,
                                       RealMatrix addtionalObj){
        int n = originalSys.getRowDimension();
        int m = originalSys.getColumnDimension();
        ArrayList<Integer> p = new ArrayList<>(originalReduct);
        RealMatrix newSys = addARow(originalSys,addtionalObj);

        ArrayList<Integer> PToleranceClass = IRISSingleTolerance(newSys,p,n);//计算 加入的对象的容差类 （n）就是矩阵newSys的第(n+1)行 也就是新加的对象
        int LP = PToleranceClass.size();


        //求 P 容差类下对 D 的划分 === 不太懂
        ArrayList<Integer> genaralDecisionP = new ArrayList<>(LP);
        for(int i = 0; i < LP; i++){
            genaralDecisionP.add(i, (int)newSys.getEntry(PToleranceClass.get(i),m - 1));
        }

        //判断新增加对象是否属于正域
        int lengthDecisionP = getUnique(genaralDecisionP).size();
        ArrayList<Integer> inPos = new ArrayList<>();
        if(lengthDecisionP == 1)
            inPos.add(n+1);


        //判定新增对象的 P-容差类 中是否造成 原有正域中的 对象 出正域。
        ArrayList<Integer> outPos = new ArrayList<>();
        for(int j = 0; j < LP; j++){
            ArrayList<Integer> qToleranceClass = IRISSingleTolerance(newSys, p, PToleranceClass.get(j));
            int LQ = qToleranceClass.size();

            ArrayList<Integer> generalDecisionQ = new ArrayList<>(LQ);
            for(int i = 0; i < LQ; i++){
                generalDecisionQ.add(i,(int)newSys.getEntry(qToleranceClass.get(i),m - 1));
            }
            int lengthDecisionQ = getUnique(generalDecisionQ).size();
            if(lengthDecisionQ != 1)
                outPos.add(PToleranceClass.get(j));
        }
        ArrayList<Integer> newPos;
        originalPos.addAll(inPos);
        newPos = setDiff(originalPos, outPos);
        return newPos;
    }

    @Test
    public void testGetNewPos(){
        ArrayList<Integer> originalReduct = new ArrayList<>();
        originalReduct.add(2);
        originalReduct.add(3);

        ArrayList<Integer> originalPos = getPos(DS, originalReduct);
        double[][] addtionalArr = {{1 ,1 ,1 ,1 ,2}};
        RealMatrix addtionalObj = new Array2DRowRealMatrix(addtionalArr);

        ArrayList<Integer> newPos = getNewPos(DS, originalReduct, originalPos, addtionalObj);
        System.out.println(newPos);
    }

    /**
     * 计算决策系统ds的决策等价类 decisionClassEachObj
     * @param DS 决策系统DS
     * @return 决策系统的决策等价类 decisionClassEachObj
     */
    private static ArrayList<ArrayList<Integer>> findDecisionClass(RealMatrix DS){
        int n = DS.getRowDimension();
        int m = DS.getColumnDimension();
        RealMatrix decisionColumn = DS.getColumnMatrix(m-1).transpose();
        double[] decisionArr = decisionColumn.getRowMatrix(0).getData()[0];//取决策属性列 转换为数组

        LinkedHashSet<Double> decisionType = new LinkedHashSet<>();
        for (double d:
             decisionArr) {
            decisionType.add(d);
        }
        ArrayList<ArrayList<Integer>> decisionClass = new ArrayList<>();
        for (double d:
             decisionType) {
            ArrayList<Integer> tempDecisionSet = new ArrayList<>();
            decisionClass.add(tempDecisionSet);
        }
        for (int i = 0; i < decisionArr.length; i++){
            int k = 0;
            for (double d:
                 decisionType) {
                if(decisionArr[i]==d){
                    decisionClass.get(k).add(i);
                    break;
                }
                k++;
            }
        }
        ArrayList<ArrayList<Integer>> decisionClassEachObj = new ArrayList<>(n);
        for(int i = 0; i < n; i++){
            for (ArrayList<Integer> h:
                 decisionClass) {
                if(h.contains(i)){
                    decisionClassEachObj.add(h);
                    break;
                }
            }
        }
        return decisionClassEachObj;
    }

    /**
     * 每次添加一个 obj 所得到的 正域和约简 包装成一个对象返回
     */
    public static class ShuIarsParam{
        ArrayList<Integer> increPosReduct;
        ArrayList<Integer> newCPos;
        int NM;

        public ArrayList<Integer> getIncrePosReduct() {
            return increPosReduct;
        }
        public ArrayList<Integer> getNewCPos() {
            return newCPos;
        }
        public int getNM(){
            return NM;
        }
        ShuIarsParam(ArrayList<Integer> increPosReduct, ArrayList<Integer> newCPos, int NM){
            this.increPosReduct = increPosReduct;
            this.newCPos = newCPos;
            this.NM = NM;
        }
    }

    /**
     * 返回 A B两个数组队列的差集（在 A中有而在 B中没有的数组队列）
     * @param A 数组队列A
     * @param B 数组队列B
     * @return A - B
     */
    private static ArrayList<Integer> setDiff(ArrayList<Integer> A, ArrayList<Integer> B){
        ArrayList<Integer> diff = new ArrayList<>();
        for (int a:
                A) {
            int num = 0;
            for (int b:
                    B) {
                if(a != b)
                    num++;
            }
            if(num == B.size())
                diff.add(a);
        }
        diff = getUnique(diff);
//        Collections.sort(diff);
        return diff;
    }

    /**
     * 给初始矩阵添加一列返回一个新的矩阵
     * @param originMatrix 初始矩阵
     * @param addedRow 添加的矩阵
     * @return 初始矩阵添加一列生成的新的矩阵
     */
    private static RealMatrix addARow(RealMatrix originMatrix, RealMatrix addedRow){
        RealMatrix newMatrix = new Array2DRowRealMatrix(originMatrix.getRowDimension()+addedRow.getRowDimension(),
                originMatrix.getColumnDimension());
        for(int i = 0; i < originMatrix.getRowDimension(); i++){
            newMatrix.setRow(i , originMatrix.getRow(i));
        }
        int j = 0;
        for(int i = originMatrix.getRowDimension(); i < newMatrix.getRowDimension(); i++){
            newMatrix.setRow(i , addedRow.getRow(j));
            j++;
        }
        return newMatrix;
    }

    /**
     * 返回 数组队列去重后的 数组队列
     * @param arrayList 待去重的队列
     * @return 去重后的队列
     */
    private static ArrayList<Integer> getUnique(ArrayList<Integer> arrayList){
        HashSet<Integer> temp = new HashSet<>(arrayList);
        return new ArrayList<>(temp);
    }

}
