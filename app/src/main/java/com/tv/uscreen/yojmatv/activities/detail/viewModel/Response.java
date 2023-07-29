package com.tv.uscreen.yojmatv.activities.detail.viewModel;

public class Response{
	private Data data;
	private Object debugMessage;
	private int responseCode;

	public void setData(Data data) {
		this.data = data;
	}

	public void setDebugMessage(Object debugMessage) {
		this.debugMessage = debugMessage;
	}

	public void setResponseCode(int responseCode) {
		this.responseCode = responseCode;
	}

	public Data getData(){
		return data;
	}

	public Object getDebugMessage(){
		return debugMessage;
	}

	public int getResponseCode(){
		return responseCode;
	}
}
