package kr.codesquad.kiosk.orders.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Hidden;
import kr.codesquad.kiosk.orders.controller.dto.request.OrderReceiptRequest;
import kr.codesquad.kiosk.orders.controller.dto.response.OrdersIdResponse;
import kr.codesquad.kiosk.orders.service.OrderService;
import lombok.RequiredArgsConstructor;

@Hidden
@RequiredArgsConstructor
@RestController
@RequestMapping("/test-api")
public class OrderTestApiController {
	private final OrderService orderService;

	@PostMapping("/orders-failure")
	public ResponseEntity<OrdersIdResponse> createOrderWithNonDelayAndAlwaysFail(
		@RequestBody OrderReceiptRequest request) {
		return ResponseEntity.ok()
			.body(orderService.createOrderWithNonDelayAndAlwaysFail(request));
	}

	@PostMapping("/orders-success")
	public ResponseEntity<OrdersIdResponse> createOrderWithNonDelayAndAlwaysSucceed(
		@RequestBody OrderReceiptRequest request) {
		return ResponseEntity.ok()
			.body(orderService.createOrderWithNonDelayAndAlwaysSucceed(request));
	}

}
