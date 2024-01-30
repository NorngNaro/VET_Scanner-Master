package com.udaya.virak_buntham.vetpickup.models.ScanQr;

import java.util.List;

public class CustomerReceiveFormResponse{
	private int total;
	private List<DataItem> data;
	private String signature;
	private String status;
	private String info;
	private String token;
	private String uom;

	public int getTotal(){
		return total;
	}

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

	public String getUom() {
		return uom;
	}
}