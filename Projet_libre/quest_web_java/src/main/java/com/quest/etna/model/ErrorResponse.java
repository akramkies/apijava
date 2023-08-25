package com.quest.etna.model;

import java.io.Serializable;

public class ErrorResponse implements Serializable {

	private final String error;

	public ErrorResponse(String error) {
		this.error = error;
	}

	public String getError() {
		return this.error;
	}
}
