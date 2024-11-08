package utc.edu.thesis.common;

import org.apache.commons.io.FilenameUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.JSONArray;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.Iterator;

public class ExcelCM {

    public static JSONArray readExcelAll(MultipartFile uploadfiles) throws IOException {
        JSONArray dataAll = new JSONArray();

        Workbook workbook = null;
        InputStream inputStream = null;

        try {
            String extension = FilenameUtils.getExtension(uploadfiles.getOriginalFilename());
            inputStream = uploadfiles.getInputStream();

            // Get workbook
            workbook = getWorkbook(inputStream, extension);

            // Get sheet
            dataAll = readWorkbook(workbook, 0);
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (workbook != null) {
                workbook.close();
            }

            if (inputStream != null) {
                inputStream.close();
            }
        }

        return dataAll;
    }

    public static JSONArray readExcelAll(File file) throws IOException {
        JSONArray dataAll = new JSONArray();

        Workbook workbook = null;
        InputStream inputStream = null;

        try {
            String extension = FilenameUtils.getExtension(file.getName());
            inputStream = new FileInputStream(file);

            // Get workbook
            workbook = getWorkbook(inputStream, extension);

            // Get sheet
            dataAll = readWorkbook(workbook, 0);
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (workbook != null) {
                workbook.close();
            }

            if (inputStream != null) {
                inputStream.close();
            }
        }

        return dataAll;
    }

    public static JSONArray readExcelAll(String pathFile) throws IOException {
        JSONArray dataAll = new JSONArray();

        Workbook workbook = null;
        InputStream inputStream = null;

        try {
            File file = new File(pathFile);
            String extension = FilenameUtils.getExtension(file.getName());
            inputStream = new FileInputStream(file);

            // Get workbook
            workbook = getWorkbook(inputStream, extension);

            // Get sheet
            dataAll = readWorkbook(workbook, 0);
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (workbook != null) {
                workbook.close();
            }

            if (inputStream != null) {
                inputStream.close();
            }
        }

        return dataAll;
    }

    public static JSONArray readWorkbook(Workbook workbook, int sheetTab) {
        JSONArray dataAll = new JSONArray();

        // Get sheet
        Sheet sheet = workbook.getSheetAt(sheetTab);

        Iterator<Row> iterator = sheet.iterator();
        int maxCellTitle = 0;
        while (iterator.hasNext()) {
            Row nextRow = iterator.next();
            if (nextRow.getRowNum() == 0) {
                maxCellTitle = nextRow.getPhysicalNumberOfCells();
                continue;
            }

            boolean flagRowNone = false;
            JSONArray jRow = new JSONArray();
            for (int i = 0; i < maxCellTitle; i++) {
                String cellValue = "";

                Cell cell = nextRow.getCell(i, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
                if (cell == null) {
                    cellValue = "";
                } else {
                    switch (cell.getCellType()) {
                        case FORMULA -> cellValue = String.valueOf(cell.getCellFormula());
                        case BOOLEAN -> cellValue = String.valueOf(cell.getBooleanCellValue());
                        case NUMERIC ->
                                cellValue = new BigDecimal(String.valueOf(cell.getNumericCellValue())).toString();
                        case STRING -> cellValue = cell.getStringCellValue();
                        default -> cellValue = "";
                    }
                }

                if (!cellValue.equals("")) {
                    flagRowNone = true;
                }

                if (flagRowNone) {
                    jRow.put(cellValue);
                }
            }

            if (jRow.length() > 0) {
                dataAll.put(jRow);
            }
        }

        return dataAll;
    }

    private static Workbook getWorkbook(InputStream inputStream, String extension) throws IOException {
        Workbook workbook = null;

        if (extension.endsWith("xlsx")) {
            workbook = new XSSFWorkbook(inputStream);
        } else if (extension.endsWith("xls")) {
            workbook = new HSSFWorkbook(inputStream);
        } else {
            throw new IllegalArgumentException("The specified file is not Excel file");
        }

        return workbook;
    }


}
