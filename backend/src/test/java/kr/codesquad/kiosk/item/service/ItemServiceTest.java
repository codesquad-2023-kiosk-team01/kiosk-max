package kr.codesquad.kiosk.item.service;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kr.codesquad.kiosk.exception.BusinessException;
import kr.codesquad.kiosk.exception.ErrorCode;
import kr.codesquad.kiosk.fixture.FixtureFactory;
import kr.codesquad.kiosk.item.controller.dto.response.ItemDetailsResponse;
import kr.codesquad.kiosk.item.controller.dto.response.OptionsResponse;
import kr.codesquad.kiosk.item.domain.Item;
import kr.codesquad.kiosk.item.domain.Options;
import kr.codesquad.kiosk.item.repository.ItemRepository;

@ExtendWith(MockitoExtension.class)
class ItemServiceTest {
	@Mock
	private ItemRepository itemRepository;

	@InjectMocks
	private ItemService itemService;

	@DisplayName("아이템 아이디로 아이템 상세 항목을 조회할 수 있다.")
	@Test
	void givenItemId_whenGetItemDetails_thenReturnsItemDetailsResponse() {
		// given
		int itemId = 1;
		Item item = FixtureFactory.createItem();
		Map<String, List<Options>> optionsMap = FixtureFactory.createOptionsMap();
		Map<String, List<OptionsResponse>> options = FixtureFactory.createOptions();

		given(itemRepository.findById(anyInt())).willReturn(Optional.of(item));
		given(itemRepository.findOptionsByItemId(anyInt())).willReturn(optionsMap);

		// when
		ItemDetailsResponse itemDetails = itemService.getItemDetails(itemId);

		/// then
		ItemDetailsResponse response = ItemDetailsResponse.from(item, options);

		SoftAssertions.assertSoftly(softAssertions -> {
			softAssertions.assertThat(itemDetails.id()).isEqualTo(response.id());
			softAssertions.assertThat(itemDetails.name()).isEqualTo(response.name());
			softAssertions.assertThat(itemDetails.price()).isEqualTo(response.price());
			softAssertions.assertThat(itemDetails.options()).isEqualTo(response.options());
		});
	}

	@DisplayName("존재하지 않는 아이템 아이디로 아이템 상세 항목을 조회하면 ITEM_NOT_FOUND 에러가 발생한다다.")
	@Test
	void givenWrongItemId_whenGetItemDetails_thenThrowsBusinessException() {
		// given
		int itemId = 999999;
		given(itemRepository.findById(anyInt())).willReturn(Optional.empty());

		// when & then
		assertAll(
			() -> assertThatThrownBy(() -> itemService.getItemDetails(itemId))
				.isInstanceOf(BusinessException.class)
				.extracting("errorCode").isEqualTo(ErrorCode.ITEM_NOT_FOUND),
			() -> then(itemRepository).should().findById(anyInt()),
			() -> then(itemRepository).should(times(0)).findOptionsByItemId(anyInt())
		);
	}
}
