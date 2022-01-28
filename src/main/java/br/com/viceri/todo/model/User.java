package br.com.viceri.todo.model;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tb_user")
public class User implements Serializable {

	private static final long serialVersionUID = -2276103813086302739L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SQ_USER")
	@SequenceGenerator(name = "SQ_USER", sequenceName = "SQ_USER", allocationSize = 1)
	@Column(name = "id")
	private Long id;

	@Column(name = "name", nullable = false)
	private String name;

	@Column(name = "email", nullable = false)
	private String email;

	@JsonIgnore
	@Column(name = "password", nullable = false)
	private String password;
}
