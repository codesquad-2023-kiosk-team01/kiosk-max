package kr.codesquad.kiosk.category.controller;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import kr.codesquad.kiosk.category.dto.response.CategoryResponse;
import kr.codesquad.kiosk.category.service.CategoryService;
import kr.codesquad.kiosk.exception.BusinessException;
import kr.codesquad.kiosk.exception.ErrorCode;
import kr.codesquad.kiosk.fixture.FixtureFactory;

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
			.andExpect(jsonPath("$[*].id").value(hasItems(response.stream().map(CategoryResponse::id).toArray())))
			.andExpect(jsonPath("$[*].name").value(hasItems(response.stream().map(CategoryResponse::name).toArray())));
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
}
