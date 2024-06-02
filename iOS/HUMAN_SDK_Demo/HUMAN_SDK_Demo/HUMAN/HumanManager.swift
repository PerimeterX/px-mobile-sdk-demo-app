//
//  HumanManager.swift
//  HUMAN_SDK_Demo
//
//  Created by HUMAN.
//

import Foundation
import HUMAN

class HumanManager: NSObject, HumanDelegate {
    
    // MARK: - HumanDelegate
    
    func humanDidRequestBlocked(url: URL?, appId: String) {
        print("Request Blocked")
    }
    
    func humanDidChallengeSolved(forAppId appId: String) {
        print("Challenge Solved")
    }
    
    func humanDidChallengeCancelled(forAppId appId: String) {
        print("Challenge Cancelled")
    }
    
    func humanHeadersWereUpdated(headers: [String : String], forAppId appId: String) {
        print("Headers Were Updated")
    }
    
    func humanDidRenderChallenge(forAppId appId: String) {
        print("Challenge Rendered")
    }
    
    func humanDidFailRenderChallenge(forAppId appId: String) {
        print("Challenge Render Failed")
    }
    
    // MARK: - singleton
    
    static let shared = HumanManager()
    
    // MARK: - properties
    
    private let pxAppId = "PXj9y4Q8Em"
    let urlRequestInterceptionType: HSAutomaticInterceptorType = .interceptWithDelayedResponse
    
    // MARK: - HUMAN
    
    func start() {
        print("SDK version: \(HumanSecurity.sdkVersion())")
        
        start(appId: pxAppId)
        setCustomParameters()
    }
    
    private func start(appId: String) {
        // Create and config the policy //
        let policy = HSPolicy()
        policy.automaticInterceptorPolicy.interceptorType = urlRequestInterceptionType
        policy.automaticInterceptorPolicy.set(interceptedDomains: ["sample-ios.pxchk.net"], forAppId: appId)
        policy.doctorAppPolicy.enabled = true
        
        
        // Start HUMAN SDK with your AppID //
        do {
            try HumanSecurity.start(appId: appId, delegate: self, policy: policy)
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
            try HumanSecurity.setCustomParameters(parameters: customParameters)
        }
        catch {
            print("error: \(error)")
        }
    }
}
