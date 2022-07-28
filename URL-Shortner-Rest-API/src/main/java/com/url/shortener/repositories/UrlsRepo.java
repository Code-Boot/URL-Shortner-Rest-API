package com.url.shortener.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.url.shortener.entities.Urls;

public interface UrlsRepo extends JpaRepository<Urls, String> {
	Urls findByUniqueKey(String uniqueKey);

	Urls findByUrl(String url);
}
