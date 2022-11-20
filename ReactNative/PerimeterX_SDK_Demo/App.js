/**
 * Sample React Native App
 * https://github.com/facebook/react-native
 *
 * @format
 * @flow strict-local
 */

import React from 'react';
import type {Node} from 'react';

import {
  SafeAreaView,
  ScrollView,
  StatusBar,
  StyleSheet,
  Text,
  useColorScheme,
  View,
  Button,
  Alert,
} from 'react-native';

import {
  Colors,
  DebugInstructions,
  Header,
  LearnMoreLinks,
  ReloadInstructions,
} from 'react-native/Libraries/NewAppScreen';

/* Import PerimeterXModule */
import {NativeModules, NativeEventEmitter} from 'react-native';
import type {Message} from 'react-native/Libraries/LogBox/Data/parseLogBoxLog';
const {PerimeterXModule} = NativeModules;

/* Create new native event emitter */
const pxEventEmitter = new NativeEventEmitter(PerimeterXModule);

/* challenge solved listener */
const onChallengeResult = result => {
  if (result === 'solved') {
    console.log('[PX] got challenge solved event');
  } else if (result === 'cancelled') {
    console.log('[PX] got challenge cancelled event');
  }
};

const subscriptionPxChallengeResult = pxEventEmitter.addListener(
  'PxChallengeResult',
  onChallengeResult,
);

const App: () => Node = () => {
  const isDarkMode = useColorScheme() === 'dark';

  const backgroundStyle = {
    backgroundColor: isDarkMode ? Colors.darker : Colors.lighter,
  };

  const sendRequest = async () => {
    /* Get HTTP headers */
    PerimeterXModule.getHTTPHeaders(async headers => {
      const obj = JSON.parse(headers);
      console.log(`[PX] got px headers from getter: ${JSON.stringify(obj)}`);
      sentRequest(obj);
    });
  };

  async function sentRequest(headers) {
    console.log(
      `[PX] sending request with headers: ${JSON.stringify(headers)}`,
    );
    try {
      const url = 'https://sample-ios.pxchk.net/login';
      const response = await fetch(url, {
        method: 'GET',
        headers: headers,
      });

      const json = await response.json();

      console.log('[PX] sending response to native module');
      /* Send the response to the SDK */
      PerimeterXModule.handleResponse(
        JSON.stringify(json),
        response.status,
        url,
        async result => {
          /*
          check the result:
            'true' - handled by the SDK
            'false' - not handled by the SDK
          */
          console.log(`[PX] result: ${result}`);
          if (result === 'true') {
            console.log('[PX] request blcoked');
          } else if (result === 'false') {
            console.log('[PX] request finished successfully');
            Alert.alert('request finished successfully');
          }
        },
      );
    } catch (error) {
      console.error(error);
    }
  }

  return (
    <SafeAreaView style={backgroundStyle}>
      <Button
        onPress={() => {
          sendRequest();
        }}
        title="Login"
      />
    </SafeAreaView>
  );
};

export default App;
