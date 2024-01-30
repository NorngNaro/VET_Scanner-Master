package com.udaya.virak_buntham.vetpickup.models.change;

public class SaveResponse{

	private String dest_to_code;
	private String item_code;
	private String date_print;
	private String date_invoice;
	private String signature;
	private String destination_to;
	private String destination_from;
	private String item_name;
	private String transfer_code;
	private String item_value;
	private String receiver_tel;
	private String token;
	private String collect_cod;
	private int item_qty;
	private String status;
	private String info;
	private String delivery_area;
	private String uom;
	private String total_amount;
	private String location_type;

	public String getUom() {
		return uom;
	}

	public String getLocationFromLocation() {
		return location_type;
	}

	public String getDeliveryArea() {
		return delivery_area;
	}

	public String getDestToCode(){
		return dest_to_code;
	}

	public String getItemCode(){
		return item_code;
	}

	public String getDatePrint(){
		return date_print;
	}

	public String getDateInvoice(){
		return date_invoice;
	}

	public String getSignature(){
		return signature;
	}

	public String getDestinationTo(){
		return destination_to;
	}

	public String getDestinationFrom(){
		return destination_from;
	}

	public String getItemName(){
		return item_name;
	}

	public String getTransferCode(){
		return transfer_code;
	}

	public String getItemValue(){
		return item_value;
	}

	public String getReceiverTel(){
		return receiver_tel;
	}

	public String getToken(){
		return token;
	}

	public String getCollectCod(){
		return collect_cod;
	}

	public int getItemQty(){
		return item_qty;
	}

	public String getStatus(){
		return status;
	}

	public String getInfo(){
		return info;
	}

	public String getTotal_amount() {
		return total_amount;
	}
}
