import Foundation
import Flutter
import PerimeterX_SDK

class PerimeterxManager {
    
    static let shared = PerimeterxManager()
    
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
    
    func setupChannel(with controller: FlutterViewController) {
        let perimeterxChannel = FlutterMethodChannel(name: "com.perimeterx.demo/perimeterx", binaryMessenger: controller.binaryMessenger)
        perimeterxChannel.setMethodCallHandler({(call: FlutterMethodCall, result: @escaping FlutterResult) -> Void in
            if call.method == "_getPxHeaders" {
                var json: String?
                do {
                    if let headers = PerimeterX.headersForURLRequest() {
                        let data = try JSONSerialization.data(withJSONObject: headers)
                        json = String(data: data, encoding: .utf8)
                    }
                }
                catch {
                    print("error: \(error)")
                }
                result(json)
            }
            else if call.method == "_handlePxResponse" {
                if let response = call.arguments as? String, let data = response.data(using: .utf8), let httpURLResponse = HTTPURLResponse(url: URL(string: "https://fake.url.com")!, statusCode: 403, httpVersion: nil, headerFields: nil) {
                    let handled = PerimeterX.handleResponse(response: httpURLResponse, data: data) { challengeResult in
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
