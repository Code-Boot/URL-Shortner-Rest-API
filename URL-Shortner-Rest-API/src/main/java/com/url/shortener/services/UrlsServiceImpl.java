package com.url.shortener.services;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.url.shortener.entities.Urls;
import com.url.shortener.exceptions.URLNotFoundException;
import com.url.shortener.payloads.UrlsDto;
import com.url.shortener.repositories.UrlsRepo;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
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
		log.info("START: Creating Shorten Url for : {}", urlsDto.getUrl());
		Urls url = urlsRepo.findByUrl(urlsDto.getUrl());
		// TODO : Add log to print url value : To be handled in enhancement pr
		if (url == null) {
			String uniqueKey = getAlphaNumericString();
			url = mapper.map(urlsDto, Urls.class);
			url.setUniqueKey(uniqueKey);
			url = urlsRepo.save(url);
		}
		UrlsDto uDto = mapper.map(url, UrlsDto.class);
		uDto.setShortenUrl(apiUrl + url.getUniqueKey());
		log.info("End: Creating Shorten Url: {} for : {}", uDto.getShortenUrl(), uDto.getUrl());
		return uDto;
	}

	@Override
	public UrlsDto getFullUrl(String uniqueKey) {
		log.info("Getting Url for uniqueKey: {}", uniqueKey);
		Urls savedUrl = urlsRepo.findByUniqueKey(uniqueKey);
		if (savedUrl == null) {
			log.error("No url found against uniqueKey: {}", uniqueKey);
			throw new URLNotFoundException(apiUrl + uniqueKey + " does not exists.");
		}

		UrlsDto uDto = mapper.map(savedUrl, UrlsDto.class);
		uDto.setShortenUrl(apiUrl + savedUrl.getUniqueKey());
		log.info("Url for UniqueKey: {} is : {}", uniqueKey, uDto.getUrl());
		return uDto;
	}

	private String getAlphaNumericString() {
		log.info("START: Creating UniqueKey");
		StringBuilder sb = new StringBuilder(uniqueKeyLen);
		// TODO: Add try catch if any error is encountered during key creation
		do {
			for (int i = 0; i < uniqueKeyLen; i++) {
				int index = (int) (AlphaNumericString.length() * Math.random());
				sb.append(AlphaNumericString.charAt(index));
			}
		} while (!isKeyUnique(sb.toString()));

		log.info("END: Created uniqueKey: {}", sb);
		return sb.toString();
	}

	private boolean isKeyUnique(String str) {
		return urlsRepo.findByUniqueKey(str) == null;
	}

}
