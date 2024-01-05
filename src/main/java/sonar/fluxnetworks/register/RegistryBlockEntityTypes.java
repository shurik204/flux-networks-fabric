package sonar.fluxnetworks.register;

import com.mojang.datafixers.DSL;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import sonar.fluxnetworks.common.device.TileFluxController;
import sonar.fluxnetworks.common.device.TileFluxPlug;
import sonar.fluxnetworks.common.device.TileFluxPoint;
import sonar.fluxnetworks.common.device.TileFluxStorage;
import team.reborn.energy.api.EnergyStorage;

import java.util.Set;

public class RegistryBlockEntityTypes {
    public static final BlockEntityType<TileFluxPlug> FLUX_PLUG = register(RegistryBlocks.FLUX_PLUG_KEY, new BlockEntityType<>(TileFluxPlug::new, Set.of(RegistryBlocks.FLUX_PLUG), DSL.remainderType()));
    public static final BlockEntityType<TileFluxPoint> FLUX_POINT = register(RegistryBlocks.FLUX_POINT_KEY, new BlockEntityType<>(TileFluxPoint::new, Set.of(RegistryBlocks.FLUX_POINT), DSL.remainderType()));
    public static final BlockEntityType<TileFluxController> FLUX_CONTROLLER = register(RegistryBlocks.FLUX_CONTROLLER_KEY, new BlockEntityType<>(TileFluxController::new, Set.of(RegistryBlocks.FLUX_CONTROLLER), DSL.remainderType()));
    public static final BlockEntityType<TileFluxStorage.Basic> BASIC_FLUX_STORAGE = register(RegistryBlocks.BASIC_FLUX_STORAGE_KEY, new BlockEntityType<>(TileFluxStorage.Basic::new, Set.of(RegistryBlocks.BASIC_FLUX_STORAGE), DSL.remainderType()));
    public static final BlockEntityType<TileFluxStorage.Herculean> HERCULEAN_FLUX_STORAGE = register(RegistryBlocks.HERCULEAN_FLUX_STORAGE_KEY, new BlockEntityType<>(TileFluxStorage.Herculean::new, Set.of(RegistryBlocks.HERCULEAN_FLUX_STORAGE), DSL.remainderType()));
    public static final BlockEntityType<TileFluxStorage.Gargantuan> GARGANTUAN_FLUX_STORAGE = register(RegistryBlocks.GARGANTUAN_FLUX_STORAGE_KEY, new BlockEntityType<>(TileFluxStorage.Gargantuan::new, Set.of(RegistryBlocks.GARGANTUAN_FLUX_STORAGE), DSL.remainderType()));

    private static <T extends BlockEntity> BlockEntityType<T> register(ResourceLocation key, BlockEntityType<T> blockEntityType) {
        return Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, key, blockEntityType);
    }

    public static void init() {
        EnergyStorage.SIDED.registerForBlockEntity(TileFluxPoint::getEnergyStorage, FLUX_POINT);
        EnergyStorage.SIDED.registerForBlockEntity(TileFluxPlug::getEnergyStorage, FLUX_PLUG);
    }
}
