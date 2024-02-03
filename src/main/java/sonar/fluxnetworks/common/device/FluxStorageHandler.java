package sonar.fluxnetworks.common.device;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import sonar.fluxnetworks.FluxConfig;
import sonar.fluxnetworks.api.FluxConstants;
import sonar.fluxnetworks.common.connection.TransferHandler;

import javax.annotation.Nonnull;

public abstract class FluxStorageHandler extends TransferHandler {

    private long mAdded;
    private long mRemoved;

    protected FluxStorageHandler(long limit) {
        super(limit);
    }

    @Override
    public void onCycleStart() {
    }

    @Override
    public void onCycleEnd() {
        mChange = mAdded - mRemoved;
        mAdded = 0;
        mRemoved = 0;
    }

    @Override
    public void addToBuffer(long energy) {
        mBuffer += energy;
        mAdded += energy;
    }

    @Override
    public long removeFromBuffer(long energy) {
        long op = Math.min(Math.min(energy, mBuffer), getLimit() - mRemoved);
        assert op >= 0;
        mBuffer -= op;
        mRemoved += op;
        return op;
    }

    @Override
    public long getRequest() {
        return Math.max(0, Math.min(getMaxEnergyStorage() - mBuffer, getLimit() - mAdded));
    }

    /**
     * Make this storage full of energy (debug or admin function).
     */
    void fillUp() {
        long energy = Math.max(0, Math.min(getMaxEnergyStorage() - mBuffer, Long.MAX_VALUE - mAdded));
        if (energy > 0) {
            mBuffer += energy;
            mAdded += energy;
        }
    }

    public abstract long getMaxEnergyStorage();

    @Override
    public int getPriority() {
        return super.getPriority() - STORAGE_PRI_DIFF;
    }

    @Override
    public void setLimit(long limit) {
        super.setLimit(Math.min(limit, getMaxEnergyStorage()));
    }

    @Override
    public void writeCustomTag(@Nonnull CompoundTag tag, byte type) {
        super.writeCustomTag(tag, type);
        if (type == FluxConstants.NBT_PHANTOM_UPDATE) {
            tag.putLong(FluxConstants.BUFFER, mBuffer);
        } else {
            tag.putLong(FluxConstants.ENERGY, mBuffer);
        }
    }

    @Override
    public void writePacketBuffer(@Nonnull FriendlyByteBuf buffer, byte type) {
        if (type == FluxConstants.DEVICE_S2C_STORAGE_ENERGY) {
            buffer.writeLong(mBuffer);
        } else {
            super.writePacketBuffer(buffer, type);
        }
    }

    @Override
    public void readPacketBuffer(@Nonnull FriendlyByteBuf buffer, byte type) {
        if (type == FluxConstants.DEVICE_S2C_STORAGE_ENERGY) {
            mBuffer = buffer.readLong();
        } else {
            super.readPacketBuffer(buffer, type);
        }
    }

    public static class Basic extends FluxStorageHandler {

        public Basic() {
            super(FluxConfig.basicTransfer);
        }

        @Override
        public long getMaxEnergyStorage() {
            return FluxConfig.basicCapacity;
        }
    }

    public static class Herculean extends FluxStorageHandler {

        public Herculean() {
            super(FluxConfig.herculeanTransfer);
        }

        @Override
        public long getMaxEnergyStorage() {
            return FluxConfig.herculeanCapacity;
        }
    }

    public static class Gargantuan extends FluxStorageHandler {

        public Gargantuan() {
            super(FluxConfig.gargantuanTransfer);
        }

        @Override
        public long getMaxEnergyStorage() {
            return FluxConfig.gargantuanCapacity;
        }
    }

    public static class Bottomless extends FluxStorageHandler {
        private long mLastEnergy;
        public Bottomless() {
            super(Long.MAX_VALUE);
        }

        @Override
        public long getMaxEnergyStorage() {
            return Long.MAX_VALUE;
        }

        @Override
        public void onCycleStart() {
            super.onCycleStart();
            mLastEnergy = mBuffer;
        }

        @Override
        public void onCycleEnd() {
            super.onCycleEnd();
            mBuffer = mLastEnergy;
        }
    }
}