package com.udaya.virak_buntham.vetpickup.rest;

import com.udaya.virak_buntham.vetpickup.models.ListSearch.SearchData;
import com.udaya.virak_buntham.vetpickup.models.PickUp.PickUpRespone;
import com.udaya.virak_buntham.vetpickup.models.SMSRespone;
import com.udaya.virak_buntham.vetpickup.models.ScanQr.CustomerReceiveFormResponse;
import com.udaya.virak_buntham.vetpickup.models.ScanQr.ScanQrRespone;
import com.udaya.virak_buntham.vetpickup.models.change.ChangeAddResponse;
import com.udaya.virak_buntham.vetpickup.models.change.ChangeResponse;
import com.udaya.virak_buntham.vetpickup.models.change.SaveResponse;
import com.udaya.virak_buntham.vetpickup.models.changepassword.ChangePasswordResponse;
import com.udaya.virak_buntham.vetpickup.models.checklogin.CheckLoginResponse;
import com.udaya.virak_buntham.vetpickup.models.getCustomer.GetCustomerResponse;
import com.udaya.virak_buntham.vetpickup.models.getItemNotReceive.ItemNotReceiveResponse;
import com.udaya.virak_buntham.vetpickup.models.getdelivery.GetDeliveryRespone;
import com.udaya.virak_buntham.vetpickup.models.locker.LockerLabelResponse;
import com.udaya.virak_buntham.vetpickup.models.locker.LockerLoginResponse;
import com.udaya.virak_buntham.vetpickup.models.locker.LockerResponse;
import com.udaya.virak_buntham.vetpickup.models.login.LoginResponse;
import com.udaya.virak_buntham.vetpickup.models.logout.LogoutResponse;
import com.udaya.virak_buntham.vetpickup.models.memmber.MemberRespone;
import com.udaya.virak_buntham.vetpickup.models.moveitemtovan.MoveItemToVanData;
import com.udaya.virak_buntham.vetpickup.models.moveitemtovan.ReceiveAddManduleResponse;
import com.udaya.virak_buntham.vetpickup.models.permission.PermissionRespone;
import com.udaya.virak_buntham.vetpickup.models.report.ReportResponse;
import com.udaya.virak_buntham.vetpickup.models.requesttoken.RequestTokenResponse;
import com.udaya.virak_buntham.vetpickup.models.saveCustomerCall.CallHistoryResponse;
import com.udaya.virak_buntham.vetpickup.models.saveCustomerCall.SaveCustomerCallResponse;
import com.udaya.virak_buntham.vetpickup.models.savegoodstransfer.GoodsTransferResponse;
import com.udaya.virak_buntham.vetpickup.models.selectionlist.DestinationFromResponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApiInterface {

    @POST("user/gettoken")
    Call<RequestTokenResponse> getToken(
            @Header("device") String device
    );

    @FormUrlEncoded
    @POST("user/login")
    Call<LoginResponse> requestLogin(
            @Header("device") String device,
            @Header("token") String token,
            @Header("signature") String signature,
            @Field("username") String username,
            @Field("password") String password
    );

    @POST("user/logout")
    Call<LogoutResponse> requestLogout(
            @Header("device") String device,
            @Header("token") String token,
            @Header("signature") String signature,
            @Header("session") String session
    );

    @POST("user/checklogin")
    Call<CheckLoginResponse> checkLogin(
            @Header("device") String device,
            @Header("token") String token,
            @Header("signature") String signature,
            @Header("session") String session
    );

    @FormUrlEncoded
    @POST("user/changepassword")
    Call<ChangePasswordResponse> changePassword(
            @Header("device") String device,
            @Header("token") String token,
            @Header("signature") String signature,
            @Header("session") String session,
            @Field("oldpassword") String oldPassword,
            @Field("newpassword") String newPassword,
            @Field("repassword") String reNewPassword
    );

    @FormUrlEncoded
    @POST("goods/getdestinationfrom")
    Call<DestinationFromResponse> getDestinationFrom(
            @Header("device") String device,
            @Header("token") String token,
            @Header("signature") String signature,
            @Header("session") String session,
            @Field("search") String search
    );
    @FormUrlEncoded
    @POST("goods/getlocation")
    Call<DestinationFromResponse> getLocation(
            @Header("device") String device,
            @Header("token") String token,
            @Header("signature") String signature,
            @Header("session") String session,
            @Field("branch_id") String branchId,
            @Field("search") String search
    );

    @FormUrlEncoded
    @POST("goods/getdestinationto")
    Call<DestinationFromResponse> getDestinationTo(
            @Header("device") String device,
            @Header("token") String token,
            @Header("signature") String signature,
            @Header("session") String session,
            @Field("destination_from_id") String destinationFromId,
            @Field("search") String search
    );

    @FormUrlEncoded
    @POST("goods/getitemtype")
    Call<DestinationFromResponse> getGoodsType(
            @Header("device") String device,
            @Header("token") String token,
            @Header("signature") String signature,
            @Header("session") String session,
            @Field("search") String search
    );

    @FormUrlEncoded
    @POST("goods/getuom")
    Call<DestinationFromResponse> getUOM(
            @Header("device") String device,
            @Header("token") String token,
            @Header("signature") String signature,
            @Header("session") String session,
            @Field("search") String search

    );

    @FormUrlEncoded
    @POST("goods/getvan")
    Call<DestinationFromResponse> getVan(
            @Header("device") String device,
            @Header("token") String token,
            @Header("signature") String signature,
            @Header("session") String session,
            @Field("search") String search

    );


    @FormUrlEncoded
    @POST("goods/getbranch")
    Call<DestinationFromResponse> getBrand(
            @Header("device") String device,
            @Header("token") String token,
            @Header("signature") String signature,
            @Header("session") String session,
            @Field("search") String search

    );

    @FormUrlEncoded
    @POST("goods/getdestinationtobranch")
    Call<DestinationFromResponse> getDestinationToByBranch(
            @Header("device") String device,
            @Header("token") String token,
            @Header("signature") String signature,
            @Header("session") String session,
            @Field("branch_id") String brandId,
            @Field("search") String search
    );

    @POST("goods/getcurrency")
    Call<DestinationFromResponse> getCurrency(
            @Header("device") String device,
            @Header("token") String token,
            @Header("signature") String signature,
            @Header("session") String session
    );

    @FormUrlEncoded
    @POST("goods/getdeliverarea")
    Call<DestinationFromResponse> getDeliveryArea(
            @Header("device") String device,
            @Header("token") String token,
            @Header("signature") String signature,
            @Header("session") String session,
            @Field("search") String search
    );


    @FormUrlEncoded
    @POST("goods/getcustomer")
    Call<DestinationFromResponse> getCustomer(
            @Header("device") String device,
            @Header("token") String token,
            @Header("signature") String signature,
            @Header("session") String session,
            @Field("search") String search
    );

    @FormUrlEncoded
    @POST("goods/getcustomer")
    Call<DestinationFromResponse> getCustomerAuto(
            @Header("device") String device,
            @Header("token") String token,
            @Header("signature") String signature,
            @Header("session") String session,
            @Field("search") String search
    );

    @FormUrlEncoded
    @POST("goods/getreason")
    Call<DestinationFromResponse> getreason(
            @Header("device") String device,
            @Header("token") String token,
            @Header("signature") String signature,
            @Header("session") String session,
            @Field("search") String search
    );
    @FormUrlEncoded
    @POST("goods/changedestinationto")
    Call<DestinationFromResponse> getchangedestinationto(
            @Header("device") String device,
            @Header("token") String token,
            @Header("signature") String signature,
            @Header("session") String session,
            @Field("branch_id") String branchId,
            @Field("search") String search
    );

    @FormUrlEncoded
    @POST("goods/getmembership")
    Call<DestinationFromResponse> getmembership(
            @Header("device") String device,
            @Header("token") String token,
            @Header("signature") String signature,
            @Header("session") String session,
            @Field("search") String search
    );

    @FormUrlEncoded
    @POST("goods/savetransfer")
    Call<GoodsTransferResponse> saveGoodsTransfer(
            @Header("device") String device,
            @Header("token") String token,
            @Header("signature") String signature,
            @Header("session") String session,
            @Field("destination_from") String destinationFrom,
            @Field("destination_to") String destinationTo,
            @Field("sender") String sender,
            @Field("receiver") String receiver,
            @Field("item_value") String itemValue,
            @Field("item_value_currency") String itemCurrency,
            @Field("item_type") String itemType,
            @Field("item_name") String itemName,
            @Field("item_qty") String itemQTY,
            @Field("uom") String uom,
            @Field("transfer_fee") String transferFee,
//            @Field("customer_type") Integer customerType,
//            @Field("customer_id") Integer customerId,
//            @Field("vip_cus_id") Integer vipCusId,
            @Field("reference") String reference,
            @Field("delivery_area") Integer deliveryArea,
            @Field("fee") String fee,
            @Field("longs") String longs,
            @Field("lats") String lats,
            @Field("delivery_fee") String deliveryFee,
            @Field("collect_cod") String collectCod,
            @Field("is_transit") String isTransit,
            @Field("is_delivery_free") int isDeliveryFee,
            @Field("delivery_address") String delivery_address


    );

    @POST("goods/getreportcollectbyuser")
    Call<ReportResponse> getReport(
            @Header("device") String device,
            @Header("token") String token,
            @Header("signature") String signature,
            @Header("session") String session
    );


    @FormUrlEncoded
    @POST("goods/savetransferlocal")
    Call<GoodsTransferResponse> saveGoodsTransferLocal(
            @Header("device") String device,
            @Header("token") String token,
            @Header("signature") String signature,
            @Header("session") String session,
            @Field("destination_from") String destinationFrom,
            @Field("destination_to") String destinationTo,
            @Field("sender") String sender,
            @Field("receiver") String receiver,
            @Field("item_value") String itemValue,
            @Field("item_value_currency") String itemCurrency,
            @Field("item_type") String itemType,
            @Field("item_name") String itemName,
            @Field("item_qty") String itemQTY,
            @Field("uom") String uom,
            @Field("transfer_fee") String transferFee,
            @Field("longs") String longs,
            @Field("lats") String lats,
            @Field("collect_cod") String collectCod,
            @Field("paid") Integer paid,
            @Field("receiver_address") String receiverAddress,
            @Field("reference") String reference,
//            @Field("customer_type") Integer customerType,
//            @Field("customer_id") Integer customerID,
//            @Field("vip_cus_id") Integer vipCusId,
            @Field("is_delivery") Integer isDelivery
    );

    @FormUrlEncoded
    @POST("goods/checktransfercode")
    Call<ScanQrRespone> getScanQrCode(
            @Header("device") String device,
            @Header("token") String token,
            @Header("signature") String signature,
            @Header("session") String session,
            @Field("code") String code
    );


    @FormUrlEncoded
    @POST("goods/checkmembership")
    Call<MemberRespone> checkMemember(
            @Header("device") String device,
            @Header("token") String token,
            @Header("signature") String signature,
            @Header("session") String session,
            @Field("code") String code,
            @Field("sender_telephone") String senderTel
    );

    @FormUrlEncoded
    @POST("goods/savereceive")
    Call<ScanQrRespone> getCustomerReceive(
            @Header("device") String device,
            @Header("token") String token,
            @Header("signature") String signature,
            @Header("session") String session,
            @Field("code") String code,
            @Field("lats") String lats,
            @Field("longs") String longs

    );

    @FormUrlEncoded
    @POST("goods/savedeliveryreceive")
    Call<ScanQrRespone> savedeliveryreceive(
            @Header("device") String device,
            @Header("token") String token,
            @Header("signature") String signature,
            @Header("session") String session,
            @Field("delivery_detail_id") int delivery_detail_id
    );

    @FormUrlEncoded
    @POST("goods/saveundelivery")
    Call<ScanQrRespone> saveUnDelivery(
            @Header("device") String device,
            @Header("token") String token,
            @Header("signature") String signature,
            @Header("session") String session,
            @Field("delivery_detail_id") int delivery_detail_id,
            @Field("reason_id") int reason_id
    );

    @FormUrlEncoded
    @POST("goods/getreceivelist")
    Call<SearchData> searchList(
            @Header("device") String device,
            @Header("token") String token,
            @Header("signature") String signature,
            @Header("session") String session,
            @Field("receiver") String receiver
    );

    @FormUrlEncoded
    @POST("goods/getbranchreceive")
    Call<SearchData> searchListByBranch(
            @Header("device") String device,
            @Header("token") String token,
            @Header("signature") String signature,
            @Header("session") String session,
            @Field("receiver") String receiver
    );


    @FormUrlEncoded
    @POST("goods/moveitemtovan")
    Call<MoveItemToVanData> moveItemToVan(
            @Header("device") String device,
            @Header("token") String token,
            @Header("signature") String signature,
            @Header("session") String session,
            @Field("sys_code") String sysCode,
            @Field("code") String code,
            @Field("branch_id") String branchId,
            @Field("van_id") String vanId,
            @Field("destination_to_id") String destinationToId,
            @Field("departure") String departure,
            @Field("type") int type,
            @Field("lats") String lats,
            @Field("longs") String longs

    );

    @FormUrlEncoded
    @POST("goods/receiveitem")
    Call<MoveItemToVanData> getReceiveItem(
            @Header("device") String device,
            @Header("token") String token,
            @Header("signature") String signature,
            @Header("session") String session,
            @Field("sys_code") String sysCode,
            @Field("code") String code,
            @Field("branch_id") String branchId,
            @Field("location_id") String locationId,
            @Field("lats") String lats,
            @Field("longs") String longs

    );

    @FormUrlEncoded
    @POST("goods/savetransit")
    Call<MoveItemToVanData> getTransitItem(
            @Header("device") String device,
            @Header("token") String token,
            @Header("signature") String signature,
            @Header("session") String session,
            @Field("sys_code") String sysCode,
            @Field("code") String code,
            @Field("branch_id") String branchId,
            @Field("lats") String lats,
            @Field("longs") String longs

    );


    @POST("user/checkpermission")
    Call<PermissionRespone> getPermission(
            @Header("device") String device,
            @Header("token") String token,
            @Header("signature") String signature,
            @Header("session") String session
    );


    @POST("goods/getdelivery")
    Call<GetDeliveryRespone> getdelivery(
            @Header("device") String device,
            @Header("token") String token,
            @Header("signature") String signature,
            @Header("session") String session
    );

    @FormUrlEncoded
    @POST("goods/getdeliverybycode")
    Call<GetDeliveryRespone> getdeliveryByCode(
            @Header("device") String device,
            @Header("token") String token,
            @Header("signature") String signature,
            @Header("session") String session,
            @Field("code") String code
    );

    @POST("goods/getpickup")
    Call<PickUpRespone> getPickUp(
            @Header("device") String device,
            @Header("token") String token,
            @Header("signature") String signature,
            @Header("session") String session
    );

    @FormUrlEncoded
    @POST("goods/getdeliverybycode")
    Call<GetDeliveryRespone> getdeliverybycode(
            @Header("device") String device,
            @Header("token") String token,
            @Header("signature") String signature,
            @Header("session") String session,
            @Field("code") String Code
    );

    @POST("goods/closedelivery")
    Call<RequestTokenResponse> closedelivery(
            @Header("device") String device,
            @Header("token") String token,
            @Header("signature") String signature,
            @Header("session") String session
    );

    @FormUrlEncoded
    @POST("goods/getpickupbycode")
    Call<PickUpRespone> getpickupbycode(
            @Header("device") String device,
            @Header("token") String token,
            @Header("signature") String signature,
            @Header("session") String session,
            @Field("code") String Code
    );

    @GET("sendSms/{code}")
    Call<SMSRespone> getPosts(@Path("code") String postId);

    @FormUrlEncoded
    @POST("goods/getcustomercall")
    Call<GetCustomerResponse> getCustomerCall(
            @Header("device") String device,
            @Header("token") String token,
            @Header("signature") String signature,
            @Header("session") String session,
            @Field("branchId") int branchId,
            @Field("locationId") int locationId,
            @Field("date") String date,
            @Field("mobileOpt") int mobileOpt,
            @Field("receiverTel") String receiverTel,
            @Field("type") int type,
            @Field("status") int status,
            @Field("goods_transfer_id") int goods_transfer_id
    );

    @FormUrlEncoded
    @POST("goods/savecustomercall")
    Call<SaveCustomerCallResponse> saveCustomerCall(
            @Header("device") String device,
            @Header("token") String token,
            @Header("signature") String signature,
            @Header("session") String session,
            @Field("id") int id,
            @Field("goods_transfer_id") int goodsTransferId,
            @Field("reason") String reason,
            @Field("status") int status,
            @Field("delivery_destination_id") int deliveryDestinationId,
            @Field("delivery_fee") String deliverFee,
            @Field("delivery_address") String deliveryAddress
    );

    @FormUrlEncoded
    @POST("goods/scancustomercall")
    Call<SaveCustomerCallResponse> scancustomercall(
            @Header("device") String device,
            @Header("token") String token,
            @Header("signature") String signature,
            @Header("session") String session,
            @Field("code") String Code,
            @Field("branch_id") int brandId

    );

    @FormUrlEncoded
    @POST("goods/getitemnotreceive")
    Call<ItemNotReceiveResponse> getItemNotReceive(
            @Header("device") String device,
            @Header("token") String token,
            @Header("signature") String signature,
            @Header("session") String session,
            @Field("goods_transfer_id") int goodTransferId,
            @Field("branch_id") int brandId,
            @Field("num") String num
    );


    @FormUrlEncoded
    @POST("goods/saveitemnotreceive")
    Call<SaveCustomerCallResponse> saveitemnotreceive(
            @Header("device") String device,
            @Header("token") String token,
            @Header("signature") String signature,
            @Header("session") String session,
            @Field("location_id") int location_id,
            @Field("branch_id") int brandId,
            @Field("goods_transfer_id") int goods_transfer_id,
            @Field("num") String num
    );

    @FormUrlEncoded
    @POST("goods/checkchangecode")
    Call<ChangeResponse> checkChangeCode(
            @Header("device") String device,
            @Header("token") String token,
            @Header("signature") String signature,
            @Header("session") String session,
            @Field("scan") String scan,
            @Field("branch_id") String brandId,
            @Field("type") String type
    );

    @FormUrlEncoded
    @POST("goods/changeaddmanaul")
    Call<ChangeAddResponse> changeAddManaul(
            @Header("device") String device,
            @Header("token") String token,
            @Header("signature") String signature,
            @Header("session") String session,
            @Field("telephone") String telephone,
            @Field("branch_id") String brandId,
            @Field("type") String type
    );

    @FormUrlEncoded
    @POST("goods/savechangebranch")
    Call<SaveResponse> saveChangeBranch(
            @Header("device") String device,
            @Header("token") String token,
            @Header("signature") String signature,
            @Header("session") String session,
            @Field("goods_transfer_id") String goodTransferId,
            @Field("branch_id") String branchId,
            @Field("destination_to_id") String destinationToId,
            @Field("customer_receiver_id") String customerReceiverId,
            @Field("num") String num
    );
    @FormUrlEncoded
    @POST("goods/savechangedestination")
    Call<SaveResponse> saveChangeDestination(
            @Header("device") String device,
            @Header("token") String token,
            @Header("signature") String signature,
            @Header("session") String session,
            @Field("goods_transfer_id") String goodTransferId,
            @Field("branch_id") String branchId,
            @Field("destination_to_id") String destinationToId,
            @Field("customer_receiver_id") String customerReceiverId,
            @Field("num") String num
    );

    @FormUrlEncoded
    @POST("goods/savereturndestination")
    Call<SaveResponse> saveReturn(
            @Header("device") String device,
            @Header("token") String token,
            @Header("signature") String signature,
            @Header("session") String session,
            @Field("goods_transfer_id") String goodTransferId,
            @Field("branch_id") String branchId,
            @Field("customer_receiver_id") String customerReceiverId,
            @Field("num") String num
    );

    @FormUrlEncoded
    @POST("goods/savereturncenter")
    Call<SaveResponse> savereturncenter(
            @Header("device") String device,
            @Header("token") String token,
            @Header("signature") String signature,
            @Header("session") String session,
            @Field("goods_transfer_id") String goodTransferId,
            @Field("branch_id") String branchId,
            @Field("customer_receiver_id") String customerReceiverId,
            @Field("num") String num
    );
    @FormUrlEncoded
    @POST("goods/customerreceiveform")
    Call<CustomerReceiveFormResponse> customerreceiveform(
            @Header("device") String device,
            @Header("token") String token,
            @Header("signature") String signature,
            @Header("session") String session,
            @Field("customer_receive_id") int customer_receive_id
    );
    @FormUrlEncoded
    @POST("goods/savereceive")
    Call<ScanQrRespone> saveCustomerReceiveForm(
            @Header("device") String device,
            @Header("token") String token,
            @Header("signature") String signature,
            @Header("session") String session,
            @Field("id") String id,
            @Field("note") String note,
            @Field("item_received") String itemReceive,
            @Field("lats") String lats,
            @Field("longs") String longs
    );

    @FormUrlEncoded
    @POST("goods/receiveaddmanaul")
    Call<ReceiveAddManduleResponse> receiveAddManaul(
            @Header("device") String device,
            @Header("token") String token,
            @Header("signature") String signature,
            @Header("session") String session,
            @Field("telephone") String telephone,
            @Field("branch_id") String branchId,
            @Field("location_id") String locationId
    );

    @FormUrlEncoded
    @POST("goods/movetovanaddmanaul")
    Call<ReceiveAddManduleResponse> moveToVanAddManaul(
            @Header("device") String device,
            @Header("token") String token,
            @Header("signature") String signature,
            @Header("session") String session,
            @Field("telephone") String telephone,
            @Field("branch_id") String branchId
    );

    @FormUrlEncoded
    @POST("goods/savereceiveaddmanaul")
    Call<ScanQrRespone> saveReceiveAddManaul(
            @Header("device") String device,
            @Header("token") String token,
            @Header("signature") String signature,
            @Header("session") String session,
            @Field("branch_id") String branchId,
            @Field("location_id") String locationId,
            @Field("item") String item,
            @Field("lats") String lats,
            @Field("longs") String longs
    );


    @FormUrlEncoded
    @POST("goods/savemovetovanaddmanaul")
    Call<ScanQrRespone> saveReceiveAddManaulMoveToVan(
            @Header("device") String device,
            @Header("token") String token,
            @Header("signature") String signature,
            @Header("session") String session,
            @Field("branch_id") String branchId,
            @Field("van_id") String van,
            @Field("item") String item,
            @Field("departure") String departure,
            @Field("type") String type,
            @Field("lats") String lats,
            @Field("longs") String longs
    );


    @FormUrlEncoded
    @POST("goods/callhistory")
    Call<CallHistoryResponse> callHistory(
            @Header("device") String device,
            @Header("token") String token,
            @Header("signature") String signature,
            @Header("session") String session,
            @Field("customer_receive_id") String customerReceiveId
    );

    @FormUrlEncoded
    @POST("goods/scancustomerreceive")
    Call<CallHistoryResponse> scanCustomerReceive(
            @Header("device") String device,
            @Header("token") String token,
            @Header("signature") String signature,
            @Header("session") String session,
            @Field("branch_id") String branchId,
            @Field("code") String code

    );


    @POST("goods/getlockerpickup")
    Call<LockerResponse> getLockerPickUp(
            @Header("device") String device,
            @Header("token") String token,
            @Header("signature") String signature,
            @Header("session") String session
    );

    @POST("goods/getlockerdropoff")
    Call<LockerResponse> getLockerDropOff(
            @Header("device") String device,
            @Header("token") String token,
            @Header("signature") String signature,
            @Header("session") String session
    );

    @POST("goods/lockerqrlogin")
    Call<LockerLoginResponse> getLoginQR(
            @Header("device") String device,
            @Header("token") String token,
            @Header("signature") String signature,
            @Header("session") String session
    );

    @FormUrlEncoded
    @POST("goods/lockerprintlabel")
    Call<LockerLabelResponse> getLockerTicket(
            @Header("device") String device,
            @Header("token") String token,
            @Header("signature") String signature,
            @Header("session") String session,
            @Field("id") int id
    );

}
