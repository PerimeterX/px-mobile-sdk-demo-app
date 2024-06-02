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
const {HumanModule} = NativeModules;

/* Create new native event emitter */
const humanEventEmitter = new NativeEventEmitter(HumanModule);

var humanHeaders = null;

/* Add new headers listener */
const onAddNewHeaders = headers => {
  const obj = JSON.parse(headers);
  console.log(`[HUMAN] got new headers from event: ${JSON.stringify(obj)}`);
  humanHeaders = obj;
};

/* challenge solved listener */
const onChallengeResult = result => {
  if (result === 'solved') {
    console.log('[HUMAN] got challenge solved event');
  } else if (result === 'cancelled') {
    console.log('[HUMAN] got challenge cancelled event');
  }
};

const subscriptionHumanNewHeaders = humanEventEmitter.addListener(
  'HumanNewHeaders',
  onAddNewHeaders,
);

const subscriptionHumanChallengeResult = humanEventEmitter.addListener(
  'HumanChallengeResult',
  onChallengeResult,
);

const App: () => Node = () => {
  const isDarkMode = useColorScheme() === 'dark';

  const backgroundStyle = {
    backgroundColor: isDarkMode ? Colors.darker : Colors.lighter,
  };

  const sendRequest = async () => {
    /* Get HTTP headers */
    if (humanHeaders == null) {
      const headers = await HumanModule.getHTTPHeaders();
      const obj = JSON.parse(headers);
      console.log(`[HUMAN] got headers from getter: ${JSON.stringify(obj)}`);
      await sentRequest(obj);
    } else {
      await sentRequest(humanHeaders);
    }
  };

  async function sentRequest(headers) {
    console.log(
      `[HUMAN] sending request with headers: ${JSON.stringify(headers)}`,
    );
    try {
      const url = 'https://sample-ios.pxchk.net/login';
      const response = await fetch(url, {
        method: 'GET',
        headers: headers,
      });

      const json = await response.json();

      console.log('[HUMAN] sending response to native module');
      /* Send the response to the SDK */
      const result = await HumanModule.handleResponse(
        JSON.stringify(json),
        response.status,
        url,
      );
      /*
        check the result:
        'false' - not handled by the SDK
        'solved' - challenge solved
        'cancelled' - challenge cancelled
      */
      console.log(`[HUMAN] result: ${result}`);
      if (result === 'solved') {
        console.log('[HUMAN] challenge solved');
        wait(5000); // not required. just for simulation.
        await sendRequest();
      } else if (result === 'false') {
        console.log('[HUMAN] request finished successfully');
        Alert.alert('request finished successfully');
      } else if (result === 'cancelled') {
        console.log('[HUMAN] challenge cancelled');
      }
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
