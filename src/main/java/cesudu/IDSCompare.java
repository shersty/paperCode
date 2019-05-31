package cesudu;


public class IDSCompare {

    public static synchronized void main(String[] args) {

        for (int i = 1; i < 8; i++) {
            String fileName;

            if (i != 8 && i != 20)
                fileName = "DataSet/data" + i + ".xls";
            else
                fileName = "DataSet/data" + i + ".xlsx";

//            test_THAV00.doit(fileName);
//            test_KGIRA.doit(fileName,new timeCounter(true));
//            test_KGIRAV2.doit(fileName,new timeCounter(true));
//            test_KGIRAV00.doit(fileName);
            test_KGIRAV02.doit(fileName);
            test_KGIRA_M.doit(fileName);
//            test_POSA.doit(fileName);
//            test_POSD.doit(fileName);
//            test_THA.doit(fileName,new timeCounter(true));
//            testPRIRA.doInit(fileName, new timeCounter(true));
//            test_THAV02.doit(fileName);
            System.out.println("---------------------------------------------------------------------------------");
            System.out.println("---------------------------------------------------------------------------------");
            System.out.println();
        }
//		test_KGIRA.doit("saveResult\\SCADI-Dataset.xlsx");
//		test_KGIRA.doit("SaveRandom\\RSCADI-Dataset.xlsx");
    }
}
