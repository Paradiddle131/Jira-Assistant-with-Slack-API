package jira.using_api;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;

public class ReadExcel {
    private XSSFWorkbook workbook;
    private XSSFSheet sheet;
    private FileInputStream stream;

    public HashMap<String, Integer> getPbiMap() throws IOException {
        readExcel();
        Iterator<Row> rowIterator = sheet.iterator();
        Row row = rowIterator.next(); // skips headers

        HashMap<String, Integer> map = new HashMap<>();
        while(rowIterator.hasNext()) {
            row = rowIterator.next();

            int storyPoint = (int) row.getCell(7).getNumericCellValue();
            String pbiName = row.getCell(2).getStringCellValue();
            map.put(pbiName, storyPoint);
        }
        workbook.close();
        stream.close();
        return map;
    }

    private void readExcel() throws IOException {
        File excel = new File("src/main/resources/sample.xlsx");
        stream = new FileInputStream(excel);
        workbook = new XSSFWorkbook(new FileInputStream(excel));
        sheet = workbook.getSheetAt(0);
    }
}
