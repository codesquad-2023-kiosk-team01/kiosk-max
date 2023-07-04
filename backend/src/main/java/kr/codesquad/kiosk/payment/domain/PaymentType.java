package kr.codesquad.kiosk.payment.domain;

import kr.codesquad.kiosk.exception.BusinessException;
import kr.codesquad.kiosk.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PaymentType {
	CARD_PAYMENT(1),
	CASH_PAYMENT(2);

	private final int id;

	public boolean isSamePaymentId(int paymentId) {
		return id == paymentId;
	}

	public static boolean isValid(int paymentId) {
		for (PaymentType type : PaymentType.values()) {
			if (type.getId() == paymentId) {
				return true;
			}
		}

		return false;
	}

	public static PaymentType getPaymentType(int paymentId) {
		for (PaymentType type : PaymentType.values()) {
			if (type.getId() == paymentId) {
				return type;
			}
		}

		throw new BusinessException(ErrorCode.PAYMENTS_NOT_FOUND);
	}

}
