//
//  ProductsViewController.swift
//  PerimeterX_SDK_Demo
//
//  Created by PerimeterX.
//

import Foundation
import UIKit

protocol ProductsViewControllerDelegate: NSObjectProtocol {
    func productsViewControllerDidLogout()
}

class ProductsViewController: UIViewController, UITableViewDataSource, UITableViewDelegate {
    
    // MARK: - UIViewController
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        loadProdcuts()
    }
    
    // MARK: - UITableViewDataSource
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return products.count
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let product = products[indexPath.row]
        let cell = tableView.dequeueReusableCell(withIdentifier: "ProductCell", for: indexPath) as! ProductCell
        cell.updateCell(title: product.title, price: product.price)
        cell.updateIcon(image: nil)
        
        APIDataManager.shared.loadImage(url: products[indexPath.row].imageUrl) { image in
            OperationQueue.main.addOperation {
                if let cell = tableView.cellForRow(at: indexPath) as? ProductCell, let image = image {
                    cell.updateIcon(image: image)
                }
            }
        }
        
        return cell
    }
    
    // MARK: - UITableViewDelegate
    
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        tableView.deselectRow(at: indexPath, animated: true)
        
        let product = products[indexPath.row]
        let productDetailsViewController = storyboard?.instantiateViewController(withIdentifier: "ProductDetailsViewController") as! ProductDetailsViewController
        productDetailsViewController.product = product
        navigationController?.pushViewController(productDetailsViewController, animated: true)
    }
    
    // MARK: - IBAction
    
    @IBAction private func buttonRefreshPressed(sender: UIBarButtonItem) {
        loadProdcuts()
    }
    
    @IBAction private func buttonLogoutPressed(sender: UIBarButtonItem) {
        delegate?.productsViewControllerDidLogout()
    }
    
    // MARK: - properties
    
    weak var delegate: ProductsViewControllerDelegate?
    private var products = [Product]()
    @IBOutlet private weak var productsTableView: UITableView!
    
    // MARK: - products
    
    private func loadProdcuts() {
        APIDataManager.shared.sendProductsRequest {  products in
            OperationQueue.main.addOperation { [weak self] in
                if let products = products {
                    self?.products = products
                    self?.productsTableView.reloadData()
                }
            }
        }
    }
}
