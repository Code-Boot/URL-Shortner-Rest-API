package com.url.shortener.payloads;

import lombok.Data;
import org.hibernate.validator.constraints.URL;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

@Data
@NoArgsConstructor
public class UrlsDto {
	@URL(message = "Invalid url pattern.")
	@NotEmpty(message = "Invalid url")
	private String url;

	private String shortenUrl;
}
