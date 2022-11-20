#import "PerimeterXModule.h"
@import PerimeterX_SDK;

static NSString *pxNewHeaders = @"PxNewHeaders";
static NSString *pxChallengeResult = @"PxChallengeResult";
static NSString *pxSolved = @"solved";
static NSString *pxCancelled = @"cancelled";
static NSString *pxTrue = @"true";
static NSString *pxFalse = @"false";

@implementation PerimeterXModule

// MARK: - NSObject

- (instancetype)init {
  self = [super init];
  shared = self;
  return self;
}

// MARK: - RCTEventEmitter

- (NSArray<NSString *> *)supportedEvents {
  return @[pxNewHeaders, pxChallengeResult];
}

// MARK: - PX Module

static PerimeterXModule *shared = nil;

+ (PerimeterXModule *)shared {
  return shared;
}

- (void)sendUpdatedHeaders:(NSDictionary<NSString *,NSString *> *)headers {
  NSData *data = [NSJSONSerialization dataWithJSONObject:headers options:0 error:nil];
  NSString *json = [[NSString alloc] initWithData:data encoding:NSUTF8StringEncoding];
  [self sendEventWithName:pxNewHeaders body:json];
}

- (void)sendChallengeSolvedEvent {
  [self sendEventWithName:pxChallengeResult body:pxSolved];
}

- (void)sendChallengeCancelledEvent {
  [self sendEventWithName:pxChallengeResult body:pxCancelled];
}

RCT_EXPORT_MODULE(PerimeterXModule);

RCT_EXPORT_METHOD(getHTTPHeaders:(RCTResponseSenderBlock)callback) {
  NSDictionary<NSString *, NSString *> *headers = [PerimeterX headersForURLRequestForAppId:nil];
  NSData *data = [NSJSONSerialization dataWithJSONObject:headers options:0 error:nil];
  NSString *json = [[NSString alloc] initWithData:data encoding:NSUTF8StringEncoding];
  callback(@[json]);
}

RCT_EXPORT_METHOD(handleResponse:(NSString *)response code:(NSInteger)code url:(NSString *)url callback:(RCTResponseSenderBlock)callback) {
  NSData *data = [response dataUsingEncoding:NSUTF8StringEncoding];
  NSHTTPURLResponse *httpURLResponse = [[NSHTTPURLResponse alloc] initWithURL:[NSURL URLWithString:url] statusCode:code HTTPVersion:nil headerFields:nil];
  BOOL handled = [PerimeterX handleResponseForAppId:nil data:data response:httpURLResponse];
  callback(@[handled ? pxTrue : pxFalse]);
}

@end
