package com.example.video;

import jxl.CellView;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.format.UnderlineStyle;
import jxl.write.*;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Locale;

//Da para criar com tecnologias mais novas?????
@Component
public class WriteExcel {

    private WritableCellFormat timesBoldUnderline;
    private WritableCellFormat times;

    // Returns an InputStream that represents the Excel Report
    public java.io.InputStream exportExcel (List<FaceItems> list){
        try{
            return write(list);
        } catch (WriteException | IOException e){
            e.printStackTrace();
        }
        return null;
    }

    public InputStream write(List<FaceItems> list) throws IOException, WriteException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        WorkbookSettings workbookSettings = new WorkbookSettings();

        workbookSettings.setLocale(new Locale("en", "EN"));

        // Create a Workbook - pass the OutputStream
        WritableWorkbook workbook = Workbook.createWorkbook(outputStream, workbookSettings);

        //Need to get the WorkItem from each list
        workbook.createSheet("Video Analyzer Sheet", 0);
        WritableSheet excelSheet = workbook.getSheet(0);
        createLabel(excelSheet);
        createContent(excelSheet, list);

        //Close the workbook
        workbook.write();
        workbook.close();

        //Diferente do original
        // Get an InputStream that represents the Report
        byte[] myBytes = outputStream.toByteArray();

        return new ByteArrayInputStream(myBytes);
    }
    // Create Headings in the Excel spreadsheet
    private void createLabel(WritableSheet sheet) throws WriteException{
        // Create a times font
        WritableFont times10pt = new WritableFont(WritableFont.TIMES, 10);
        //Define the cell Format
        times = new WritableCellFormat(times10pt);
        //Lets automatically wrap the cells
        times.setWrap(true);

        //Create a bold font with underlines
        WritableFont times10ptBoldUnderline = new WritableFont(WritableFont.TIMES, 10, WritableFont.BOLD,false, UnderlineStyle.SINGLE);
        timesBoldUnderline = new WritableCellFormat(times10ptBoldUnderline);
        timesBoldUnderline.setWrap(true);

        //não faz nada???
        CellView cv = new CellView();
        cv.setFormat(times);
        cv.setFormat(timesBoldUnderline);
        cv.setAutosize(true);

        //Write a few headers
        addCaption(sheet, 0, "Age Range");
        addCaption(sheet, 1, "Beard");
        addCaption(sheet, 2, "Eye glasses");
        addCaption(sheet, 3, "Eyes open");
        addCaption(sheet, 4, "Mustache");
        addCaption(sheet, 4, "Smile");
    }

    // Write the Work Item Data to the Excel Report
    private void createContent(WritableSheet sheet, List<FaceItems> list) throws WriteException{

        int size = list.size();

        // list
        for (int i = 0; i < size; i++){
            FaceItems items = list.get(i);

            String age = items.getAgeRange();
            String beard = items.getBeard();
            String eyeglasses = items.getEyeglasses();
            String eyesOpen = items.getEyesOpen();
            String mustache = items.getMustache();
            String smile = items.getSmile();

            addLabel(sheet, 0, i + 2, age);
            addLabel(sheet, 1, i + 2, beard);
            addLabel(sheet, 2, i + 2, eyeglasses);
            addLabel(sheet, 3, i + 2, eyesOpen);
            addLabel(sheet, 4, i + 2, mustache);
            addLabel(sheet, 5, i + 2, smile);
        }
    }

    private void addCaption(WritableSheet sheet, int column, String s) throws WriteException {
        Label label;
        label = new Label(column, 0, s, timesBoldUnderline);

        int cc = countString(s);
        sheet.setColumnView(column, cc);
        sheet.addCell(label);
    }

    private void addLabel(WritableSheet sheet, int column, int row, String s) throws WriteException {
        Label label;
        label = new Label(column, row, s, times);
        int cc = countString(s);
        if (cc > 200)
            sheet.setColumnView(column, 150);
        else
            sheet.setColumnView(column, cc+6);

        sheet.addCell(label);

    }

    private int countString (String ss) {
        int count = 0;
        //Counts each character except space
        for(int i = 0; i < ss.length(); i++) {
            if(ss.charAt(i) != ' ')
                count++;
        }
        return count;
    }
}
























