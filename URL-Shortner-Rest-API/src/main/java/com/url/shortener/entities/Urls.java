package com.url.shortener.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "urls")
@NoArgsConstructor
@Data
public class Urls {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	@Column(name = "url", nullable = false, unique = true)
	private String url;

	@Column(name = "uniqueKey", nullable = false, unique = true)
	private String uniqueKey;
}
