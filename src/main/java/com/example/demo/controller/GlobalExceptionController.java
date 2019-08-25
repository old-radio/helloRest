package com.example.demo.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.example.demo.exception.ErrorResponse;
import com.example.demo.exception.UserDuplicatedException;
import com.example.demo.exception.UserNotFoundException;

@ControllerAdvice
public class GlobalExceptionController {
	// --------------------★★UserNotFoundException 예외를 처리하는 핸들러★★-------------------------------------
	@ExceptionHandler(UserNotFoundException.class)
	// 리턴값으로 body부분에 ErrorResponse를 넣어서 넘길예정
	public ResponseEntity<ErrorResponse> handleUserNotFoundException(HttpServletRequest req, UserNotFoundException ex) {

		// requestURL 가져와서 값 저장
		String requestURL = req.getRequestURL().toString();

		ErrorResponse errorResponse = new ErrorResponse();
		// errorResponse에 requestURL 설정
		errorResponse.setRequestURL(requestURL);
		// errorResponse에 ErrorCode 설정
		errorResponse.setErrorCode("User.notFound.exception");
		// errorResponse에 ErrorMsg 설정
		//ex는 실행된 exception를 말한다.
		errorResponse.setErrorMsg("User with id=" + ex.getUserId() + " not found");
		
		// 응답메세지(body)에 errorResponse와 HttpStatus에 NOT_FOUND를 넣어서 전달한다
		return new ResponseEntity<ErrorResponse>(errorResponse,HttpStatus.NOT_FOUND);

	}
	
	// --------------------★★UserDuplicatedException 예외를 처리하는 핸들러★★-------------------------------------
	@ExceptionHandler(UserDuplicatedException.class)
	// 리턴값으로 body부분에 ErrorResponse를 넣어서 넘길예정
	public ResponseEntity<ErrorResponse> handleUserDuplicatedException
					(HttpServletRequest req, UserDuplicatedException ex) {

		// requestURL 가져와서 값 저장
		String requestURL = req.getRequestURL().toString();

		ErrorResponse errorResponse = new ErrorResponse();
		// errorResponse에 requestURL 설정
		errorResponse.setRequestURL(requestURL);
		// errorResponse에 ErrorCode 설정
		errorResponse.setErrorCode("User.duplicated.exception");
		// errorResponse에 ErrorMsg 설정
		//ex는 실행된 exception를 말한다.
		errorResponse.setErrorMsg("Unable to create. A user with name " + 
				ex.getUsername() + " already exist");
		
		// 응답메세지(body)에 errorResponse와 HttpStatus에 CONFLICT를 넣어서 전달한다
		return new ResponseEntity<ErrorResponse>(errorResponse,HttpStatus.CONFLICT);

	}
}