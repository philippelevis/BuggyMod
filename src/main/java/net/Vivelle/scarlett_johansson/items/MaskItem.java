package net.Vivelle.scarlett_johansson.items;

import com.google.common.collect.Multimap;
import net.Vivelle.scarlett_johansson.mixin.PlayerDisplayNameMixin;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
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

    @Override
    public InteractionResultHolder<ItemStack> use(Level p_40395_, Player p_40396_, InteractionHand p_40397_) {
        return this.swapWithEquipmentSlot(this, p_40395_, p_40396_, p_40397_);
    }

    private InteractionResultHolder<ItemStack> swapWithEquipmentSlot(Item p_270453_, Level p_270395_, Player p_270300_, InteractionHand p_270262_) {
        ItemStack $$4 = p_270300_.getItemInHand(p_270262_);
        EquipmentSlot $$5 = Mob.getEquipmentSlotForItem($$4);
        ItemStack $$6 = p_270300_.getItemBySlot($$5);
        if (!EnchantmentHelper.hasBindingCurse($$6) && !ItemStack.matches($$4, $$6)) {
            if (!p_270395_.isClientSide()) {
                p_270300_.awardStat(Stats.ITEM_USED.get(p_270453_));
            }

            ItemStack $$7 = $$6.isEmpty() ? $$4 : $$6.copyAndClear();
            ItemStack $$8 = $$4.copyAndClear();
            p_270300_.setItemSlot($$5, $$8);
            return InteractionResultHolder.sidedSuccess($$7, p_270395_.isClientSide());
        } else {
            return InteractionResultHolder.fail($$4);
        }
    }
}
