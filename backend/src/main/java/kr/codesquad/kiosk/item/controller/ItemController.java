package kr.codesquad.kiosk.item.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.codesquad.kiosk.exception.ErrorResponse;
import kr.codesquad.kiosk.item.controller.dto.response.ItemDetailsResponse;
import kr.codesquad.kiosk.item.service.ItemService;
import lombok.RequiredArgsConstructor;

@Tag(name = "메뉴")
@RequiredArgsConstructor
@RestController
public class ItemController {
	private final ItemService itemService;

	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "메뉴 상세 내역 조회 성공", content = @Content(schema = @Schema(implementation = ItemDetailsResponse.class))),
		@ApiResponse(responseCode = "404", description = "잘못된 메뉴 id를 전달 받았을 때", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))})
	@Operation(summary = "메뉴 상세 내역 조회", description = "메뉴 id를 이용해 메뉴 상세 내역을 조회해서 반환합니다.")
	@GetMapping("/api/categories/{categoryId}/items/{itemId}")
	public ResponseEntity<ItemDetailsResponse> getItemDetails(
		@Parameter(name = "itemId", description = "메뉴 id") @PathVariable Integer itemId) {
		return ResponseEntity.ok()
			.body(itemService.getItemDetails(itemId));
	}
}
