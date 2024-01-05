package sonar.fluxnetworks.api.energy;

import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntity;

import javax.annotation.Nonnull;

public interface IBlockEnergyConnector {

    boolean hasEnergyStorage(@Nonnull BlockEntity target, @Nonnull Direction side);

    boolean supportsInsertion(@Nonnull BlockEntity target, @Nonnull Direction side);

    boolean supportsExtraction(@Nonnull BlockEntity target, @Nonnull Direction side);

    long insert(long amount, @Nonnull BlockEntity target, @Nonnull Direction side, boolean simulate);

    long extract(long amount, @Nonnull BlockEntity target, @Nonnull Direction side, boolean simulate);
}
