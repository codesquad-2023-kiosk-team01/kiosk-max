package kr.codesquad.kiosk.orders.domain;

import java.util.Random;

import kr.codesquad.kiosk.exception.BusinessException;
import kr.codesquad.kiosk.exception.ErrorCode;

public enum OrderResultType {
	SUCCESS,
	NETWORK_ERROR,
	CARD_LIMIT_EXCEEDED,
	MAGNETIC_NOT_RECOGNIZED,
	RESPONSE_DELAY;

	private static final Random random = new Random();

	public static OrderResultType getRandomOrderResultType() {
		int randomInt = random.nextInt(10) + 1;

		switch (randomInt) {
			case 1, 2, 3, 4, 5, 6, 7 -> {
				return OrderResultType.SUCCESS;
			}
			case 8 -> {
				return OrderResultType.NETWORK_ERROR;
			}
			case 9 -> {
				return OrderResultType.CARD_LIMIT_EXCEEDED;
			}
			default -> {
				return OrderResultType.MAGNETIC_NOT_RECOGNIZED;
			}
		}
	}

	public static BusinessException getBusinessException(OrderResultType orderResultType) {
		switch (orderResultType) {
			case CARD_LIMIT_EXCEEDED -> {
				return new BusinessException(ErrorCode.CARD_LIMIT_EXCEEDED_ERROR);
			}
			case MAGNETIC_NOT_RECOGNIZED -> {
				return new BusinessException(ErrorCode.MAGNETIC_NOT_RECOGNIZED_ERROR);
			}
			case NETWORK_ERROR -> {
				return new BusinessException(ErrorCode.NETWORK_FAIN_ERROR);
			}
			default -> {
				return new BusinessException(ErrorCode.RESPONSE_DELAY_ERROR);
			}
		}
	}
}
