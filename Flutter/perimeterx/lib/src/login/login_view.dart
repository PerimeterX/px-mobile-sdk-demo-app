import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import '../settings/settings_view.dart';
import 'dart:async';
import 'dart:convert';
import 'package:http/http.dart' as http;

class LoginView extends StatelessWidget {
  
  static const routeName = '/';
  static const perimeterxPlatform = MethodChannel('com.perimeterx.demo/perimeterx');

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('PerimeterX Demo App'),
        actions: [
          IconButton(
            icon: const Icon(Icons.settings),
            onPressed: () {
              Navigator.restorablePushNamed(context, SettingsView.routeName);
            },
          ),
        ],
      ),
      body: ElevatedButton(
            onPressed: () {
              _sendUrlRequest();
            },
            child: Text('Login'),
          ),
    );
  }

  Future<void> _sendUrlRequest() async {
    try {
      final String pxHeaders = await perimeterxPlatform.invokeMethod('_getPxHeaders');
      final Map<String, String> pxHeadersMap = Map.from(json.decode(pxHeaders));
      print(pxHeadersMap);
      var url = Uri.https('sample-ios.pxchk.net', '/login');
      var response = await http.get(url, headers: pxHeadersMap);
      if (response.statusCode == 403) {
        final String result = await perimeterxPlatform.invokeMethod('_handlePxResponse', response.body);
        print(result);
        /*
          check the result:
          'false' - not handled by the SDK
          'solved' - challenge solved
          'cancelled' - challenge cancelled
        */
        if (result == 'solved') {
          // challenge solved. you may retry the request here
          print("challenge solved");
        } else if (result == 'false') {
          // request finished successfully
          print("request finished successfully");
        } else if (result == 'cancelled') {
          // challenge cancelled
          print("challenge cancelled");
        }
      }
    } on PlatformException catch (e) {
      print("failed to get px's headers: '${e.message}'.");
    }
  }
}