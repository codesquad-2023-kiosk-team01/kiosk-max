package kr.codesquad.kiosk.orders.controller.dto.request;

public record OrdersRequest(
	int paymentId,
	int amount
) {
}
