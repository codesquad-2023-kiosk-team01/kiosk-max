package kr.codesquad.kiosk.category.repository;

import java.util.Collections;
import java.util.List;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import kr.codesquad.kiosk.category.domain.Category;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Repository
public class CategoryRepository {
	private final NamedParameterJdbcTemplate jdbcTemplate;
	private final RowMapper<Category> categoryRowMapper = ((rs, rowNum) -> new Category(rs.getInt("id"),
		rs.getString("name")));

	public List<Category> findAll() {
		try {
			return Collections.unmodifiableList(jdbcTemplate.query("SELECT id, name FROM category", categoryRowMapper));
		} catch (EmptyResultDataAccessException e) {
			return List.of();
		}
	}
}
