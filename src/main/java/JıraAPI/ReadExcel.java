package JıraAPI;

import Objects.PBI;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

public class ReadExcel {
    private XSSFWorkbook workbook;
    private XSSFSheet sheet;
    private FileInputStream stream;

    public ArrayList<PBI> getPbiFromExcel() throws IOException {
        readExcel();
        Iterator<Row> rowIterator = sheet.iterator();
        Row row = rowIterator.next(); // skips headers
        ArrayList<PBI> pbiList = new ArrayList<>();

        while(rowIterator.hasNext()) {
            row = rowIterator.next();
            pbiList.add(new PBI(
                    row.getCell(0).getStringCellValue(),
                    (int) row.getCell(1).getNumericCellValue(),
                    row.getCell(2).getStringCellValue(),
                    row.getCell(3).getStringCellValue(),
                    row.getCell(4).getStringCellValue(),
                    row.getCell(5).getStringCellValue(),
                    (int) row.getCell(7).getNumericCellValue()
            ));
        }
        workbook.close();
        stream.close();
        return pbiList;
    }

    private void readExcel() throws IOException {
        File excel = new File("src/main/resources/sample.xlsx");
        stream = new FileInputStream(excel);
        workbook = new XSSFWorkbook(new FileInputStream(excel));
        sheet = workbook.getSheetAt(0);
    }
}
