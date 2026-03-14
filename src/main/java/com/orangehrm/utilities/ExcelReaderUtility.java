package com.orangehrm.utilities;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ExcelReaderUtility {

    public static List<String[]> getSheetData(String filePath, String sheetName){
        List<String[]> data = new ArrayList<String[]>();

        try (FileInputStream fis = new FileInputStream(filePath);
             Workbook workbook = new XSSFWorkbook(fis)
        ){
            Sheet sheet = workbook.getSheet(sheetName);
            if(sheet == null){
                throw new IllegalArgumentException("Excel sheet "+sheetName+" does not exist");
            }
            for(Row row: sheet){
                if(row.getRowNum() == 0){
                    continue;
                }
                //Read the row data
                List<String> rowData = new ArrayList<String>();
                for(Cell cell: row){
                    rowData.add(getCellValue(cell));
                }
                //convert row data to String Array
                data.add(rowData.toArray(new String[0]));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return data;
    }


    private static String getCellValue(Cell cell){
        if(cell == null){
            return "";
        }
        CellType type = cell.getCellType();
        switch(type) {
            case STRING:  return cell.getStringCellValue();
            case NUMERIC:
                if(DateUtil.isCellDateFormatted(cell)){
                    return String.valueOf(cell.getDateCellValue());
                }
                return String.valueOf((int)cell.getNumericCellValue());
            case BOOLEAN: cell.getBooleanCellValue();
            default: return "";
        }
    }

}
