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

var pxHeader = null;

/* Add new headers listener */
const onAddNewHeaders = headers => {
  const obj = JSON.parse(headers);
  console.log(`[PX] got new px headers from event: ${JSON.stringify(obj)}`);
  pxHeader = obj;
};

/* challenge solved listener */
const onChallengeResult = result => {
  if (result === 'solved') {
    console.log('[PX] got challenge solved event');
  } else if (result === 'cancelled') {
    console.log('[PX] got challenge cancelled event');
  }
};

const subscriptionPxNewHeaders = pxEventEmitter.addListener(
  'PxNewHeaders',
  onAddNewHeaders,
);

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
    if (pxHeader == null) {
      PerimeterXModule.getHTTPHeaders(async headers => {
        const obj = JSON.parse(headers);
        console.log(`[PX] got px headers from getter: ${JSON.stringify(obj)}`);
        sentRequest(obj);
      });
    } else {
      sentRequest(pxHeader);
    }
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
            'false' - not handled by the SDK
            'solved' - challenge solved
            'cancelled' - challenge cancelled
          */
          console.log(`[PX] result: ${result}`);
          if (result === 'solved') {
            console.log('[PX] challenge solved');
            wait(5000); // not required. just for simulation.
            sendRequest();
          } else if (result === 'false') {
            console.log('[PX] request finished successfully');
            Alert.alert('request finished successfully');
          } else if (result === 'cancelled') {
            console.log('[PX] challenge cancelled');
          }
        },
      );
    } catch (error) {
      console.error(error);
    }
  }

  function wait(ms) {
    const start = new Date().getTime();
    let end = start;
    while (end < start + ms) {
      end = new Date().getTime();
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
