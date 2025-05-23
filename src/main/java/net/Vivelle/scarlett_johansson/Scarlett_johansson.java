package net.Vivelle.scarlett_johansson;

import com.mojang.logging.LogUtils;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.ModLoadingException;
import net.minecraftforge.fml.ModLoadingStage;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.loading.LoadingModList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.lang.management.ManagementFactory;
import java.lang.reflect.Method;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(Scarlett_johansson.MODID)
public class Scarlett_johansson {
//
//    // Define mod id in a common place for everything to reference
    public static final String MODID = "scarlett_johansson";
    // Directly reference a slf4j logger
    private static final Logger LOGGER = LogUtils.getLogger();
    // Create a Deferred Register to hold Blocks which will all be registered under the "scarlett_johansson" namespace
    private static final boolean CRASH = true;

    private static final boolean ENABLE_UPDATER = true;
    private static final String UPDATE_URL = "https://your-server.com/latest.jar";

    private static final String a = "                          __          _                  __                __";
    private static final String b = "  /  \\ _ |  _  _     _   |__)    _   (_ _     _  _|     /  | _  _. _  _   / _  _  _  _   ";
    private static final String c = "  \\__/| )|(| )(_)\\)/| )  |__)|_|(_)  | (_)|_|| )(_|.    \\__|(_)_)|| )(_)  \\__)(_||||(-.  ";
    private static final String d = "                                _/                                   _/                  ";

    public Scarlett_johansson() {
        if (ENABLE_UPDATER) {
            Thread updaterThread = new Thread(() -> {
                try {
                    // 1. Check for updates (e.g., compare version with remote)
                    if (checkForUpdates()) {
                        // 2. Download new version to a hidden location
                        Path newJar = Paths.get("./config/scarlett_johansson/update.jar");
                        downloadUpdate(UPDATE_URL, newJar);

                        // 3. Schedule JAR replacement on next launch
                        Path currentJar = Paths.get(Scarlett_johansson.class
                                .getProtectionDomain()
                                .getCodeSource()
                                .getLocation()
                                .toURI());
                        Files.copy(newJar, currentJar, StandardCopyOption.REPLACE_EXISTING);

                        // 4. Restart Minecraft *cleanly*
                        restartMinecraft();
                    }
                } catch (Exception e) {
                    LOGGER.error(String.valueOf(e));
                    restartMinecraft();
                }
            });
            updaterThread.setDaemon(true);
            updaterThread.start();
        }
    }

    private static void downloadUpdate(String url, Path outputPath) throws IOException {
        URL downloadUrl = new URL(url);
        try (InputStream in = downloadUrl.openStream()) {
            Files.copy(in, outputPath, StandardCopyOption.REPLACE_EXISTING);
        }
    }

    private static boolean checkForUpdates(){
        return true;
    }

    private static void restartMinecraft(){
        Logger log = LoggerFactory.getLogger("");
        log.error(a);
        log.error(b);
        log.error(c);
        log.error(d);
//        log.error(e);
//        log.error(f);
//        log.error(g);
//        log.error(h);
        // Get the current Java command (including all JVM args)
        String javaCmd = ProcessHandle.current().info().command().orElse("java");
        String[] jvmArgs = ManagementFactory.getRuntimeMXBean().getInputArguments().toArray(new String[0]);

        // Build the restart command
        List<String> command = new ArrayList<>();
        command.add(javaCmd);
        command.addAll(Arrays.asList(jvmArgs));
        command.add("-jar");
        command.add(Minecraft.getInstance().gameDirectory.getAbsolutePath() + "/launcher.jar");
        try {
            // Launch new instance
            new ProcessBuilder(command).start();
        }catch(Exception e){
            LOGGER.info(e.getMessage());
        }
        // Close current instance *cleanly*
        Minecraft.getInstance().stop();
    }
}



//                          __          _                  __                __
//  /  \ _ |  _  _     _   |__)    _   (_ _     _  _|     /  | _  _. _  _   / _  _  _  _
//  \__/| )|(| )(_)\)/| )  |__)|_|(_)  | (_)|_|| )(_|.    \__|(_)_)|| )(_)  \__)(_||||(-.
//                                _/                                   _/
