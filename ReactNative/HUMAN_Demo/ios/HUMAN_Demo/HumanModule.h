#import <Foundation/Foundation.h>
#import <React/RCTBridgeModule.h>
#import <React/RCTEventEmitter.h>

NS_ASSUME_NONNULL_BEGIN

@interface HumanModule : RCTEventEmitter <RCTBridgeModule>

+ (HumanModule *)shared;
- (void)handleUpdatedHeaders:(NSDictionary<NSString *,NSString *> *)headers;
- (void)handleChallengeSolvedEvent;
- (void)handleChallengeCancelledEvent;

@end

NS_ASSUME_NONNULL_END
