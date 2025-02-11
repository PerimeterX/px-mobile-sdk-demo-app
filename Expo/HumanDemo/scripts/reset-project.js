#!/usr/bin/env node

/**
 * This script resets the project by:
 * - Moving old directories (e.g., /app, /components, etc.) to /app-example
 * - Creating a fresh /app directory with a minimal index.tsx file
 * You can delete this script if you no longer need it.
 */

const fs = require("fs");
const path = require("path");

const root = process.cwd();
const oldDirs = ["app", "components", "hooks", "constants", "scripts"];
const newDir = "app-example";
const newAppDir = "app";

const indexContent = `import { Text, View } from "react-native";

export default function Index() {
  return (
    <View
      style={{
        flex: 1,
        justifyContent: "center",
        alignItems: "center",
      }}
    >
      <Text>Welcome to your new project!</Text>
    </View>
  );
}
`;

const resetProject = async () => {
  try {
    // Create /app-example directory if needed
    const newDirPath = path.join(root, newDir);
    await fs.promises.mkdir(newDirPath, { recursive: true });
    console.log(`📁 Created /${newDir} directory.`);

    // Move old directories into /app-example
    for (const dir of oldDirs) {
      const oldDirPath = path.join(root, dir);
      const backupDirPath = path.join(newDirPath, dir);
      if (fs.existsSync(oldDirPath)) {
        await fs.promises.rename(oldDirPath, backupDirPath);
        console.log(`➡️ Moved /${dir} to /${newDir}/${dir}.`);
      } else {
        console.log(`❌ /${dir} not found, skipping.`);
      }
    }

    // Create new /app directory
    const newAppDirPath = path.join(root, newAppDir);
    await fs.promises.mkdir(newAppDirPath, { recursive: true });
    console.log("\n📁 Created new /app directory.");

    // Create minimal index.tsx
    const indexPath = path.join(newAppDirPath, "index.tsx");
    await fs.promises.writeFile(indexPath, indexContent);
    console.log("📄 Created app/index.tsx.");

    console.log("\n✅ Reset complete. Next steps:");
    console.log("1. Run `npx expo start` to start your development server.");
    console.log("2. Edit app/index.tsx to customize your app.");
    console.log("3. Delete /app-example if no longer needed.");
  } catch (error) {
    console.error(`❌ Error during reset: ${error}`);
  }
};

resetProject();
