package com.example.jdbc.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.example.jdbc.dto.MemberDto;
import com.example.jdbc.dto.MemberUpdateDto;
import com.example.jdbc.entity.Member;

@Repository
public interface MemberRepository {
	List<Member> selectAll();
	MemberDto save(MemberDto memberDto);
	Member update(MemberUpdateDto memberUpdateDto, String id);
	List<Member> delete(String id);
}
