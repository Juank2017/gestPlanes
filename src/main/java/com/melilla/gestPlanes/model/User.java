package com.melilla.gestPlanes.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;

import java.util.List;

import org.hibernate.annotations.SQLDelete;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;


import jakarta.persistence.Basic;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinTable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;

import jakarta.persistence.Table;
import jakarta.persistence.Version;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;




@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="user")
@SQLDelete(sql = "UPDATE user SET deleted=true, deleted_at= NOW() WHERE id=?")
@EntityListeners(AuditingEntityListener.class)
public class User implements UserDetails {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Basic(optional = false)
	@Column(name = "username")
	private String userName;

	@JsonIgnore
	@Column(name = "password")
	private String password;

	@Column(name = "enabled")
	private boolean active;
	
	
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name="Usuario_Rol",
				joinColumns = @JoinColumn(name="idUsuario", referencedColumnName = "id"),
				inverseJoinColumns = @JoinColumn(name="idRol", referencedColumnName = "id"))
	private Collection<Role> roles;

	@Version
	private Long version;
	

	@Override
	@JsonIgnore
    public Collection<? extends GrantedAuthority> getAuthorities() {
		List<GrantedAuthority> authorities= new ArrayList<>();
		for (Role rol : this.roles) {
			authorities.add(new SimpleGrantedAuthority(rol.getRoleName()));
			
		}
        return authorities;
    }

    @Override
    public String getUsername() {
        // email in our case
        return userName;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
    	

        if(!this.active) return false;

        return true;
    }
    
	@CreatedDate
	@JsonFormat(shape = JsonFormat.Shape.STRING,  pattern = "dd/MM/yyy")
	private LocalDateTime createdAt;
	
	private boolean deleted;
	
	private LocalDateTime deletedAt;

}
