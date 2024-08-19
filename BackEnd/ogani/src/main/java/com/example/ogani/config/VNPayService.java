package com.example.ogani.config;

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
            if ((fieldValue != null) && (fieldValue.length() > 0)) {
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
