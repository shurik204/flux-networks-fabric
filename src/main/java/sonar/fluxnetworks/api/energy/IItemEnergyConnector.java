package sonar.fluxnetworks.api.energy;

import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.base.SingleSlotStorage;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nonnull;

@SuppressWarnings("UnstableApiUsage")
public interface IItemEnergyConnector {

    boolean hasEnergyStorage(@Nonnull ItemStack stack);

    boolean supportsInsertion(@Nonnull ItemStack stack);

    boolean supportsExtraction(@Nonnull ItemStack stack);

    long insert(long amount, @Nonnull ServerPlayer player, @Nonnull SingleSlotStorage<ItemVariant> slot, boolean simulate);

    long extract(long amount, @Nonnull ServerPlayer player, @Nonnull SingleSlotStorage<ItemVariant> slot, boolean simulate);
}
