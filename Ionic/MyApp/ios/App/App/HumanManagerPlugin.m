#import <Foundation/Foundation.h>
#import <Capacitor/Capacitor.h>

CAP_PLUGIN(HumanManager, "Human",
    CAP_PLUGIN_METHOD(getHttpHeaders, CAPPluginReturnPromise);
    CAP_PLUGIN_METHOD(handleResponse, CAPPluginReturnPromise);
)
