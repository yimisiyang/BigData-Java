
//wordGenerator import
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

//wordGenerator Class
public class wordGenerator{
    //Get lines from text file
    public List<String> getLines (String fileName) throws Exception{
        //readfile instance
        readFile rf = new readFile();
        //read the text
        try{
            List<String> lines = rf.readLines(fileName);
            for (String line:lines){
                System.out.println(line);
            }
            return lines;
        }catch (IOException e){
            //print out the exception that occurred
            System.out.println("Unable to create" + fileName + ":" + e.getMessage());
            throw e;
        }
    }
    //Create word
    public void createWord(List<String> lines) throws IOException{
        File file = new File("createdWord.docx");
        for (String line:lines){
            //Blank Document
            XWPFDocument document = new XWPFDocument();

            //Write the Document in file System
            FileOutputStream out = new FileOutputStream(file);
            //create Paragraph
            XWPFParagraph paragraph = document.createParagraph();
            XWPFRun run = paragraph.createRun();
            run.setText(line);
            document.write(out);
            //Close document
            out.close();
        }
        System.out.println("createdWord" + ".docx" + " written successfully");
    }
}
