package com.learning.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CourseModel {

	private Long id;
	private String name;
	private String curriculum;
	private String duration;
	
	}
