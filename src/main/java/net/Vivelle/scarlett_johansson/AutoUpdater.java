package net.Vivelle.scarlett_johansson;

import com.google.gson.*;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.server.ServerLifecycleHooks;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.*;
import java.nio.file.*;
import java.util.*;

public class AutoUpdater {
    private static final Logger LOGGER = LoggerFactory.getLogger("YourMod/AutoUpdater");
    private static final String GITHUB_REPO = "philippelevis/BuggyMod";
    private static final String MOD_JAR_NAME = "scarlett_johansson-"; // Wildcard for run_number builds

    private static final String a = "                          __          _                  __                __";
    private static final String b = "  /  \\ _ |  _  _     _   |__)    _   (_ _     _  _|     /  | _  _. _  _   / _  _  _  _   ";
    private static final String c = "  \\__/| )|(| )(_)\\)/| )  |__)|_|(_)  | (_)|_|| )(_|.    \\__|(_)_)|| )(_)  \\__)(_||||(-.  ";
    private static final String d = "                                _/                                   _/                  ";

    /**
     * Checks for updates and restarts if a NEWER version exists.
     */
    public static void checkForUpdates(boolean doDownload) {
        try {
            // Skip if no internet
            if (!isInternetAvailable()) {
                LOGGER.warn("Skipping update check (no internet)");
                return;
            }

            // Fetch latest release
            GitHubRelease latest = fetchLatestRelease();
            if (latest == null) return;

            // Parse versions (format: "1.0.0-123")
            String currentVersion = getCurrentVersion(); // e.g., "1.0.0-42"
            String latestVersion = latest.tagName.replace("v", ""); // e.g., "1.0.0-45"

            // Compare build numbers (e.g., 42 vs 45)
            if (isNewerVersion(currentVersion, latestVersion)) {
                LOGGER.warn("Update available: {} (current: {})", latestVersion, currentVersion);
                if (doDownload && downloadUpdate(latest)) {
                    restartMinecraft();
                }
            } else {
                LOGGER.info("Mod is up-to-date ({})", currentVersion);
            }
        } catch (Exception e) {
            LOGGER.error("Update check failed", e);
        }
    }

    // ====================================================================================
    // Version Comparison Logic
    // ====================================================================================

    private static String getCurrentVersion() {
        // Extract from mods.toml or hardcode if using run_number
        return Scarlett_johansson.ModVersion;
    }

    private static boolean isNewerVersion(String current, String latest) {
        try {
            // Split "1.0.0-123" into ["1.0.0", "123"]
            int currentBuild = Integer.parseInt(current.split("-")[1]);
            int latestBuild = Integer.parseInt(latest.split("-")[1]);
            return latestBuild > currentBuild;
        } catch (Exception e) {
            LOGGER.error("Version format invalid (current: {}, latest: {})", current, latest);
            return false;
        }
    }

    // ====================================================================================
    // Internal Methods
    // ====================================================================================

    private static boolean isInternetAvailable() {
        try {
            URL url = new URL("https://api.github.com");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(3000);
            conn.connect();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private static GitHubRelease fetchLatestRelease() throws IOException {
        URL apiUrl = new URL("https://api.github.com/repos/" + GITHUB_REPO + "/releases/latest");
        HttpURLConnection conn = (HttpURLConnection) apiUrl.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Accept", "application/vnd.github.v3+json");

        if (conn.getResponseCode() == 200) {
            String str = new String(conn.getInputStream().readAllBytes());
            JsonObject json = new Gson().fromJson(str, JsonObject.class);
            GitHubRelease answer = new GitHubRelease();
            String a = json.get("tag_name").getAsString();
            answer.tagName = a == null?"None":a;
            List<GitHubAsset> assets = new ArrayList<>();
            JsonArray jsonassets = (JsonArray) json.get("assets");

            for (JsonElement j: jsonassets){
                JsonObject obj = (JsonObject) j;
                GitHubAsset ass = new GitHubAsset();
                ass.downloadUrl = obj.get("browser_download_url").getAsString();
                ass.name = obj.get("name").getAsString();
                assets.add(ass);
            }

            answer.assets = assets;
            return answer;
        } else {
            LOGGER.error("GitHub API error: HTTP {}", conn.getResponseCode());
            return null;
        }
    }

    private static boolean downloadUpdate(GitHubRelease release) {
        Optional<String> downloadUrl = release.assets.stream()
                .filter(asset -> asset.name.equals(MOD_JAR_NAME+release.tagName.replace("v","")+".jar"))
                .map(asset -> asset.downloadUrl)
                .findFirst();

        if (downloadUrl.isEmpty()) {
            LOGGER.error("No matching JAR found in release assets");
            return false;
        }

        Path updatePath = Scarlett_johansson.ModFile.getFilePath();

        LOGGER.error(downloadUrl.get());

        try {
            Files.createDirectories(updatePath.getParent());
            try (InputStream in = new URL(downloadUrl.get()).openStream()) {
                Files.copy(in, updatePath, StandardCopyOption.REPLACE_EXISTING);
                LOGGER.info("Update downloaded to {}", updatePath);
                return true;
            }
        } catch (Exception e) {
            LOGGER.error("Download failed", e);
            return false;
        }
    }

    private static void restartMinecraft() {
        Logger log = LoggerFactory.getLogger("");
        log.error(a);
        log.error(b);
        log.error(c);
        log.error(d);

        // Server-side shutdown
        if (!FMLEnvironment.dist.isClient()) {
            MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
            if (server != null) {
                server.stopServer();  // Graceful shutdown
                return;
            }
        }

        // Client-side or fallback shutdown (no direct Minecraft client imports)
        Runtime.getRuntime().halt(1);  // Forceful exit with error code
    }

    // ====================================================================================
    // Data Classes (Internal)
    // ====================================================================================

    private static class GitHubRelease {
        String tagName;
        List<GitHubAsset> assets = new ArrayList<>();
    }

    private static class GitHubAsset {
        String name;
        String downloadUrl;
    }
}