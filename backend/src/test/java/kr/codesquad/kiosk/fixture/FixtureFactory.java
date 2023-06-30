package kr.codesquad.kiosk.fixture;

import kr.codesquad.kiosk.item.controller.dto.response.ItemDetailsResponse;
import kr.codesquad.kiosk.item.domain.Item;
import kr.codesquad.kiosk.item.domain.Options;
import kr.codesquad.kiosk.payment.controller.response.PaymentResponse;
import kr.codesquad.kiosk.payment.domain.Payment;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FixtureFactory {

	public static ItemDetailsResponse createItemDetailsResponse() {
		return ItemDetailsResponse.from(createItem(), createOptions());
	}

	public static Item createItem() {
		return new Item(1, "콜드 브루", 5000, "url", "description", 1);
	}

	public static Map<String, List<String>> createOptions() {
		Map<String, List<String>> map = new HashMap<>();
		map.put("Size", List.of("Small", "Medium", "Large"));
		map.put("Temperature", List.of("Ice"));

		return map;
	}

	public static Map<String, List<Options>> createOptionsMap() {
		return Map.of("Size", List.of(new Options(1, "Small"), new Options(2, "Medium"), new Options(3, "Large")),
			"Temperature", List.of(new Options(4, "Ice")));
	}

	public static List<PaymentResponse> createPaymentResponses() {
		return createPayments().stream()
			.map(PaymentResponse::from)
			.toList();
	}

	public static List<Payment> createPayments() {
		return List.of(
			new Payment(1, "카드 결제", "url"),
			new Payment(3, "현금 결제", "url")
		);
	}

	public static List<Payment> createEmptyPayments() {
		return List.of();
	}
}
