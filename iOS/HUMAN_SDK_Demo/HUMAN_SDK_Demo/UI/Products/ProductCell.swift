//
//  ProductCell.swift
//  HUMAN_SDK_Demo
//
//  Created by HUMAN.
//

import Foundation
import UIKit

class ProductCell: UITableViewCell {
    
    // MARK: - properties
    
    @IBOutlet private weak var productTitleLabel: UILabel!
    @IBOutlet private weak var productPriceLabel: UILabel!
    @IBOutlet private weak var iconImageView: UIImageView!
    
    // MARK: - UI
    
    func updateCell(title: String, price: String) {
        productTitleLabel.text = title
        productPriceLabel.text = price
    }
    
    func updateIcon(image: UIImage?) {
        iconImageView.image = image
    }
}
