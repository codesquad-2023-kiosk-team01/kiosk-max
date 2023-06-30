package kr.codesquad.kiosk.item.controller.dto.response;

import java.util.List;
import java.util.Map;

import kr.codesquad.kiosk.item.domain.Item;

public record ItemDetailsResponse(
	Integer id,
	String name,
	Integer price,
	String description,
	String image,
	Map<String, List<OptionsResponse>> options
) {
	public static ItemDetailsResponse from(Item item, Map<String, List<OptionsResponse>> options) {
		return new ItemDetailsResponse(
			item.getId(),
			item.getName(),
			item.getPrice(),
			item.getDescription(),
			item.getImage(),
			options);
	}
}
