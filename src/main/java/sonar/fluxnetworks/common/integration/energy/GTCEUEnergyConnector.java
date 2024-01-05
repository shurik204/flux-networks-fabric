package sonar.fluxnetworks.common.integration.energy;

//import com.gregtechceu.gtceu.api.capability.IElectricItem;
//import com.gregtechceu.gtceu.api.capability.IEnergyContainer;
//import com.gregtechceu.gtceu.api.capability.fabric.GTCapability;
//import net.fabricmc.fabric.api.lookup.v1.block.BlockApiCache;
//import net.minecraft.core.Direction;
//import net.minecraft.server.level.ServerLevel;
//import net.minecraft.world.item.ItemStack;
//import net.minecraft.world.level.Level;
//import net.minecraft.world.level.block.entity.BlockEntity;
//import sonar.fluxnetworks.api.energy.IBlockEnergyConnector;
//import sonar.fluxnetworks.api.energy.IItemEnergyConnector;
//import sonar.fluxnetworks.common.util.FluxUtils;
//
//import javax.annotation.Nonnull;

//public class GTCEUEnergyConnector implements IBlockEnergyConnector, IItemEnergyConnector {
//
//    public static final GTCEUEnergyConnector INSTANCE = new GTCEUEnergyConnector();
//
//
//
//    public static BlockApiCache<IEnergyContainer, Direction> createCache(Level level, BlockEntity target) {
//
//    }
//
//    @Override
//    public boolean hasEnergyStorage(@Nonnull BlockEntity target, @Nonnull Direction side) {
//        return !target.isRemoved() && GTCapability.CAPABILITY_ENERGY.find(target.getLevel(), target.getBlockPos(), target.getBlockState(), target, side) != null;
//    }
//
//    @Override
//    public boolean supportsInsertion(@Nonnull BlockEntity target, @Nonnull Direction side) {
//        if (!target.isRemoved()) {
//            IEnergyContainer container = FluxUtils.getBlockEnergy(target, GTCapability.CAPABILITY_ENERGY_CONTAINER, side);
//            return container != null && container.inputsEnergy(side);
//        }
//        return false;
//    }
//
//    @Override
//    public boolean supportsExtraction(@Nonnull BlockEntity target, @Nonnull Direction side) {
//        if (!target.isRemoved()) {
//            IEnergyContainer container = FluxUtils.getBlockEnergy(target, GTCapability.CAPABILITY_ENERGY_CONTAINER, side);
//            return container != null && container.outputsEnergy(side);
//        }
//        return false;
//    }
//
//    @Override
//    public long insert(long amount, @Nonnull BlockEntity target, @Nonnull Direction side, boolean simulate) {
//        IEnergyContainer container = FluxUtils.getBlockApi(target, GTCapability.CAPABILITY_ENERGY, side);
//        if (container == null) {
//            return 0;
//        }
//        long demand = container.getEnergyCanBeInserted();
//        if (demand == 0) {
//            return 0;
//        }
//        long voltage = Math.min(container.getInputVoltage(), demand);
//        if (simulate) {
//            return Math.min(voltage << 2, amount);
//        }
//        voltage = Math.min(voltage, amount >> 2);
//        if (voltage == 0) {
//            return 0;
//        }
//        long energy = voltage * container.acceptEnergyFromNetwork(side, voltage, 1);
//        return energy << 2;
//    }
//
//    @Override
//    public long receiveFrom(long amount, @Nonnull BlockEntity target, @Nonnull Direction side, boolean simulate) {
//        IEnergyContainer container = FluxUtils.getBlockEnergy(target, GTCapability.CAPABILITY_ENERGY_CONTAINER, side);
//        if (container == null) {
//            return 0;
//        }
//        return container.removeEnergy(container.getOutputVoltage() * container.getOutputAmperage()) << 2;
//    }
//
//    @Override
//    public boolean hasCapability(@Nonnull ItemStack stack) {
//        return !stack.isEmpty() && stack.getCapability(GTCapability.CAPABILITY_ELECTRIC_ITEM).isPresent();
//    }
//
//    @Override
//    public boolean canSendTo(@Nonnull ItemStack stack) {
//        return hasCapability(stack);
//    }
//
//    @Override
//    public boolean canReceiveFrom(@Nonnull ItemStack stack) {
//        return hasCapability(stack);
//    }
//
//    @Override
//    public long sendTo(long amount, @Nonnull ItemStack stack, boolean simulate) {
//        IElectricItem electricItem = FluxUtils.getItemEnergy(stack, GTCapability.CAPABILITY_ELECTRIC_ITEM);
//        if (electricItem != null) {
//            return electricItem.charge(amount >> 2, electricItem.getTier(), false, simulate) << 2;
//        }
//        return 0;
//    }
//
//    @Override
//    public long receiveFrom(long amount, @Nonnull ItemStack stack, boolean simulate) {
//        IElectricItem electricItem = FluxUtils.getItemEnergy(stack, GTCapability.CAPABILITY_ELECTRIC_ITEM);
//        if (electricItem != null) {
//            return electricItem.discharge(amount >> 2, electricItem.getTier(), false, true, false) << 2;
//        }
//        return 0;
//    }
//}