package kr.codesquad.kiosk.orders.controller.dto;

import java.util.List;
import java.util.Map;

public record OrderItemResponse(
		String name,
		Integer quantity,
		Integer price,
		List<Map<String, OptionDetailsParam>> options
) {
}
