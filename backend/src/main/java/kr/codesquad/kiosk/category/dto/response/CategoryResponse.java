package kr.codesquad.kiosk.category.dto.response;

public record CategoryResponse(
	Integer id,
	String name
) {
	public static CategoryResponse from(int id, String name) {
		return new CategoryResponse(
			id,
			name
		);
	}
}
