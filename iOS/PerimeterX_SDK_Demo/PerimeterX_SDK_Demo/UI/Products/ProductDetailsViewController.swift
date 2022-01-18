//
//  ProductDetailsViewController.swift
//  PerimeterX_SDK_Demo
//
//  Created by PerimeterX.
//

import Foundation
import UIKit
import SafariServices

class ProductDetailsViewController: UIViewController {
    
    // MARK: - properties
    
    var product: Product!
    @IBOutlet private weak var productTitleLabel: UILabel!
    @IBOutlet private weak var productPriceLabel: UILabel!
    @IBOutlet private weak var iconImageView: UIImageView!
    
    // MARK: - UIViewController
    
    override func viewDidLoad() {
        super.viewDidLoad()
        updateUI()
    }
    
    // MARK: - IBAction
    
    @IBAction private func buttonAddToCartPressed(sender: UIButton) {
        let safariViewController = SFSafariViewController(url: URL(string: "https://www.perimeterx.com")!)
        present(safariViewController, animated: true, completion: nil)
    }
    
    // MARK: - UI
    
    private func updateUI() {
        navigationItem.title = product.title
        productTitleLabel.text = product.title
        productPriceLabel.text = product.price
        
        APIDataManager.shared.loadImage(url: product.imageUrl) { image in
            OperationQueue.main.addOperation { [weak self] in
                self?.iconImageView.image = image
            }
        }
    }
}
