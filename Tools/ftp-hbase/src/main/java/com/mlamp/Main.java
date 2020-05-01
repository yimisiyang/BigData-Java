package com.mlamp;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import org.apache.commons.io.IOUtils;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpMethod;

import java.io.*;
import java.sql.DriverManager;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {
    String target = new RSADecoder().privateDecrypt(args[0]);
    JSONObject json = JSON.parseObject(target);
    int jobId = json.getIntValue("jobId");
    String batchNo = json.getString("batchNo");

    String sjhjDriver = json.getString("sjhjDriver");
    String sjhjJdbcUrl = json.getString("sjhjJdbcUrl");
    String sjhjUsername = json.getString("sjhjUsername");
    String sjhjPassword = json.getString("sjhjPassword");

    String sourceUsername = json.getString("sourceUsername");
    String sourcePassword = json.getString("sourcePassword");
    String sourcePort = json.getString("sourcePort");
    String sourceIp = json.getString("sourceIp");
    String sourceDir = json.getString("sourceDir");
//    String sourceFileType = json.getString("sourceFileType");

    boolean recursive = json.getBoolean("recursive"); //是否递归
    String matchFile = json.getString("matchFile"); //匹配文件名
    String splitter = json.getString("splitter"); //分隔符
//    String encodingFormat = json.getString("encodingFormat");//编码
//    int dataLineNumber = json.getIntValue("dataLineNumber"); //从第几行开始读取

    String targetPort = json.getString("zookeeper.port");
    String targetIp = json.getString("zookeeper.ips");
    String targetParent = json.getString("zookeeper.parent.path");
    String targetDatabase = json.getString("target.db");
    String targetTable = json.getString("target.table");

    String logRecodUrl = json.getString("logRecordUrl");
    String wirteBackUrl = json.getString("wirteBackUrl");

    HttpMethod logBackRest = HttpMethod.POST;
    Map<String, Object> logMap = new HashMap<>();

    //1.获取FTP连接
    FtpUtil ftpUtil = new FtpUtil();
    FTPClient ftp = null;

    try{
        ftp = ftpUtil.getConnect(sourceIp, Integer.parseInt(sourcePort), sourceUsername,sourcePassword,sourceDir);
    }catch(Exception e) {
        logMap = getLogMap(jobId,batchNo,false,"连接FTP失败，用户名密码错误");
        new RestUtils<>().request(logRecodUrl, logMap, logBackRest, String.class);
        throw new RuntimeException(e.toString());
    }
    String rootDir = ftp.printWorkingDirectory();

    //2.获取目标库Hbase连接
    org.apache.hadoop.hbase.client.Connection connHbase = null;

    try {
        connHbase = HbaseUtil.getConn(targetIp, targetPort, targetParent);
    } catch (Exception e) {
        logMap = getLogMap(jobId, batchNo, false, "连接目标库失败：") + e.getMessage();
        new RestUtils<>().request(logRecodUrl, logMap, logBackRest, String.class);
        throw new RuntimeException(e.toString());
    }

    //3.获取SJHJ库连接
    java.sql.Connection connSjhj = null;
    try{
        Class.forName(sjhjDriver);
        connSjhj = DriverManager.getConnection(sjhjJdbcUrl, sjhjUsername, sjhjPassword);
    } catch (Exception e) {
        logMap = getLogMap(jobId, batchNo, false, "数据汇聚库连接失败：" + e.toString());
        new RestUtils<>().request(logRecodUrl, logMap, logBackRest, String.class);
        throw new RuntimeException(e.toString());

    }

}
