package com.tv.uscreen.yojmatv.bean_model_v1_0.listAll.likeList;

public class Response{
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
			"Response{" + 
			"data = '" + data + '\'' + 
			",debugMessage = '" + debugMessage + '\'' + 
			",responseCode = '" + responseCode + '\'' + 
			"}";
		}
}
