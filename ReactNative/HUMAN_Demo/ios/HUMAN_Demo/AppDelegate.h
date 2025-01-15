#import <React/RCTBridgeDelegate.h>
#import <UIKit/UIKit.h>
@import HUMAN;

@interface AppDelegate : UIResponder <UIApplicationDelegate, RCTBridgeDelegate, HSBotDefenderDelegate>

@property (nonatomic, strong) UIWindow *window;

@end
