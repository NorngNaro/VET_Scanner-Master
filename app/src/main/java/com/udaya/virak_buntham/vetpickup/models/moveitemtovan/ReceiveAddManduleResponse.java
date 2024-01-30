package com.udaya.virak_buntham.vetpickup.models.moveitemtovan;

import java.util.List;

public class ReceiveAddManduleResponse{
	private List<DataItem> data;
	private String signature;
	private String status;
	private String info;
	private String token;

	public List<DataItem> getData(){
		return data;
	}

	public String getSignature(){
		return signature;
	}

	public String getStatus(){
		return status;
	}

	public String getInfo(){
		return info;
	}

	public String getToken(){
		return token;
	}
}