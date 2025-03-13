import React from 'react';
import MainScreen from './src/screens/MainScreen';
import { HumanSecurityManager } from './src/services/HumanSecurityManager';

// Initialize HumanSecurity SDK
HumanSecurityManager.start(); // Or use `HumanSecurityManager.startHybrid();` if needed.

export default function App() {
  return <MainScreen />;
}
