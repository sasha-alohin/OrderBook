package com.softbistro.order;

import java.util.List;

public class Book {
	private String id;
	private String title;
	private String isbn;
	private String ean;
	private String imageUrl;
	private List<Object> authors;

	public Book(String id, String title, String isbn, String ean, String imageUrl, List<Object> authors) {
		this.id = id;
		this.title = title;
		this.isbn = isbn;
		this.ean = ean;
		this.imageUrl = imageUrl;
		this.authors = authors;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public String getEan() {
		return ean;
	}

	public void setEan(String ean) {
		this.ean = ean;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getIsbn() {
		return isbn;
	}

	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}

	public List<Object> getAuthors() {
		return authors;
	}

	public void setAuthors(List<Object> authors) {
		this.authors = authors;
	}

}
