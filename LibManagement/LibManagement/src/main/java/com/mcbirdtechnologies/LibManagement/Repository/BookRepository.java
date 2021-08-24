package com.mcbirdtechnologies.LibManagement.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.mcbirdtechnologies.LibManagement.Models.Book;

public interface BookRepository extends JpaRepository<Book, Integer>{

	@Query(value = "SELECT * FROM books WHERE issued_by = -1",nativeQuery=true)
	public List<Book> findAvailableBooks( );

	@Query(value = "SELECT * FROM books WHERE issued_by = ?",nativeQuery=true)
	public List<Book> findIssuedBooks(int username);
	
}
