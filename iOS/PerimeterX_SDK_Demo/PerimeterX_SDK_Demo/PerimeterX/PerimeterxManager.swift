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
    
    func perimeterxDidRequestBlocked(forAppId appId: String) {
        print("PerimeterX Request Blocked")
    }
    
    func perimeterxDidChallengeSolved(forAppId appId: String) {
        print("PerimeterX Challenge Solved")
    }
    
    func perimeterxDidChallengeCancelled(forAppId appId: String) {
        print("PerimeterX Challenge Cancelled")
    }
    
    // MARK: - singleton
    
    static let shared = PerimeterxManager()
    
    // MARK: - properties
    
    private let pxAppId = "PXj9y4Q8Em"
    
    // MARK: - PerimeterX
    
    func start() {
        print("SDK version: \(PerimeterX.sdkVersion())")
        
        start(appId: pxAppId)
        setPolicy()
        setCustomParameters()
    }
    
    private func start(appId: String) {
        // Start PerimeterX SDK with your AppID //
        PerimeterX.start(appId: appId, delegate: self, enableDoctorCheck: true) { success, error in
            if success {
                if let vid = PerimeterX.vid(forAppId: nil) {
                    print("vid: \(vid)")
                }
            }
            else {
                if let error = error {
                    print("error: \(error)")
                }
                
                DispatchQueue.main.asyncAfter(deadline: .now() + 1) {
                    // Make sure to start PerimeterX SDK again when it fails (network issue, etc.) //
                    self.start(appId: appId)
                }
            }
        }
    }
    
    private func setPolicy() {
        let policy = PXPolicy()
        policy.requestsInterceptedAutomaticallyEnabled = true
        PerimeterX.setPolicy(policy: policy, completion: nil)
    }
    
    private func setCustomParameters() {
        var customParameters = [String: String]()
        customParameters["custom_param1"] = "hello"
        customParameters["custom_param2"] = "world"
        PerimeterX.setCustomParameters(parameters: customParameters, completion: nil)
    }
}
