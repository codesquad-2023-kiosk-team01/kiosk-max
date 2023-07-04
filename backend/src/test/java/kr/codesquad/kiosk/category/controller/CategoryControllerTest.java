package kr.codesquad.kiosk.category.controller;

import kr.codesquad.kiosk.category.controller.dto.response.CategoryResponse;
import kr.codesquad.kiosk.category.service.CategoryService;
import kr.codesquad.kiosk.exception.BusinessException;
import kr.codesquad.kiosk.exception.ErrorCode;
import kr.codesquad.kiosk.fixture.FixtureFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.hasItems;
import static org.mockito.BDDMockito.anyInt;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = CategoryController.class)
class CategoryControllerTest {

	@Autowired
	MockMvc mockMvc;

	@MockBean
	CategoryService categoryService;

	@DisplayName("모든 카테고리 항목을 조회할 수 있다.")
	@Test
	void whenGetAllCategories_thenResponse200OK() throws Exception {
		// given
		List<CategoryResponse> response = FixtureFactory.createCategoriesResponse();
		when(categoryService.getAllCategories()).thenReturn(response);

		// when & then
		mockMvc.perform(
						get("/api/categories")
				)
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.length()").value(response.size()))
				.andExpect(jsonPath("$[*].id")
						.value(hasItems(response.stream().map(CategoryResponse::id).toArray())))
				.andExpect(jsonPath("$[*].name")
						.value(hasItems(response.stream().map(CategoryResponse::name).toArray())));
	}

	@DisplayName("카테고리 항목을 불러오지 못하면 500 Connection Error를 반환한다.")
	@Test
	void whenGetAllCategories_thenResponse500ConnectionError() throws Exception {
		// given
		when(categoryService.getAllCategories()).thenThrow(new BusinessException(ErrorCode.EMPTY_RESULT));

		// when & then
		mockMvc.perform(
						get("/api/categories")
				)
				.andDo(print())
				.andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.message").value(ErrorCode.EMPTY_RESULT.getDescription()));
	}

	@DisplayName("카테고리 아이디로 조회했을 때 해당 아이템 목록을 불러온다.")
	@Test
	void whenGetCategoryItems_thenResponse200OK() throws Exception {
		// given
		given(categoryService.getCategoryItems(anyInt())).willReturn(FixtureFactory.createCategoryItemsResponse());

		// when & then
		mockMvc.perform(
						get("/api/categories/1")
				)
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id").value(1))
				.andExpect(jsonPath("$.items").exists())
				.andExpect(jsonPath("$['items'][*]['id']").exists())
				.andExpect(jsonPath("$['items'][*]['name']").exists())
				.andExpect(jsonPath("$['items'][*]['price']").exists())
				.andExpect(jsonPath("$['items'][*]['isSignature']").exists())
				.andExpect(jsonPath("$['items'][*]['image']").exists());
	}

	@DisplayName("카테고리 항목을 불러오지 못하면 404 NOT_FOUND를 반환한다.")
	@Test
	void whenGetCategoryItems_then404NotFound() throws Exception {
		// given
		given(categoryService.getCategoryItems(anyInt()))
				.willThrow(new BusinessException(ErrorCode.CATEGORY_NOT_FOUND));

		// when & then
		mockMvc.perform(
						get("/api/categories/1")
				)
				.andDo(print())
				.andExpect(status().isNotFound())
				.andExpect(jsonPath("$.message").value(ErrorCode.CATEGORY_NOT_FOUND.getDescription()));
	}
}
