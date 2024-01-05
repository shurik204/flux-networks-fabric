package sonar.fluxnetworks.common.device;

import io.github.fabricators_of_create.porting_lib.common.util.Lazy;
import net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import sonar.fluxnetworks.api.device.FluxDeviceType;
import sonar.fluxnetworks.api.device.IFluxPlug;
import sonar.fluxnetworks.common.util.FluxGuiStack;
import sonar.fluxnetworks.common.util.FluxUtils;
import sonar.fluxnetworks.register.RegistryBlockEntityTypes;
import team.reborn.energy.api.EnergyStorage;

import javax.annotation.Nonnull;

@SuppressWarnings("UnstableApiUsage")
public class TileFluxPlug extends TileFluxConnector implements IFluxPlug {

    private final FluxPlugHandler mHandler = new FluxPlugHandler();

    private final Lazy<?>[] mEnergyCaps = new Lazy[FluxUtils.DIRECTIONS.length];

    public TileFluxPlug(@Nonnull BlockPos pos, @Nonnull BlockState state) {
        super(RegistryBlockEntityTypes.FLUX_PLUG, pos, state);
    }

    @Nonnull
    @Override
    public FluxDeviceType getDeviceType() {
        return FluxDeviceType.PLUG;
    }

    @Nonnull
    @Override
    public FluxPlugHandler getTransferHandler() {
        return mHandler;
    }

    @Nonnull
    @Override
    public ItemStack getDisplayStack() {
        return FluxGuiStack.FLUX_PLUG;
    }

    public EnergyStorage getEnergyStorage(Direction direction) {
        if (!isRemoved()) {
            final int index = direction == null ? 0 : direction.get3DDataValue();
            Lazy<?> handler = mEnergyCaps[index];
            if (handler == null) {
                final EnergyStorageImpl storage = new EnergyStorageImpl(
                        direction == null ? Direction.from3DDataValue(0) : direction);
                handler = Lazy.of(() -> storage);
                mEnergyCaps[index] = handler;
            }
            return (EnergyStorage) handler.get();
        }
        return null;
    }

    private class EnergyStorageImpl implements EnergyStorage {
        @Nonnull
        private final Direction mSide;

        public EnergyStorageImpl(@Nonnull Direction side) {
            mSide = side;
        }

        @Override
        public boolean supportsInsertion() {
            return getNetwork().isValid();
        }

        @Override
        public boolean supportsExtraction() {
            return false;
        }

        @Override
        public long insert(long maxAmount, TransactionContext tCtx) {
            if (getNetwork().isValid()) {
                return mHandler.insert(maxAmount, mSide, tCtx, getNetwork().getBufferLimiter());
            }
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
