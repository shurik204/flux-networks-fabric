package sonar.fluxnetworks.common.integration.jade;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;
import sonar.fluxnetworks.FluxConfig;
import sonar.fluxnetworks.api.FluxTranslate;
import sonar.fluxnetworks.api.energy.EnergyType;
import sonar.fluxnetworks.client.ClientCache;
import sonar.fluxnetworks.common.connection.FluxNetwork;
import sonar.fluxnetworks.common.device.TileFluxDevice;
import sonar.fluxnetworks.common.util.FluxUtils;

public enum JadeFluxDeviceDataProvider implements IBlockComponentProvider {
    INSTANCE;

    @Override
    @Environment(EnvType.CLIENT)
    public void appendTooltip(ITooltip tooltip, BlockAccessor accessor, IPluginConfig config) {
        if (!(FluxConfig.enableJadeBasicInfo || FluxConfig.enableJadeAdvancedInfo)) {
            return;
        }
        if (!(accessor.getBlockEntity() instanceof TileFluxDevice device)) {
            return;
        }
        if (FluxConfig.enableJadeBasicInfo) {
            if (getClientNetwork(device.getNetworkID()).isValid()) {
                tooltip.add(Component.literal(getClientNetwork(device.getNetworkID()).getNetworkName()).withStyle(ChatFormatting.AQUA));
            } else {
                tooltip.add(FluxTranslate.ERROR_NO_SELECTED.makeComponent().withStyle(ChatFormatting.AQUA));
            }

            tooltip.add(Component.literal(FluxUtils.getTransferInfo(device, EnergyType.E)));

            if (Minecraft.getInstance().player.isShiftKeyDown()) {
                if (device.getDeviceType().isStorage()) {
                    tooltip.add(FluxTranslate.ENERGY_STORED.makeComponent()
                            .append(": " + ChatFormatting.GREEN + EnergyType.E.getStorage(device.getTransferBuffer()))
                    );
                } else {
                    tooltip.add(FluxTranslate.INTERNAL_BUFFER.makeComponent()
                            .append(": " + ChatFormatting.GREEN + EnergyType.E.getStorage(device.getTransferBuffer()))
                    );
                }
            }
        }
        if (FluxConfig.enableJadeAdvancedInfo &&
                (!FluxConfig.enableJadeSneaking || Minecraft.getInstance().player.isShiftKeyDown())) {

            if (device.getDisableLimit()) {
                tooltip.add(FluxTranslate.TRANSFER_LIMIT.makeComponent()
                        .append(": " + ChatFormatting.GREEN + FluxTranslate.UNLIMITED)
                );
            } else {
                tooltip.add(FluxTranslate.TRANSFER_LIMIT.makeComponent()
                        .append(": " + ChatFormatting.GREEN + EnergyType.E.getUsage(device.getRawLimit()))
                );
            }

            if (device.getSurgeMode()) {
                tooltip.add(FluxTranslate.PRIORITY.makeComponent()
                        .append(": " + ChatFormatting.GREEN + FluxTranslate.SURGE)
                );
            } else {
                tooltip.add(FluxTranslate.PRIORITY.makeComponent()
                        .append(": " + ChatFormatting.GREEN + device.getRawPriority())
                );
            }

            if (device.isForcedLoading()) {
                tooltip.add(FluxTranslate.FORCED_LOADING.makeComponent()
                        .withStyle(ChatFormatting.GOLD));
            }
        }
    }

    private FluxNetwork getClientNetwork(int networkID) {
        return ClientCache.getNetwork(networkID);
    }

    @Override
    public ResourceLocation getUid() {
        return JadeEntrypoint.ID;
    }
}