package kr.codesquad.kiosk.payment.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.codesquad.kiosk.exception.ErrorResponse;
import kr.codesquad.kiosk.payment.controller.response.PaymentsResponse;
import kr.codesquad.kiosk.payment.service.PaymentService;
import lombok.RequiredArgsConstructor;

@Tag(name = "결제")
@RestController
@RequiredArgsConstructor
public class PaymentController {
	private final PaymentService paymentService;

	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "결제 내역 조회 성공", content = @Content(schema = @Schema(implementation = PaymentsResponse.class))),
		@ApiResponse(responseCode = "404", description = "결제 내역을 조회할 수 없을 때", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))})
	@Operation(summary = "결제 내역 조회", description = "결제 내역 조회하여 반환한다.")
	@GetMapping("/api/payments")
	public ResponseEntity<PaymentsResponse> getPayments() {
		return ResponseEntity.ok()
			.body(new PaymentsResponse(paymentService.getPayments()));
	}
}
