//wordGenerator import
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;


//import org.apache.poi.xwpf.usermodel.TextAlignment;
//import org.apache.poi.xwpf.usermodel.UnderlinePatterns;
//import org.apache.poi.xwpf.usermodel.VerticalAlign;
//import org.apache.poi.xwpf.usermodel.Borders;
//import org.apache.poi.xwpf.usermodel.BreakClear;
//import org.apache.poi.xwpf.usermodel.BreakType;
//import org.apache.poi.xwpf.usermodel.LineSpacingRule;
import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;

//wordGenerator Class
public class wordGenerator{
    public List<String> getLines (String fileName) throws Exception{
        //readFile instance
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


    public void createWord(String filePathAndFileName) throws Exception{
        File file = new File("试飞日志.docx");
        //Blank Document
        XWPFDocument document = new XWPFDocument();
//        document.getStyle();
        //Create Title
        XWPFParagraph titleParagraph = document.createParagraph();//创建标题
        titleParagraph.setAlignment(ParagraphAlignment.CENTER);   //对齐方式
        XWPFRun rTitleParagraph = titleParagraph.createRun();    //段落末尾创建XWPFRun
        rTitleParagraph.setFontFamily("宋体");
        rTitleParagraph.setFontSize(18);
        rTitleParagraph.setText("飞行日志");
        rTitleParagraph.setBold(true);
        rTitleParagraph.setTextPosition(28);

        //Create time and people message
        XWPFParagraph timeAndPeople = document.createParagraph();
        timeAndPeople.setAlignment(ParagraphAlignment.CENTER);
        XWPFRun rTimeAndPeople = timeAndPeople.createRun();
        rTimeAndPeople.setFontSize(12);
        rTimeAndPeople.setText("2019-11-12    进场人员：小明");
        rTimeAndPeople.setTextPosition(20);

        //Create Paragraph one Title
        XWPFParagraph paragraphOneTitle = document.createParagraph(); //创建段落
        paragraphOneTitle.setAlignment(ParagraphAlignment.BOTH);
//        paragraphOneTitle.setStyle("1");
        XWPFRun rParagraphOneTitle = paragraphOneTitle.createRun();
        rParagraphOneTitle.setFontFamily("黑体");
        rParagraphOneTitle.setBold(true);
        rParagraphOneTitle.setFontSize(15);
        rParagraphOneTitle.setText("一、试飞情况");


        //Create Paragraph one subtitle1
        XWPFParagraph paragraphOneSubTitle1 = document.createParagraph();
        paragraphOneSubTitle1.setAlignment(ParagraphAlignment.BOTH);
        paragraphOneSubTitle1.setIndentationFirstLine(600);
        XWPFRun rParagraphOneSubTitle1 = paragraphOneSubTitle1.createRun();  //将一个新运行行追加到这一段
        rParagraphOneSubTitle1.setFontSize(14);
        rParagraphOneSubTitle1.setFontFamily("仿宋");
        rParagraphOneSubTitle1.setText("（1）实施内容");

        //Create Paragraph one subtitle1 message
        XWPFParagraph paragraphOneSubTitle1Mess = document.createParagraph();
        paragraphOneSubTitle1Mess.setAlignment(ParagraphAlignment.BOTH);
        paragraphOneSubTitle1Mess.setIndentationFirstLine(580);
        XWPFRun rParagraphOneSubTitle1Mess = paragraphOneSubTitle1Mess.createRun();
        rParagraphOneSubTitle1Mess.setFontSize(14);
        rParagraphOneSubTitle1Mess.setFontFamily("仿宋");
        rParagraphOneSubTitle1Mess.setText("8:00-10:00................");

        //Create Paragraph one subtitle2
        XWPFParagraph paragraphOneSubtitle2 = document.createParagraph();
        paragraphOneSubtitle2.setAlignment(ParagraphAlignment.BOTH);
        paragraphOneSubtitle2.setIndentationFirstLine(600);
        XWPFRun rParagraphOneSubtitle2 = paragraphOneSubtitle2.createRun();
        rParagraphOneSubtitle2.setFontSize(14);
        rParagraphOneSubtitle2.setFontFamily("仿宋");
        rParagraphOneSubtitle2.setText("（2）打弹情况");

        //Create Paragraph one subtitle2 message
        XWPFParagraph paragraphOneSubtitle2Mess = document.createParagraph();
        paragraphOneSubtitle2Mess.setAlignment(ParagraphAlignment.BOTH);
        paragraphOneSubtitle2Mess.setIndentationFirstLine(580);
        XWPFRun rParagraphOneSubtitle2Mess = paragraphOneSubtitle2Mess.createRun();
        rParagraphOneSubtitle2Mess.setFontSize(14);
        rParagraphOneSubtitle2Mess.setFontFamily("仿宋");
        rParagraphOneSubtitle2Mess.setText("ACMI...................................................................................................................................");

        //Create Paragraph one subtitle3
        XWPFParagraph paragraphOneSubtitle3 = document.createParagraph();
        paragraphOneSubtitle3.setAlignment(ParagraphAlignment.BOTH);
        paragraphOneSubtitle3.setIndentationFirstLine(600);
        XWPFRun rParagraphOneSubtitle3 = paragraphOneSubtitle3.createRun();
        rParagraphOneSubtitle3.setFontSize(14);
        rParagraphOneSubtitle3.setFontFamily("仿宋");
        rParagraphOneSubtitle3.setText("（3）网络规模");

        //Create Paragraph one subtitle3 message
        XWPFParagraph paragraphOneSubtitle3Mess = document.createParagraph();
        paragraphOneSubtitle3Mess.setAlignment(ParagraphAlignment.BOTH);
        paragraphOneSubtitle3Mess.setIndentationFirstLine(580);
        XWPFRun rParagraphOneSubtitle3Mess = paragraphOneSubtitle3Mess.createRun();
        rParagraphOneSubtitle3Mess.setFontSize(14);
        rParagraphOneSubtitle3Mess.setFontFamily("仿宋");
        rParagraphOneSubtitle3Mess.setText("设备使用............................................");

        //Create Paragraph two title
        XWPFParagraph paragraphTwoTitle = document.createParagraph();
        paragraphTwoTitle.setAlignment(ParagraphAlignment.BOTH);
//        paragraphTwoTitle.setStyle("1");
        XWPFRun rParagraphTwoTitle = paragraphTwoTitle.createRun();
        rParagraphTwoTitle.setFontSize(14);
        rParagraphTwoTitle.setFontFamily("仿宋");
        rParagraphTwoTitle.setBold(true);
        rParagraphTwoTitle.setText("二、问题情况");

        //Create Paragraph two message
        XWPFParagraph paragraphTwoMess = document.createParagraph();
        paragraphTwoMess.setAlignment(ParagraphAlignment.BOTH);
        paragraphTwoMess.setIndentationFirstLine(600);
        XWPFRun rParagraphTwoMess = paragraphTwoMess.createRun();
        rParagraphTwoMess.setFontSize(14);
        rParagraphTwoMess.setFontFamily("仿宋");
        rParagraphTwoMess.setText("问题分类");

        //Create Paragraph three title
        XWPFParagraph paragraphThreeTitle = document.createParagraph();
        paragraphThreeTitle.setAlignment(ParagraphAlignment.BOTH);
        XWPFRun rParagraphThreeTitle = paragraphThreeTitle.createRun();
        rParagraphThreeTitle.setFontSize(14);
        rParagraphThreeTitle.setFontFamily("仿宋");
        rParagraphThreeTitle.setBold(true);
        rParagraphThreeTitle.setText("三、升级情况");
//        paragraphThreeTitle.setStyle("1");

        //Create Paragraph three message
        XWPFParagraph paragraphThreeMess = document.createParagraph();
        paragraphThreeMess.setAlignment(ParagraphAlignment.BOTH);
        paragraphThreeMess.setIndentationFirstLine(580);
        XWPFRun rParagraphThreeMess = paragraphThreeMess.createRun();
        rParagraphThreeMess.setFontSize(14);
        rParagraphThreeMess.setFontFamily("仿宋");
        rParagraphThreeMess.setText("升级单位");

        FileOutputStream out = new FileOutputStream(file);
        document.write(out);
        System.out.println("写入成功");
    }

    public void createWordUseModel() throws Exception{

    }

}
