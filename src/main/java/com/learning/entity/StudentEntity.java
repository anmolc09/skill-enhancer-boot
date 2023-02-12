package com.learning.entity;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Getter
@Setter
@Entity
@Table(name = "student")
public class StudentEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@NotNull
	private String name;
	//@Pattern(regexp = "^\\d{10}$")
	private Long contactDetails;
	@Size(min = 2, max = 30)
	private String qualification;
	@Email
	private String email;

}
