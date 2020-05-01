package com.mlamp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
@AllArgsConstructor

public class FileInfo {

    private String fileName;
    private String filePath;
    private String createTime;

}
