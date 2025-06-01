package net.Vivelle.scarlett_johansson;

import com.mojang.logging.LogUtils;
import net.Vivelle.scarlett_johansson.items.ItemReg;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.loading.FMLEnvironment;
import org.slf4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(Scarlett_johansson.MODID)
public class Scarlett_johansson {
//
//    // Define mod id in a common place for everything to reference
    public static final String MODID = "scarlett_johansson";
    public static String modVersion = ModList.get().getModContainerById(MODID)
            .orElseThrow(() -> new IllegalStateException("Mod " + MODID + " not found!"))
            .getModInfo()
            .getVersion()
            .toString();
    // Directly reference a slf4j logger
    public static final Logger LOGGER = LogUtils.getLogger();

    public Scarlett_johansson() {
        if (!FMLEnvironment.production) {
            LOGGER.error("Running in Forge dev environment!");
            LOGGER.error("AutoUpdater Disabled");
            AutoUpdater.checkForUpdates(false);
            // Skip auto-updates in dev
        } else {
            AutoUpdater.checkForUpdates(true); // Only runs in production
        }

        ItemReg.init();


    }

    @SubscribeEvent
    public void buildContents(BuildCreativeModeTabContentsEvent event) {
        // Add to ingredients tab
        if (event.getTabKey() == CreativeModeTabs.COMBAT) {
            ItemReg.getMasks().forEach((i, entry)->event.accept(entry));
        }
    }
}



//                          __          _                  __                __
//  /  \ _ |  _  _     _   |__)    _   (_ _     _  _|     /  | _  _. _  _   / _  _  _  _
//  \__/| )|(| )(_)\)/| )  |__)|_|(_)  | (_)|_|| )(_|.    \__|(_)_)|| )(_)  \__)(_||||(-.
//                                _/                                   _/
