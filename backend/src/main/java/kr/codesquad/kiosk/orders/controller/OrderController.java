package kr.codesquad.kiosk.orders.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.codesquad.kiosk.exception.ErrorResponse;
import kr.codesquad.kiosk.orders.controller.dto.request.OrderReceiptRequest;
import kr.codesquad.kiosk.orders.controller.dto.response.OrderReceiptResponse;
import kr.codesquad.kiosk.orders.controller.dto.response.OrdersIdResponse;
import kr.codesquad.kiosk.orders.service.OrderService;
import lombok.RequiredArgsConstructor;

@Tag(name = "주문")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/orders")
public class OrderController {
	private final OrderService orderService;

	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "영수증 내역 조회 성공", content = @Content(schema = @Schema(implementation = OrderReceiptResponse.class))),
		@ApiResponse(responseCode = "404", description = "잘못된 주문 id를 전달 받았을 때", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))})
	@Operation(summary = "영수증 내역 조회", description = "주문 id를 이용해 영수증 내역을 조회해서 반환합니다.")
	@GetMapping("/{orderId}")
	public ResponseEntity<OrderReceiptResponse> getReceipt(
		@Parameter(name = "orderId", description = "주문 id") @PathVariable Integer orderId) {
		return ResponseEntity.ok()
			.body(orderService.getReceipt(orderId));
	}

	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "주문 생성 성공", content = @Content(schema = @Schema(implementation = OrderReceiptResponse.class))),
		@ApiResponse(responseCode = "404", description = "잘못된 주문 내역을 전달 받았을 때", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
		@ApiResponse(responseCode = "403", description = "사용자의 카드가 한도 초과일 때", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
		@ApiResponse(responseCode = "403", description = "사용자 카드의 마그네틱을 인식할 수 없을 때", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
		@ApiResponse(responseCode = "503", description = "네트워크 오류로 결제를 진행할 수 없을 때", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))})
	@Operation(summary = "주문 생성", description = "주문 내역을 받아와 주문을 생성한다.")
	@PostMapping
	public ResponseEntity<OrdersIdResponse> createOrder(@RequestBody OrderReceiptRequest request) {
		return ResponseEntity.ok()
			.body(orderService.createOrderWithDelayAndRandomSucceed(request));
	}
}
