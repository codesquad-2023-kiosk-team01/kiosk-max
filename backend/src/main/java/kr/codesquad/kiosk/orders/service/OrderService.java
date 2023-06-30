package kr.codesquad.kiosk.orders.service;

import java.util.List;
import java.util.Random;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.codesquad.kiosk.exception.BusinessException;
import kr.codesquad.kiosk.exception.ErrorCode;
import kr.codesquad.kiosk.orders.controller.dto.OrderItemResponse;
import kr.codesquad.kiosk.orders.controller.dto.OrdersResponse;
import kr.codesquad.kiosk.orders.controller.dto.request.OrderReceiptRequest;
import kr.codesquad.kiosk.orders.controller.dto.response.OrderReceiptResponse;
import kr.codesquad.kiosk.orders.controller.dto.response.OrdersIdResponse;
import kr.codesquad.kiosk.orders.domain.OrderResultType;
import kr.codesquad.kiosk.orders.repository.OrderRepository;
import kr.codesquad.kiosk.payment.domain.PaymentType;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class OrderService {
	private final OrderRepository orderRepository;
	private final Random random = new Random();

	@Transactional(readOnly = true)
	public OrderReceiptResponse getReceipt(Integer orderId) {
		OrdersResponse ordersResponse = orderRepository.findOrdersResponseByOrderId(orderId)
			.orElseThrow(() -> new BusinessException(ErrorCode.ORDER_NOT_FOUND));
		List<OrderItemResponse> itemResponses = orderRepository.findOrderItemResponsesByOrderId(orderId);

		return OrderReceiptResponse.from(orderId, itemResponses, ordersResponse);
	}

	@Transactional
	public OrdersIdResponse createOrder(OrderReceiptRequest request, OrderResultType orderResultType) {
		if (!PaymentType.isValid(request.toOrders().getPaymentId())) {
			throw new BusinessException(ErrorCode.PAYMENTS_NOT_FOUND);
		}

		if (orderResultType == OrderResultType.SUCCESS) {
			return new OrdersIdResponse(
				orderRepository.saveOrder(request.toOrders())
			);
		}

		throw OrderResultType.getBusinessException(orderResultType);
	}

	@Transactional
	public OrdersIdResponse createOrderWithDelayAndRandomSucceed(OrderReceiptRequest request) {
		causePaymentDelay(request.getPaymentType());
		return createOrder(request, determineOrderResultType(request.getPaymentType()));
	}

	/**
	 * 현재 사용하지 않는 메서드입니다.
	 * 백엔드단에서 지연 응답을 구현한 것을 프론트엔드단에서 처리하지 못할 경우 사용될 예정입니다.
	 */
	@Transactional
	public OrdersIdResponse createOrderWithNonDelayAndRandomSucceed(OrderReceiptRequest request) {
		return createOrder(request, determineOrderResultType(request.getPaymentType()));
	}

	@Transactional
	public OrdersIdResponse createOrderWithNonDelayAndAlwaysSucceed(OrderReceiptRequest request) {
		return createOrder(request, OrderResultType.SUCCESS);
	}

	@Transactional
	public OrdersIdResponse createOrderWithNonDelayAndAlwaysFail(OrderReceiptRequest request) {
		return createOrder(request, OrderResultType.NETWORK_ERROR);
	}

	private void causePaymentDelay(PaymentType paymentType) {
		if (PaymentType.CARD_PAYMENT != paymentType) {
			return;
		}

		int randomInt = random.nextInt(7) + 1;
		try {
			Thread.sleep(randomInt * 1000);
		} catch (InterruptedException e) {
			throw OrderResultType.getBusinessException(OrderResultType.RESPONSE_DELAY);
		}
	}

	private OrderResultType determineOrderResultType(PaymentType paymentType) {
		if (PaymentType.CARD_PAYMENT != paymentType) {
			return OrderResultType.SUCCESS;
		}

		return OrderResultType.getRandomOrderResultType();
	}
}
