package net.Vivelle.scarlett_johansson.items;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.CuriosCapability;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurio;
import top.theillusivec4.curios.api.type.capability.ICurioItem;
import top.theillusivec4.curios.api.type.capability.ICuriosItemHandler;

import static net.Vivelle.scarlett_johansson.Scarlett_johansson.LOGGER;

public class WeddingRingItem extends Item implements ICurioItem {

    public static final Capability<IItemHandler> ITEM_HANDLER = CapabilityManager.get(new CapabilityToken<>(){});


    public WeddingRingItem() {
        super(new Item.Properties().stacksTo(1).defaultDurability(0));
    }

    @Override
    public @Nullable ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundTag nbt) {
        return new ICapabilityProvider() {
            private final LazyOptional<IItemHandler> itemHandler = LazyOptional.of(() -> new ItemStackHandler(1) {
                @Override
                public boolean isItemValid(int slot, @NotNull ItemStack item) {
                    return item.getItem() instanceof WeddingRingItem;
                }
            });

            private final LazyOptional<ICurio> curio = LazyOptional.of(() -> new ICurio() {
                @Override
                public ItemStack getStack() {
                    return stack;
                }

                @Override
                public boolean canUnequip(SlotContext slotContext) {
                    return !slotContext.entity().isAlive();
                }

                @Override
                public boolean canEquip(SlotContext slotContext) {
                    return stack.getOrCreateTag().contains("target") && stack.getOrCreateTag().getUUID("target").compareTo(slotContext.entity().getUUID()) == 0;
                }

                @Override
                public void onUnequip(SlotContext slotContext, ItemStack newStack) {
                    ICurio.super.onUnequip(slotContext, newStack);
                    slotContext.entity().sendSystemMessage(Component.translatable("message.scarlett_johansson.ring.try_unequip"));
                    CompoundTag tag = newStack.getOrCreateTag();
                    tag.remove("owner");
                    tag.remove("target");
                    newStack.setTag(tag);
                }
            });

            @Override
            public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
                if (cap == ITEM_HANDLER) {
                    return itemHandler.cast();
                }
                if (cap == CuriosCapability.ITEM) {
                    return curio.cast();
                }
                return LazyOptional.empty();
            }
        };
    }

    @Override
    public boolean hasCurioCapability(ItemStack stack) {
        return ICurioItem.super.hasCurioCapability(stack);
    }

    @Override
    public InteractionResult interactLivingEntity(ItemStack stack, Player self, LivingEntity other, InteractionHand hand) {
        if (!self.level().isClientSide && other instanceof Player otherPlayer) {
            LazyOptional<ICuriosItemHandler> curiosInventory = CuriosApi.getCuriosInventory(otherPlayer);

            if (curiosInventory.isPresent()) {
                ICuriosItemHandler handler = curiosInventory.resolve().get();

                return handler.getStacksHandler("wed").map(slotHandler -> {
                    ItemStack copy = stack.copy();
                    copy.setCount(1);
                    CompoundTag tag = copy.getTag();
                    if(tag == null){ tag = new CompoundTag(); copy.setTag(tag);}

                    tag.putUUID("owner", self.getUUID());
                    tag.putUUID("target", otherPlayer.getUUID());
                    // Attempt insertion
                    if (CanApplyRing(self, otherPlayer, false)) {
                        // Success - update stack

                        slotHandler.getStacks().setStackInSlot(slotHandler.getSlots() - 1, copy);
                        otherPlayer.getInventory().setChanged();

                        // Proper stack handling
                        stack.shrink(1);

                        // Force inventory update
                        self.getInventory().setChanged();

                        // Send messages
                        ((ServerPlayer)self).sendSystemMessage(
                                Component.translatable("message.scarlett_johansson.ring.given", otherPlayer.getName()),
                                true
                        );
                        ((ServerPlayer)otherPlayer).sendSystemMessage(
                                Component.translatable("message.scarlett_johansson.ring.received", otherPlayer.getName()),
                                true
                        );

                        if(BothPlayersHaveRings(self, otherPlayer)){
                            ((ServerPlayer)self).sendSystemMessage(
                                    Component.translatable("message.scarlett_johansson.ring.married", otherPlayer.getName()),
                                    true
                            );
                            ((ServerPlayer)otherPlayer).sendSystemMessage(
                                    Component.translatable("message.scarlett_johansson.ring.married", otherPlayer.getName()),
                                    true
                            );
                        }

                        return InteractionResult.CONSUME; // Important for client sync
                    }else{
                        // Failed case - no space
                        ((ServerPlayer)self).sendSystemMessage(
                                Component.translatable("message.scarlett_johansson.ring.no_space", otherPlayer.getName()),
                                true
                        );
                        return InteractionResult.FAIL;
                    }
                }).orElse(InteractionResult.PASS);
            }
        }
        return InteractionResult.PASS;
    }

    private boolean BothPlayersHaveRings(Player self, Player other) {
        LazyOptional<ICuriosItemHandler> myCurios = CuriosApi.getCuriosInventory(self);
        LazyOptional<ICuriosItemHandler> otherCurios = CuriosApi.getCuriosInventory(other);

        if (myCurios.isPresent() && otherCurios.isPresent()) {
            ICuriosItemHandler myhandler = myCurios.resolve().get();
            ICuriosItemHandler otherhandler = otherCurios.resolve().get();
            if (myhandler.isEquipped(ItemReg.RING.get()) && otherhandler.isEquipped(ItemReg.RING.get())) {
                LOGGER.info("self -> other match: {}", CanApplyRing(self, other, true));
                LOGGER.info("other -> self match: {}", CanApplyRing(other, self, true));
                boolean a = CanApplyRing(self, other, true) && CanApplyRing(other, self, true);
                LOGGER.info("both players have a ring: {}", a);
                return a;
            }
        }
        return false;
    }


    private boolean CanApplyRing(Player self, Player other, boolean strict){
        LazyOptional<ICuriosItemHandler> myCurios = CuriosApi.getCuriosInventory(self);
        LazyOptional<ICuriosItemHandler> otherCurios = CuriosApi.getCuriosInventory(other);

        if (otherCurios.isPresent()){
            ICuriosItemHandler handler = otherCurios.resolve().get();
            if(!handler.isEquipped(ItemReg.RING.get())){
                LOGGER.info("no ring");
                return !strict;
            }
        }

        if (myCurios.isPresent()) {
            ICuriosItemHandler handler = myCurios.resolve().get();
            return handler.getStacksHandler("wed").map(stacksHandler -> {
                ItemStack ring = stacksHandler.getStacks().getStackInSlot(stacksHandler.getSlots()-1);
                if (ring.getItem() instanceof WeddingRingItem){
                    CompoundTag tag = ring.getTag();
                    LOGGER.info("we got a ring, returning if its owner and current target cross-match");
                    LOGGER.info("{}, {}; {}",tag.getUUID("owner"),other.getUUID(),tag.getUUID("owner").compareTo(other.getUUID()));
                    return tag.getUUID("owner").compareTo(other.getUUID()) == 0;
                }
                LOGGER.info("no ring, returning {}",ring.isEmpty());
                return ring.isEmpty();
            }).orElse(false);
        }
        LOGGER.info("fallback, returning false");
        return false;
    }

}
