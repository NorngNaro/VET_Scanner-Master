package com.udaya.virak_buntham.vetpickup.models.change;

import java.util.List;

public class ChangeResponse{
	private String code;
	private String receiver;
	private String signature;
	private String sender;
	private String destTo;
	private List<Integer> item_scan;
	private int qty;
	private int cus_loc_id;
	private int id;
	private int destToId;

	public List<Integer> getItem_scan() {
		return item_scan;
	}

	private String status;
	private String info;
	private String token;

	public String getDestTo() {
		return destTo;
	}

	public String getCode(){
		return code;
	}

	public String getReceiver(){
		return receiver;
	}

	public String getSignature(){
		return signature;
	}

	public String getSender(){
		return sender;
	}

	public List<Integer> getItemScan(){
		return item_scan;
	}

	public int getQty(){
		return qty;
	}

	public int getId(){
		return id;
	}

	public int getCus_loc_id() {
		return cus_loc_id;
	}

	public int getDestToId() {
		return destToId;
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