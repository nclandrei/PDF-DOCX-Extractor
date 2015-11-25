/**
 * Created by richy734 on 19/11/15.
 */

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;

import java.io.FileInputStream;
import java.io.IOException;

public class testDoc {

    public static void main(String args[]) {
        String fileName = "res/AnnandaleandEskdale-ProfileDraft1306.docx";
        XWPFDocument testFile = null;
        try {
            testFile = new XWPFDocument(new FileInputStream(fileName));
        } catch (IOException e) {
            System.out.printf("ERROR: File <%s> not found\n", fileName);
            e.printStackTrace();
            return;
        }

        for(XWPFTable table: testFile.getTables()){
            for(XWPFTableRow row:table.getRows()){
                for(XWPFTableCell cell: row.getTableCells()){
                    if(!cell.getText().equals(" ") && !cell.getText().equals(""))
                        System.out.print(cell.getText()+", ");
                }
                System.out.println();
            }
            System.out.println();
        }

        XWPFWordExtractor extractor = new XWPFWordExtractor(testFile);

    }

}
