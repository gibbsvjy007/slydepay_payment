#import "SlydepayPaymentPlugin.h"
#import <slydepay_payment/slydepay_payment-Swift.h>

@implementation SlydepayPaymentPlugin
+ (void)registerWithRegistrar:(NSObject<FlutterPluginRegistrar>*)registrar {
  [SwiftSlydepayPaymentPlugin registerWithRegistrar:registrar];
}
@end
