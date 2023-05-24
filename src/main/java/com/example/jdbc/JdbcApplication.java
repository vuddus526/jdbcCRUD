package com.example.jdbc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.example.jdbc.repository.JdbcMemberRepository;
import com.example.jdbc.repository.MemberRepository;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@SpringBootApplication
public class JdbcApplication {
	public static void main(String[] args) {
		// SpringApplication.run(JdbcApplication.class, args);

	}
}
