package com.mlamp;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.apache.commons.io.IOUtils;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.*;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.io.StringReader;
import java.io.StringWriter;
import java.sql.*;
import java.util.*;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) throws Exception{
        String mysqlUsername = "root";
        String mysqlPassword = "Mlamp1234!";
        String mysqlUrl = "jdbc:mysql://127.0.0.1/s jh jdb?useUnicode = true& characterEncoding=UTF-8";

        String ftpUsername = "hdfs";
        String ftpPassword = "123456";
        String ftpIp = "192.168.0.146";
        String ftpPort = "2222";
        String ftpScanPath ="/ftp/targetdata/";

        String jobCreateUrl = "http://127.0.0.1/api/job/create";
        HttpMethod jobCreateRest = HttpMethod.POST;

        //以下参数需要从汇聚库查看
        //数据源FTP id 和名称【修改】
        int srcDatasourceId = 0;
        String srcDatabase = "abcd";
        // 目标数据源Hbase id和名称
        int targetDatasourcedId = 0;
        String targetDatabase = "abcd";

        //1.获取FTP连接
        FtpUtil ftpUtil = new FtpUtil();
        FTPClient ftp = null;
        try{
            ftp =  ftpUtil.getConnect(ftpIp,Integer.parseInt(ftpPort),ftpUsername,ftpPassword,ftpScanPath);
        }catch(Exception e){
            e.printStackTrace();
        }
        String rootDir = new String(ftp.printWorkingDirectory().getBytes(FTP.DEFAULT_CONTROL_ENCODING),"utf8");
        System.out.println(rootDir);

        //2.获取SJHJ库连接
        java.sql.Connection connSjhj = null;
        try{
            Class.forName("com.mysql.jdbc.Driver");
            connSjhj = DriverManager.getConnection(mysqlUrl,mysqlUsername,mysqlPassword);
        }catch(Exception e){
            e.printStackTrace();
        }

        //3.递归扫描所有目录，去重获取单一文件名
        List<FileInfo> fileInfos = List.newArrayList();
        ftpUtil.findFiles(ftp,null,fileInfos,true);
        Set<String> fileNameSet = fileInfos.stream().map(FileInfo::getFileName).collect(Collectors.toSet());
        System.out.println(fileNameSet);

        //获取汇聚作业名称
        String sql = "select name from transfer_job_model";
        List<String> sjhjJob = DbTool.queryListBySQL(connSjhj,sql);

        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setConnectTimeout(5000*1000);
        requestFactory.setReadTimeout(36000*1000);
        RestTemplate restTemplate = new RestTemplate(requestFactory);

        Configuration config = new Configuration();
        config.setNumberFormat("#");

        //4.创建作业

        for(String fileName:fileNameSet){
            if(sjhjJob.contains(fileName)){
                continue;
            }
            ClassPathResource cpr = new ClassPathResource("Test.ft1");
            String templateContent = IOUtils.toString(cpr,getInputStream(),"UTF-8");
            Template temppate = new Template("Test",new StringReader(templateContent),config);
            StringWriter stringWriter = new StringWriter();
            Map<String,Object> schemaParams = new HashMap<>();
            schemaParams.put("srcDatasourceId", srcDatasourceId);
            schemaParams.put("srcDatabase", srcDatabase);
            schemaParams.put("targetDatabase", targetDatabase);
            schemaParams.put("targetDatasourceId",targetDatasourcedId);
            schemaParams.put("targetTable",fileName.split(".")[0]);
            schemaParams.put("ftpScanPath", ftpScanPath);
            schemaParams.put("filename",fileName);
            temppate.process(schemaParams,stringWriter);
            String json = stringWriter.toString();
            //System.out.println(json);
            JSONObject jsonObject = JSON.parseObject(json);
            restTemplate.postForObject(jobCreateRest, jsonObject, String.class);
        }

        //freemarker设置 数据不用逗号分隔

    }
}
