package online.mdfactory.backend.excel;

import org.apache.commons.lang3.time.DurationFormatUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellUtil;
import org.apache.poi.xssf.usermodel.*;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.OptionalInt;

public class XlsGenerator {
    private final XSSFWorkbook workbook;
    private final XSSFSheet sheet;
    private int rowIndex = 0;

    public XlsGenerator(String sheetName) {
        this.workbook = new XSSFWorkbook();
        this.sheet = workbook.createSheet(sheetName);
    }

    public static class RowValue {
        private final int columnStart;
        private final int columnEnd;
        private final Object value;
        private final int numOfLines;

        public RowValue(int column, Object value) {
            this.columnStart = column - 1;
            this.columnEnd = column - 1;
            numOfLines = 1;
            this.value = value;
        }

        public RowValue(int start, int end, Object value) {
            this.columnStart = start - 1;
            this.columnEnd = end - 1;
            numOfLines = 1;
            this.value = value;
        }

        public RowValue(int start, int end, int numOfLines, Object value) {
            this.columnStart = start - 1;
            this.columnEnd = end - 1;
            this.numOfLines = Math.max(numOfLines, 1);
            this.value = value;
        }
    }

    public void skip(int lines) {
        rowIndex += lines;
    }

    public void writeLine(List<RowValue> values, boolean bold, int fontHeight) {
        XSSFRow row = sheet.createRow(rowIndex);
        int maxHeight = values.stream().mapToInt(v -> v.numOfLines).max().orElse(1);
        row.setHeightInPoints(row.getHeightInPoints() * maxHeight);
        XSSFCellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setBold(bold);
        font.setFontHeight(fontHeight);
        style.setFont(font);
        for (RowValue rowValue : values) {
            writeCell(row, rowValue, style);
            int firstCol = rowValue.columnStart;
            int lastCol = rowValue.columnEnd;
            if (Math.abs(firstCol - lastCol) < 2) continue;

            CellRangeAddress address = new CellRangeAddress(rowIndex, rowIndex, firstCol, lastCol);
            sheet.addMergedRegion(address);
        }
        rowIndex++;
    }

    private void writeCell(Row row, RowValue rowValue, CellStyle style) {
        sheet.autoSizeColumn(rowValue.columnStart);
        Cell cell = row.createCell(rowValue.columnStart);
        Object value = rowValue.value;
        if (value instanceof LocalDate) {
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd.MM.yyyy");
            cell.setCellValue(dtf.format((LocalDate) value));
        } else if (value instanceof LocalTime) {
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss");
            cell.setCellValue(dtf.format((LocalTime) value));
        } else if (value instanceof Duration) {
            long millis = ((Duration) value).toMillis();
            String formattedValue = DurationFormatUtils.formatDuration(millis, "H:mm:ss", true);
            cell.setCellValue(formattedValue);
        } else if (value instanceof Boolean) {
            cell.setCellValue((Boolean) value ? "да" : "нет");
        } else if (value instanceof String) {
            cell.setCellValue((String) value);
        }
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        cell.setCellStyle(style);
        CellUtil.setVerticalAlignment(cell, VerticalAlignment.CENTER);
        CellUtil.setAlignment(cell, HorizontalAlignment.CENTER);
    }

    public XSSFWorkbook get() {
        return workbook;
    }
}
