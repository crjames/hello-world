package com.ctrip.tingting.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import redis.clients.jedis.Jedis;

import com.ctrip.tingting.bean.CityKeyEntity;

public class ReadKeyFromExcelUtil {
	
	/**
     * 读取excel中的数据
     * @param path
     * @return List<CityKeyEntity>
     */
    public List<CityKeyEntity> readExcel(String path) {

        if (path != null && !path.equals("")) {
            String ext = getExt(path);
            if (ext!=null && !ext.equals("")) {
                if (ext.equals("xls")) {
                    return readXls(path);
                } else if (ext.equals("xlsx")) {
                    return readXlsx(path);
                }
            }
        }
        return new ArrayList<CityKeyEntity>();
    }

    /**
     * 读取后缀为xls的excel文件的数据
     * @param path
     * @return List<CityKeyEntity>
     */
    private List<CityKeyEntity> readXls(String path) {

        HSSFWorkbook hssfWorkbook = null;
        try {
            InputStream is = new FileInputStream(path);
            hssfWorkbook = new HSSFWorkbook(is);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        CityKeyEntity cityKey = null;
        List<CityKeyEntity> list = new ArrayList<CityKeyEntity>();
        if (hssfWorkbook != null) {
            // Read the Sheet
            for (int numSheet = 0; numSheet < hssfWorkbook.getNumberOfSheets(); numSheet++) {
                HSSFSheet hssfSheet = hssfWorkbook.getSheetAt(numSheet);
                if (hssfSheet == null) {
                    continue;
                }
                // Read the Row
                for (int rowNum = 1; rowNum <= hssfSheet.getLastRowNum(); rowNum++) {
                    HSSFRow hssfRow = hssfSheet.getRow(rowNum);
                    if (hssfRow != null) {
                    	cityKey = new CityKeyEntity();
                        HSSFCell city = hssfRow.getCell(0);
                        HSSFCell key = hssfRow.getCell(1);
                        cityKey.setCity(getValue(city));
                        cityKey.setCity_Key(getValue(key));
                        list.add(cityKey);
                    }
                }
            }
        }
        return list;
    }

    /**
     * 读取后缀为xlsx的excel文件的数据
     * @param path
     * @return List<CityKeyEntity>
     */
    private List<CityKeyEntity> readXlsx(String path) {

        XSSFWorkbook xssfWorkbook = null;
        try {
            InputStream is = new FileInputStream(path);
            xssfWorkbook = new XSSFWorkbook(is);
        } catch (IOException e) {
            e.printStackTrace();
        }
        CityKeyEntity cityKey = null;
        List<CityKeyEntity> list = new ArrayList<CityKeyEntity>();
        if(xssfWorkbook!=null){
            // Read the Sheet
            for (int numSheet = 0; numSheet < xssfWorkbook.getNumberOfSheets(); numSheet++) {
                XSSFSheet xssfSheet = xssfWorkbook.getSheetAt(numSheet);
                if (xssfSheet == null) {
                    continue;
                }
                // Read the Row
                for (int rowNum = 1; rowNum <= xssfSheet.getLastRowNum(); rowNum++) {
                    XSSFRow xssfRow = xssfSheet.getRow(rowNum);
                    if (xssfRow != null) {
                        cityKey = new CityKeyEntity();
                        XSSFCell city = xssfRow.getCell(0);
                        XSSFCell key = xssfRow.getCell(1);
                        cityKey.setCity(getValue(city));
                        cityKey.setCity_Key(getValue(key));
                        list.add(cityKey);
                    }
                }
            }
        }
        return list;
    }

    /**
     * 获取文件扩展名
     * @param path
     * @return String
     */
    private String getExt(String path) {
        if (path == null || path.equals("") || !path.contains(".")) {
            return null;
        } else {
            return path.substring(path.lastIndexOf(".") + 1, path.length());
        }
    }


    /**
     * 判断后缀为xlsx的excel文件的数据类型
     * @param xssfRow
     * @return String
     */
    @SuppressWarnings("static-access")
    private String getValue(XSSFCell xssfRow) {
        if (xssfRow.getCellType() == xssfRow.CELL_TYPE_BOOLEAN) {
            return String.valueOf(xssfRow.getBooleanCellValue());
        } else if (xssfRow.getCellType() == xssfRow.CELL_TYPE_NUMERIC) {
            return String.valueOf(xssfRow.getNumericCellValue());
        } else {
            return String.valueOf(xssfRow.getStringCellValue());
        }
    }

    /**
     * 判断后缀为xls的excel文件的数据类型
     * @param hssfCell
     * @return String
     */
    @SuppressWarnings("static-access")
    private String getValue(HSSFCell hssfCell) {
        if (hssfCell.getCellType() == hssfCell.CELL_TYPE_BOOLEAN) {
            return String.valueOf(hssfCell.getBooleanCellValue());
        } else if (hssfCell.getCellType() == hssfCell.CELL_TYPE_NUMERIC) {
            return String.valueOf(hssfCell.getNumericCellValue());
        } else {
            return String.valueOf(hssfCell.getStringCellValue());
        }
    }

    public static void main(String[] args) {
    	Jedis jedis = new Jedis("localhost");
//    	List<CityKeyEntity> list = new ReadKeyFromExcelUtil().readExcel("d:/Users/wang.donga/Desktop/口袋导游语音讲解/2017-08-24/keys.xlsx");
//        System.out.println(list.size());
//        if(list!=null && list.size()>0){
//        	for (CityKeyEntity cityKey : list) {
//				jedis.set(cityKey.getCity(), cityKey.getCity_Key());
//			}
//        }
    	System.out.println(jedis.get("重庆"));
    }
}
