package kr.codesquad.kiosk.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum ErrorCode {
	EMPTY_RESULT(500, "데이터 불러오기를 실패했습니다."),
	ORDER_NOT_FOUND(404, "해당 주문을 찾을 수 없습니다."),
	ITEM_NOT_FOUND(404, "해당 아이템을 찾을 수 없습니다."),
	PAYMENTS_NOT_FOUND(404, "결제 방식을 찾을 수 없습니다."),
	CATEGORY_NOT_FOUND(404, "해당 카테고리를 찾을 수 없습니다."),
	NETWORK_FAIN_ERROR(503, "네트워크 오류로 인해 결제에 실패하였습니다."),
	CARD_LIMIT_EXCEEDED_ERROR(403, "카드 한도 초과로 인해 결제에 실패하였습니다."),
	MAGNETIC_NOT_RECOGNIZED_ERROR(403, "마그네틱 인식 불량으로 인해 결제에 실패하였습니다."),
	RESPONSE_DELAY_ERROR(403, "응답이 지연되고 있습니다. 나중에 다시 시도해 주세요."),
	SERVER_ERROR(500, "예상치 못한 서버 에러가 발생했습니다.");

	private final int statusCode;
	private final String description;
}
