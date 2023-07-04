package kr.codesquad.kiosk.payment.controller.response;

import kr.codesquad.kiosk.payment.domain.Payment;

public record PaymentResponse(
	Integer id,
	String name,
	String image
) {
	public static PaymentResponse from(Payment payment) {
		return
			new PaymentResponse(
				payment.getId(),
				payment.getName(),
				payment.getImage()
			);
	}
}
