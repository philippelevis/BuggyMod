package net.Vivelle.scarlett_johansson.items;

import net.Vivelle.scarlett_johansson.Scarlett_johansson;
import net.minecraft.world.item.Item;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.HashMap;
import java.util.Map;

import static net.Vivelle.scarlett_johansson.Scarlett_johansson.LOGGER;


public class ItemReg {

    private static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Scarlett_johansson.MODID);
    private static final String[] mask_names = new String[]{"owl", "stalker", "cat", "star", "book", "foe", "friend", "stranger", "bunny"};
    private static Map<String, RegistryObject<Item>> masks = new HashMap<>();

    @SuppressWarnings("deprecated")
    public static void init(){
        LOGGER.info("regging items");
        regMasks();
        ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
    }

    public static Map<String, RegistryObject<Item>> getMasks(){
        return masks;
    }

    private static void regMasks() {
        for(String str: mask_names) {
            RegistryObject<Item> next = ITEMS.register(str+"_mask",
                    () -> new MaskItem(str, new Item.Properties()));
            masks.put(str, next);
        }
    }
}
