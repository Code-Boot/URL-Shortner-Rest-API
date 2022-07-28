package com.url.shortener.services;

import com.url.shortener.payloads.UrlsDto;

public interface UrlsService {
	UrlsDto createShortUrl(UrlsDto urlsDto);

	UrlsDto getFullUrl(String uniqueKey);
}
