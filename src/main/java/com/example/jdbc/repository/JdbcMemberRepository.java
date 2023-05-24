package com.example.jdbc.repository;

import org.springframework.jdbc.datasource.DataSourceUtils;

import javax.sql.*;
import java.sql.*;
import java.util.*;

import com.example.jdbc.dto.MemberDto;
import com.example.jdbc.dto.MemberUpdateDto;
import com.example.jdbc.entity.Member;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class JdbcMemberRepository implements MemberRepository {

	private final DataSource dataSource;

	private String sql = "";
	private Connection conn = null;	// 접속정보
	private PreparedStatement pstmt = null;	// sql 구문을 실행하기 위한 객체
	private ResultSet rs = null;	// sql 실행 결과를 받아오는 객체

	// 스프링을 통해서 DB Connection 을 얻을 때는 반드시 DataSourceUtils 를 통해 얻어야한다
	// 이는 DB 커넥션 정보 유지를 위해서이다
	private Connection getConnection() {
		return DataSourceUtils.getConnection(dataSource);
	}

	@Override
	public List<Member> selectAll() {

		sql = "select id, pw, name from member";	// sql문

		try {
			conn = getConnection();	// DB 연동을 위한 커넥션 객체를 생성한다
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();	// 조회하기 위한 쿼리문

			List<Member> members = new ArrayList<>();
			while (rs.next()) {
				Member member = new Member();
				member.setId(rs.getString("id"));
				member.setPw(rs.getString("pw"));
				member.setName(rs.getString("name"));
			}

			return members;

		} catch (Exception e) {
			throw new IllegalStateException(e);
		} finally {
			close(conn, pstmt, rs);
		}
	}

	@Override
	public MemberDto save(MemberDto memberDto) {

		sql = "insert into member(id, pw, name) values(?, ?, ?)";

		try {
			conn = getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, memberDto.getId());
			pstmt.setString(2, memberDto.getPw());
			pstmt.setString(3, memberDto.getName());
			pstmt.executeUpdate();

			rs = pstmt.getGeneratedKeys();

			return memberDto;

		} catch (Exception e) {
			throw new IllegalStateException(e);
		} finally {
			close(conn, pstmt, rs);
		}
	}

	@Override
	public Member update(MemberUpdateDto memberUpdateDto, String id) {

		sql = "update member set id=?, pw=?, name=? where id=?";

		Member member = new Member();

		try {
			conn = getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, id);
			pstmt.setString(2, memberUpdateDto.getPw());
			pstmt.setString(3, memberUpdateDto.getName());
			pstmt.setString(4, id);
			pstmt.executeUpdate();

			rs = pstmt.getGeneratedKeys();

			member.setId(id);
			member.setPw(memberUpdateDto.getPw());
			member.setName(memberUpdateDto.getName());

			return member;

		} catch (Exception e) {
			throw new IllegalStateException(e);
		} finally {
			close(conn, pstmt, rs);
		}
	}

	@Override
	public List<Member> delete(String id) {

		sql = "delete from member where id=?";

		try {
			conn = getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, id);
			pstmt.executeUpdate();

			rs = pstmt.getGeneratedKeys();

			return selectAll();

		} catch (Exception e) {
			throw new IllegalStateException(e);
		} finally {
			close(conn, pstmt, rs);
		}
	}

	private void close(Connection conn, PreparedStatement pstmt, ResultSet rs) {
		try {
			if (rs != null) {
				rs.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		try {
			if (pstmt != null) {
				pstmt.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		try {
			if (conn != null) {
				close(conn);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private void close(Connection conn) throws SQLException {
		DataSourceUtils.releaseConnection(conn, dataSource);
	}
}
