package kr.codesquad.kiosk.payment.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Payment {
	private int id;
	private String name;
	private String image;
}
