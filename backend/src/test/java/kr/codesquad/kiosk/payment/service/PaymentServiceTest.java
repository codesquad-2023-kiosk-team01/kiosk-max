package kr.codesquad.kiosk.payment.service;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.List;

import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kr.codesquad.kiosk.exception.BusinessException;
import kr.codesquad.kiosk.exception.ErrorCode;
import kr.codesquad.kiosk.fixture.FixtureFactory;
import kr.codesquad.kiosk.payment.controller.response.PaymentResponse;
import kr.codesquad.kiosk.payment.domain.Payment;
import kr.codesquad.kiosk.payment.repository.PaymentRepository;

@ExtendWith(MockitoExtension.class)
class PaymentServiceTest {
	@Mock
	private PaymentRepository paymentRepository;

	@InjectMocks
	private PaymentService paymentService;

	@DisplayName("결제 방식 목록을 조회할 수 있다.")
	@Test
	void whenGetPayments_thenReturnPayments() {
		// given
		List<Payment> payments = FixtureFactory.createPayments();
		given(paymentRepository.findAll()).willReturn(payments);

		// when
		List<PaymentResponse> responsesActual = paymentService.getPayments();

		// then
		List<PaymentResponse> responsesExpected = FixtureFactory.createPaymentResponses();

		SoftAssertions.assertSoftly(softAssertions -> {
			for (int i = 0; i < responsesActual.size(); i++) {
				softAssertions.assertThat(responsesActual.get(i).id()).isEqualTo(responsesExpected.get(i).id());
				softAssertions.assertThat(responsesActual.get(i).name()).isEqualTo(responsesExpected.get(i).name());
				softAssertions.assertThat(responsesActual.get(i).image()).isEqualTo(responsesExpected.get(i).image());
			}
		});
	}

	@DisplayName("결제 방식 목록을 조회했을 때 목록이 없으면 PAYMENTS_NOT_FOUND 에러가 발생한다.")
	@Test
	void whenGetPayments_thenThrowsBusinessException() {
		// given
		given(paymentRepository.findAll()).willReturn(FixtureFactory.createEmptyPayments());

		// when & then
		assertAll(
			() -> assertThatThrownBy(() -> paymentService.getPayments())
				.isInstanceOf(BusinessException.class)
				.extracting("errorCode").isEqualTo(ErrorCode.PAYMENTS_NOT_FOUND),
			() -> then(paymentRepository).should(times(1)).findAll()
		);
	}
}
