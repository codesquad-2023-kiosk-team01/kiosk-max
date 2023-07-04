package kr.codesquad.kiosk.orders.service;

import kr.codesquad.kiosk.exception.BusinessException;
import kr.codesquad.kiosk.exception.ErrorCode;
import kr.codesquad.kiosk.fixture.FixtureFactory;
import kr.codesquad.kiosk.orders.controller.dto.OptionDetailsParam;
import kr.codesquad.kiosk.orders.controller.dto.OrderItemResponse;
import kr.codesquad.kiosk.orders.controller.dto.response.OrderReceiptResponse;
import kr.codesquad.kiosk.orders.repository.OrderRepository;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {
	@Mock
	private OrderRepository orderRepository;

	@InjectMocks
	private OrderService orderService;

	@DisplayName("영수증 정보를 얻을 때 주문번호가 주어지면 영수증 정보를 얻는데 성공한다.")
	@Test
	void givenOrderId_whenGetReceipt_thenReturnsOrderReceipt() {
		// given
		int orderId = 1;

		given(orderRepository.findOrdersResponseByOrderId(orderId))
				.willReturn(Optional.of(FixtureFactory.createOrdersResponse()));
		given(orderRepository.findOrderItemResponsesByOrderId(orderId))
				.willReturn(List.of(FixtureFactory.createOrderItemResponse()));

		// when
		OrderReceiptResponse receipt = orderService.getReceipt(orderId);

		// then
		SoftAssertions.assertSoftly(softAssertions -> {
			softAssertions.assertThat(receipt.items()).isEqualTo(List.of(new OrderItemResponse("콜드브루", 2, 10000,
					List.of(Map.of("Size", new OptionDetailsParam(1, "Large")),
							Map.of("Temperature", new OptionDetailsParam(3, "Hot"))))));
			softAssertions.assertThat(receipt.payments()).isEqualTo("card");
			softAssertions.assertThat(receipt.amount()).isEqualTo(10000);
			softAssertions.assertThat(receipt.total()).isEqualTo(10000);
			softAssertions.assertThat(receipt.remain()).isEqualTo(0);
		});
	}

	@DisplayName("영수증 정보를 얻을 때 잘못된 주문번호가 주어지면 예외를 던진다.")
	@Test
	void givenWrongOrderId_whenGetReceipt_thenThrowsException() {
		// given
		given(orderRepository.findOrdersResponseByOrderId(anyInt())).willReturn(Optional.empty());

		// when & then
		assertAll(
				() -> assertThatThrownBy(() -> orderService.getReceipt(9876))
						.isInstanceOf(BusinessException.class)
						.extracting("errorCode")
						.isEqualTo(ErrorCode.ORDER_NOT_FOUND),
				() -> then(orderRepository).should(times(1)).findOrdersResponseByOrderId(anyInt()),
				() -> then(orderRepository).should(times(0)).findOrderItemResponsesByOrderId(anyInt())
		);
	}
}
