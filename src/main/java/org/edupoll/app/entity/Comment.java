package org.edupoll.app.entity;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Comment {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String commentContent;
	private LocalDate writedAt;
	
	@ManyToOne
	@JoinColumn(name = "account")
	private Account account;
	
	@ManyToOne
	@JoinColumn(name = "post")
	private Post post;
	
	
	@PrePersist
	public void prePersist() {
		this.writedAt = LocalDate.now();
	}
}
