package com.melilla.gestPlanes.DTO;

import lombok.Data;

@Data
public class updateConfigDTO {

	private long idConfig;
	
	private String uploadDir;
	
	private String tempDir;
	
	private String trashcanDir;
	
	private String contrato;
}
