package com.example.demo.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.stereotype.Service;

import com.example.demo.model.User;

//service 기능을 하는 bean으로 등록하는 어노테이션
@Service
public class UserService {
	//AtomicLong -> id값을 상호배체를 시켜주기 위한 클래스
	private static final AtomicLong counter = new AtomicLong();
	
	//users라는 리스트 생성 //db를 사용하지 않고 메모리 상에 올리겠다는거
	private static List<User> users;
	
	//생성자
	//처음 생성될때 user를 만든다.
	public UserService() {
		
		users = new ArrayList<User>();
		//id(사용자 추가될때마다 id값 증가 시키고 그 값 가져오기), name, age, salary
		users.add(new User(counter.incrementAndGet(),"Sam",30,70000));
		users.add(new User(counter.incrementAndGet(),"Tom",40,50000));
		users.add(new User(counter.incrementAndGet(),"Jerome",45,30000));
		users.add(new User(counter.incrementAndGet(),"Silvia",50,40000));
		
	}
	
	//모든 사용자 조회
	public List<User> findAllUsers(){
		return users;
	}
	
	//id를 바탕으로 해당되는 사용자 조회
	public User findById(long id) {
		for(User user: users) {
			if(user.getId() == id) {
				return user;
			}
		}
		return null;
	}
	
	//이름을 바탕으로 해당되는 사용자 조회
	public User findByName(String name) {
		for(User user: users) {
			if(user.getName().equalsIgnoreCase(name)) {
				return user;
			}
		}
		return null;
	}
	
	//사용자 저장
	public void saveUser(User user) {
		user.setId(counter.incrementAndGet());
		users.add(user);
	}
	
	//사용자 업데이트
	public void updateUser(User user) {
		int index = users.indexOf(user);
		users.set(index, user);
	}
	
	//id를 바탕으로 해당되는 사용자 삭제
	public void deleteUserById(long id) {
		for (Iterator<User> iterator = users.iterator(); iterator.hasNext();) {
			User user = iterator.next();
			if(user.getId() == id) {
				iterator.remove();
			}
		}
	}
	
	//사용자가 존재하는지 -> 추가할때 사용
	public boolean isUserExist(User user) {
		return findByName(user.getName()) != null;
	}
	
	//모든 사용자 삭제
	public void deleteAllUsers() {
		users.clear();
	}
	
	
}