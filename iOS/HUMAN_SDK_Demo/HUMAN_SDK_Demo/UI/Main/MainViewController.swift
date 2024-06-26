//
//  MainViewController.swift
//  HUMAN_SDK_Demo
//
//  Created by HUMAN.
//

import Foundation
import UIKit

class MainViewController: UIViewController, LoginViewControllerDelegate, ProductsViewControllerDelegate {
    
    // MARK: - UIViewController
    
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        super.prepare(for: segue, sender: sender)
        if let navigationController = segue.destination as? UINavigationController {
            mainNavigationController = navigationController
            showLoginScreen()
        }
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
        supportLightInterfaceStyleOnly()
    }
    
    // MARK: - LoginViewControllerDelegate
    
    func loginViewControllerDidLogin() {
        showProductsScreen()
    }
    
    // MARK: - ProductsViewControllerDelegate
    
    func productsViewControllerDidLogout() {
        showLoginScreen()
    }
    
    // MARK: - properties
    
    weak var mainNavigationController: UINavigationController!
    
    // MARK: - UI
    
    private func showLoginScreen() {
        let loginViewController = self.storyboard?.instantiateViewController(withIdentifier: "LoginViewController") as! LoginViewController
        loginViewController.delegate = self
        mainNavigationController.viewControllers = [loginViewController]
    }
    
    private func showProductsScreen() {
        let productsViewController = self.storyboard?.instantiateViewController(withIdentifier: "ProductsViewController") as! ProductsViewController
        productsViewController.delegate = self
        mainNavigationController.viewControllers = [productsViewController]
    }
    
    private func supportLightInterfaceStyleOnly() {
        if #available(iOS 13.0, *) {
            overrideUserInterfaceStyle = .light
        }
    }
}
