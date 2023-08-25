package com.quest.etna.model;

import java.io.Serializable;

public class SuccessResponse implements Serializable {

	private final Boolean success;

	public SuccessResponse(Boolean success) {
		this.success = success;
	}

	public Boolean getSuccess() {
		return this.success;
	}
}
