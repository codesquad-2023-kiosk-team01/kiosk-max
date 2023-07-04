package kr.codesquad.kiosk.payment.repository;

import java.util.Collections;
import java.util.List;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import kr.codesquad.kiosk.payment.domain.Payment;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Repository
public class PaymentRepository {
	private final NamedParameterJdbcTemplate jdbcTemplate;

	private final RowMapper<Payment> paymentRowMapper = (rs, rowNum) -> new Payment(
		rs.getInt("id"),
		rs.getString("name"),
		rs.getString("image")
	);

	public List<Payment> findAll() {
		try {
			String sql = "SELECT id, name, image FROM payment";
			return Collections.unmodifiableList(
				jdbcTemplate.query(
					sql,
					paymentRowMapper)
			);
		} catch (EmptyResultDataAccessException e) {
			return List.of();
		}
	}
}
