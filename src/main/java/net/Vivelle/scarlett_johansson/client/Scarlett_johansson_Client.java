package net.Vivelle.scarlett_johansson.client;


import com.mojang.logging.LogUtils;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.slf4j.Logger;

@OnlyIn(Dist.CLIENT)
public class Scarlett_johansson_Client {
    public static final Logger LOGGER = LogUtils.getLogger();
    public static void init(){
        LOGGER.error("hello from client init!");
    }
}
