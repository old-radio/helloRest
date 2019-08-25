package com.example.demo.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.example.demo.exception.ErrorResponse;
import com.example.demo.exception.UserDuplicatedException;
import com.example.demo.exception.UserNotFoundException;
import com.example.demo.model.User;
import com.example.demo.service.UserService;

@RestController //@controller + @ResponseBody
@RequestMapping("/api")
public class RestAPIController {
	@Autowired
	UserService userService;
	
	//★★모든 사용자 조회★★
	@RequestMapping(value="/users", method=RequestMethod.GET)
	//RequestEntity를 사용하게 되면 header, body, HTTP.status를 모두 저장해서 넘긴다.
	public ResponseEntity<List<User>> listAllUsers(){
		//body에 담길때 json 포맷으로 바꿔서 저장  -> jackson-databind 라이브러리가 해줌
		//users에 모든 사용자 정보를 넣음	
		List<User> users = userService.findAllUsers();
		
		//사용자 리스트가 비었으면
		if(users.isEmpty()) {
			//HttpStatus에 NO_CONTENT를 넣어서 전달한다.
			return new ResponseEntity<List<User>>(HttpStatus.NO_CONTENT);
		}
		//사용자 리스트에 사용자가 있으면
		//응답메세지에 리스트와 HttpStatus에 OK를 넣어서 전달한다.
		return new ResponseEntity<List<User>>(users, HttpStatus.OK);
	}
	
	//★★id를 바탕으로 해당되는 사용자 조회★★
	@RequestMapping(value="/users/{id}", method=RequestMethod.GET)
	//RequestEntity를 사용하게 되면 header, body, HTTP.status를 모두 저장해서 넘긴다.
	public ResponseEntity<User> getUserById(@PathVariable("id") long id){
		//body에 담길때 json 포맷으로 바꿔서 저장  -> jackson-databind 라이브러리가 해줌
		
		//user에 id에 해당하는 사용자를 넣음
		User user = userService.findById(id);
		
		//user 값이 null -> id에 해당하는 사용자가 없으면
		if(user==null) {
			// to do list : exception -> 예외 처리해야함
			throw new UserNotFoundException(id);
		}
		//id에 해당하는 사용자가 있으면
		//응답메세지에 id에 해당하는 사용자와 HttpStatus에 OK를 넣어서 전달한다
		return new ResponseEntity<User>(user, HttpStatus.OK);
	}
	
	//★★사용자 생성★★
	@RequestMapping(value="/users", method=RequestMethod.POST)
	//RequestEntity를 사용하게 되면 header, body, HTTP.status를 모두 저장해서 넘긴다.
	//body 부분에 리턴할값이 없기때문에 void로 해준다.
	
	// post메소드를 사용하면 request body에 json포맷으로 새로생성되는 사용자의 정보를 넘겨오게되는데
	// 그 넘어온값(json 포맷)을 User객체로 변환시켜 user에 저장함.
	
	// UriComponentsBuilder ucBuilder 나중에 새롭게 생성된 사용자가 있을때
	// 그 사용자의 uri를 해더정보에 담아서 보내줘야하니까..
	public ResponseEntity<Void> createUser(@RequestBody User user, 
											UriComponentsBuilder ucBuilder){
		//추가하기 전에 존재하는지 확인
		if(userService.isUserExist(user)){
			// to do list: exception -> 예외 처리해야함
			throw new UserDuplicatedException(user.getName());
		}
		//존재하지 않으면
		userService.saveUser(user);
		
		//헤더를 만들어서 헤더에 방금 만든 사용자의 uri를 넘겨줄거임
		HttpHeaders headers = new HttpHeaders();
		//headers의 로케이션 설정 
		//ucBuilder를 사용하여 uri의 id값을 
		//방금 만든 사용자의 user.getId()의 값으로 설정하고
		//그 값을 toUri()-> uri 형태로 바꿔서 해더의 로케이션을 설정한다.
		headers.setLocation(ucBuilder.path("api/users/{id}").
							buildAndExpand(user.getId()).toUri());
		return new ResponseEntity<Void>(headers, HttpStatus.CREATED);
	}
	
	//--------------------★★id에 해당하는 사용자 업데이트★★-------------------------------------
	//update를 하려면 put메소드 사용함
	@RequestMapping(value="/users/{id}", method=RequestMethod.PUT)
	// put메소드 사용하면 request body에 json포맷으로 수정되는 사용자의 정보를 넘겨오게되는데
	// 그 넘어온값(json 포맷)을 User객체로 변환시켜 user에 저장함.
	public ResponseEntity<User> updateUserById(@PathVariable("id") long id,
												@RequestBody User user ){
		//업데이트하려는 사용자를 currentUser에 넣음
		User currentUser = userService.findById(id);
		//해당하는 사용자가 없다면
		if(currentUser == null) {
			//to do list : exception -> 예외 처리 해야함
			throw new UserNotFoundException(id);
		}
		
		//해당하는 사용자가 있다면
		//currentUser의 name,age,salary값에
		//user의 name,age,salary의 값을 설정함.
		currentUser.setName(user.getName());
		currentUser.setAge(user.getAge());
		currentUser.setSalary(user.getSalary());
		
		//설정된 currentUser의 값을 업데이트 한다.
		userService.updateUser(currentUser);
		//응답메세지(body)에 업데이트하려는 사용자의 수정된 값과 HttpStatus에 OK를 넣어서 전달한다
		return new ResponseEntity<User>(currentUser, HttpStatus.OK);
		
	}
	
	
	//--------------------★★id에 해당하는 사용자 삭제★★-------------------------------------
	@RequestMapping(value="/users/{id}", method=RequestMethod.DELETE)
	public ResponseEntity<User> deleteUserById(@PathVariable("id") long id){
		
		//id에 해당하는 사용자를 찾아서 user에 저장
		User user = userService.findById(id);
		//해당하는 사용자가 없다면
		if(user == null) {
			// to do list : exception -> 예외 처리해야함
			throw new UserNotFoundException(id);
		}
		//있다면
		//id에 해당하는 사용자를 지우고
		userService.deleteUserById(id);
		//응답메세지(body)에  HttpStatus에 no_content를 넣어서 전달한다
		return new ResponseEntity<User>(HttpStatus.NO_CONTENT);
	}
	
	
	//--------------------★★모든 사용자 삭제★★-------------------------------------
	@RequestMapping(value="/users", method=RequestMethod.DELETE)
	public ResponseEntity<User> deleteAllUsers(){
		
		//모든 사용자 삭제
		userService.deleteAllUsers();
		//응답메세지(body)에  HttpStatus에 no_content를 넣어서 전달한다
		return new ResponseEntity<User>(HttpStatus.NO_CONTENT);
	}

}
