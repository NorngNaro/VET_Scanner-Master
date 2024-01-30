package com.udaya.virak_buntham.vetpickup.models.ScanQr;

public class DataItem{
	private String item;
	private String location;
	private String received_date;
	private int id;
	private int status;
	private String cod;

	public String getCod() {
		return cod;
	}

	public String getItem(){
		return item;
	}

	public String getLocation(){
		return location;
	}

	public int getId(){
		return id;
	}

	public int getStatus(){
		return status;
	}

	public String getReceived_date() {
		return received_date;
	}
}
