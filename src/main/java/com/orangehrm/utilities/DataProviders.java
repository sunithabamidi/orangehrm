package com.orangehrm.utilities;

import org.testng.annotations.DataProvider;

import java.util.List;

public class DataProviders {
    private static final String FILE_PATH = System.getProperty("user.dir")+"/src/test/resources/testdata/testdata.xlsx";

    @DataProvider (name = "validLogin")
    public static Object[][] getValidData()
    {
        return getSheetData("validLoginTest");
    }

    @DataProvider (name = "invalidLogin")
    public static Object[][] getInvalidData(){
        return getSheetData("invalidLoginTest");
    }

    private static Object[][] getSheetData(String sheetName){
       List<String[]> sheetData = ExcelReaderUtility.getSheetData(FILE_PATH, sheetName);

       Object[][] data = new Object[sheetData.size()][sheetData.get(0).length];

       for(int i=0; i< sheetData.size(); i++){
           data[i]= sheetData.get(i);
        }

       return data;
    }
}
