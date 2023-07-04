package kr.codesquad.kiosk.category.service;

import kr.codesquad.kiosk.category.controller.dto.ItemResponse;
import kr.codesquad.kiosk.category.controller.dto.response.CategoryItemsResponse;
import kr.codesquad.kiosk.category.controller.dto.response.CategoryResponse;
import kr.codesquad.kiosk.category.repository.CategoryRepository;
import kr.codesquad.kiosk.exception.BusinessException;
import kr.codesquad.kiosk.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class CategoryService {
	private final CategoryRepository categoryRepository;

	@Transactional(readOnly = true)
	public List<CategoryResponse> getAllCategories() {
		List<CategoryResponse> categories = categoryRepository.findAll()
				.stream()
				.map(category -> CategoryResponse.from(category.getId(), category.getName()))
				.collect(Collectors.toList());

		if (categories.isEmpty()) {
			throw new BusinessException(ErrorCode.EMPTY_RESULT);
		}

		return categories;
	}

	@Transactional(readOnly = true)
	public CategoryItemsResponse getCategoryItems(Integer categoryId) {
		List<ItemResponse> itemResponses = categoryRepository.findItemsByCategoryId(categoryId);
		if (itemResponses.isEmpty()) {
			throw new BusinessException(ErrorCode.CATEGORY_NOT_FOUND);
		}

		List<Integer> top3ItemIds = categoryRepository.findTop3ItemsByCategoryId(categoryId);

		itemResponses.stream()
				.filter(itemResponse -> top3ItemIds.contains(itemResponse.getId()))
				.forEach(ItemResponse::pickSignature);

		return new CategoryItemsResponse(categoryId, itemResponses);
	}
}
