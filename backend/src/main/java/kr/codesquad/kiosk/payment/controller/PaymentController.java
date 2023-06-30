package kr.codesquad.kiosk.payment.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import kr.codesquad.kiosk.payment.controller.response.PaymentsResponse;
import kr.codesquad.kiosk.payment.service.PaymentService;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class PaymentController {
	private final PaymentService paymentService;

	@GetMapping("/api/payments")
	public ResponseEntity<PaymentsResponse> getPayments() {
		return ResponseEntity.ok()
			.body(new PaymentsResponse(paymentService.getPayments()));
	}
}
