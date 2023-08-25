package com.quest.etna.model;

import java.io.Serializable;

public class MessageResponse implements Serializable {

	private final String message;

	public MessageResponse(String message) {
		this.message = message;
	}

	public String getMessage() {
		return this.message;
	}
}
