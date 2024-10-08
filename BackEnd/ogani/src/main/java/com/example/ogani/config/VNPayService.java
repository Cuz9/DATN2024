package com.example.ogani.config;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class VNPayService {

    public String createOrder(int total, String orderInfor, String urlReturn) {
        String vnp_Version = "2.1.0";
        String vnp_Command = "pay";
        String vnp_TxnRef = VNPayConfig.getRandomNumber(8);
        String vnp_IpAddr = "127.0.0.1";
        String vnp_TmnCode = VNPayConfig.vnp_TmnCode;
        String orderType = "order-type";

        Map<String, String> vnp_Params = new HashMap<>();
        vnp_Params.put("vnp_Version", vnp_Version);
        vnp_Params.put("vnp_Command", vnp_Command);
        vnp_Params.put("vnp_TmnCode", vnp_TmnCode);
        vnp_Params.put("vnp_Amount", String.valueOf(total * 100));
        vnp_Params.put("vnp_CurrCode", "VND");

        vnp_Params.put("vnp_TxnRef", vnp_TxnRef);
        vnp_Params.put("vnp_OrderInfo", orderInfor);
        vnp_Params.put("vnp_OrderType", orderType);

        String locate = "vn";
        vnp_Params.put("vnp_Locale", locate);

        urlReturn += VNPayConfig.vnp_Returnurl;
        vnp_Params.put("vnp_ReturnUrl", urlReturn);
        vnp_Params.put("vnp_IpAddr", vnp_IpAddr);

        Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        String vnp_CreateDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_CreateDate", vnp_CreateDate);

        cld.add(Calendar.MINUTE, 15);
        String vnp_ExpireDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_ExpireDate", vnp_ExpireDate);

        List fieldNames = new ArrayList(vnp_Params.keySet());
        Collections.sort(fieldNames);
        StringBuilder hashData = new StringBuilder();
        StringBuilder query = new StringBuilder();
        Iterator itr = fieldNames.iterator();
        while (itr.hasNext()) {
            String fieldName = (String) itr.next();
            String fieldValue = (String) vnp_Params.get(fieldName);
            if ((fieldValue != null) && (!fieldValue.isEmpty())) {
                // Build hash data
                hashData.append(fieldName);
                hashData.append('=');
                try {
                    hashData.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                    // Build query
                    query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII.toString()));
                    query.append('=');
                    query.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                if (itr.hasNext()) {
                    query.append('&');
                    hashData.append('&');
                }
            }
        }
        String queryUrl = query.toString();
        String vnp_SecureHash = VNPayConfig.hmacSHA512(VNPayConfig.vnp_HashSecret, hashData.toString());
        queryUrl += "&vnp_SecureHash=" + vnp_SecureHash;
        String paymentUrl = VNPayConfig.vnp_PayUrl + "?" + queryUrl;
        return paymentUrl;
    }

    public String createOrder2(int total, String orderInfor, String urlReturn) throws UnsupportedEncodingException {
        String vnp_Version = "2.1.0";
        String vnp_Command = "pay";
        String vnp_OrderInfo = "Thanh toan don hang";
        String orderType = "other";
        String vnp_TxnRef = Long.toString(System.currentTimeMillis());
        String vnp_IpAddr = "127.0.0.1";
        String vnp_TmnCode = VNPayConfig.vnp_TmnCode;

        int amount = total * 100;
        Map<String, String> vnp_Params = new HashMap<String, String>();
        vnp_Params.put("vnp_Version", vnp_Version);
        vnp_Params.put("vnp_Command", vnp_Command);
        vnp_Params.put("vnp_TmnCode", vnp_TmnCode);
        vnp_Params.put("vnp_Amount", String.valueOf(amount));
        vnp_Params.put("vnp_CurrCode", "VND");
        String bank_code = "VCB";
//        vnp_Params.put("vnp_BankCode", bank_code);
//        if (bank_code != null && !bank_code.isEmpty()) {
//            vnp_Params.put("vnp_BankCode", bank_code);
//        }
        vnp_Params.put("vnp_TxnRef", vnp_TxnRef);
        vnp_Params.put("vnp_OrderInfo", vnp_OrderInfo);
        vnp_Params.put("vnp_OrderType", orderType);

        vnp_Params.put("vnp_Locale", "vn");

        vnp_Params.put("vnp_ReturnUrl", "http://localhost:4200/checkout");
        vnp_Params.put("vnp_IpAddr", vnp_IpAddr);
        Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));

        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        String vnp_CreateDate = formatter.format(cld.getTime());

        vnp_Params.put("vnp_CreateDate", vnp_CreateDate);
        cld.add(Calendar.MINUTE, 15);
        String vnp_ExpireDate = formatter.format(cld.getTime());
        //Add Params of 2.1.0 Version
        vnp_Params.put("vnp_ExpireDate", vnp_ExpireDate);
        //Billing
        vnp_Params.put("vnp_Bill_Mobile", "0346206280");
        vnp_Params.put("vnp_Bill_Email", "hoangthien66771508@gmail.com");
        String fullName = "Hoang Xuan Thien";
        if (fullName != null && !fullName.isEmpty()) {
            int idx = fullName.indexOf(' ');
            String firstName = fullName.substring(0, idx);
            String lastName = fullName.substring(fullName.lastIndexOf(' ') + 1);
            vnp_Params.put("vnp_Bill_FirstName", firstName);
            vnp_Params.put("vnp_Bill_LastName", lastName);

        }
        vnp_Params.put("vnp_Bill_Address", "TuHoang");
        vnp_Params.put("vnp_Bill_City", "HaNoi");
        vnp_Params.put("vnp_Bill_Country", "VND");
        vnp_Params.put("vnp_Bill_State", "NamTuLiem");
        // Invoice
        vnp_Params.put("vnp_Inv_Phone", "0346206280");
        vnp_Params.put("vnp_Inv_Email", "hoangthien66771508@gmail.com");
        vnp_Params.put("vnp_Inv_Customer", "HoangXuanThien");
        vnp_Params.put("vnp_Inv_Address", "TuHoang");
        vnp_Params.put("vnp_Inv_Company", "NamTuLiem");
        vnp_Params.put("vnp_Inv_Taxcode", "Tax");
        vnp_Params.put("vnp_Inv_Type", "other");
        //Build data to hash and querystring
        List<String> fieldNames = new ArrayList<>(vnp_Params.keySet());
        Collections.sort(fieldNames);
        StringBuilder hashData = new StringBuilder();
        StringBuilder query = new StringBuilder();
        Iterator itr = fieldNames.iterator();
        while (itr.hasNext()) {
            String fieldName = (String) itr.next();
            String fieldValue = (String) vnp_Params.get(fieldName);
            if ((fieldValue != null) && (!fieldValue.isEmpty())) {
                //Build hash data
                hashData.append(fieldName);
                hashData.append('=');
                hashData.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                //Build query
//                query.append(fieldName);
                query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII.toString()));
                query.append('=');
                query.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                if (itr.hasNext()) {
                    query.append('&');
                    hashData.append('&');
                }
            }
        }
        String queryUrl = query.toString();
        String vnp_SecureHash = VNPayConfig.hmacSHA512(VNPayConfig.vnp_HashSecret, hashData.toString());
        queryUrl += "&vnp_SecureHash=" + vnp_SecureHash;
        String paymentUrl = VNPayConfig.vnp_PayUrl + "?" + queryUrl;
        com.google.gson.JsonObject job = new JsonObject();
        job.addProperty("code", "00");
        job.addProperty("message", "success");
        job.addProperty("data", paymentUrl);
        Gson gson = new Gson();
        System.out.println(gson.toJson(job));
        return paymentUrl;
    }

    public int orderReturn(int orderTotal, String orderInfo, String vnp_SecureHash, String vnp_TransactionStatus) {
        Map<String, String> fields = new HashMap();
        try {
            // Encode key và value trước khi đưa vào map
            String encodedOrderInfoKey = URLEncoder.encode("orderInfo", StandardCharsets.UTF_8.toString());
            String encodedOrderInfoValue = URLEncoder.encode(orderInfo, StandardCharsets.UTF_8.toString());

            String encodedSecureHashKey = URLEncoder.encode("vnp_SecureHash", StandardCharsets.UTF_8.toString());
            String encodedSecureHashValue = URLEncoder.encode(vnp_SecureHash, StandardCharsets.UTF_8.toString());

            String encodedTransactionStatusKey = URLEncoder.encode("vnp_TransactionStatus",
                    StandardCharsets.UTF_8.toString());
            String encodedTransactionStatusValue = URLEncoder.encode(vnp_TransactionStatus,
                    StandardCharsets.UTF_8.toString());

            String encodedOrderTotal = URLEncoder.encode("orderTotal",
                    StandardCharsets.UTF_8.toString());
            String encodedOrderTotalValue = URLEncoder.encode(String.valueOf(orderTotal),
                    StandardCharsets.UTF_8.toString());

            // Đưa các key và value đã encode vào map
            if (encodedOrderInfoValue != null && encodedOrderInfoValue.length() > 0) {
                fields.put(encodedOrderInfoKey, encodedOrderInfoValue);
            }else if (encodedSecureHashValue != null && encodedSecureHashValue.length() > 0) {
                fields.put(encodedSecureHashKey, encodedSecureHashValue);
            }else if (encodedTransactionStatusValue != null && encodedTransactionStatusValue.length() > 0) {
                fields.put(encodedTransactionStatusKey, encodedTransactionStatusValue);
            }else if (encodedOrderTotalValue != null && encodedOrderTotalValue.length() > 0) {
                fields.put(encodedOrderTotal, encodedOrderTotalValue);
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        String vnp_SecureHashField = vnp_SecureHash;
        if (fields.containsKey("vnp_SecureHashType")) {
            fields.remove("vnp_SecureHashType");
        }
        if (fields.containsKey("vnp_SecureHash")) {
            fields.remove("vnp_SecureHash");
        }
        if ("00".equals(vnp_TransactionStatus)) {
            return 1;
        } else {
            return 0;
        }

        // String signValue = VNPayConfig.hashAllFields(fields);

        // System.out.println("sign value: "+signValue);
        // System.out.println("params secureHash: " +vnp_SecureHashField);
        // if (signValue.equals(vnp_SecureHashField)) {
            
        // } else {
        //     return -1;
        // }
    }

}
