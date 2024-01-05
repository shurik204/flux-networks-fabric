package sonar.fluxnetworks.common.integration.energy;

import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import sonar.fluxnetworks.api.energy.*;
import sonar.fluxnetworks.common.util.FluxUtils;
import team.reborn.energy.api.EnergyStorage;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.function.ToLongBiFunction;

@SuppressWarnings("UnstableApiUsage")
public class TREnergyConnector implements IBlockEnergyConnector, IItemEnergyConnector {

    public static final TREnergyConnector INSTANCE = new TREnergyConnector();

    private TREnergyConnector() {}

    //
    // Block
    //
    @Override
    public boolean hasEnergyStorage(@Nonnull BlockEntity target, @Nonnull Direction side) {
        return FluxUtils.getBlockEnergy(target, side) != null;
    }

    @Override
    public boolean supportsInsertion(@Nonnull BlockEntity target, @Nonnull Direction side) {
        if (!target.isRemoved()) {
            return supportsInsertion(FluxUtils.getBlockEnergy(target, side));
        }
        return false;
    }

    @Override
    public boolean supportsExtraction(@Nonnull BlockEntity target, @Nonnull Direction side) {
        if (!target.isRemoved()) {
            return supportsExtraction(FluxUtils.getBlockEnergy(target, side));
        }
        return false;
    }

    @Override
    public long insert(long amount, @Nonnull BlockEntity target, @Nonnull Direction side, boolean simulate) {
        EnergyStorage storage = FluxUtils.getBlockEnergy(target, side);
        return tryAction(amount, storage, simulate, storage::insert);
    }

    @Override
    public long extract(long amount, @Nonnull BlockEntity target, @Nonnull Direction side, boolean simulate) {
        EnergyStorage storage = FluxUtils.getBlockEnergy(target, side);
        return tryAction(amount, storage, simulate, storage::extract);
    }

    //
    // Item
    //
    @Override
    public boolean hasEnergyStorage(@Nonnull ItemStack stack) {
        return !stack.isEmpty() && FluxUtils.getItemEnergy(stack) != null;
    }

    @Override
    public boolean supportsInsertion(@Nonnull ItemStack stack) {
        if (!stack.isEmpty()) {
            return supportsInsertion(FluxUtils.getItemEnergy(stack));
        }
        return false;
    }

    @Override
    public boolean supportsExtraction(@Nonnull ItemStack stack) {
        if (!stack.isEmpty()) {
            return supportsExtraction(FluxUtils.getItemEnergy(stack));
        }
        return false;
    }

    @Override
    public long insert(long amount, @Nonnull ItemStack stack, boolean simulate) {
        EnergyStorage storage = FluxUtils.getItemEnergy(stack);
        return tryAction(amount, storage, simulate, storage::insert);
    }

    @Override
    public long extract(long amount, @Nonnull ItemStack stack, boolean simulate) {
        EnergyStorage storage = FluxUtils.getItemEnergy(stack);
        return tryAction(amount, storage, simulate, storage::extract);
    }

    // TODO: move these to a more appropriate place
    public static long tryAction(long amount, EnergyStorage storage, boolean simulate, ToLongBiFunction<Long, TransactionContext> action) {
        if (storage == null) return 0;
        long result = 0;
        try (Transaction tx = Transaction.openNested(Transaction.getCurrentUnsafe())) {
            result = action.applyAsLong(amount, tx);
            if (simulate) tx.abort();
            else tx.commit();
        }
        return result;
    }

    public static boolean supportsInsertion(@Nullable EnergyStorage storage) {
        return storage != null && storage.supportsInsertion();
    }

    public static boolean supportsExtraction(@Nullable EnergyStorage storage) {
        return storage != null && storage.supportsExtraction();
    }
}
