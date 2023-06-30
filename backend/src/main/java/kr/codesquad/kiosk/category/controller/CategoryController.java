package kr.codesquad.kiosk.category.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.codesquad.kiosk.category.controller.dto.response.CategoryItemsResponse;
import kr.codesquad.kiosk.category.controller.dto.response.CategoryResponse;
import kr.codesquad.kiosk.category.service.CategoryService;
import kr.codesquad.kiosk.exception.ErrorResponse;
import lombok.RequiredArgsConstructor;

@Tag(name = "카테고리")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/categories")
public class CategoryController {
	private final CategoryService categoryService;

	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "카테고리 목록 조회 성공", content = @Content(schema = @Schema(implementation = CategoryResponse.class))),
		@ApiResponse(responseCode = "500", description = "데이터 연결 오류", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))})
	@Operation(summary = "카테고리 목록 조회", description = "카테고리 목록을 조회해서 반환합니다.")
	@GetMapping
	public ResponseEntity<List<CategoryResponse>> showCategories() {
		return ResponseEntity.ok().body(categoryService.getAllCategories());
	}

	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "카테고리의 메뉴 리스트 조회 성공", content = @Content(schema = @Schema(implementation = CategoryItemsResponse.class))),
		@ApiResponse(responseCode = "404", description = "잘못된 카테고리 id를 전달 받았을 때", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))})
	@Operation(summary = "카테고리별 메뉴 리스트 조회", description = "카테고리 id를 이용해 카테고리별 메뉴 리스트 조회해서 반환합니다.")
	@GetMapping("/{categoryId}")
	public ResponseEntity<CategoryItemsResponse> getCategoryItems(
		@Parameter(name = "categoryId", description = "카테고리 id") @PathVariable Integer categoryId) {
		return ResponseEntity.ok().body(categoryService.getCategoryItems(categoryId));
	}
}
