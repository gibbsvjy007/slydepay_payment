import 'dart:async';

import 'package:flutter/services.dart';

class SlydepayPayment {
  static const MethodChannel _channel =
      const MethodChannel('slydepay_payment');

  static Future<String> get platformVersion async {
    final String version = await _channel.invokeMethod('getPlatformVersion');
    return version;
  }

  static Future<List> openSlydepayPaymentDialog({ double amount, String itemName, String description, String customerName, String customerEmail, String orderCode, String phoneNumber, String merchantKey, String merchantEmail }) async {
    final Map<String, dynamic> params = <String, dynamic> {
      'amount': amount,
      'itemName': itemName,
      'description': description,
      'customerName': customerName,
      'customerEmail': customerEmail,
      'orderCode': orderCode,
      'phoneNumber': phoneNumber,
      'merchantKey': merchantKey,
      'merchantEmail': merchantEmail
    };
   final List<dynamic> result = await _channel.invokeMethod('startSlydepayPayment', params);
   return result;
  }
}
