package kr.codesquad.kiosk.payment.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.codesquad.kiosk.exception.BusinessException;
import kr.codesquad.kiosk.exception.ErrorCode;
import kr.codesquad.kiosk.payment.controller.response.PaymentResponse;
import kr.codesquad.kiosk.payment.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class PaymentService {
	private final PaymentRepository paymentRepository;

	@Transactional(readOnly = true)
	public List<PaymentResponse> getPayments() {
		List<PaymentResponse> payments = paymentRepository.findAll()
			.stream()
			.map(PaymentResponse::from)
			.toList();

		if (payments.isEmpty()) {
			throw new BusinessException(ErrorCode.PAYMENTS_NOT_FOUND);
		}

		return payments;
	}
}
