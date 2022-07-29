package com.url.shortener.services;

import com.google.common.base.Preconditions;
import com.url.shortener.entities.Urls;
import com.url.shortener.exceptions.URLNotFoundException;
import com.url.shortener.payloads.UrlsDto;
import com.url.shortener.repositories.UrlsRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UrlsServiceImpl implements UrlsService {

	@Autowired
	UrlsRepo urlsRepo;

	@Value("${unique.key.length}")
	Integer uniqueKeyLen;

	@Value("${api.url}")
	String apiUrl;

	@Value("${alphanumeric.string}")
	String AlphaNumericString;

	private static final UrlsDto uDto = new UrlsDto();
	private static Urls url;
	private static String uniqueKey;


	/**
	 * This method creates shorten Url for the url that is passed
	 * @param urlsDto - It has Url that needs to be shortened
	 * @return UrlsDto
	 */
	@Override
	public UrlsDto createShortUrl(UrlsDto urlsDto) {
		try {
			log.info("Validating url: {}", urlsDto.getUrl());
			log.info("START: Creating Shorten Url for : {}", urlsDto.getUrl());
			// Find url if present
			url = urlsRepo.findByUrl(urlsDto.getUrl());

			if (url == null) {
				log.info("Generating a unique key for url passed");
				uniqueKey = getAlphaNumericString();
				//Initialise url
				url = new Urls();

				BeanUtils.copyProperties(urlsDto, url);
				url.setUniqueKey(uniqueKey);
				urlsRepo.save(url);
			}

			BeanUtils.copyProperties(url, uDto);
			uDto.setShortenUrl(apiUrl + url.getUniqueKey());
			log.info("End: Created Shorten Url: {} for : {}", uDto.getShortenUrl(), uDto.getUrl());

			return uDto;
		} catch (Exception e) {
			log.error("Exception occurred while creating shorten url: {}", e.getMessage());
			throw new URLNotFoundException(e.getMessage());
		}

	}

	@Override
	public UrlsDto getFullUrl(String uniqueKey) {
		try {
			log.info("Getting Url for uniqueKey: {}", uniqueKey);
			Urls savedUrl = urlsRepo.findByUniqueKey(uniqueKey);
			Preconditions.checkNotNull(savedUrl, "No url found against uniqueKey ->", uniqueKey);

			BeanUtils.copyProperties(savedUrl, uDto);
			uDto.setShortenUrl(apiUrl + savedUrl.getUniqueKey());
			log.info("Url for UniqueKey: {} is : {}", uniqueKey, uDto.getUrl());

			return uDto;
		} catch (Exception e) {
			log.error("Exception occurred while getting the url: {}", e.getMessage());
			throw new URLNotFoundException(e.getMessage());
		}

	}

	private String getAlphaNumericString() {
		log.info("START: Creating UniqueKey");
		StringBuilder sb = new StringBuilder(uniqueKeyLen);
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
