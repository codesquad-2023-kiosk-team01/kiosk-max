package kr.codesquad.kiosk.item.controller;

import kr.codesquad.kiosk.item.controller.dto.response.ItemDetailsResponse;
import kr.codesquad.kiosk.item.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class ItemController {
	private final ItemService itemService;

	@GetMapping("/api/categories/{categoryId}/items/{itemId}")
	public ResponseEntity<ItemDetailsResponse> getItemDetails(@PathVariable Integer itemId) {
		return ResponseEntity.ok()
				.body(itemService.getItemDetails(itemId));
	}
}
