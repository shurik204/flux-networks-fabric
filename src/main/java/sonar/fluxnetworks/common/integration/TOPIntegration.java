//package sonar.fluxnetworks.common.integration;
//
//import mcjty.theoneprobe.api.*;
//import net.minecraft.ChatFormatting;
//import net.minecraft.nbt.CompoundTag;
//import net.minecraft.network.chat.Component;
//import net.minecraft.resources.ResourceLocation;
//import net.minecraft.world.entity.player.Player;
//import net.minecraft.world.item.ItemStack;
//import net.minecraft.world.level.Level;
//import net.minecraft.world.level.block.state.BlockState;
//import sonar.fluxnetworks.FluxConfig;
//import sonar.fluxnetworks.FluxNetworks;
//import sonar.fluxnetworks.api.FluxConstants;
//import sonar.fluxnetworks.api.FluxTranslate;
//import sonar.fluxnetworks.api.energy.EnergyType;
//import sonar.fluxnetworks.common.device.TileFluxDevice;
//import sonar.fluxnetworks.common.util.FluxUtils;
//
//import javax.annotation.Nonnull;
//
//public class TOPIntegration implements Function<ITheOneProbe, Void> {
//
//    @Override
//    public Void apply(@Nonnull ITheOneProbe iTheOneProbe) {
//        iTheOneProbe.registerProvider(new FluxDeviceInfoProvider());
//        iTheOneProbe.registerBlockDisplayOverride(new FluxDeviceDisplayOverride());
//        return null;
//    }
//
//    public static class FluxDeviceInfoProvider implements IProbeInfoProvider {
//
//        private static final ResourceLocation ID = FluxNetworks.location("device_provider");
//
//        @Override
//        public ResourceLocation getID() {
//            return ID;
//        }
//
//        @Override
//        public void addProbeInfo(ProbeMode probeMode, IProbeInfo probeInfo, Player player, Level level,
//                                 BlockState blockState, IProbeHitData hitData) {
//            if (!(FluxConfig.enableOneProbeBasicInfo || FluxConfig.enableOneProbeAdvancedInfo)) {
//                return;
//            }
//            if (!(level.getBlockEntity(hitData.getPos()) instanceof TileFluxDevice device)) {
//                return;
//            }
//            if (FluxConfig.enableOneProbeBasicInfo) {
//                if (device.getNetwork().isValid()) {
//                    probeInfo.text(Component.literal(device.getNetwork().getNetworkName()).withStyle(ChatFormatting.AQUA));
//                } else {
//                    probeInfo.text(FluxTranslate.ERROR_NO_SELECTED.makeComponent().withStyle(ChatFormatting.AQUA));
//                }
//
//                probeInfo.text(Component.literal(FluxUtils.getTransferInfo(device, EnergyType.FE)));
//
//                if (player.isShiftKeyDown()) {
//                    if (device.getDeviceType().isStorage()) {
//                        probeInfo.text(FluxTranslate.ENERGY_STORED.makeComponent()
//                                .append(": " + ChatFormatting.GREEN + EnergyType.FE.getStorage(device.getTransferBuffer()))
//                        );
//                    } else {
//                        probeInfo.text(FluxTranslate.INTERNAL_BUFFER.makeComponent()
//                                .append(": " + ChatFormatting.GREEN + EnergyType.FE.getStorage(device.getTransferBuffer()))
//                        );
//                    }
//                }/* else {
//                        if (flux.getDeviceType().isStorage()) {
//                            iProbeInfo.text(FluxTranslate.ENERGY_STORED.getTextComponent()
//                                    .appendString(": " + ChatFormatting.GREEN + FluxUtils.format(flux
//                                    .getTransferBuffer(),
//                                            NumberFormatType.COMPACT, EnergyType.FE, false))
//                            );
//                        } else {
//                            iProbeInfo.text(FluxTranslate.INTERNAL_BUFFER.getTextComponent()
//                                    .appendString(": " + ChatFormatting.GREEN + FluxUtils.format(flux
//                                    .getTransferBuffer(),
//                                            NumberFormatType.COMPACT, EnergyType.FE, false))
//                            );
//                        }
//                    }*/
//            }
//            if (FluxConfig.enableOneProbeAdvancedInfo &&
//                    (!FluxConfig.enableOneProbeSneaking || player.isShiftKeyDown())) {
//
//                if (device.getDisableLimit()) {
//                    probeInfo.text(FluxTranslate.TRANSFER_LIMIT.makeComponent()
//                            .append(": " + ChatFormatting.GREEN + FluxTranslate.UNLIMITED)
//                    );
//                } else {
//                    probeInfo.text(FluxTranslate.TRANSFER_LIMIT.makeComponent()
//                            .append(": " + ChatFormatting.GREEN + EnergyType.FE.getUsage(device.getRawLimit()))
//                    );
//                }
//
//                if (device.getSurgeMode()) {
//                    probeInfo.text(FluxTranslate.PRIORITY.makeComponent()
//                            .append(": " + ChatFormatting.GREEN + FluxTranslate.SURGE)
//                    );
//                } else {
//                    probeInfo.text(FluxTranslate.PRIORITY.makeComponent()
//                            .append(": " + ChatFormatting.GREEN + device.getRawPriority())
//                    );
//                }
//
//                if (device.isForcedLoading()) {
//                    probeInfo.text(FluxTranslate.FORCED_LOADING.makeComponent()
//                            .withStyle(ChatFormatting.GOLD));
//                }
//            }
//        }
//    }
//
//    public static class FluxDeviceDisplayOverride implements IBlockDisplayOverride {
//
//        @Override
//        public boolean overrideStandardInfo(ProbeMode probeMode, IProbeInfo probeInfo, Player player,
//                                            @Nonnull Level level, BlockState blockState,
//                                            @Nonnull IProbeHitData hitData) {
//            if (level.getBlockEntity(hitData.getPos()) instanceof TileFluxDevice device) {
//                ItemStack itemStack = device.getDisplayStack();
//                CompoundTag tag = itemStack.getOrCreateTagElement(FluxConstants.TAG_FLUX_DATA);
//                tag.putInt(FluxConstants.NETWORK_ID, device.getNetworkID());
//                tag.putString(FluxConstants.CUSTOM_NAME, device.getCustomName());
//                probeInfo.horizontal().item(itemStack)
//                        .vertical().itemLabel(itemStack)
//                        .text(Component.literal(TextStyleClass.MODNAME + FluxNetworks.NAME));
//                return true;
//            }
//            return false;
//        }
//    }
//}
