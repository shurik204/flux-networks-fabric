package sonar.fluxnetworks.common.integration.energy;

import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.base.SingleSlotStorage;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import sonar.fluxnetworks.api.energy.IBlockEnergyConnector;
import sonar.fluxnetworks.api.energy.IItemEnergyConnector;
import sonar.fluxnetworks.common.util.EnergyUtils;
import sonar.fluxnetworks.common.util.FluxUtils;
import team.reborn.energy.api.EnergyStorage;

import javax.annotation.Nonnull;

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
            return EnergyUtils.supportsInsertion(FluxUtils.getBlockEnergy(target, side));
        }
        return false;
    }

    @Override
    public boolean supportsExtraction(@Nonnull BlockEntity target, @Nonnull Direction side) {
        if (!target.isRemoved()) {
            return EnergyUtils.supportsExtraction(FluxUtils.getBlockEnergy(target, side));
        }
        return false;
    }

    @Override
    public long insert(long amount, @Nonnull BlockEntity target, @Nonnull Direction side, boolean simulate) {
        EnergyStorage storage = FluxUtils.getBlockEnergy(target, side);
        //noinspection DataFlowIssue
        return EnergyUtils.tryAction(amount, storage, simulate, storage::insert);
    }

    @Override
    public long extract(long amount, @Nonnull BlockEntity target, @Nonnull Direction side, boolean simulate) {
        EnergyStorage storage = FluxUtils.getBlockEnergy(target, side);
        //noinspection DataFlowIssue
        return EnergyUtils.tryAction(amount, storage, simulate, storage::extract);
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
            return EnergyUtils.supportsInsertion(FluxUtils.getItemEnergy(stack));
        }
        return false;
    }

    @Override
    public boolean supportsExtraction(@Nonnull ItemStack stack) {
        if (!stack.isEmpty()) {
            return EnergyUtils.supportsExtraction(FluxUtils.getItemEnergy(stack));
        }
        return false;
    }

    @Override
    public long insert(long amount, @Nonnull ServerPlayer player, @Nonnull SingleSlotStorage<ItemVariant> slot, boolean simulate) {
        EnergyStorage storage = FluxUtils.getItemEnergy(player, slot);
        long result = 0;
        if (storage != null) {
            try (Transaction tx = Transaction.openOuter()) {
                result = storage.insert(amount, tx);
                if (simulate) tx.abort();
                else tx.commit();
            }
        }
        return result;
    }

    @Override
    public long extract(long amount, @Nonnull ServerPlayer player, @Nonnull SingleSlotStorage<ItemVariant> slot, boolean simulate) {
        EnergyStorage storage = FluxUtils.getItemEnergy(player, slot);
        //noinspection DataFlowIssue
        return EnergyUtils.tryAction(amount, storage, simulate, storage::extract);
    }
}
