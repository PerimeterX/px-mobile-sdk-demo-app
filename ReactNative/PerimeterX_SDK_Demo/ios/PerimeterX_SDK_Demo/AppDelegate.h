#import <React/RCTBridgeDelegate.h>
#import <UIKit/UIKit.h>
@import PerimeterX_SDK;

@interface AppDelegate : UIResponder <UIApplicationDelegate, RCTBridgeDelegate, PerimeterXDelegate>

@property (nonatomic, strong) UIWindow *window;

@end
