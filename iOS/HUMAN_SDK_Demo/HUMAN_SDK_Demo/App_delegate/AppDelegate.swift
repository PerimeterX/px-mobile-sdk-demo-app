//
//  AppDelegate.swift
//  HUMAN_SDK_Demo
//
//  Created by HUMAN.
//

import UIKit

@main
class AppDelegate: UIResponder, UIApplicationDelegate {
    
    // MARK: - UIApplicationDelegate
    
    func application(_ application: UIApplication, didFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey: Any]?) -> Bool {
        
        // Start HUMAN SDK here //
        HumanManager.shared.start()
        
        if #available(iOS 13, *) {}
        else {
            self.window = UIWindow()
            self.window!.rootViewController = UIStoryboard(name: "Main", bundle: nil).instantiateInitialViewController()
            self.window!.makeKeyAndVisible()
        }
        
        return true
    }

    // MARK: UISceneSession Lifecycle

    @available(iOS 13.0, *)
    func application(_ application: UIApplication, configurationForConnecting connectingSceneSession: UISceneSession, options: UIScene.ConnectionOptions) -> UISceneConfiguration {
        return UISceneConfiguration(name: "Default Configuration", sessionRole: connectingSceneSession.role)
    }

    @available(iOS 13.0, *)
    func application(_ application: UIApplication, didDiscardSceneSessions sceneSessions: Set<UISceneSession>) {
        
    }
    
    // MARK: - properties
    
    var window: UIWindow?
}
