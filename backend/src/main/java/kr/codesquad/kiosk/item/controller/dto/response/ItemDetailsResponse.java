package kr.codesquad.kiosk.item.controller.dto.response;

import kr.codesquad.kiosk.item.domain.Item;

import java.util.List;
import java.util.Map;

public record ItemDetailsResponse(
		Integer id,
		String name,
		Integer price,
		String description,
		String image,
		Map<String, List<String>> options
) {
	public static ItemDetailsResponse from(Item item, Map<String, List<String>> options) {
		return new ItemDetailsResponse(
				item.getId(),
				item.getName(),
				item.getPrice(),
				item.getDescription(),
				item.getImage(),
				options);
	}
}
