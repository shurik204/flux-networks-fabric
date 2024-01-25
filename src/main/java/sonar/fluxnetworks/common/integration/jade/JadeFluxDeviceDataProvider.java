package sonar.fluxnetworks.common.integration.jade;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.IServerDataProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;
import sonar.fluxnetworks.FluxConfig;
import sonar.fluxnetworks.FluxNetworks;
import sonar.fluxnetworks.api.FluxTranslate;
import sonar.fluxnetworks.api.energy.EnergyType;
import sonar.fluxnetworks.common.connection.FluxNetwork;
import sonar.fluxnetworks.common.device.TileFluxDevice;
import sonar.fluxnetworks.common.util.FluxUtils;

import javax.annotation.Nonnull;
import java.util.Optional;

public enum JadeFluxDeviceDataProvider implements IBlockComponentProvider, IServerDataProvider<BlockEntity> {
    INSTANCE;

    public static final String KEY_JADE_ENERGY_STORAGE = "JadeEnergyStorage";
    public static final String KEY_JADE_ENERGY_STORAGE_UID = "JadeEnergyStorageUid";
    public static final String KEY_BASIC_DATA = FluxNetworks.NAME_CPT + "Basic";
    public static final String KEY_NETWORK_ID = "n_id";
    public static final String KEY_NETWORK_NAME = "n_n";
    public static final String KEY_ENERGY_CHANGE = "e_c";
    public static final String KEY_ENERGY_STORED = "e_s";
    public static final String KEY_ADVANCED_DATA = FluxNetworks.NAME_CPT + "Advanced";
    public static final String KEY_TRANSFER_LIMIT_DISABLED = "t_l_d";
    public static final String KEY_TRANSFER_LIMIT = "t_l";
    public static final String KEY_SURGE_MODE = "s_m";
    public static final String KEY_PRIORITY = "pr";
    public static final String KEY_FORCED_LOADING = "f_l";

    @Override
    @Environment(EnvType.CLIENT)
    public void appendTooltip(ITooltip tooltip, BlockAccessor accessor, IPluginConfig config) {
        if (!(FluxConfig.enableJadeBasicInfo || FluxConfig.enableJadeAdvancedInfo)) {
            return;
        }
        if (!(accessor.getBlockEntity() instanceof TileFluxDevice device)) {
            return;
        }

        CompoundTag data = accessor.getServerData();
        if (!device.getDeviceType().isStorage()) {
            data.remove(KEY_JADE_ENERGY_STORAGE);
            data.remove(KEY_JADE_ENERGY_STORAGE_UID);
        }


        if (FluxConfig.enableJadeBasicInfo && data.contains(KEY_BASIC_DATA)) {
            CompoundTag bData = data.getCompound(KEY_BASIC_DATA);
            // Network
            if (bData.getInt(KEY_NETWORK_ID) != FluxNetwork.INVALID.getNetworkID()) {
                tooltip.add(Component.literal(bData.getString(KEY_NETWORK_NAME)).withStyle(ChatFormatting.AQUA));
                // Energy change
                tooltip.add(Component.literal(FluxUtils.getTransferInfo(device.getDeviceType(), bData.getLong(KEY_ENERGY_CHANGE), EnergyType.E)));
            } else {
                tooltip.add(FluxTranslate.ERROR_NO_SELECTED.makeComponent().withStyle(ChatFormatting.AQUA));
            }

            // Energy storage
            if (!device.getDeviceType().isStorage()) {
                tooltip.add(FluxTranslate.INTERNAL_BUFFER.makeComponent().append(": " + getFormattedEnergy(ChatFormatting.GREEN, EnergyType.E, bData, KEY_ENERGY_STORED)));
            }
        }

        if (FluxConfig.enableJadeAdvancedInfo && data.contains(KEY_ADVANCED_DATA) && (!FluxConfig.enableJadeSneaking || accessor.showDetails())) {
            CompoundTag bData = data.getCompound(KEY_ADVANCED_DATA);
            // Energy storage
            long transferLimit = bData.getLong(KEY_TRANSFER_LIMIT);
            tooltip.add(FluxTranslate.TRANSFER_LIMIT.makeComponent().append(": " + ChatFormatting.GREEN + (transferLimit == Long.MAX_VALUE ? FluxTranslate.UNLIMITED : EnergyType.E.getUsage(transferLimit))));

            // Priority
            tooltip.add(FluxTranslate.PRIORITY.makeComponent().append(": " + ChatFormatting.GREEN + (bData.getBoolean(KEY_SURGE_MODE) ? FluxTranslate.SURGE : device.getRawPriority())));

            // Forced loading
            if (device.isForcedLoading()) {
                tooltip.add(FluxTranslate.FORCED_LOADING.makeComponent().withStyle(ChatFormatting.GOLD));
            }
        }
    }

    @Override
    public ResourceLocation getUid() {
        return JadeEntrypoint.DEVICE_DATA_PROVIDER_ID;
    }

    @Override
    public void appendServerData(CompoundTag data, ServerPlayer player, Level level, BlockEntity blockEntity, boolean b) {
        if (blockEntity instanceof TileFluxDevice device) {
            if (FluxConfig.enableJadeBasicInfo) {
                data.put(KEY_BASIC_DATA, createBasicData(device));
            }
            if (FluxConfig.enableJadeAdvancedInfo) {
                data.put(KEY_ADVANCED_DATA, createAdvancedData(device));
            }
        }
    }

    @Nonnull
    private static CompoundTag createBasicData(TileFluxDevice device) {
        CompoundTag data = new CompoundTag();
        // Network id (not visible), name, energy change, energy stored
        // Network id
        data.putInt(KEY_NETWORK_ID, device.getNetworkID());
        // Network info (if device is connected)
        if (device.getNetwork().isValid()) {
            data.putString(KEY_NETWORK_NAME, device.getNetwork().getNetworkName());
            data.putLong(KEY_ENERGY_CHANGE, device.getTransferChange());
        }
        // Energy change
        data.putLong(KEY_ENERGY_STORED, device.getTransferBuffer());
        return data;
    }

    @Nonnull
    private static CompoundTag createAdvancedData(TileFluxDevice device) {
        CompoundTag data = new CompoundTag();
        // Bypass limit status, transfer limit, surge mode, priority, is forced loading.
        // Transfer limit
        // (bypass status)
        data.putBoolean(KEY_TRANSFER_LIMIT_DISABLED, device.getDisableLimit());
        // (value)
        data.putLong(KEY_TRANSFER_LIMIT, device.getTransferHandler().getRawLimit());
        // Surge mode
        data.putBoolean(KEY_SURGE_MODE, device.getSurgeMode());
        // Add priority info
        data.putInt(KEY_PRIORITY, device.getRawPriority());
        // Add forced loading info
        data.putBoolean(KEY_FORCED_LOADING, device.isForcedLoading());
        return data;
    }

    public static Optional<Long> getLong(CompoundTag data, String key) {
        if (data.contains(key)) {
            return Optional.of(data.getLong(key));
        }
        return Optional.empty();
    }

    @Nonnull
    public static String getFormattedEnergy(ChatFormatting formatting, EnergyType type, CompoundTag data, String key) {
        return getLong(data, key).map(energy -> formatting + type.getStorage(energy)).orElse( ChatFormatting.RED + "ERROR");
    }
}