package kr.codesquad.kiosk.orderitem.domain;

import lombok.Getter;

@Getter
public class OrderItem {
	private int id;
	private int itemQuantity;
	private int itemId;
	private int ordersId;
	private int price;
}
