package sonar.fluxnetworks.api.energy;

import net.minecraft.world.item.ItemStack;

import javax.annotation.Nonnull;

public interface IItemEnergyConnector {

    boolean hasEnergyStorage(@Nonnull ItemStack stack);

    boolean supportsInsertion(@Nonnull ItemStack stack);

    boolean supportsExtraction(@Nonnull ItemStack stack);

    long insert(long amount, @Nonnull ItemStack stack, boolean simulate);

    long extract(long amount, @Nonnull ItemStack stack, boolean simulate);
}
