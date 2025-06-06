package net.Vivelle.scarlett_johansson;

import com.mojang.logging.LogUtils;
import net.Vivelle.scarlett_johansson.items.ItemReg;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModContainer;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.forgespi.locating.IModFile;
import org.slf4j.Logger;
import net.Vivelle.scarlett_johansson.client.Scarlett_johansson_Client;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(Scarlett_johansson.MODID)
public class Scarlett_johansson {
//
//    // Define mod id in a common place for everything to reference
    public static final String MODID = "scarlett_johansson";

    private static final ModContainer ModContainer = ModList.get().getModContainerById(MODID).orElseThrow(() -> new IllegalStateException("Mod " + MODID + " not found!"));

    public static IModFile ModFile = ModContainer.getModInfo().getOwningFile().getFile();

    public static String ModVersion = ModContainer
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
            //LOGGER.warn("AUTOUPDATING BRO< GET READY!");
            AutoUpdater.checkForUpdates(true); // Only runs in production
        }
        final IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
        eventBus.addListener(this::buildContents);
        //eventBus.addListener(this::setup);
        ItemReg.init();
        if(FMLEnvironment.dist == Dist.CLIENT) {
            Scarlett_johansson_Client.init();
        }
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
