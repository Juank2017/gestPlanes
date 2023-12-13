package com.melilla.gestPlanes.model;


import java.time.LocalDateTime;


import org.hibernate.annotations.SQLDelete;
import org.springframework.data.annotation.CreatedDate;



import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor
@Setter
@Getter
@AllArgsConstructor
@Builder
@Entity
@SQLDelete(sql = "UPDATE tipo_documento SET deleted=true, deleted_at= NOW() WHERE id=?")
public class TipoDocumento {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long idTipoDocumento;
	
	private String tipo;
	
	@CreatedDate
	private LocalDateTime createdAt;
	
	private boolean deleted;
	
	private LocalDateTime deletedAt;

}
