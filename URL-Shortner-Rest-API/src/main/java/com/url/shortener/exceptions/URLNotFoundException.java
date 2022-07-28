package com.url.shortener.exceptions;

public class URLNotFoundException extends RuntimeException {
	public URLNotFoundException(String message) {
		super(message);
	}
}
