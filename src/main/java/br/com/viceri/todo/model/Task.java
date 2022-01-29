package br.com.viceri.todo.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tb_task")
public class Task implements Serializable {

	private static final long serialVersionUID = -8923741745016007666L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SQ_USER")
	@SequenceGenerator(name = "SQ_USER", sequenceName = "SQ_USER", allocationSize = 1)
	@Column(name = "id")
	private Long id;
	
	@Column(name = "description", nullable = false)
	private String description;

	@Column(name = "due_date")
	private Date dueDate;

	@Column(name = "create_date", nullable = false)
	private Date createDate;

	@Enumerated(EnumType.STRING)
	@Column(name = "priority", nullable = false)
	private Priority priority;

	@ManyToOne
	@JoinColumn(name = "id_user", nullable = false)
	private User user;
}
