//
//  PerimeterxManager.swift
//  App
//
//  Created by Oren Yaar on 02/08/2023.
//

import Foundation
import Capacitor
import PerimeterX_SDK

@objc(PerimeterxManagerPlugin)
class PerimeterxManagerPlugin: CAPPlugin {
    
    static let shared = PerimeterxManagerPlugin()
    
    func start() {
        do {
            let policy = PXPolicy()
            policy.urlRequestInterceptionType = .none
            policy.doctorCheckEnabled = true
            try PerimeterX.start(appId: "PXj9y4Q8Em", delegate: nil, policy: policy)
        }
        catch {
            print("error: \(error)")
        }
    }
    
    @objc func getHttpHeaders(_ call: CAPPluginCall) {
        call.resolve(PerimeterX.headersForURLRequest()!)
    }
    
    @objc func handleResponse(_ call: CAPPluginCall) {
        let response = call.getString("value") ?? ""
        let data = response.data(using: .utf8)
        let httpURLResponse = HTTPURLResponse(url: URL(string: "https://fake.url.com")!, statusCode: 403, httpVersion: nil, headerFields: nil)
        let handled = PerimeterX.handleResponse(response: httpURLResponse!, data: data!) { challengeResult in
            call.resolve(["value": challengeResult == .solved ? "solved" : "cancelled"])
        }
        if !handled {
            call.resolve(["value": "false"])
        }
    }
}
