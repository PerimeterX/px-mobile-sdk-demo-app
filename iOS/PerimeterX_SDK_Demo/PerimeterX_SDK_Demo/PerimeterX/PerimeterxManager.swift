//
//  PerimeterxManager.swift
//  PerimeterX_SDK_Demo
//
//  Created by PerimeterX.
//

import Foundation
import PerimeterX_SDK

class PerimeterxManager: NSObject, PerimeterXDelegate {
    
    // MARK: - PerimeterXDelegate
    
    func perimeterxDidRequestBlocked(url: URL?, appId: String) {
        print("PerimeterX Request Blocked")
    }
    
    func perimeterxDidChallengeSolved(forAppId appId: String) {
        print("PerimeterX Challenge Solved")
    }
    
    func perimeterxDidChallengeCancelled(forAppId appId: String) {
        print("PerimeterX Challenge Cancelled")
    }
    
    func perimeterxHeadersWereUpdated(headers: [String : String], forAppId appId: String) {
        print("PerimeterX Headers Were Updated")
    }
    
    // MARK: - singleton
    
    static let shared = PerimeterxManager()
    
    // MARK: - properties
    
    private let pxAppId = "PXj9y4Q8Em"
    let urlRequestInterceptionType: PXPolicyUrlRequestInterceptionType = .interceptWithDelayedResponse
    
    // MARK: - PerimeterX
    
    func start() {
        print("SDK version: \(PerimeterX.sdkVersion())")
        
        start(appId: pxAppId)
        setCustomParameters()
    }
    
    private func start(appId: String) {
        // Create and config the policy //
        let policy = PXPolicy()
        policy.urlRequestInterceptionType = urlRequestInterceptionType
        policy.doctorCheckEnabled = true
        policy.set(domains: ["sample-ios.pxchk.net"], forAppId: appId)
        
        // Start PerimeterX SDK with your AppID //
        do {
            try PerimeterX.start(appId: appId, delegate: self, policy: policy)
        }
        catch {
            print("error: \(error)")
        }
    }
    
    private func setCustomParameters() {
        var customParameters = [String: String]()
        customParameters["custom_param1"] = "hello"
        customParameters["custom_param2"] = "world"
        do {
            try PerimeterX.setCustomParameters(parameters: customParameters)
        }
        catch {
            print("error: \(error)")
        }
    }
}
