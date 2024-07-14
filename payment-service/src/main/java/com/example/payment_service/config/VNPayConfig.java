package com.example.payment_service.config;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class VNPayConfig {
    public static final String PAYMENT_URL = "https://sandbox.vnpayment.vn/paymentv2/vpcpay.html";
    public static final String RETURN_URL = "http://localhost:8086/api/payment/result";

    public static final String VERSION = "2.1.0";
    public static final String COMMAND = "pay";
    public static final String TMNCODE = "1Z3D7392";
    public static final String HASH_SECRET = "6X3RYQ9V7SBM7JBLYZO8WMOLSOFF4M7J";

    public static final String VERSION_PARAM = "vnp_Version";
    public static final String COMMAND_PARAM = "vnp_Command";
    public static final String TMNCODE_PARAM = "vnp_TmnCode";
    public static final String AMOUNT_PARAM = "vnp_Amount";
    public static final String CREATE_DATE_PARAM = "vnp_CreateDate";
    public static final String CURRENCY_PARAM = "vnp_CurrCode";
    public static final String IP_ADDRESS_PARAM = "vnp_IpAddr";
    public static final String LOCALE_PARAM = "vnp_Locale";
    public static final String ORDER_INFO_PARAM = "vnp_OrderInfo";
    public static final String ORDER_TYPE_PARAM = "vnp_OrderType";
    public static final String RETURN_URL_PARAM = "vnp_ReturnUrl";
    public static final String EXPIRE_DATE_PARAM = "vnp_ExpireDate";
    public static final String TXNREF_PARAM = "vnp_TxnRef";
    public static final String SECURE_HASH_PARAM = "vnp_SecureHash";
}
