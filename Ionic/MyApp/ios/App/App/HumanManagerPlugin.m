//
//  HumanManagerPlugin.m
//  App
//
//  Created by Oren Yaar on 03/08/2023.
//

#import <Foundation/Foundation.h>
#import <Capacitor/Capacitor.h>

CAP_PLUGIN(HumanManagerPlugin, "HUMAN",
    CAP_PLUGIN_METHOD(getHttpHeaders, CAPPluginReturnPromise);
    CAP_PLUGIN_METHOD(handleResponse, CAPPluginReturnPromise);
)
