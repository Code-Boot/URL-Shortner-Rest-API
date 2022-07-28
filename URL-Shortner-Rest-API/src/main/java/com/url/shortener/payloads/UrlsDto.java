package com.url.shortener.payloads;

import org.hibernate.validator.constraints.URL;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class UrlsDto {
	@URL(message = "Invalid url pattern.")
	private String url;

	private String shortenUrl;
}
