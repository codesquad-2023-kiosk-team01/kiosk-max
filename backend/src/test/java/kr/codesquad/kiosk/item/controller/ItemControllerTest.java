package kr.codesquad.kiosk.item.controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import kr.codesquad.kiosk.exception.BusinessException;
import kr.codesquad.kiosk.exception.ErrorCode;
import kr.codesquad.kiosk.fixture.FixtureFactory;
import kr.codesquad.kiosk.item.controller.dto.response.ItemDetailsResponse;
import kr.codesquad.kiosk.item.service.ItemService;

@WebMvcTest(controllers = ItemController.class)
class ItemControllerTest {

	@Autowired
	MockMvc mockMvc;

	@MockBean
	ItemService itemService;

	@DisplayName("아이템 아이디로 아이템 하나의 세부 항목을 조회할 수 있다.")
	@Test
	void givenItemId_whenGetItemDetails_thenResponse200OK() throws Exception {
		// given
		int itemId = 1;
		ItemDetailsResponse response = FixtureFactory.createItemDetailsResponse();
		when(itemService.getItemDetails(itemId)).thenReturn(response);

		// when & then
		mockMvc.perform(
				get("/api/categories/1/items/" + itemId)
			)
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.id").value(response.id()))
			.andExpect(jsonPath("$.name").exists())
			.andExpect(jsonPath("$.price").exists())
			.andExpect(jsonPath("$.description").exists())
			.andExpect(jsonPath("$.image").exists())
			.andExpect(jsonPath("$.options").exists());
	}

	@DisplayName("존재하지 않는 아이템 아이디로 아이템 하나의 세부 항목을 조회한 경우 404 Not Found를 반환한다.")
	@Test
	void givenDoesNotExistItemId_whenGetItemDetails_thenResponse404NotFound() throws Exception {
		int itemId = 99999999;
		when(itemService.getItemDetails(itemId)).thenThrow(new BusinessException(ErrorCode.ITEM_NOT_FOUND));

		mockMvc.perform(
			get("/api/categories/1/items/" + itemId)
			).andDo(print())
			.andExpect(status().isNotFound())
			.andExpect(jsonPath("$.message").exists());
	}
}

