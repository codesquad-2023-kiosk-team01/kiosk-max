package kr.codesquad.kiosk.orders.controller;

import kr.codesquad.kiosk.exception.BusinessException;
import kr.codesquad.kiosk.exception.ErrorCode;
import kr.codesquad.kiosk.fixture.FixtureFactory;
import kr.codesquad.kiosk.orders.controller.dto.OrderItemResponse;
import kr.codesquad.kiosk.orders.controller.dto.OrdersResponse;
import kr.codesquad.kiosk.orders.controller.dto.response.OrderReceiptResponse;
import kr.codesquad.kiosk.orders.service.OrderService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.BDDMockito.anyInt;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = OrderController.class)
class OrderControllerTest {
	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private OrderService orderService;


	@DisplayName("올바른 주문 번호가 url parameter로 주어지면 영수증 정보를 조회할 수 있다.")
	@Test
	void whenGetReceipt_thenResponse200OK() throws Exception {
		// given
		OrderItemResponse orderItemResponse = FixtureFactory.createOrderItemResponse();
		OrdersResponse ordersResponse = FixtureFactory.createOrdersResponse();
		given(orderService.getReceipt(anyInt()))
				.willReturn(OrderReceiptResponse.from(1, List.of(orderItemResponse), ordersResponse));

		// when & then
		mockMvc.perform(get("/api/orders/1"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.items").exists())
				.andExpect(jsonPath("$['items'][*]['name']").exists())
				.andExpect(jsonPath("$['items'][*]['quantity']").exists())
				.andExpect(jsonPath("$['items'][*]['price']").exists())
				.andExpect(jsonPath("$['items'][*]['options']").exists())
				.andExpect(jsonPath("$.payments").exists())
				.andExpect(jsonPath("$.amount").exists())
				.andExpect(jsonPath("$.total").exists())
				.andExpect(jsonPath("$.remain").exists())
				.andDo(print());
	}

	@DisplayName("영수증 정보를 조회할 때 잘못된 주문번호가 주어지면 404 NOT FOUND를 응답한다.")
	@Test
	void givenWrongOrderId_whenGetReceipt_thenResponse404NotFound() throws Exception {
		// given
		given(orderService.getReceipt(anyInt())).willThrow(new BusinessException(ErrorCode.ORDER_NOT_FOUND));

		// when & then
		mockMvc.perform(get("/api/orders/1"))
				.andExpect(status().isNotFound())
				.andExpect(jsonPath("$.message").value("해당 주문을 찾을 수 없습니다."));
	}
}
