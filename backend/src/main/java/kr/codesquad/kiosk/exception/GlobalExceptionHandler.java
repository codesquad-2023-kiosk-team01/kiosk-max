package kr.codesquad.kiosk.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
	@ExceptionHandler(BusinessException.class)
	public ResponseEntity<ErrorResponse> handleBusinessException(BusinessException ex) {
		return ResponseEntity.status(ex.getErrorCode().getStatusCode())
				.body(new ErrorResponse(ex.getErrorCode(), ex.getErrorCode().getDescription()));
	}
}
