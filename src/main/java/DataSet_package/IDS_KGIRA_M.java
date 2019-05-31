package DataSet_package;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class IDS_KGIRA_M {
    int Un[][] = null;
    int Ux[][] = null;

    public IDS_KGIRA_M(int a[][], int b[][]) {
        Un = a;
        Ux = b;
    }

    //返回实例变量Un
    public int[][] getUn() {
        return Un;
    }

    //返回实例变量Ux
    public int[][] getUx() {
        return Ux;
    }

    //设置实例变量Un
    public void setUn(ArrayList<ArrayList<Integer>> nData) {
        int row = nData.size(), col = nData.get(0).size();
        Un = new int[row][col];
        for (int i = 0; i < row; i++)
            for (int j = 0; j < col; j++)
                Un[i][j] = nData.get(i).get(j);
    }

    //设置实例变量Ux
    public void setUx(ArrayList<ArrayList<Integer>> xData) {
        int row = xData.size(), col = xData.get(0).size();
        Ux = new int[row][col];
        for (int i = 0; i < row; i++)
            for (int j = 0; j < col; j++)
                Ux[i][j] = xData.get(i).get(j);
    }

    //构造函数，给两个实例变量赋值
    public IDS_KGIRA_M() {

    }

    //构造函数，给两个实例变量赋值
    public IDS_KGIRA_M(ArrayList<ArrayList<Integer>> orgData, ArrayList<ArrayList<Integer>> addData) {
        setUn(orgData);
        setUx(addData);
    }

    //求属性集P下的相容矩阵
    public int[][] get_ToleranceMatrix(int[][] Un, List<Integer> P) {
        int k = 0, n = Un.length, m = P.size();
        int ToleranceMatrix[][] = new int[n][n];
        int attribute[] = new int[m];
        //利用Iterator实现遍历
        int s = 0;
        Iterator<Integer> value = P.iterator();
        while (value.hasNext()) {
            attribute[s] = value.next();
            //System.out.print(attribute[s]+" ");
            s++;
        }

        for (int i = 0; i < n; i++)
            ToleranceMatrix[i][i] = 1;


        for (int i = 0; i < n; i++)
            for (int j = i + 1; j < n; j++) {
                k = 0;
                for (int v = 0; v < m; v++)
                    if ((Un[i][attribute[v]] != Un[j][attribute[v]]) && (Un[i][attribute[v]] != -1) && (Un[j][attribute[v]] != -1))
                        break;
                    else
                        k = k + 1;

                if (k == m) {
                    ToleranceMatrix[i][j] = 1;
                    ToleranceMatrix[j][i] = 1;
                }
            }

        return ToleranceMatrix;
    }

    //求决策系统DS在属性集P下的知识粒度GP
    public double get_GP(int[][] Un, List<Integer> P) {
        int n = Un.length;
        int ToleranceMatrix[][] = get_ToleranceMatrix(Un, P);
        int sum = 0;
        for (int i = 0; i < ToleranceMatrix.length; i++) {
            for (int j = 0; j < ToleranceMatrix[i].length; j++) {
                sum += ToleranceMatrix[i][j];
            }
        }
        return sum ;/// Math.pow(n, 2);
    }

    //求决策系统DS在属性集P下的相对知识粒度GP(D|P)
    public double get_GPRelative(int[][] Un, List<Integer> P) {
        int m = Un[0].length;//
        List<Integer> PUD = new ArrayList<Integer>();
        Iterator<Integer> value = P.iterator();
        while (value.hasNext()) {
            PUD.add(value.next());
        }

        PUD.add(m - 1);

//			double k1=get_GP(P);System.out.println("k1="+k1);
//			double k2=get_GP(PUD);System.out.println("k2="+k2);
        double GPRelative = get_GP(Un, P) - get_GP(Un, PUD);
        return GPRelative;//k1-k2;
    }

    //计算决策系统DS属性a对属性集P的外部重要性Sig_U_Outer(a,P,D)=GP(D|P)-GP(D|PU{a})
    public double get_SigInner(int[][] Un, int a, List<Integer> P) {
        List<Integer> P_D = new ArrayList<Integer>();

        Iterator<Integer> value = P.iterator();
        while (value.hasNext()) {
            int num = value.next();
            if (num != a)
                P_D.add(num);
        }
        double k = get_GPRelative(Un, P_D);
        double s = get_GPRelative(Un, P);
        return (k - s);
    }

    //计算决策系统DS属性a对属性集P的外部重要性Sig_U_Outer(a,P,D)=GP(D|P)-GP(D|PU{a})
    public double get_SigOuter(int[][] Un, int a, List<Integer> P) {
        double k = get_GPRelative(Un, P);

        List<Integer> PUa = new ArrayList<Integer>();
        PUa.addAll(P);
        PUa.add(a);

        double s = get_GPRelative(Un, PUa);
        return (k - s);
    }

    //计算决策系统DS增加对象集Ux后，在属性集P下，新增对象集Ux和DS中元素的容差矩阵。譬如|Ux|=s,|DS|=n,则所求容差矩阵维数为sxn
    public int[][] get_PTC(int Un[][], int Ux[][], List<Integer> P) {
        int k = 0, n = Un.length, s = Ux.length, m = P.size();
        int val_Ux = 0, val_Un = 0;
        int ToleranceMatrix[][] = new int[s][n];
        int attribute[] = new int[m];
        //利用Iterator实现遍历
        int w = 0;
        Iterator<Integer> value = P.iterator();
        while (value.hasNext()) {
            attribute[w] = value.next();
            w++;
        }

        for (int i = 0; i < s; i++) {
            for (int j = 0; j < n; j++) {
                k = 0;
                for (int v = 0; v < m; v++) {
                    val_Ux = Ux[i][attribute[v]];
                    val_Un = Un[j][attribute[v]];
                    if ((val_Ux != val_Un) && (val_Ux != -1) && (val_Un != -1))
                        break;
                    else
                        k = k + 1;
                }
                if (k == m) {
                    ToleranceMatrix[i][j] = 1;
                }
            }
        }
        return ToleranceMatrix;
    }

    //计算增加对象集Ux在属性集P下的容差矩阵
    public int[][] get_QUxTC(int Un[][], int Ux[][], List<Integer> P) {
        int n = Ux.length, k = 0;
        int m = P.size();
        int QUxTC[][] = new int[n][n];
        int val_i = 0, val_j = 0;

        int attribute[] = new int[m];
        int w = 0;
        Iterator<Integer> value = P.iterator();
        while (value.hasNext()) {
            attribute[w] = value.next();
            w++;
        }

        for (int i = 0; i < n; i++)
            QUxTC[i][i] = 1;


        for (int i = 0; i < n; i++)
            for (int j = i + 1; j < n; j++) {
                k = 0;
                for (int v = 0; v < m; v++) {
                    val_i = Ux[i][attribute[v]];
                    val_j = Ux[j][attribute[v]];
                    if ((val_i != val_j) && (val_i != -1) && (val_j != -1))
                        break;
                    else
                        k = k + 1;
                }
                if (k == m) {
                    QUxTC[i][j] = 1;
                    QUxTC[j][i] = 1;
                }
            }
        return QUxTC;
    }

    //计算决策系统DS和新增对象集Ux属性集C相对于决策属性D的知识粒度GP_UUUx(D|C)=GP(P)
    public double get_UUUxRelative(int Un[][], int Ux[][], List<Integer> P) {

        int n = Un.length, m = Un[0].length;
        List<Integer> D = new ArrayList<Integer>();
        D.add(m - 1);//决策属性序号

        int s = Ux.length;
        int PTC[][] = get_PTC(Un, Ux, P);//计算DS和新增对象集Ux在属性集P下的相容矩阵
        int PTD[][] = get_PTC(Un, Ux, D);//计算DS和新增对象集Ux在决策属性D下的相容矩阵
        int PTCUD[][] = new int[s][n];//计算DS和新增对象集Ux在属性集CUD下的相容矩阵
        for (int i = 0; i < s; i++)
            for (int j = 0; j < n; j++)
                PTCUD[i][j] = PTC[i][j] & PTD[i][j];

        int QTC[][] = get_QUxTC(Un, Ux, P);//计算新增对象集Ux在属性集P下的相容矩阵
        int QTD[][] = get_QUxTC(Un, Ux, D);//计算新增对象集Ux在决策属性D下的相容矩阵
        int QTCUD[][] = new int[s][s];//计算新增对象集Ux在属性集CUD下的相容矩阵
        for (int i = 0; i < s; i++)
            for (int j = 0; j < s; j++)
                QTCUD[i][j] = QTC[i][j] & QTD[i][j];

        double UUUxRelative = (Math.pow(n, 2) * get_GPRelative(Un, P) + Math.pow(s , 2) * (sum(QTC) - sum(QTCUD)) + 2 * (sum(PTC) - sum(PTCUD)));// / Math.pow(n + s, 2);
        return UUUxRelative;
    }

    double sum(int a[][]) {
        double sum1 = 0;
        for (int i = 0; i < a.length; i++)
            for (int j = 0; j < a[i].length; j++)
                sum1 += a[i][j];

        return sum1;
    }

    //计算决策系统DS和新增对象集Ux的属性a对属性集P的外部重要性Sig_UUUx_Outer(a,P,D)=GP(D|P)-GP(D|PU{a})
    public double get_UUUxSigOuter(int Un[][], int Ux[][], int a, List<Integer> P) {
        int n = Un.length, m = Un[0].length;
        int s = Ux.length;
        List<Integer> D = new ArrayList<Integer>();
        D.add(m - 1);//决策属性序号

        int PTC[][] = get_PTC(Un, Ux, P);//计算DS和新增对象集Ux在属性集P下的相容矩阵
        int PTD[][] = get_PTC(Un, Ux, D);//计算DS和新增对象集Ux在决策属性D下的相容矩阵
        int PTCUD[][] = new int[s][n];//计算DS和新增对象集Ux在属性集CUD下的相容矩阵
        for (int i = 0; i < s; i++)
            for (int j = 0; j < n; j++)
                PTCUD[i][j] = PTC[i][j] & PTD[i][j];

        List<Integer> PUa = new ArrayList<Integer>();
        PUa.addAll(P);
        PUa.add(a);

        int PTPUa[][] = get_PTC(Un, Ux, PUa);//计算DS和新增对象集Ux在属性集PUa下的相容矩阵
        int PTPUaUD[][] = new int[s][n];//计算DS和新增对象集Ux在属性集PUaUD下的相容矩阵
        for (int i = 0; i < s; i++)
            for (int j = 0; j < n; j++)
                PTPUaUD[i][j] = PTPUa[i][j] & PTD[i][j];

        double UUUxSigOuter = (Math.pow(n, 2) * get_SigOuter(Un, a, P) + Math.pow(s, 2) * get_SigOuter(Ux, a, P) + 2 * (sum(PTC) - sum(PTCUD)) - 2 * (sum(PTPUa) - sum(PTPUaUD))) ;/// ((n + s) ^ 2);

        return UUUxSigOuter;
    }

    //增量式计算求约简KGIRA
    public List<Integer> KGIRA(int Un[][], List<Integer> SReduct, int Ux[][]) {
        List<Integer> B = new ArrayList<Integer>();
        B.addAll(SReduct);
        int m = Un[0].length;    //System.out.println(m);

        List<Integer> P = new ArrayList<Integer>();
        List<Integer> C = new ArrayList<Integer>();
        for (int i = 0; i < m - 1; i++)
            P.add(i);

        C.addAll(P);
        double new_GPRelativeC = get_UUUxRelative(Un, Ux, C);//计算GP_UUUx(D|C)
        double new_GPRelativeB = get_UUUxRelative(Un, Ux, B);//计算GP_UUUx(D|B)

        if (new_GPRelativeC == new_GPRelativeB)
            return B;

        //do{
        while (new_GPRelativeC != get_UUUxRelative(Un, Ux, B)){
            List<Integer> temp = new ArrayList<Integer>();
            temp.addAll(P);
            temp.removeAll(B);
            Iterator<Integer> v1 = temp.iterator();
            double preSigOuter = Double.NEGATIVE_INFINITY;
            int prenum = -1;
            while (v1.hasNext()) {
                int num = v1.next();
                double gm = get_UUUxSigOuter(Un, Ux, num, B);//System.out.print(num+"*"+gm+"///");
                if (preSigOuter < gm) {
                    prenum = num;
                    preSigOuter = gm;
                }
            }
            B.add(prenum);
        } //while (new_GPRelativeC != get_UUUxRelative(Un, Ux, B));

//        System.out.println("此次增量计算后的约简：" + B);

        //约简最小化
        List<Integer> temp2 = new ArrayList<Integer>();
        temp2.addAll(B);
        Iterator<Integer> v2 = temp2.iterator();
        List<Integer> temp3 = new ArrayList<Integer>();
        while (v2.hasNext()) {
            Object objnum = v2.next();
            temp3.addAll(B);
            temp3.remove(objnum);
            if (get_UUUxRelative(Un, Ux, temp3) == new_GPRelativeC){
                System.out.println("移除属性：" + objnum);
                B.remove(objnum);
            }
            temp3.clear();
        }

        return B;
    }
}