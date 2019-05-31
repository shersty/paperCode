package DataSet_package;

import com.sun.tools.javadoc.DocImpl;
import com.sun.xml.internal.bind.v2.model.core.ID;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Test;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;

public class testIdsPrira {
    @Test
    public void testReadExcel(){
        for(int i = 1; i < 10; i++){
            String filePre = "/home/mi/文档/bishe/论文/program/buildExcelFinall/src/main/resources/data";
            String filename;
//            if(i != 9)
//                continue;
            if(i == 8)
                filename = filePre + i + ".xlsx";
            else
                filename = filePre + i + ".xls";
            RealMatrix dsMatrix = readExcel(filename);
//        RealMatrix dsMatrix = readExcel("/home/mi/文档/bishe/论文/program/buildExcelFinall/src/main/resources/test.xlsx");
//        System.out.println(dsMatrix);
//        System.out.println("行数是：" + dsMatrix.getRowDimension());
//        System.out.println("列数是：" + dsMatrix.getColumnDimension());
            int percents = dsMatrix.getRowDimension()/10;
            int origin = percents*2;
            int add = dsMatrix.getRowDimension() - origin;
            RealMatrix originMatrix = dsMatrix.getSubMatrix(0,origin - 1,0, dsMatrix.getColumnDimension() - 1);
            RealMatrix addMatrix = dsMatrix.getSubMatrix(origin, dsMatrix.getRowDimension() - 1,0, dsMatrix.getColumnDimension() - 1);
            System.out.println("**********data"+i+"上PRIRA的运行时间"+"***********");
            ArrayList<Integer> increPosReduct = IDS_PRIRA.shuExperiment1(originMatrix, addMatrix);
            System.out.println("\n"+"RoughSetDataSet/data" + i + ".xls上PRIRA的约简为：" + increPosReduct);
            System.out.println("---------------------------------------------------------------------------------");
            System.out.println();
        }
    }

    @Test
    public void testGetPos(){
        RealMatrix dsMatrix = readExcel("/home/mi/文档/bishe/论文/program/buildExcelFinall/src/main/resources/test.xlsx");
        ArrayList<Integer> p = new ArrayList<>();
        for(int i = 0; i < 5; i++){
            p.add(i);
        }
        System.out.println(p);
        ArrayList<Integer> pos = IDS_PRIRA.getPos(dsMatrix, p);
        System.out.println(pos);
    }

    @Test
    public void testGetPosReduct(){
        RealMatrix dsMatrix = readExcel("/home/mi/文档/bishe/论文/program/buildExcelFinall/src/main/resources/data1.xls");
//        RealMatrix dsMatrix = readExcel("/home/mi/文档/bishe/论文/program/buildExcelFinall/src/main/resources/test.xlsx");
        System.out.println(dsMatrix);
        System.out.println("行数是：" + dsMatrix.getRowDimension());
        System.out.println("列数是：" + dsMatrix.getColumnDimension());
        int percents = dsMatrix.getRowDimension()/10;
        int origin = percents*8;
        int add = dsMatrix.getRowDimension() - origin;
        System.out.println("作为原始数据的是：" + origin);
        System.out.println("作为增量数据的是：" + add);
        RealMatrix originMatrix = dsMatrix.getSubMatrix(0,origin - 1,0, dsMatrix.getColumnDimension() - 1);
        RealMatrix addMatrix = dsMatrix.getSubMatrix(origin, dsMatrix.getRowDimension() - 1,0, dsMatrix.getColumnDimension() - 1);
        System.out.println("原始数据矩阵是：" + originMatrix);
        System.out.println("增量数据矩阵是：" + addMatrix);

        ArrayList<Integer> originalPosReduct = IDS_PRIRA.getPosReduct(originMatrix);
        System.out.println("originalPosReduct:" + originalPosReduct) ;

        ArrayList<Integer> originalPos = IDS_PRIRA.getPos(originMatrix, originalPosReduct);
        System.out.println("originalPos:" + originalPos);

        ArrayList<Integer> newPos = IDS_PRIRA.getNewPos(originMatrix,originalPosReduct,originalPos,addMatrix.getRowMatrix(0));
        System.out.println("newPos:" + newPos);

        IDS_PRIRA.ShuIarsParam shuIarsParam = IDS_PRIRA.shuIARS(originMatrix,originalPosReduct,originalPos,addMatrix.getRowMatrix(0));
        System.out.println("increPosReduct:" + shuIarsParam.increPosReduct);
        System.out.println("tempPos:" + shuIarsParam.newCPos);
        System.out.println("NM:"+shuIarsParam.NM);
    }

    @Test
    public void testTemp(){
        int[] test = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, 62, 63, 64, 65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90, 91, 92, 93, 94, 95, 96, 97, 98, 99, 100, 101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 111, 112, 113, 114, 115, 116, 117, 118, 119, 120, 121, 122, 123, 124, 125, 126, 127, 128, 129, 130, 131, 132, 133, 134, 135, 136, 137, 138, 139, 140, 141, 142, 143, 144, 145, 146, 147, 148, 149, 150, 151, 152, 153, 154, 155, 156, 157, 158, 159, 160, 161, 162, 163, 164, 165, 166, 167, 168, 169, 170, 171, 172, 173, 174, 175, 176, 177, 178, 179, 180, 181, 182, 183, 184, 185, 186, 187, 188, 189, 190, 191, 192, 193, 194, 195, 196, 197, 198, 199, 200, 201, 202, 203, 204, 205, 206, 207, 208, 209, 210, 211, 212, 213, 214, 215, 216, 217, 218, 219, 220, 221, 222, 223, 224, 225, 226, 227, 228, 229, 230, 231, 232, 233, 234, 235, 236, 237, 238, 239, 240, 241, 242, 243, 244, 245, 246, 247, 248, 249, 250, 251, 252, 253, 254, 255, 256, 257, 258, 259, 260, 261, 262, 263, 264, 265, 266, 267, 268, 269, 270, 271, 272, 273, 274, 275, 276, 277, 278, 279, 280, 281, 282, 283, 284, 285, 286, 287, 288, 289, 290, 291, 292, 293, 294, 295, 296, 297, 298, 299, 300, 301, 302, 303, 304, 305, 306, 307, 308, 309, 310, 311, 312, 313, 314, 315, 316, 317, 318, 319, 320, 321, 322};
        int[] test2 = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, 62, 63, 64, 65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90, 91, 92, 93, 94, 95, 96, 97, 98, 99, 100, 101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 111, 112, 113, 114, 115, 116, 117, 118, 119, 120, 121, 122, 123, 124, 125, 126, 127, 128, 129, 130, 131, 132, 133, 134, 135, 136, 137, 138, 139, 140, 141, 142, 143, 144, 145, 146, 147, 148, 149, 150, 151, 152, 153, 154, 155, 156, 157, 158, 159, 160, 161, 162, 163, 164, 165, 166, 167, 168, 169, 170, 171, 172, 173, 174, 175, 176, 177, 178, 179, 180, 181, 182, 183, 184, 185, 186, 187, 188, 189, 190, 191, 192, 193, 194, 195, 196, 197, 198, 199, 200, 201, 202, 203, 204, 205, 206, 207, 208, 209, 210, 211, 212, 213, 214, 215, 216, 217, 218, 219, 220, 221, 222, 223, 224, 225, 226, 227, 228, 229, 230, 231, 232, 233, 234, 235, 236, 237, 238, 239, 240, 241, 242, 243, 244, 245, 246, 247, 248, 249, 250, 251, 252, 253, 254, 255, 256, 257, 258, 259, 260, 261, 262, 263, 264, 265, 266, 267, 268, 269, 270, 271, 272, 273, 274, 275, 276, 277, 278, 279, 280, 281, 282, 283, 284, 285, 286, 287, 288, 289, 290, 291, 292, 293, 294, 295, 296, 297, 298, 299, 300, 301, 302, 303, 304, 305, 306, 307, 308, 309, 310, 311, 312, 313, 314, 315, 316, 317, 318, 319, 320, 321, 322, 323};
        System.out.println(test.length);
        System.out.println(test2.length);
    }

    public static RealMatrix readExcel(String fileName) {
        RealMatrix dsMatrix = null;
        Sheet sheet = null;
        Row row = null;
        Workbook wb = null;

        String extString = fileName.substring(fileName.lastIndexOf("."));
        System.out.println(extString);
        System.out.println(fileName);
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
                dsMatrix = new Array2DRowRealMatrix(rownum,colnum);
                for (int i = 0; i < rownum; i++) {
                    ArrayList<Double> temp_arrRow=new ArrayList<Double>();
                    row = sheet.getRow(i);
                    if (row != null) {
                        for (int j = 0; j < colnum; j++) {
                            Cell cellinfo=row.getCell(j);//System.out.println(j);
                            if (cellinfo==null)
                                dsMatrix.setEntry(i,j,Double.NaN);
                            else
                                dsMatrix.setEntry(i,j,cellinfo.getNumericCellValue());
                        }
                    } else {
                        break;
                    }
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return dsMatrix;
    }

    @Test
    public void test(){
        int n = 156;
        String[] objs = Integer.toString(n).split("");
        HashSet<Integer> hashSet = new HashSet<>();
        hashSet.add(1);
        hashSet.add(2);
        hashSet.add(5);
        hashSet.add(4);
    }

}

