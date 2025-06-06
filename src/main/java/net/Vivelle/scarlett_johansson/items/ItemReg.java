package net.Vivelle.scarlett_johansson.items;

import net.Vivelle.scarlett_johansson.Scarlett_johansson;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.CuriosCapability;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurio;

import java.util.HashMap;
import java.util.Map;

import static net.Vivelle.scarlett_johansson.Scarlett_johansson.LOGGER;


public class ItemReg {

    private static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Scarlett_johansson.MODID);
    private static final String[] mask_names = new String[]{"owl", "stalker", "cat", "star", "book", "foe", "friend", "stranger", "bunny"};
    private static Map<String, RegistryObject<Item>> masks = new HashMap<>();

    public static RegistryObject<Item> RING = ITEMS.register("ring", WeddingRingItem::new);

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
                    () -> new MaskItem(str, new Item.Properties().stacksTo(1)));
            masks.put(str, next);
        }
    }
}
