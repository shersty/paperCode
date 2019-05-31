package cesudu;

import DataSet_package.IDS_PRIRA;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class testPRIRA {
    public static void doInit(String fileName,timeCounter timer){
        Thread timerListen = new Thread(new timerThread(timer));
        timerListen.setDaemon(true);
        timerListen.start();

        System.out.println("**********"+fileName+"上PRIRA的运行时间"+"***********");
        long times[] = new  long[8];
        int t = 0;

        RealMatrix allDsMatrix = readExcel(fileName);
        int totleRowNum = allDsMatrix.getRowDimension();
        int totleColNum = allDsMatrix.getColumnDimension();
        int percents = totleRowNum/10;
        int originalNum = percents*2;
        int addedNum = totleRowNum - originalNum;

        RealMatrix originalDsMatrix = allDsMatrix.getSubMatrix(0, originalNum - 1, 0, totleColNum - 1);
        RealMatrix addedDsMatrix = allDsMatrix.getSubMatrix(originalNum, totleRowNum - 1, 0, totleColNum - 1);

        ArrayList<Integer> posReduct = IDS_PRIRA.getPosReduct(originalDsMatrix);
        System.out.println("初始约简是：" + posReduct);
        ArrayList<Integer> originalReduct = new ArrayList<>(posReduct);
        ArrayList<Integer> originalPos = IDS_PRIRA.getPos(originalDsMatrix, posReduct);
        RealMatrix originalSys = originalDsMatrix;
        int K = 0;

        IDS_PRIRA.ShuIarsParam shuIarsParam = null;

        int addCounts = 0;
        long startTime = System.currentTimeMillis();
        for(int i = 0; i < addedNum; i++){
            RealMatrix addedObj = addedDsMatrix.getRowMatrix(i);
            shuIarsParam = IDS_PRIRA.shuIARS(originalSys, originalReduct, originalPos, addedObj);
            K = K + shuIarsParam.getNM();
            originalSys = addARow(originalSys, addedObj);
            originalReduct = shuIarsParam.getIncrePosReduct();
            originalPos = shuIarsParam.getNewCPos();
            addCounts++;
            if(addCounts % percents == 0){
                long endTime = System.currentTimeMillis();
                times[t++] = endTime - startTime;
            }
        }
        for (long time:times)
            System.out.print((double) time/1000+"s ");

        Collections.sort(shuIarsParam.getIncrePosReduct());
        System.out.println("\n"+fileName+"上PRIRA的约简为："+shuIarsParam.getIncrePosReduct()+"\n");

    }

    private static RealMatrix readExcel(String fileName) {
        RealMatrix dsMatrix = null;
        Sheet sheet;
        Row row;
        Workbook wb;

        String extString = fileName.substring(fileName.lastIndexOf("."));
        InputStream is;

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

    static RealMatrix addARow(RealMatrix originMatrix, RealMatrix addedRow){
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

}
