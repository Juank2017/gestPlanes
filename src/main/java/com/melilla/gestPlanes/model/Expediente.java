//package com.melilla.gestPlanes.model;
//
//import java.util.Date;
//import java.util.List;
//
//import org.hibernate.envers.Audited;
//import org.hibernate.envers.NotAudited;
//
//import com.fasterxml.jackson.annotation.JsonManagedReference;
//
//import jakarta.persistence.CascadeType;
//import jakarta.persistence.Entity;
//import jakarta.persistence.GeneratedValue;
//import jakarta.persistence.GenerationType;
//import jakarta.persistence.Id;
//import jakarta.persistence.JoinColumn;
//import jakarta.persistence.JoinTable;
//import jakarta.persistence.ManyToMany;
//import jakarta.persistence.OneToMany;
//import jakarta.persistence.OneToOne;
//import jakarta.persistence.Temporal;
//import jakarta.persistence.TemporalType;
//
//import lombok.Getter;
//import lombok.RequiredArgsConstructor;
//import lombok.Setter;
//
//@Entity
//@RequiredArgsConstructor
//@Setter
//@Getter
//@Audited
//public class Expediente {
//	
//	
//	@Id
//	@GeneratedValue(strategy = GenerationType.IDENTITY)
//	private Long idExpediente;
//	
//	@Temporal(TemporalType.DATE)
//	private Date fechaCreacion;
//	
//	private boolean activo;
//	
//	@JsonManagedReference
//    @ManyToMany(cascade = {
//            CascadeType.PERSIST,
//            CascadeType.MERGE
//        })
//        @JoinTable(name = "expediente_ciudadano",
//            joinColumns = @JoinColumn(name = "idExpediente"),
//            inverseJoinColumns = @JoinColumn(name = "idCiudadano")
//        )
//	private List<Ciudadano> interesados;
//	
//
//	
//	@OneToMany(mappedBy = "expediente", cascade = CascadeType.ALL)
//	private List<Documento> documentos;
//	
//
//	
//
//}
