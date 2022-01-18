//
//  Product.swift
//  PerimeterX_SDK_Demo
//
//  Created by PerimeterX.
//

import Foundation

struct Product: Codable {
    
    // MARK: - Codable
    
    enum CodingKeys: String, CodingKey {
        case productId = "id"
        case title = "title"
        case price = "price"
        case imageUrl = "imageUrl"
    }
    
    // MARK: properties
    
    let productId: String
    let title: String
    let price: String
    let imageUrl: String
}
