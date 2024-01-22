import { IonContent, IonHeader, IonPage, IonTitle, IonToolbar } from '@ionic/react';
import * as React from 'react';
import ExploreContainer from '../components/ExploreContainer';
import './Tab1.css';
import { registerPlugin } from '@capacitor/core';


const Tab1: React.FC = () => {
  return (
    <IonPage>
      <IonHeader>
        <IonToolbar>
          <IonTitle>Tab 1</IonTitle>
        </IonToolbar>
      </IonHeader>
      <IonContent fullscreen>
        <IonHeader collapse="condense">
          <IonToolbar>
            <IonTitle size="large">Tab 1</IonTitle>
          </IonToolbar>
        </IonHeader>
        <ExploreContainer name="Tab 1 page" />
        <div>
          <button type="button" onClick={sendUrlRequest}>
            Click Me
          </button>
        </div>
      </IonContent>
    </IonPage>
  );
};

export interface HumanPlugin {
  getHttpHeaders():Promise<{ value: string }>;
  handleResponse(value : any):Promise<{ value: string }>;
}

const Human = registerPlugin<HumanPlugin>('Human');

const sendUrlRequest = async () => {
  const result = await Human.getHttpHeaders()
  const json = JSON.parse(JSON.stringify(result))
  const map = new Map(Object.entries(json))

  var xhr = new XMLHttpRequest()
  xhr.addEventListener('load', async () => {
    const challengeResult = await Human.handleResponse({ "value": xhr.responseText })
    const challengeResultJson = JSON.parse(JSON.stringify(challengeResult))
    const challengeResultMap = new Map(Object.entries(challengeResultJson))
    console.log("challenge result = ", challengeResultMap.get("value"))
  })
  xhr.open('GET', 'https://sample-ios.pxchk.net/login')
  for (const [key, value] of map) {
    xhr.setRequestHeader(key, value as string)
  }
  xhr.send()
};

export default Tab1;
