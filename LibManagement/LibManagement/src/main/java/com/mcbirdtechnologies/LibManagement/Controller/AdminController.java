package com.mcbirdtechnologies.LibManagement.Controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mcbirdtechnologies.LibManagement.Models.Book;
import com.mcbirdtechnologies.LibManagement.Repository.BookRepository;

@RestController
@RequestMapping("/admin")
public class AdminController {

	@Autowired
	BookRepository bookRepo;
	
	private final String ADD_BOOK_URL = "/addBooks";
	private final String VIEW_BOOKS_URL = "/viewBooks";
	private final String REMOVE_BOOK_URL = "/removeBook";

	@PostMapping(ADD_BOOK_URL)
	public ResponseEntity<?> addBook(@RequestBody Map<String, String> requestBody) {
		Book books = new Book();
		
		if(!requestBody.containsKey("name") || requestBody.get("name") == null ||requestBody.get("name").isEmpty()){
			return  ResponseEntity.badRequest().body("Please provide name of book");
		}

		books.setName(requestBody.get("name"));

		bookRepo.save(books);

		return ResponseEntity.ok("Book added");

	}

	@GetMapping(VIEW_BOOKS_URL)
	public List<Map> view() {

		List<Map> response = new ArrayList<Map>();
		List<Book> availableBooks = bookRepo.findAll();
		
		for(Book individualBook: availableBooks){
			
			Map<String,Object> tempMap = new HashMap<>();
			tempMap.put("bookName", individualBook.getName());
			tempMap.put("bookId", individualBook.getId());
			
			if(individualBook.getIssuedBy() == -1){
				tempMap.put("bookStatus", "not issued");
			}else{
				tempMap.put("bookStatus", "issued by userID "+individualBook.getIssuedBy());
			}
			
			response.add(tempMap);
		}


		return response;
	}

	@PostMapping(REMOVE_BOOK_URL)
	public ResponseEntity<String> removeBook(@RequestBody Map<String, String> requestBody) {

		if(!requestBody.containsKey("id") || requestBody.get("id") == null ||requestBody.get("id").isEmpty()){
			return  ResponseEntity.badRequest().body("Please provide id of book");
		}
		
		if (bookRepo.existsById(Integer.parseInt(requestBody.get("id")))) {

			try {
				
				bookRepo.deleteById(Integer.valueOf((String) requestBody.get("id")));
			
			} catch (Exception e) {
				return ResponseEntity.badRequest().body("Bad Request");
			}
		} else {
			
			return ResponseEntity.badRequest().body("Book does not exist");
		
		}

		return ResponseEntity.ok().body("Book removed");
	}

}
