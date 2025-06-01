package net.Vivelle.scarlett_johansson.items;

import com.google.common.collect.Multimap;
import net.Vivelle.scarlett_johansson.mixin.PlayerDisplayNameMixin;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

public class MaskItem extends Item{
    private final String textureVariant; // e.g., "clown", "ghost", "devil"

    public MaskItem(String variant, Properties props) {
        super(props);
        this.textureVariant = variant;
    }

    @Override
    public @Nullable EquipmentSlot getEquipmentSlot(ItemStack stack) {
        return EquipmentSlot.HEAD;
    }

    public String getTextureVariant() {
        return textureVariant;
    }
}
