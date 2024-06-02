//
//  HumanManagerPlugin.swift
//  App
//
//  Created by Oren Yaar on 02/08/2023.
//

import Foundation
import Capacitor
import HUMAN

@objc(HumanManagerPlugin)
class HumanManagerPlugin: CAPPlugin {
    
    static let shared = HumanManagerPlugin()
    
    func start() {
        do {
            let policy = HSPolicy()
            policy.automaticInterceptorPolicy.interceptorType = .none
            policy.doctorAppPolicy.enabled = true
            try HumanSecurity.start(appId: "PXj9y4Q8Em", policy: policy)
        }
        catch {
            print("error: \(error)")
        }
    }
    
    @objc func getHttpHeaders(_ call: CAPPluginCall) {
        call.resolve(HumanSecurity.BD.headersForURLRequest(forAppId: nil))
    }
    
    @objc func handleResponse(_ call: CAPPluginCall) {
        let response = call.getString("value") ?? ""
        let data = response.data(using: .utf8)
        let httpURLResponse = HTTPURLResponse(url: URL(string: "https://fake.url.com")!, statusCode: 403, httpVersion: nil, headerFields: nil)
        let handled = HumanSecurity.BD.handleResponse(response: httpURLResponse!, data: data!) { challengeResult in
            call.resolve(["value": challengeResult == .solved ? "solved" : "cancelled"])
        }
        if !handled {
            call.resolve(["value": "false"])
        }
    }
}
