package com.melilla.gestPlanes.model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.envers.Audited;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
@Audited
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "idPagoReclamacion")
@SQLDelete(sql = "UPDATE pago_reclamacion SET deleted=true, deleted_at= NOW() WHERE id_pago_reclamacion=?")
@EntityListeners(AuditingEntityListener.class)
public class PagoReclamacion {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long idPagoReclamacion;
	
	private LocalDate fechaPago;
	
	private BigDecimal  importe = new BigDecimal("0").setScale(2,RoundingMode.HALF_DOWN );
	
	@JsonIgnore
	@ManyToOne
	@JoinColumn(name="idProcedimiento")
	private Procedimiento procedimiento;

}
