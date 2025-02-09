import {
    HSPolicy,
    HumanSecurity,
    HSBotDefenderChallengeResult,
} from "@humansecurity/expo-mobile-sdk";
import axios from "axios";

export class HumanSecurityManager {
    static appId = "PX5730PH91";

    // Start the HumanSecurity SDK
    static start(): void {
        HumanSecurity.startWithAppId(this.appId)
            .then(() => {
                console.log("HumanSecurity SDK started successfully");
            })
            .catch((error: any) => {
                console.error("Error starting HumanSecurity SDK:", error);
            });
    }

    static startHybrid(): void {
        const policy: HSPolicy = {
            hybridAppPolicy: {
                webRootDomains: {
                    PX5730PH91: [".sandysbundtcakes.com"],
                },
            },
            detectionPolicy: {
                allowTouchDetection: true,
                allowDeviceMotionDetection: true,
            },
        };

        HumanSecurity.startWithAppId(this.appId, policy)
            .then(() => {
                console.log("HumanSecurity SDK started successfully with policy");
            })
            .catch((error: any) => {
                console.error("Error starting HumanSecurity SDK:", error);
            });
    }

    static async makeApiCall(url: string): Promise<any> {
        try {
            const headers = HumanSecurity.BD.headersForURLRequest(this.appId);
            // If you are using Account Defender you should register your URL here.
            // HumanSecurity.AD.registerOutgoingUrlRequest(url, this.appId);

            const response = await fetch(url, {
                method: "GET",
                headers,
            });

            const statusCode = response.status;
            if (statusCode !== 200) {
                const responseText = await response.text();
                const canHandle = HumanSecurity.BD.canHandleResponse(
                    responseText,
                    statusCode,
                    url
                );
                if (canHandle) {
                    const result = await HumanSecurity.BD.handleResponse(
                        responseText,
                        statusCode,
                        url
                    );
                    if (result === HSBotDefenderChallengeResult.SOLVED) {
                        console.log("Challenge solved successfully! Retrying request...");
                    } else {
                        console.log("Challenge was cancelled, or failed.");
                    }
                    return { challengeResult: HSBotDefenderChallengeResult[result] };
                } else {
                    console.log("Response cannot be handled by the library.");
                    throw new Error("Unhandled response");
                }
            } else {
                console.log(`API Call Successful with status ${statusCode}`);
                let data;
                const contentType = response.headers.get("content-type") || "";
                if (contentType.includes("application/json")) {
                    data = await response.json();
                } else {
                    // Fallback: if not JSON, assume text
                    data = await response.text();
                }
                return { status: statusCode, data };
            }
        } catch (error) {
            console.error("Error making API call:", error);
            throw error;
        }
    }

    static async makeApiCallWithAxios(url: string): Promise<any> {
        try {
            const headers = HumanSecurity.BD.headersForURLRequest(this.appId);
            const response = await axios.get(url, { headers });

            console.log(`API Call Successful with response ${response.status}`);
            return { status: response.status, data: response.data };
        } catch (error: any) {
            console.log("Error making API call:", error);

            const statusCode = error.response?.status || 0;
            const responseText = error.response?.data || "No response data";

            console.log(`API Call failed with status: ${statusCode}`);

            const canHandle = HumanSecurity.BD.canHandleResponse(
                JSON.stringify(responseText),
                statusCode,
                url
            );
            if (canHandle) {
                const result = await HumanSecurity.BD.handleResponse(
                    JSON.stringify(responseText),
                    statusCode,
                    url
                );
                console.log(`Challenge result: ${HSBotDefenderChallengeResult[result]}`);
                return { challengeResult: HSBotDefenderChallengeResult[result] };
            } else {
                console.log("Response cannot be handled by the library.");
                throw error;
            }
        }
    }

    static setUserId(userId: string): void {
        try {
            HumanSecurity.AD.setUserId(userId, this.appId);
            console.log("User ID Set Successfully");
        } catch (error) {
            console.error("Failed to set user ID:", error);
        }
    }

    static logoutUser(): void {
        try {
            HumanSecurity.AD.setUserId(null, this.appId);
            console.log("User ID Cleared Successfully");
        } catch (error) {
            console.error("Failed to clear user ID:", error);
        }
    }

    static setCustomParams(appId: string, parameters: Record<string, string>): void {
        try {
            HumanSecurity.BD.setCustomParameters(parameters, appId);
            console.log("Custom Parameters Set Successfully");
        } catch (error) {
            console.error("Failed to set custom parameters:", error);
        }
    }

    static setAdditionalData(appId: string, parameters: Record<string, string>): void {
        try {
            HumanSecurity.AD.setAdditionalData(parameters, appId);
            console.log("Additional Data Set Successfully");
        } catch (error) {
            console.error("Failed to set additional data:", error);
        }
    }
}