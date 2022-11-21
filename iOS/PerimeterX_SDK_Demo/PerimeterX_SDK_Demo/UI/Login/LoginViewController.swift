//
//  LoginViewController.swift
//  PerimeterX_SDK_Demo
//
//  Created by PerimeterX.
//

import UIKit
import PerimeterX_SDK

protocol LoginViewControllerDelegate: NSObjectProtocol {
    func loginViewControllerDidLogin()
}

class LoginViewController: UIViewController, UITextFieldDelegate {

    // MARK: - UIViewController
    
    override func viewDidLoad() {
        super.viewDidLoad()
        versionLabel.text = "PerimeterX iOS SDK v\(PerimeterX.sdkVersion())"
    }
    
    // MARK: - UITextFieldDelegate
    
    func textFieldShouldReturn(_ textField: UITextField) -> Bool {
        textField.resignFirstResponder()
        return false
    }
    
    // MARK: - IBAction
    
    @IBAction private func buttonLoginPressed(sender: UIButton) {
        sender.isEnabled = false
        activityIndicatorView.startAnimating()
        APIDataManager.shared.sendLoginRequest(email: "email", password: "password") { success in
            OperationQueue.main.addOperation { [weak self] in
                self?.activityIndicatorView.stopAnimating()
                sender.isEnabled = true
                if success {
                    self?.delegate?.loginViewControllerDidLogin()
                }
            }
        }
    }
    
    // MARK: - properties
    
    weak var delegate: LoginViewControllerDelegate?
    @IBOutlet private weak var versionLabel: UILabel!
    @IBOutlet private weak var loginButton: UIButton!
    @IBOutlet private weak var activityIndicatorView: UIActivityIndicatorView!
}

