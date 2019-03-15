package com.capgemini.testcasesdemo;

public class Json {
	
	private JsonRequest request;

	private JsonResponse response;
	private String responseMessage;
	public JsonRequest getRequest() {
		return request;
	}

	public void setRequest(JsonRequest request) {
		this.request = request;
	}

	public JsonResponse getResponse() {
		return response;
	}

	public void setResponse(JsonResponse response) {
		this.response = response;
	}

	public String getResponseMessage() {
		return responseMessage;
	}

	public void setResponseMessage(String responseMessage) {
		this.responseMessage = responseMessage;
	}
	
}
