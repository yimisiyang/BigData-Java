package com.mlamp;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;

import java.io.IOException;
import java.util.Date;
import java.util.List;

public class FtpUtil {

    public FTPClient getConnect(String ip, Integer port, String username, String password, String rootDir) throws IOException {
        FTPClient ftp = new FTPClient();
        ftp.connect(ip, port);
        if (FTPReply.isPositiveCompletion(ftp.getReplyCode())) {
            if (ftp.login(username, password)) {
                ftp.setControlEncoding("UTF-8");
                ftp.setFileType(FTPClient.BINARY_FILE_TYPE);
                ftp.enterLocalPassiveMode();
                if (!FTPReply.isPositiveCompletion(ftp.getReplyCode())) {
                    System.out.println("connect to host" + ip + "failed, username or password error");
                    ftp.disconnect();
                }
                ftp.changeWorkingDirectory(rootDir);
                System.out.println("connect to host " + ip + " success, file dir: " + ftp.printWorkingDirectory());
            }
        }
        ftp.setFileType(FTP.BINARY_FILE_TYPE);
        ftp.enterLocalPassiveMode();
        ftp.setDefaultTimeout(86400000);
        ftp.setDataTimeout(86400000);
        ftp.setSoTimeout(86400000);
        return ftp;
    }

    public List<FileInfo> findFiles (FTPClient ftp, String matchFileName, List<FileInfo> fileInfos, boolean recursive) throws IOException {
        FTPFile[] fileList = ftp.listFiles();
        for (FTPFile file : fileList){
            if (file.isFile()) {
                //获取目录兼容中文将ISO-8895-1转UTF8
                String filePath = new String(ftp.printWorkingDirectory().getBytes(FTP.DEFAULT_CONTROL_ENCODING), "UTF8");
                if (StringUtils.isNotBlank(matchFileName)) {
                    if (file.getName().equals(matchFileName)) {
                        fileInfos.add(new FileInfo(file.getName(), filePath, DateUtil.getTimeStampStr(new Date())));
                        System.out.println(filePath + " : " + file.getName());
                    }else{
                        continue;
                    }
                }else{
                    System.out.println(filePath + " : " + file.getName());
                   fileInfos.add(new FileInfo(file.getName(), filePath, DateUtil.getTimeStampStr(new Date())));
                }
            }else if(recursive) {
                //切换目录必须将UTF8转ISO-8895-1
                ftp.changeWorkingDirectory(new String(file.getName().getBytes(), FTP.DEFAULT_CONTROL_ENCODING));
                findFiles(ftp, matchFileName, fileInfos,recursive);
                ftp.changeToParentDirectory();
            }
        }
        return fileInfos;
    }

    public boolean buildDirAbsolutePath(FTPClient ftp, String dir) throws Exception {
        String encryptRootDir = new String(dir.getBytes(), FTP.DEFAULT_CONTROL_ENCODING);
        if (StringUtils.isBlank(dir)) {
            return true;
        }
        if (ftp.changeWorkingDirectory(encryptRootDir)) {
            System.out.println(dir + "is exist");
            return true;
        }
        ftp.changeWorkingDirectory("/");
        String[] split = dir.split("/");
        for (String subDir : split) {
            if (StringUtils.isBlank(subDir))
                continue;
                //解决中文目录乱码
                String encryptSubDir = new String(subDir.getBytes(), FTP.DEFAULT_CONTROL_ENCODING);
                if (ftp.changeWorkingDirectory(encryptSubDir))
                    continue;
                if (!ftp.makeDirectory(encryptRootDir)) {
                    System.out.println("[失败]ftp创建目录：" + subDir);
                }
                ftp.changeWorkingDirectory(encryptRootDir);
            }

            //将目录切换至指定路径
            return ftp.changeWorkingDirectory(encryptRootDir);
        }
    public boolean moveFile(FTPClient ftp, String from, String to) throws Exception {
        String encryptFrom = new String(from.getBytes(), FTP.DEFAULT_CONTROL_ENCODING);
        String encryptTo = new String(to.getBytes(), FTP.DEFAULT_CONTROL_ENCODING);
        return ftp.rename(encryptFrom, encryptTo);
    }
}
