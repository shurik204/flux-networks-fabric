package sonar.fluxnetworks.common.integration.jade;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import snownee.jade.api.Accessor;
import snownee.jade.api.view.*;
import sonar.fluxnetworks.common.device.TileFluxStorage;

import java.util.List;

public enum JadeEnergyStorageProvider implements IServerExtensionProvider<TileFluxStorage, CompoundTag>, IClientExtensionProvider<CompoundTag, EnergyView> {
    INSTANCE;

    @Override
    public ResourceLocation getUid() {
        return JadeEntrypoint.ENERGY_STORAGE_PROVIDER_ID;
    }

    @Override
    public List<ClientViewGroup<EnergyView>> getClientGroups(Accessor<?> accessor, List<ViewGroup<CompoundTag>> groups) {
        return ClientViewGroup.map(groups, tag -> EnergyView.read(tag, "E"), (group, clientGroup) -> {
            // TODO: render energy bar in the block's color
        });
    }

    @Override
    public List<ViewGroup<CompoundTag>> getGroups(Accessor<?> accessor, TileFluxStorage storage) {
        return List.of(new ViewGroup<>(List.of(EnergyView.of(storage.getTransferBuffer(), storage.getTransferHandler().getMaxEnergyStorage()))));
    }
}