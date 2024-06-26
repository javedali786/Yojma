package com.tv.uscreen.yojmatv.bean_model_v1_0.authResponse;

public class AuthResponse {
	private Data data;
	private Object debugMessage;
	private int responseCode;
	private boolean status;

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	public Data getData() {
		return data;
	}

	public void setData(Data data) {
		this.data = data;
	}

	public Object getDebugMessage() {
		return debugMessage;
	}

	public void setDebugMessage(Object debugMessage) {
		this.debugMessage = debugMessage;
	}

	public int getResponseCode() {
		return responseCode;
	}

	public void setResponseCode(int responseCode) {
		this.responseCode = responseCode;
	}
}
