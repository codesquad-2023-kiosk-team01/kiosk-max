package kr.codesquad.kiosk.payment.controller.response;

import java.util.List;

public record PaymentsResponse (
	List<PaymentResponse> payments
) {}
