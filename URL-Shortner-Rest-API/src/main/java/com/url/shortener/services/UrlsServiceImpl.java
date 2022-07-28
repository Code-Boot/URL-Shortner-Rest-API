package com.url.shortener.services;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.url.shortener.entities.Urls;
import com.url.shortener.exceptions.URLNotFoundException;
import com.url.shortener.payloads.UrlsDto;
import com.url.shortener.repositories.UrlsRepo;

@Service
public class UrlsServiceImpl implements UrlsService {

	@Autowired
	ModelMapper mapper;

	@Autowired
	UrlsRepo urlsRepo;

	@Value("${unique.key.length}")
	Integer uniqueKeyLen;

	@Value("${api.url}")
	String apiUrl;

	@Value("${alphanumeric.string}")
	String AlphaNumericString;

	@Override
	public UrlsDto createShortUrl(UrlsDto urlsDto) {
		Urls url = urlsRepo.findByUrl(urlsDto.getUrl());
		if (url == null) {
			String uniqueKey = getAlphaNumericString();
			url = mapper.map(urlsDto, Urls.class);
			url.setUniqueKey(uniqueKey);
			url = urlsRepo.save(url);
		}
		UrlsDto uDto = mapper.map(url, UrlsDto.class);
		uDto.setShortenUrl(apiUrl + url.getUniqueKey());
		return uDto;
	}

	@Override
	public UrlsDto getFullUrl(String uniqueKey) {
		Urls savedUrl = urlsRepo.findByUniqueKey(uniqueKey);
		if (savedUrl == null)
			throw new URLNotFoundException(apiUrl + uniqueKey + " does not exists.");
		UrlsDto uDto = mapper.map(savedUrl, UrlsDto.class);
		uDto.setShortenUrl(apiUrl + savedUrl.getUniqueKey());
		return uDto;
	}

	private String getAlphaNumericString() {
		StringBuilder sb = new StringBuilder(uniqueKeyLen);
		do {
			for (int i = 0; i < uniqueKeyLen; i++) {
				int index = (int) (AlphaNumericString.length() * Math.random());
				sb.append(AlphaNumericString.charAt(index));
			}
		} while (!isKeyUnique(sb.toString()));

		return sb.toString();
	}

	private boolean isKeyUnique(String str) {
		return urlsRepo.findByUniqueKey(str) == null;
	}

}
