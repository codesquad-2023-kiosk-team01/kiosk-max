package kr.codesquad.kiosk.orderitem.domain;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Getter
@Builder
public class OrderItem {
	private int id;
	private int itemQuantity;
	private int itemId;
	private int ordersId;
	private int price;

	private List<OrderItemOption> orderItemOptions;

	public OrderItem assignOrdersId(int ordersId) {
		this.ordersId = ordersId;
		return this;
	}

	public int calculatePrice(int itemPrice) {
		price = itemPrice * itemQuantity;
		return price;
	}
}
