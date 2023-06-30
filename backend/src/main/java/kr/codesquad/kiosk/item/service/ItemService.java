package kr.codesquad.kiosk.item.service;

import kr.codesquad.kiosk.exception.BusinessException;
import kr.codesquad.kiosk.exception.ErrorCode;
import kr.codesquad.kiosk.item.controller.dto.response.ItemDetailsResponse;
import kr.codesquad.kiosk.item.domain.Item;
import kr.codesquad.kiosk.item.domain.Options;
import kr.codesquad.kiosk.item.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class ItemService {
	private final ItemRepository itemRepository;

	@Transactional(readOnly = true)
	public ItemDetailsResponse getItemDetails(Integer itemId) {
		Item item = itemRepository.findById(itemId)
				.orElseThrow(() -> new BusinessException(ErrorCode.ITEM_NOT_FOUND));
		Map<String, List<String>> options = new HashMap<>();
		for (Map.Entry<String, List<Options>> entry : itemRepository.findOptionsByItemId(itemId).entrySet()) {
			options.put(entry.getKey(), entry.getValue().stream().map(Options::getName).toList());
		}
		return ItemDetailsResponse.from(item, options);
	}
}
