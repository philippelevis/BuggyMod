package net.Vivelle.scarlett_johansson.mixin;

import net.Vivelle.scarlett_johansson.items.MaskItem;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.CampfireBlockEntity;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Player.class)
public abstract class PlayerDisplayNameMixin {
    @Shadow @Final private Inventory inventory;
    @Shadow private Component displayname;
    @Shadow public abstract Inventory getInventory();

    @Shadow @Final private static Logger LOGGER;

    @Inject(method = "getName",at = @At("RETURN"), cancellable = true)
    private void namefix(CallbackInfoReturnable<Component> cir){
        Player self = (Player)(Object)this;
        Component fixedName = cir.getReturnValue();
        if(this.getInventory().getArmor(EquipmentSlot.HEAD.getIndex()).getItem() instanceof MaskItem mask){
            fixedName = Component.empty().withStyle(ChatFormatting.OBFUSCATED).append(cir.getReturnValue());
        }
        updateNames(fixedName);
        cir.setReturnValue(fixedName);
    }

    @Inject(method = "getDisplayName", at = @At("HEAD"))
    private void namefix2(CallbackInfoReturnable<Component> cir){
        this.displayname = null;
    }


    @Unique
    private void updateNames(Component fixedname){
        Player self = (Player)(Object)this;
        this.displayname = fixedname;
        if(!self.level().isClientSide) {
            ((ServerPlayer) self).refreshTabListName();
        }
    }

}
