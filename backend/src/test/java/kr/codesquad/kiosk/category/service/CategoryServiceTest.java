package kr.codesquad.kiosk.category.service;

import kr.codesquad.kiosk.category.controller.dto.response.CategoryItemsResponse;
import kr.codesquad.kiosk.category.controller.dto.response.CategoryResponse;
import kr.codesquad.kiosk.category.domain.Category;
import kr.codesquad.kiosk.category.repository.CategoryRepository;
import kr.codesquad.kiosk.exception.BusinessException;
import kr.codesquad.kiosk.exception.ErrorCode;
import kr.codesquad.kiosk.fixture.FixtureFactory;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {

	@Mock
	private CategoryRepository categoryRepository;

	@InjectMocks
	private CategoryService categoryService;

	@DisplayName("모든 카테고리 항목을 조회할 수 있다.")
	@Test
	void whenGetAllCategories_thenResponse200OK() {
		// given
		List<Category> categories = FixtureFactory.createCategories();
		given(categoryRepository.findAll()).willReturn(categories);

		// when
		List<CategoryResponse> actualResponse = categoryService.getAllCategories();

		// then
		SoftAssertions softAssertions = new SoftAssertions();
		softAssertions.assertThat(actualResponse)
				.isNotNull()
				.hasSize(categories.size());

		List<CategoryResponse> expectedResponse = categories.stream()
				.map(category -> CategoryResponse.from(category.getId(), category.getName()))
				.collect(Collectors.toList());

		softAssertions.assertThat(actualResponse)
				.containsExactlyElementsOf(expectedResponse);

		softAssertions.assertAll();
	}

	@DisplayName("카테고리 목록을 조회했을 때 목록이 비어있는 경우 DATABASE_UNAVAILABLE 에러가 발생한다.")
	@Test
	void whenGetAllCategories_thenThrowsBusinessException() {
		// given
		given(categoryRepository.findAll()).willReturn(FixtureFactory.createEmptyCategories());

		// when & then
		assertAll(
				() -> assertThatThrownBy(() -> categoryService.getAllCategories()).isInstanceOf(BusinessException.class)
						.extracting("errorCode")
						.isEqualTo(ErrorCode.EMPTY_RESULT),
				() -> then(categoryRepository).should(times(1)).findAll());
	}

	@DisplayName("카테고리 아이디로 조회했을 때 해당 아이템 목록을 불러오고 상위 3개의 메뉴를 표시한다.")
	@Test
	void givenCategoryId_whenGetCategoryItems_thenSuccess() {
		// given
		given(categoryRepository.findItemsByCategoryId(anyInt())).willReturn(FixtureFactory.createItemResponses());
		given(categoryRepository.findTop3ItemsByCategoryId(anyInt())).willReturn(List.of(1, 2, 3));

		// when
		CategoryItemsResponse response = categoryService.getCategoryItems(1);

		// then
		SoftAssertions.assertSoftly(softAssertions -> {
			softAssertions.assertThat(response.items().get(0).getIsSignature()).isTrue();
			softAssertions.assertThat(response.items().get(1).getIsSignature()).isTrue();
			softAssertions.assertThat(response.items().get(2).getIsSignature()).isTrue();
		});
	}

	@DisplayName("잘못된 카테고리 아이디로 조회했을 때 예외를 던진다.")
	@Test
	void givenWrongCategoryId_whenGetCategoryItems_thenThrowsException() {
		// given
		given(categoryRepository.findItemsByCategoryId(anyInt())).willReturn(List.of());

		// when & then
		assertAll(
				() -> assertThatThrownBy(() -> categoryService.getCategoryItems(1))
						.isInstanceOf(BusinessException.class)
						.extracting("errorCode")
						.isEqualTo(ErrorCode.CATEGORY_NOT_FOUND),
				() -> then(categoryRepository).should(times(1)).findItemsByCategoryId(1),
				() -> then(categoryRepository).should(never()).findTop3ItemsByCategoryId(anyInt())
		);
	}
}
