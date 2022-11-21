//
//  APIDataManager.swift
//  PerimeterX_SDK_Demo
//
//  Created by PerimeterX.
//

import Foundation
import PerimeterX_SDK
import UIKit

class APIDataManager {
    
    // MARK: - singleton
    
    static let shared = APIDataManager()
    
    // MARK: - properties
    
    private let serverURL = "https://sample-ios.pxchk.net/"
    private var loginURL: URL {
        get {
            return URL(string: "\(serverURL)login")!
        }
    }
    private var productsURL: URL {
        get {
            return URL(string: "\(serverURL)products")!
        }
    }
    
    // MARK: - URL requests
    
    func sendLoginRequest(email: String, password: String, completion: @escaping (Bool) -> ()) {
        let request = requestForURL(url: loginURL)
        let dataTask = URLSession.shared.dataTask(with: request) { data, response, error in
            self.handleResponse(data: data, response: response, error: error)
            
            var result = false
            if let response = response as? HTTPURLResponse {
                result = response.statusCode == 200
            }
            completion(result)
        }
        dataTask.resume()
    }
    
    func sendProductsRequest(completion: @escaping ([Product]?) -> ()) {
        let request = requestForURL(url: productsURL)
        let dataTask = URLSession.shared.dataTask(with: request) { data, response, error in
            self.handleResponse(data: data, response: response, error: error)
            
            var products: [Product]?
            if let response = response as? HTTPURLResponse, response.statusCode == 200 {
                if let data = data {
                    let decoder = JSONDecoder()
                    do {
                        products = try decoder.decode([Product].self, from: data)
                    }
                    catch {
                        print("error: \(error)")
                    }
                }
            }
            completion(products)
        }
        dataTask.resume()
    }
    
    func loadImage(url: String, completion: @escaping (UIImage?) -> ()) {
        let request = requestForURL(url: URL(string: url)!)
        let dataTask = URLSession.shared.dataTask(with: request) { data, response, error in
            self.handleResponse(data: data, response: response, error: error)
            
            var image: UIImage?
            if let data = data {
                image = UIImage(data: data)
            }
            completion(image)
        }
        dataTask.resume()
    }
    
    private func requestForURL(url: URL) -> URLRequest {
        var request = URLRequest(url: url)
        request.httpMethod = "GET"
        
        // When PXPolicy.urlRequestInterceptionType is set to `PXPolicyUrlRequestInterceptionType/none` => get HTTP headers from PerimeterX and add them to your request //
        if PerimeterxManager.shared.urlRequestInterceptionType == .none {
            let headers = PerimeterX.headersForURLRequest()
            request.allHTTPHeaderFields = headers
        }
        
        return request
    }
    
    private func handleResponse(data: Data?, response: URLResponse?, error: Error?) {
        if let error = error {
            // When PXPolicy.urlRequestInterceptionType is set to any value rather than `PXPolicyUrlRequestInterceptionType/none`  => check that the error is "Request blocked by PerimeterX" //
            if PerimeterxManager.shared.urlRequestInterceptionType != .none {
                let isRequestBlockedError = PerimeterX.isRequestBlockedError(error: error)
                if isRequestBlockedError {
                    print("request was blocked by PX")
                }
            }
        }
        
        if let data = data, let response = response as? HTTPURLResponse {
            // When PXPolicy.urlRequestInterceptionType is set to `PXPolicyUrlRequestInterceptionType/none` => pass the data and response to PerimeterX to handle it //
            if PerimeterxManager.shared.urlRequestInterceptionType == .none {
                let isHandledByPX = PerimeterX.handleResponse(response: response, data: data) { result in
                    print("challenge result = \(result)")
                }
                if isHandledByPX {
                    print("block response was handled by PX")
                }
            }
        }
    }
}
