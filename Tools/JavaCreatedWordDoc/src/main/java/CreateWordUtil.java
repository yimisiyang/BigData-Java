import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import pojo.Problem;
import pojo.RTKN;
import pojo.Upgrade;

public class CreateWordUtil {
    public static void main(String[] args) throws Exception{
        Map<String,Object> pcjc = new HashMap<String, Object>();
        List<String> jclist= new ArrayList<String>();
        jclist.add(" 2344 泽");
        jclist.add(" 2357 白");
        pcjc.put("batch_time","8:10-10:00");
        pcjc.put("batch_name","351");
        pcjc.put("pilotAndFighterCode",jclist);
        pcjc.put("batch_subject","XXXJ");
        List<RTKN> rtkns = new ArrayList<RTKN>();
        RTKN rtkn1= new RTKN();
        rtkn1.setDdlx("P1");
        rtkn1.setIsHit("命中");
        rtkns.add(rtkn1);
        RTKN rtkn2= new RTKN();
        rtkn2.setDdlx("P2");
        rtkn2.setIsHit("未命中");
        rtkns.add(rtkn2);
        RTKN rtkn3= new RTKN();
        rtkn3.setDdlx("P1");
        rtkn3.setIsHit("未命中");
        rtkns.add(rtkn3);
        RTKN rtkn4= new RTKN();
        rtkn4.setDdlx("P2");
        rtkn4.setIsHit("命中");
        rtkns.add(rtkn4);
        List<Problem> problems=new ArrayList<Problem>();
        Problem problem1= new Problem("实时","x16","yxddddfs","dz");
        Problem problem2= new Problem("定时","x155","yxdddd道德风尚fs","dz");
        problems.add(problem1);
        problems.add(problem2);
        List<Upgrade> upgrades=new ArrayList<Upgrade>();
        Upgrade upgrade1= new Upgrade("X1","x30","jzdj","xgkdlfsldf","1.0->2.0");
        Upgrade upgrade2= new Upgrade("X2","x31","jzdjddfd","xgkdlfslddfdfdf","2.0->2.1");
        upgrades.add(upgrade1);
        upgrades.add(upgrade2);
        createWord("试飞日志.docx","2019-11-26","姚",pcjc,rtkns,"X1","N2",problems,upgrades);

    }

    /**
     * @param filePathAndName 生成文件的存储路径和文件名
     * @param sfdate 试飞日期  yyyy-MM-dd格式
     * @param officers 入场人员，如果多人逗号分隔
     * @param pcjc 批次架次对应的信息：批次起止时间（到分即可），批次名称，批次下架次对应的飞机代号和飞行员代字，科目名称
     *             {"batch_time":"8:10-10:00",""batch_name":"351","pilotAndFighterCode":["2782 泽","2718 白"],"batch_subject":"XXXX"}
     * @param wgtype 网管类型：X1、X2
     * @param cymodle 成员模式：N1、N2、N3
     * @param problems 问题情况
     * */
    public static void createWord(String filePathAndName, String sfdate, String officers, Map<String,Object> pcjc, List<RTKN> rtkns, String wgtype, String cymodle, List<Problem> problems, List<Upgrade> upgrades) throws Exception{
        File file = new File(filePathAndName);
        //Blank Document
        XWPFDocument document = new XWPFDocument();
//        document.getStyle();
        //Create Title
        XWPFParagraph titleParagraph = document.createParagraph();
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
        rTimeAndPeople.setText(sfdate+"\t\t\t"+"进场人员："+officers);
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
        String batch_time=(String)pcjc.get("batch_time");
        String batch_name=(String)pcjc.get("batch_name");
        List<String> pilotAndFighterCodes=(List<String>)pcjc.get("pilotAndFighterCode");
        String batch_subject=(String)pcjc.get("batch_subject");
        String pilotAndFighterCode="";
        if (pilotAndFighterCodes.size()==1){
            pilotAndFighterCode=pilotAndFighterCodes.get(0);
        }else {
            for (String pfc:pilotAndFighterCodes) {
                pilotAndFighterCode += pfc + " vs ";
            }
            pilotAndFighterCode=pilotAndFighterCode.substring(0,pilotAndFighterCode.lastIndexOf(" vs "));
        }
        rParagraphOneSubTitle1Mess.setText(batch_time+"\t"+batch_name+"\t"+pilotAndFighterCode+"\t"+batch_subject+"\t"+"1批"+pilotAndFighterCodes.size()+"架次。");

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
        Map<String,Map<String,Integer>> qkmap=new HashMap<String,Map<String,Integer>>();
        //计算导弹命中情况
        for (RTKN r:rtkns){
            if (qkmap.containsKey(r.getDdlx())){
                Map<String, Integer> tjmap = qkmap.get(r.getDdlx());
                int ddtj = tjmap.get("ddtj")+1;
                int ishit=tjmap.get("hittj");
                if (r.getIsHit().equals("命中")){
                    ishit++;
                }
                tjmap.put("ddtj",ddtj);
                tjmap.put("hittj",ishit);
                qkmap.put(r.getDdlx(),tjmap);
            }else {
                Map<String,Integer> tj=new HashMap<String, Integer>();
                tj.put("ddtj",1);//导弹统计
                int ishit=0;
                if (r.getIsHit().equals("命中")){
                    ishit++;
                }
                tj.put("hittj",ishit);//命中统计
                qkmap.put(r.getDdlx(),tj);
            }
        }
        //编写打弹情况
        StringBuffer ddqk= new StringBuffer(batch_subject+" 科目：模拟发射 ");
        for (String ddlx:qkmap.keySet()){
            Map<String, Integer> tjmap = qkmap.get(ddlx);
            int ddtj = tjmap.get("ddtj");
            int hittj = tjmap.get("hittj");
            ddqk.append(ddlx+"导弹"+ddtj+"枚，命中"+hittj+"枚、");
        }
        rParagraphOneSubtitle2Mess.setText(ddqk.toString().substring(0,ddqk.toString().lastIndexOf("、"))+"。");

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
        rParagraphOneSubtitle3Mess.setText("设备使用"+wgtype+"+XXYY站（N个）+XXYY站（N个）；网络规模使用"+cymodle+"成员模式。");

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
        int problemxh=1;
        for (Problem pro:problems){
            XWPFParagraph paragraphTwoMess = document.createParagraph();
            paragraphTwoMess.setAlignment(ParagraphAlignment.BOTH);
            paragraphTwoMess.setIndentationFirstLine(600);
            XWPFRun xhrun = paragraphTwoMess.createRun();
            xhrun.setFontSize(14);
            xhrun.setFontFamily("仿宋");
            xhrun.setText("（"+problemxh+"）");

            XWPFParagraph problemtypeMess = document.createParagraph();
            problemtypeMess.setAlignment(ParagraphAlignment.BOTH);
            problemtypeMess.setIndentationFirstLine(600);
            XWPFRun problemtype = problemtypeMess.createRun();
            problemtype.setFontSize(14);
            problemtype.setFontFamily("仿宋");
            problemtype.setText("问题分类："+pro.getProblemType());

            XWPFParagraph jxMess = document.createParagraph();
            jxMess.setAlignment(ParagraphAlignment.BOTH);
            jxMess.setIndentationFirstLine(600);
            XWPFRun jx = jxMess.createRun();
            jx.setFontSize(14);
            jx.setFontFamily("仿宋");
            jx.setText("问题机型："+pro.getJx());

            XWPFParagraph describeMess = document.createParagraph();
            describeMess.setAlignment(ParagraphAlignment.BOTH);
            describeMess.setIndentationFirstLine(600);
            XWPFRun describe = describeMess.createRun();
            describe.setFontSize(14);
            describe.setFontFamily("仿宋");
            describe.setText("问题描述："+pro.getDescribe());

            XWPFParagraph unitMess = document.createParagraph();
            unitMess.setAlignment(ParagraphAlignment.BOTH);
            unitMess.setIndentationFirstLine(600);
            XWPFRun unit = unitMess.createRun();
            unit.setFontSize(14);
            unit.setFontFamily("仿宋");
            unit.setText("责任单位："+pro.getUnit());
            problemxh++;
        }

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
        int upaxh=1;
        for (Upgrade upa:upgrades){
            XWPFParagraph paragraphThreeMess = document.createParagraph();
            paragraphThreeMess.setAlignment(ParagraphAlignment.BOTH);
            paragraphThreeMess.setIndentationFirstLine(580);
            XWPFRun xhcreaterun = paragraphThreeMess.createRun();
            xhcreaterun.setFontSize(14);
            xhcreaterun.setFontFamily("仿宋");
            xhcreaterun.setText("（"+upaxh+"）");

            XWPFParagraph unitMess = document.createParagraph();
            unitMess.setAlignment(ParagraphAlignment.BOTH);
            unitMess.setIndentationFirstLine(580);
            XWPFRun unitcreaterun = unitMess.createRun();
            unitcreaterun.setFontSize(14);
            unitcreaterun.setFontFamily("仿宋");
            unitcreaterun.setText("升级单位"+upa.getUpUnit());

            XWPFParagraph JXMess = document.createParagraph();
            JXMess.setAlignment(ParagraphAlignment.BOTH);
            JXMess.setIndentationFirstLine(580);
            XWPFRun JXcreaterun = JXMess.createRun();
            JXcreaterun.setFontSize(14);
            JXcreaterun.setFontFamily("仿宋");
            JXcreaterun.setText("升级机型/机号："+upa.getUpJX());

            XWPFParagraph deviceMess = document.createParagraph();
            deviceMess.setAlignment(ParagraphAlignment.BOTH);
            deviceMess.setIndentationFirstLine(580);
            XWPFRun devicecreaterun = deviceMess.createRun();
            devicecreaterun.setFontSize(14);
            devicecreaterun.setFontFamily("仿宋");
            devicecreaterun.setText("升级设备："+upa.getUpdevice());

            XWPFParagraph describeMess = document.createParagraph();
            describeMess.setAlignment(ParagraphAlignment.BOTH);
            describeMess.setIndentationFirstLine(580);
            XWPFRun describecreaterun = describeMess.createRun();
            describecreaterun.setFontSize(14);
            describecreaterun.setFontFamily("仿宋");
            describecreaterun.setText("修改内容："+upa.getUpdescribe());

            XWPFParagraph versionMess = document.createParagraph();
            versionMess.setAlignment(ParagraphAlignment.BOTH);
            versionMess.setIndentationFirstLine(580);
            XWPFRun versioncreaterun = versionMess.createRun();
            versioncreaterun.setFontSize(14);
            versioncreaterun.setFontFamily("仿宋");
            versioncreaterun.setText("版本变更："+upa.getUpdateVersion());

            upaxh++;
        }
        FileOutputStream out = new FileOutputStream(file);
        document.write(out);
        System.out.println("写入成功");
    }
}
