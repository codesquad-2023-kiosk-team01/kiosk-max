package kr.codesquad.kiosk.category.controller.dto;

import lombok.Getter;

@Getter
public final class ItemResponse {
	private final Integer id;
	private final String name;
	private final Integer price;
	private Boolean isSignature;
	private final String image;

	public ItemResponse(Integer id, String name, Integer price, String image) {
		this.id = id;
		this.name = name;
		this.price = price;
		this.image = image;
		isSignature = Boolean.FALSE;
	}

	public void pickSignature() {
		isSignature = Boolean.TRUE;
	}
}
