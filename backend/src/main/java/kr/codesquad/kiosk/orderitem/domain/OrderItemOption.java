package kr.codesquad.kiosk.orderitem.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Getter
@Builder
public class OrderItemOption {
	private int id;
	private int optionsId;
	private int orderItemId;

	public OrderItemOption(int optionsId) {
		this.optionsId = optionsId;
	}

	public OrderItemOption assignOrderItemId(int orderItemId) {
		this.orderItemId = orderItemId;
		return this;
	}
}
