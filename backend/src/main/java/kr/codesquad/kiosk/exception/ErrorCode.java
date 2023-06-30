package kr.codesquad.kiosk.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum ErrorCode {
	ITEM_NOT_FOUND(404, "해당 아이템을 찾을 수 없습니다."),
	PAYMENTS_NOT_FOUND(404, "결제 방식을 찾을 수 없습니다.");

	private final int statusCode;
	private final String description;
}
