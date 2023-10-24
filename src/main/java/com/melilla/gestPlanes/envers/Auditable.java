package com.melilla.gestPlanes.envers;


	import java.time.LocalDateTime;



	import org.springframework.data.annotation.CreatedBy;
	import org.springframework.data.annotation.CreatedDate;
	import org.springframework.data.annotation.LastModifiedBy;
	import org.springframework.data.annotation.LastModifiedDate;
	import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Data;

	@MappedSuperclass
	@EntityListeners(AuditingEntityListener.class)
	@Data
	public abstract class Auditable  {

	    @CreatedBy
	    @Column(name = "CREATED_BY", columnDefinition = "varchar(25)", updatable = false)
	    private String createdBy;

	    @CreatedDate
	    @Column(name = "CREATE_DATE_TIME", columnDefinition = "timestamp default '2021-06-10 20:47:05.967394'", updatable = false)
	    private LocalDateTime createdDate;

	    @LastModifiedBy
	    @Column(name = "MODIFIED_BY", columnDefinition = "varchar(25)")
	    private String lastModifiedBy;

	    @LastModifiedDate
	    @Column(name = "MODIFIED_DATE_TIME", columnDefinition = "timestamp default '2021-06-10 20:47:05.967394'")
	    private LocalDateTime lastModifiedDate;
}
