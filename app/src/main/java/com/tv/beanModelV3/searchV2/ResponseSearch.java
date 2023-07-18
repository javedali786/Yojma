package com.tv.beanModelV3.searchV2;

public class ResponseSearch {
	private Data data;
	private Object debugMessage;
	private int responseCode;

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
			"ResponseSearch{" +
			"data = '" + data + '\'' + 
			",debugMessage = '" + debugMessage + '\'' + 
			",responseCode = '" + responseCode + '\'' + 
			"}";
		}
}
