//
//  PerimeterxManager.swift
//  PerimeterX_SDK_Demo
//
//  Created by HUMAN.
//

import Foundation
import PerimeterX_SDK

class HumanManager: NSObject, HumanDelegate {
    
    // MARK: - singleton
    
    static let shared = HumanManager()
    
    // MARK: - properties
    
    private let humanAppID = "PXj9y4Q8Em"
    let interceptorType: HSAutomaticInterceptorType = .interceptWithDelayedResponse
    
    // MARK: - PerimeterX
    
    func start() {
        print("HUMAN Security SDK v\(HumanSecurity.sdkVersion())")
        
        // Create and config the policy.
        let policy = HSPolicy()
        
        // Set the automatic interceptor's type. Set it to `none` in order to disable it.
        policy.automaticInterceptorPolicy.interceptorType = interceptorType
        
        // Enable the Doctor App - a tool that helps you to verify the integration.
        policy.doctorAppPolicy.enabled = true
        
        // Start the SDK with your AppID.
        do {
            try HumanSecurity.start(appId: humanAppID, delegate: self, policy: policy)
        }
        catch {
            print("error: \(error)")
        }
        
        setCustomParametersForBotDefender()
    }
    
    private func setCustomParametersForBotDefender() {
        // Set custom parameters for Bot Defender (optional).
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
    
    // MARK: - HumanDelegate
    
    func humanDidRequestBlocked(url: URL?, appId: String) {
        print("Request was blocked. URL = \(String(describing: url))")
    }
    
    func humanDidChallengeSolved(forAppId appId: String) {
        print("Challenge was solved")
    }
    
    func humanDidChallengeCancelled(forAppId appId: String) {
        print("Challenge wa cancelled")
    }
    
    func humanHeadersWereUpdated(headers: [String : String], forAppId appId: String) {
        print("Headers were updated")
    }
}
