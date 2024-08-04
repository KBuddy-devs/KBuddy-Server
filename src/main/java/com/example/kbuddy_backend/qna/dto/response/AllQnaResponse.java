package com.example.kbuddy_backend.qna.dto.response;

import java.util.List;

public record AllQnaResponse(long totalElement, long totalPage, int currentPage, List<QnaResponse> results) {
	public static AllQnaResponse of(long totalElement, long totalPage, int currentPage, List<QnaResponse> results) {
		return new AllQnaResponse(totalElement, totalPage, currentPage, results);
	}
}
