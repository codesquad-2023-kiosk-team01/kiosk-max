package kr.codesquad.kiosk.category.repository;

import kr.codesquad.kiosk.category.controller.dto.ItemResponse;
import kr.codesquad.kiosk.category.domain.Category;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Repository
public class CategoryRepository {
	private final NamedParameterJdbcTemplate jdbcTemplate;
	private final RowMapper<Category> categoryRowMapper = ((rs, rowNum) -> new Category(rs.getInt("id"),
			rs.getString("name")));
	private final RowMapper<ItemResponse> itemResponseRowMapper = ((rs, rowNum) -> new ItemResponse(
			rs.getInt("id"),
			rs.getString("name"),
			rs.getInt("price"),
			rs.getString("image")
	));

	public List<Category> findAll() {
		try {
			return Collections.unmodifiableList(jdbcTemplate.query("SELECT id, name FROM category", categoryRowMapper));
		} catch (EmptyResultDataAccessException e) {
			return List.of();
		}
	}

	public List<ItemResponse> findItemsByCategoryId(Integer categoryId) {
		final String sql = "SELECT i.id, i.name, i.price, i.image, " +
				"CASE WHEN oi.total_quantity is null THEN 0 ELSE oi.total_quantity END AS total_quantity " +
				"FROM item as i " +
				"LEFT JOIN (" +
				"SELECT item_id, SUM(item_quantity) AS total_quantity " +
				"FROM order_item " +
				"JOIN orders ON order_item.orders_id = orders.id " +
				"WHERE DATE(orders.order_date) = CURDATE()" +
				"GROUP BY item_id) AS oi ON i.id = oi.item_id " +
				"JOIN category AS c ON i.category_id = c.id " +
				"WHERE c.id = :categoryId " +
				"ORDER BY total_quantity DESC, i.id";

		return jdbcTemplate.query(sql, Map.of("categoryId", categoryId), itemResponseRowMapper);
	}

	public List<Integer> findTop3ItemsByCategoryId(Integer categoryId) {
		return jdbcTemplate.query("SELECT i.id, coalesce(SUM(oi.item_quantity), 0) as total_quantity " +
				"FROM item i " +
				"LEFT JOIN order_item oi ON i.id = oi.item_id " +
				"WHERE i.category_id = :categoryId " +
				"GROUP BY i.id " +
				"ORDER BY total_quantity DESC, i.id " +
				"LIMIT 3", Map.of("categoryId", categoryId), (rs, rowNum) -> rs.getInt("id"));
	}
}
