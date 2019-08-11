package com.ikkong.utils;

import java.io.File;
import java.io.FileInputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.springframework.web.multipart.MultipartFile;
/**
 *
 * @author XiongYC
 * @date 2017年11月2日
 *
 */
public class ImportExcelUntil{

    /**
     * 拼装单个obj
     * @param fields
     * @param row
     * @return
     * @throws Exception
     */
    private  static  Map<String, String>  dataObj( List<String> fields, HSSFRow row) throws Exception {

        if (fields == null || fields.size() < 1) {
            return null;
        }

        //容器
        Map<String, String> map = new HashMap<String, String>();

        //注意excel表格字段顺序要和obj字段顺序对齐 （如果有多余字段请另作特殊下标对应处理）
        for (int j = 0; j < fields.size(); j++) {
            map.put(fields.get(j), getVal(row.getCell(j)));
        }
        return map;
    }

    public  static   List<Map<String, String>> importExcel(File file, List<String> fields) throws Exception {

        //装载流
        POIFSFileSystem fs = new POIFSFileSystem(new FileInputStream(file));
        HSSFWorkbook hw= new HSSFWorkbook(fs);

        //获取第一个sheet页
        HSSFSheet sheet = hw.getSheetAt(0);

        //容器
        List<Map<String, String>> ret = new ArrayList<Map<String, String>>();

        //遍历行 从下标第一行开始（去除标题）
        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            HSSFRow row= sheet.getRow(i);
            if(row!=null){
                //装载obj
                ret.add(dataObj(fields,row));
            }
        }
        return ret;
    }

    /**
     * 处理val（暂时只处理string和number，可以自己添加自己需要的val类型）
     * @param hssfCell
     * @return
     */
    public static String getVal(HSSFCell hssfCell) {
        //return  hssfCell.getStringCellValue();
        if (HSSFCell.CELL_TYPE_STRING==hssfCell.getCellType( )) {
            return hssfCell.getStringCellValue();
        } else {
            return String.valueOf(hssfCell.getNumericCellValue());
        }
    }
}

