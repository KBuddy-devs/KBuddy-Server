package com.example.kbuddy_backend.qna.dto.response;

import java.util.List;

public record AllQnaResponse(Long nextId, List<QnaPaginationResponse> qnaPaginationResponses) {
	public static AllQnaResponse of(Long nextId, List<QnaPaginationResponse> qnaPaginationResponses) {
		return new AllQnaResponse(nextId, qnaPaginationResponses);
	}
}
