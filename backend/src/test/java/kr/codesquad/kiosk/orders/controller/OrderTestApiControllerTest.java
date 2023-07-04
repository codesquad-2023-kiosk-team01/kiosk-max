package kr.codesquad.kiosk.orders.controller;

import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import kr.codesquad.kiosk.exception.BusinessException;
import kr.codesquad.kiosk.exception.ErrorCode;
import kr.codesquad.kiosk.fixture.FixtureFactory;
import kr.codesquad.kiosk.orders.controller.dto.request.OrderReceiptRequest;
import kr.codesquad.kiosk.orders.controller.dto.response.OrdersIdResponse;
import kr.codesquad.kiosk.orders.service.OrderService;

@WebMvcTest(controllers = OrderTestApiController.class)
class OrderTestApiControllerTest {
	@Autowired
	MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@MockBean
	OrderService orderService;

	@DisplayName("결제 요청(주문)이 들어오면 주문 정보를 등록한 후 주문 id를 반환한다.")
	@Test
	void whenCreateOrder_thenResponse200OK() throws Exception {
		// given
		OrderReceiptRequest orderReceiptRequest = FixtureFactory.createOrderReceiptRequest();
		OrdersIdResponse ordersIdResponse = FixtureFactory.createOrdersIdResponse();
		given(orderService.createOrderWithNonDelayAndAlwaysSucceed(orderReceiptRequest)).willReturn(
			ordersIdResponse);

		// when & then
		mockMvc.perform(
				post("/test-api/orders-success")
					.contentType(MediaType.APPLICATION_JSON)
					.content(objectMapper.writeValueAsString(orderReceiptRequest))
					.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.ordersId").value(ordersIdResponse.ordersId()));
	}

	@DisplayName("결제 요청(주문) 실패시 503 에러를 반환한다.")
	@Test
	void whenCreateOrderFail_thenResponse503ServiceUnavailable() throws Exception {
		OrderReceiptRequest orderReceiptRequest = FixtureFactory.createOrderReceiptRequest();
		when(orderService.createOrderWithNonDelayAndAlwaysFail(orderReceiptRequest))
			.thenThrow(new BusinessException(ErrorCode.NETWORK_FAIN_ERROR));

		mockMvc.perform(
				post("/test-api/orders-failure")
					.contentType(MediaType.APPLICATION_JSON)
					.content(objectMapper.writeValueAsString(orderReceiptRequest))
					.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().is5xxServerError())
			.andExpect(jsonPath("$.message").exists());
	}

}
