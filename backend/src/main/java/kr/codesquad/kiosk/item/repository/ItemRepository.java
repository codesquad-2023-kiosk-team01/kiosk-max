package kr.codesquad.kiosk.item.repository;

import kr.codesquad.kiosk.item.domain.Item;
import kr.codesquad.kiosk.item.domain.Options;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.*;

@RequiredArgsConstructor
@Repository
public class ItemRepository {
	private final NamedParameterJdbcTemplate jdbcTemplate;
	private final RowMapper<Item> itemRowMapper = (rs, rowNum) -> new Item(
			rs.getInt("id"),
			rs.getString("name"),
			rs.getInt("price"),
			rs.getString("image"),
			rs.getString("description"),
			rs.getInt("category_id")
	);

	public Optional<Item> findById(Integer id) {
		try {
			return Optional.ofNullable(
					jdbcTemplate.queryForObject("SELECT id, name, price, image, description, category_id FROM item WHERE id = :id",
							Map.of("id", id),
							itemRowMapper));
		} catch (EmptyResultDataAccessException ex) {
			return Optional.empty();
		}
	}

	private record ItemOptionDto(
			String optionTypeName,
			String optionName,
			Integer optionId
	) {
	}

	public Map<String, List<Options>> findOptionsByItemId(Integer itemId) {
		List<ItemOptionDto> itemOptionDtos = jdbcTemplate.query("SELECT ot.name AS option_type_name, o.name AS option_name, o.id AS option_id " +
						"FROM item_option io " +
						"JOIN options o ON io.options_id = o.id " +
						"JOIN option_type ot ON o.option_type_id = ot.id " +
						"WHERE io.item_id = :itemId",
				Map.of("itemId", itemId),
				(rs, row) -> new ItemOptionDto(
						rs.getString("option_type_name"),
						rs.getString("option_name"),
						rs.getInt("option_id")
				));

		return createOptionMap(itemOptionDtos);
	}

	private Map<String, List<Options>> createOptionMap(List<ItemOptionDto> itemOptionDtos) {
		Map<String, List<Options>> map = new HashMap<>();
		itemOptionDtos.forEach(itemOption -> map.merge(itemOption.optionTypeName,
				List.of(new Options(itemOption.optionId, itemOption.optionName)),
				(v1, v2) -> {
					List<Options> options = new ArrayList<>();
					options.addAll(v1);
					options.addAll(v2);
					return options;
				}));
		return map;
	}
}
