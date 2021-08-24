package com.mcbirdtechnologies.LibManagement.Controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mcbirdtechnologies.LibManagement.Models.Book;
import com.mcbirdtechnologies.LibManagement.Models.User;
import com.mcbirdtechnologies.LibManagement.Repository.BookRepository;
import com.mcbirdtechnologies.LibManagement.Repository.UserRepository;
import com.mcbirdtechnologies.LibManagement.UserDetail.MyUserDetailsService;

@RestController
@RequestMapping("/user")

public class UserController {

	@Autowired
	BookRepository bookRepo;

	@Autowired
	MyUserDetailsService userDetail;
	
	@Autowired
	UserRepository userRepo;
	
	private final String AVAILABLE_BOOKS_URL = "/availableBooks";
	private final String ISSUED_BOOKS_URL = "/issuedBooks";

	@GetMapping(AVAILABLE_BOOKS_URL)
	public List<Map<String,Object>> availableBooks() {

		List<Map<String,Object>> response = new ArrayList<Map<String,Object>>();
		List<Book> availableBooks = bookRepo.findAvailableBooks();
		
		for(Book individualBook: availableBooks){
			
			Map<String,Object> tempMap = new HashMap<>();
			tempMap.put("bookName", individualBook.getName());
			tempMap.put("bookId", individualBook.getId());
			
			response.add(tempMap);
		}

		return response;
	}

	@GetMapping(ISSUED_BOOKS_URL)
	public ResponseEntity<?> viewIssuedBooks() {

		String userName = SecurityContextHolder.getContext().getAuthentication().getName();
		
		Optional<User> user = userRepo.findByUserName(userName);
		
		List<Book> response = bookRepo.findIssuedBooks(user.get().getId());
		
		if (response.size() == 0){
			return ResponseEntity.ok("No Books Issued");
		}
		
		return ResponseEntity.ok(response);
	}

}
