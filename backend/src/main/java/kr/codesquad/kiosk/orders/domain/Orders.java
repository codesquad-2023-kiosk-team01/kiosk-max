package kr.codesquad.kiosk.orders.domain;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class Orders {
	private int id;
	private int amount;
	private int total;
	private int remain;
	private LocalDateTime orderDate;
	private int paymentId;
}
