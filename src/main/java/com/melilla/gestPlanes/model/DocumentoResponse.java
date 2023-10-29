package com.melilla.gestPlanes.model;

import lombok.AllArgsConstructor;
import lombok.Data;


@Data
@AllArgsConstructor
public class DocumentoResponse {
	
    private String fileName;
    private String fileDownloadUri;
    private String fileType;
    private long size;

}
