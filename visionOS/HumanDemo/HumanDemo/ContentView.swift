//
//  ContentView.swift
//  HumanDemo
//
//  Created by Oren Yaar on 20/05/2024.
//

import SwiftUI
import HUMAN

struct ContentView: View {
    let model = ViewModel()
    
    var body: some View {
        VStack {
            HSChallengeView(uuid: model.challengeUUID)
            Image("HUMAN_logo")
                .padding(EdgeInsets(top: 0, leading: 0, bottom: 70, trailing: 0))
            Text("DEMO")
                .font(.custom("HelveticaNeue-Bold", size: 38))
                .padding(EdgeInsets(top: 0, leading: 0, bottom: 80, trailing: 0))
            if model.isLoggedIn {
                Text("Welcome back John Doe!")
                    .font(.custom("HelveticaNeue-Medium", size: 30))
            }
            else {
                Button {
                    model.performLogin()
                } label: {
                    Text("Login")
                        .font(.custom("HelveticaNeue-Medium", size: 21))
                        .padding(.horizontal, 60)
                        .padding(.vertical, 15)
                }
            }
        }
        .padding()
        .onDisappear() {
            HSChallengeModelViewHelper.unregister(uuid: model.challengeUUID)
        }
    }
}

#Preview(windowStyle: .automatic) {
    ContentView()
}
