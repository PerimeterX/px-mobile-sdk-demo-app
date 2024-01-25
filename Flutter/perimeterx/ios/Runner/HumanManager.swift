import Foundation
import Flutter
import PerimeterX_SDK

class HumanManager {
    
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
    
    func setupChannel(with controller: FlutterViewController) {
        let humanChannel = FlutterMethodChannel(name: "com.humansecurity/sdk", binaryMessenger: controller.binaryMessenger)
        humanChannel.setMethodCallHandler({(call: FlutterMethodCall, result: @escaping FlutterResult) -> Void in
            if call.method == "humanGetHeaders" {
                var json: String?
                do {
                    let headers = HumanSecurity.headersForURLRequest()
                    let data = try JSONSerialization.data(withJSONObject: headers)
                    json = String(data: data, encoding: .utf8)
                }
                catch {
                    print("error: \(error)")
                }
                result(json)
            }
            else if call.method == "humanHandleResponse" {
                if let response = call.arguments as? String, let data = response.data(using: .utf8), let httpURLResponse = HTTPURLResponse(url: URL(string: "https://example.com")!, statusCode: 403, httpVersion: nil, headerFields: nil) {
                    let handled = HumanSecurity.handleResponse(response: httpURLResponse, data: data) { challengeResult in
                        result(challengeResult == .solved ? "solved" : "cancelled")
                    }
                    if handled {
                        return
                    }
                }
                result("false")
            }
            else {
                result("")
            }
        })
    }
}
