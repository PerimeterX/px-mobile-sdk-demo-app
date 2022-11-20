#import <Foundation/Foundation.h>
#import <React/RCTBridgeModule.h>
#import <React/RCTEventEmitter.h>

NS_ASSUME_NONNULL_BEGIN

@interface PerimeterXModule : RCTEventEmitter <RCTBridgeModule>

+ (PerimeterXModule *)shared;
- (void)sendUpdatedHeaders:(NSDictionary<NSString *,NSString *> *)headers;
- (void)sendChallengeSolvedEvent;
- (void)sendChallengeCancelledEvent;

@end

NS_ASSUME_NONNULL_END
