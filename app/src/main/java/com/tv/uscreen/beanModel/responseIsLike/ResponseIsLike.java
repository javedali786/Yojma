package com.tv.uscreen.beanModel.responseIsLike;

public class ResponseIsLike{
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

	public void setData(Data data){
		this.data = data;
	}

	public Data getData(){
		return data;
	}

	public void setDebugMessage(Object debugMessage){
		this.debugMessage = debugMessage;
	}

	public Object getDebugMessage(){
		return debugMessage;
	}

	public void setResponseCode(int responseCode){
		this.responseCode = responseCode;
	}

	public int getResponseCode(){
		return responseCode;
	}

	@Override
 	public String toString(){
		return 
			"ResponseIsLike{" + 
			"data = '" + data + '\'' + 
			",debugMessage = '" + debugMessage + '\'' + 
			",responseCode = '" + responseCode + '\'' + 
			"}";
		}
}
