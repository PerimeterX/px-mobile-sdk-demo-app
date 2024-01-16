#import "HumanModule.h"
@import PerimeterX_SDK;

static NSString *strNewHeaders = @"HumanNewHeaders";
static NSString *strChallengeResult = @"HumanChallengeResult";
static NSString *strSolved = @"solved";
static NSString *strCancelled = @"cancelled";
static NSString *strFalse = @"false";

@implementation HumanModule

static HumanModule *shared = nil;

+ (HumanModule *)shared {
  return shared;
}

- (instancetype)init {
  self = [super init];
  shared = self;
  return self;
}

- (NSArray<NSString *> *)supportedEvents {
  return @[strNewHeaders, strChallengeResult];
}

- (void)handleUpdatedHeaders:(NSDictionary<NSString *,NSString *> *)headers {
  NSData *data = [NSJSONSerialization dataWithJSONObject:headers options:0 error:nil];
  NSString *json = [[NSString alloc] initWithData:data encoding:NSUTF8StringEncoding];
  [self sendEventWithName:strNewHeaders body:json];
}

- (void)handleChallengeSolvedEvent {
  [self sendEventWithName:strChallengeResult body:strSolved];
}

- (void)handleChallengeCancelledEvent {
  [self sendEventWithName:strChallengeResult body:strCancelled];
}

RCT_EXPORT_MODULE(HumanModule);

RCT_REMAP_METHOD(getHTTPHeaders,
                 resolver:(RCTPromiseResolveBlock)resolve
                 rejecter:(RCTPromiseRejectBlock)reject) {
  NSDictionary<NSString *, NSString *> *headers = [HumanSecurity headersForURLRequestForAppId:nil];
  NSData *data = [NSJSONSerialization dataWithJSONObject:headers options:0 error:nil];
  NSString *json = [[NSString alloc] initWithData:data encoding:NSUTF8StringEncoding];
  resolve(@[json]);
}

RCT_REMAP_METHOD(handleResponse,
                 rsponse:(NSString *)response
                 code:(NSInteger)code
                 url:(NSString *)url
                 resolver:(RCTPromiseResolveBlock)resolve
                 rejecter:(RCTPromiseRejectBlock)reject) {
  NSData *data = [response dataUsingEncoding:NSUTF8StringEncoding];
  NSHTTPURLResponse *httpURLResponse = [[NSHTTPURLResponse alloc] initWithURL:[NSURL URLWithString:url] statusCode:code HTTPVersion:nil headerFields:nil];
  BOOL handled = [HumanSecurity handleResponseWithResponse:httpURLResponse data:data forAppId:nil callback:^(enum HumanChallengeResult result) {
    resolve((result == HumanChallengeResultSolved ? strSolved : strCancelled));
  }];
  if (!handled) {
    resolve(strFalse);
  }
}

@end
