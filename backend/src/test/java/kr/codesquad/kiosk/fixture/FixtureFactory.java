package kr.codesquad.kiosk.fixture;

import kr.codesquad.kiosk.category.domain.Category;
import kr.codesquad.kiosk.category.dto.response.CategoryResponse;
import kr.codesquad.kiosk.item.controller.dto.response.ItemDetailsResponse;
import kr.codesquad.kiosk.item.domain.Item;
import kr.codesquad.kiosk.item.domain.Options;
import kr.codesquad.kiosk.orders.controller.dto.OrderItemResponse;
import kr.codesquad.kiosk.orders.controller.dto.OrdersResponse;
import kr.codesquad.kiosk.payment.controller.response.PaymentResponse;
import kr.codesquad.kiosk.payment.domain.Payment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FixtureFactory {

	public static List<CategoryResponse> createCategoriesResponse() {
		List<Category> categories = createCategories();
		List<CategoryResponse> categoriesResponse = new ArrayList<>();

		for (Category category : categories) {
			CategoryResponse response = CategoryResponse.from(category.getId(), category.getName());
			categoriesResponse.add(response);
		}

		return categoriesResponse;
	}

	public static List<Category> createEmptyCategories() {
		return List.of();
	}

	public static Category createCategory(int id, String name) {
		return new Category(id, name);
	}

	public static List<Category> createCategories() {
		List<Category> categories = new ArrayList<>();
		categories.add(createCategory(1, "커피"));
		categories.add(createCategory(2, "라떼"));
		categories.add(createCategory(3, "티"));

		return categories;
	}

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

	public static OrdersResponse createOrdersResponse() {
		return new OrdersResponse("card", 10000, 10000, 0);
	}

	public static OrderItemResponse createOrderItemResponse() {
		return new OrderItemResponse("콜드브루", 2, 10000);
	}
}
