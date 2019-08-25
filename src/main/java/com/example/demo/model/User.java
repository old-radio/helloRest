package com.example.demo.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
//인자가 없는 생성자
@NoArgsConstructor
//모든 인자를 가지는 생성자
@AllArgsConstructor
@ToString

public class User {
	
	private long id;
	private String name;
	private int age;
	private double salary;
}