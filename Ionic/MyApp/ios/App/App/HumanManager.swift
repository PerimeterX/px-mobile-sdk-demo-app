import Foundation
import Capacitor
import PerimeterX_SDK

@objc(HumanManager)
class HumanManager: CAPPlugin {
    
    let strSolved = "solved"
    let strCancelled = "cancelled"
    let strFalse = "false"
    let strValue = "value"
    
    static let shared = HumanManager()
    
    func start() {
        do {
            let policy = HSPolicy()
            policy.automaticInterceptorPolicy.interceptorType = .none
            policy.doctorAppPolicy.enabled = true
            try HumanSecurity.start(appId: "PXj9y4Q8Em", delegate: nil, policy: policy)
        }
        catch {
            print("error: \(error)")
        }
    }
    
    @objc func getHttpHeaders(_ call: CAPPluginCall) {
        call.resolve(HumanSecurity.headersForURLRequest()!)
    }
    
    @objc func handleResponse(_ call: CAPPluginCall) {
        let response = call.getString(strValue) ?? ""
        let data = response.data(using: .utf8)
        let httpURLResponse = HTTPURLResponse(url: URL(string: "https://example.com")!, statusCode: 403, httpVersion: nil, headerFields: nil)
        let handled = HumanSecurity.handleResponse(response: httpURLResponse!, data: data!) { challengeResult in
            call.resolve([self.strValue: challengeResult == .solved ? self.strSolved : self.strCancelled])
        }
        if !handled {
            call.resolve([strValue: strFalse])
        }
    }
}
