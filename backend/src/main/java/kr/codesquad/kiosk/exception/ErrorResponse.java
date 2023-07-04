package kr.codesquad.kiosk.exception;

public record ErrorResponse(
		ErrorCode errorCode,
		String message
) {
}
