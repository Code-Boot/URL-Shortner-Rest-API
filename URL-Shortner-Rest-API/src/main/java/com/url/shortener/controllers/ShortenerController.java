package com.url.shortener.controllers;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.url.shortener.payloads.UrlsDto;
import com.url.shortener.services.UrlsService;

@RestController
@RequestMapping(value = "/api")
public class ShortenerController {

	@Autowired
	UrlsService urlsService;

	@PostMapping(value = "/")
	public ResponseEntity<UrlsDto> createShortUrl(@Valid @RequestBody UrlsDto urlsDto) {
		return new ResponseEntity<UrlsDto>(urlsService.createShortUrl(urlsDto), HttpStatus.CREATED);
	}

	@GetMapping(value = "/{uniqueKey}")
	public ResponseEntity<UrlsDto> getFullUrl(@PathVariable("uniqueKey") String uniqueKey) {
		return new ResponseEntity<UrlsDto>(urlsService.getFullUrl(uniqueKey), HttpStatus.OK);
	}
}
