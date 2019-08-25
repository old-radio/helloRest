package com.example.demo.exception;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;

public class UserDuplicatedException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -225727447477592036L;
	
	private String username;

	public UserDuplicatedException(String username) {
		super();
		this.username = username;
	}
	
	public String getUsername() {
		return username;
	}


}
