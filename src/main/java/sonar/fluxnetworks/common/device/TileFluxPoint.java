package sonar.fluxnetworks.common.device;

import io.github.fabricators_of_create.porting_lib.util.Lazy;
import net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import sonar.fluxnetworks.api.device.FluxDeviceType;
import sonar.fluxnetworks.api.device.IFluxPoint;
import sonar.fluxnetworks.common.util.FluxGuiStack;
import sonar.fluxnetworks.register.RegistryBlockEntityTypes;
import team.reborn.energy.api.EnergyStorage;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class TileFluxPoint extends TileFluxConnector implements IFluxPoint {

    private final FluxPointHandler mHandler = new FluxPointHandler();

    @Nullable
    private Lazy<EnergyStorageImpl> mEnergyCap;

    public TileFluxPoint(@Nonnull BlockPos pos, @Nonnull BlockState state) {
        super(RegistryBlockEntityTypes.FLUX_POINT, pos, state);
    }

    @Nonnull
    @Override
    public FluxDeviceType getDeviceType() {
        return FluxDeviceType.POINT;
    }

    @Nonnull
    @Override
    public FluxPointHandler getTransferHandler() {
        return mHandler;
    }

    @Nonnull
    @Override
    public ItemStack getDisplayStack() {
        return FluxGuiStack.FLUX_POINT;
    }

    @Nullable
    public EnergyStorage getEnergyStorage(@Nullable Direction direction) {
        if (!isRemoved()) {
             if (mEnergyCap == null) {
                 final EnergyStorageImpl storage = new EnergyStorageImpl();
                 // save an immutable pointer to an immutable object
                 mEnergyCap = Lazy.of(() -> storage);
             }
             return mEnergyCap.get();
        }
        return null;
    }

    // TODO: what to do with this?
    // On Forge was called from BlockEntity::setRemoved
    // @Override
    // public void invalidateCaps() {
    //     super.invalidateCaps();
    //     if (mEnergyCap != null) {
    //         mEnergyCap.invalidate();
    //         mEnergyCap = null;
    //     }
    // }

    // @Nonnull
    // @Override
    // public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
    //     if (!isRemoved()) {
    //         if (cap == ForgeCapabilities.ENERGY || cap == FluxCapabilities.FN_ENERGY_STORAGE) {
    //             if (mEnergyCap == null) {
    //                 final EnergyStorageImpl storage = new EnergyStorageImpl();
    //                 // save an immutable pointer to an immutable object
    //                 mEnergyCap = LazyOptional.of(() -> storage);
    //             }
    //             return mEnergyCap.cast();
    //         }
    //     }
    //     return super.getCapability(cap, side);
    // }

    private class EnergyStorageImpl implements EnergyStorage {

        public EnergyStorageImpl() {}

        @Override
        public boolean supportsInsertion() {
            return false;
        }

        @Override
        public boolean supportsExtraction() {
            return false;
        }

        @Override
        public long insert(long maxAmount, TransactionContext transaction) {
            return 0;
        }

        @Override
        public long extract(long maxAmount, TransactionContext transaction) {
            return 0;
        }

        @Override
        public long getAmount() {
            return mHandler.getBuffer();
        }

        @Override
        public long getCapacity() {
            return Math.max(mHandler.getBuffer(), mHandler.getLimit());
        }
    }
}
