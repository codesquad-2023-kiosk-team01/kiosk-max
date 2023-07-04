package kr.codesquad.kiosk.orders.controller.dto;

public record OrdersResponse(
	String payments,
	Integer amount,
	Integer total,
	Integer remain
) {
}
