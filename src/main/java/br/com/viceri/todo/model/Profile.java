package br.com.viceri.todo.model;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

public class Profile {


	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SQ_PPOFILE")
	@SequenceGenerator(name = "SQ_PPOFILE", sequenceName = "SQ_PPOFILE", allocationSize = 1)
	private Long id;
	
	@Column(nullable = false)
	private String name;
}
