import java.io.FileOutputStream;
import java.math.BigInteger;

import org.apache.poi.xwpf.usermodel.Borders;
import org.apache.poi.xwpf.usermodel.BreakClear;
import org.apache.poi.xwpf.usermodel.BreakType;
import org.apache.poi.xwpf.usermodel.LineSpacingRule;
import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.TextAlignment;
import org.apache.poi.xwpf.usermodel.UnderlinePatterns;
import org.apache.poi.xwpf.usermodel.VerticalAlign;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.*;


//import java.util.List;



/**
 * Created by ONUR  BASKIPT on 14.11.2019.
 */
public class Main {
    //Main Function
//    public static void main(String[] args) throws Exception {
//        //Instantiate WordGenerator Class
//        wordGenerator wg = new wordGenerator();
//
//        //VK lines text filename
//        String vkFilename = "vk_no.txt";
//
//        //Gather VK Lines from text file
//        List<String> vkLines = wg.getLines(vkFilename);
//
//        //Create word document according to VK lines
//        wg.createWord(vkLines);
//    }

    public CustomTOC(CTSdtBlock block) {
        this.block = block;
        CTSdtPr sdtPr = block.addNewSdtPr();
        CTDecimalNumber id = sdtPr.addNewId();
        id.setVal(new BigInteger("4844945"));
        sdtPr.addNewDocPartObj().addNewDocPartGallery().setVal("Table of contents");
        CTSdtEndPr sdtEndPr = block.addNewSdtEndPr();
        CTRPr rPr = sdtEndPr.addNewRPr();
        CTFonts fonts = rPr.addNewRFonts();
        fonts.setAsciiTheme(STTheme.MINOR_H_ANSI);
        fonts.setEastAsiaTheme(STTheme.MINOR_H_ANSI);
        fonts.setHAnsiTheme(STTheme.MINOR_H_ANSI);
        fonts.setCstheme(STTheme.MINOR_BIDI);
        rPr.addNewB().setVal(STOnOff.OFF);
        rPr.addNewBCs().setVal(STOnOff.OFF);
        rPr.addNewColor().setVal("auto");
        rPr.addNewSz().setVal(new BigInteger("24"));
        rPr.addNewSzCs().setVal(new BigInteger("24"));
        CTSdtContentBlock content = block.addNewSdtContent();
        CTP p = content.addNewP();
        p.setRsidR("00EF7E24".getBytes(LocaleUtil.CHARSET_1252));
        p.setRsidRDefault("00EF7E24".getBytes(LocaleUtil.CHARSET_1252));
        p.addNewPPr().addNewPStyle().setVal("TOCHeading");
        p.addNewR().addNewT().setStringValue("目     录");//源码中为"Table of contents"
        //设置段落对齐方式，即将“目录”二字居中
        CTPPr pr = p.getPPr();
        CTJc jc = pr.isSetJc() ? pr.getJc() : pr.addNewJc();
        STJc.Enum en = STJc.Enum.forInt(ParagraphAlignment.CENTER.getValue());
        jc.setVal(en);
        //"目录"二字的字体
        CTRPr pRpr = p.getRArray(0).addNewRPr();
        fonts = pRpr.isSetRFonts() ? pRpr.getRFonts() : pRpr.addNewRFonts();
        fonts.setAscii("Times New Roman");
        fonts.setEastAsia("华文中宋");
        fonts.setHAnsi("华文中宋");
        //"目录"二字加粗
        CTOnOff bold = pRpr.isSetB() ? pRpr.getB() : pRpr.addNewB();
        bold.setVal(STOnOff.TRUE);
        // 设置“目录”二字字体大小为24号
        CTHpsMeasure sz = pRpr.isSetSz() ? pRpr.getSz() : pRpr.addNewSz();
        sz.setVal(new BigInteger("36"));
    }


}