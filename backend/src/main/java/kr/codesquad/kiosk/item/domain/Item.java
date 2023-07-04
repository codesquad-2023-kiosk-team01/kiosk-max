package kr.codesquad.kiosk.item.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Item {
	private int id;
	private String name;
	private int price;
	private String image;
	private String description;
	private int categoryId;
}
