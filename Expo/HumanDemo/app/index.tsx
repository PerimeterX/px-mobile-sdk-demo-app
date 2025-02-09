import MainScreen from "./MainScreen";
import { HumanSecurityManager } from "./HumanSecurityManager";

HumanSecurityManager.start();
// HumanSecurityManager.startHybrid();

export default function Index() {
    return <MainScreen />;
}
