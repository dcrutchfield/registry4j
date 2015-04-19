package org.bower.registry.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name = "packages")
public class Package {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(nullable=false, updatable=false)
	private long id;
	
	@NotNull
	@Column(nullable = false, unique = true)
	private String name;
	
	@NotNull
	@Column(nullable = false)
	private String url;
	
	@NotNull
	@DateTimeFormat(style = "SS")
	@Temporal(TemporalType.TIMESTAMP)
	@Column(nullable = false, name = "created_at")
	private Date createdAt;
	
	private int hits = 0;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	@PreUpdate
	public void setCreatedAtDate() {
		this.createdAt = new Date();
	}

	public int getHits() {
		return hits;
	}

	public void setHits(int hits) {
		this.hits = hits;
	}
	
	@Override
	public String toString() {
		return String.format("Package[id=%d, name=%s, url=%s, created_at=%s, hits=%d]", id, name, url, createdAt, hits);
	}

	@Override
	public int hashCode() {
		return HashCodeBuilder.reflectionHashCode(this);
	}

	@Override
	public boolean equals(Object obj) {
		return EqualsBuilder.reflectionEquals(this, obj);
	}
}