package com.github.devsns.domain.chat.dto;

import lombok.Getter;
import lombok.Setter;

@Getter@Setter
public class UploadFileResponse {
    private String fileName;
    private String fileDownloadUri;
    private String fileType;
    private long size;
}
