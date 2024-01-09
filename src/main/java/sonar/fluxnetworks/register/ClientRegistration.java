package sonar.fluxnetworks.register;

import dev.architectury.registry.client.rendering.ColorHandlerRegistry;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.event.client.player.ClientPickBlockGatherCallback;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.BlockHitResult;
import sonar.fluxnetworks.FluxNetworks;
import sonar.fluxnetworks.api.FluxConstants;
import sonar.fluxnetworks.client.FluxColorHandler;
import sonar.fluxnetworks.client.gui.GuiFluxAdminHome;
import sonar.fluxnetworks.client.gui.GuiFluxDeviceHome;
import sonar.fluxnetworks.client.render.FluxStorageEntityRenderer;
import sonar.fluxnetworks.common.connection.FluxMenu;
import sonar.fluxnetworks.common.device.TileFluxDevice;
import sonar.fluxnetworks.common.integration.MUIIntegration;

import javax.annotation.Nonnull;

@Environment(EnvType.CLIENT)
public class ClientRegistration {
    public static void init() {
        // TODO: maybe move this to another thread again?
        MenuScreens.register(RegistryMenuTypes.FLUX_MENU,
                FluxNetworks.isModernUILoaded() ? MUIIntegration.upgradeScreenFactory(getScreenFactory()) : getScreenFactory()
        );
        registerEntityRenderers();
        registerItemColorHandlers();
        registerBlockColorHandlers();
        ClientPlayNetworking.registerGlobalReceiver(Channel.CHANNEL_NAME, (client, handler, buf, responseSender) ->
                ClientMessages.msg(buf.readShort(), buf, () -> Minecraft.getInstance().player));
        // onPlayerLoggedOut is now handled by MinecraftClientMixin
        // Add device data to the picked block
        ClientPickBlockGatherCallback.EVENT.register((player, result) -> {
            // TODO: fix
            if (result instanceof BlockHitResult blockHit && player.level().getBlockEntity(blockHit.getBlockPos()) instanceof TileFluxDevice fluxDevice) {
                ItemStack stack = fluxDevice.getDisplayStack();
                // Fix colors on storages
                stack.removeTagKey(FluxConstants.FLUX_COLOR);
                // Copy data from the TileEntity
                CompoundTag tag = stack.getOrCreateTagElement(FluxConstants.TAG_FLUX_DATA);
                tag.putInt(FluxConstants.NETWORK_ID, fluxDevice.getNetworkID());
                tag.putString(FluxConstants.CUSTOM_NAME, fluxDevice.getCustomName());
                tag.putLong(FluxConstants.LIMIT, fluxDevice.getRawLimit());
                tag.putLong(FluxConstants.PRIORITY, fluxDevice.getRawPriority());

                return stack;
            }

            return ItemStack.EMPTY;
        });
    }

    @Nonnull
    private static MenuScreens.ScreenConstructor<FluxMenu, AbstractContainerScreen<FluxMenu>> getScreenFactory() {
        return (menu, inventory, title) -> {
            if (menu.mProvider instanceof TileFluxDevice) {
                return new GuiFluxDeviceHome(menu, inventory.player);
            }
            /*if (menu.bridge instanceof ItemFluxConfigurator.MenuBridge) {
                return new GuiFluxConfiguratorHome(menu, inventory.player);
            }*/
            return new GuiFluxAdminHome(menu, inventory.player);
        };
    }

    public static void registerEntityRenderers() {
        BlockEntityRenderers.register(RegistryBlockEntityTypes.BASIC_FLUX_STORAGE, FluxStorageEntityRenderer.PROVIDER);
        BlockEntityRenderers.register(RegistryBlockEntityTypes.HERCULEAN_FLUX_STORAGE, FluxStorageEntityRenderer.PROVIDER);
        BlockEntityRenderers.register(RegistryBlockEntityTypes.GARGANTUAN_FLUX_STORAGE, FluxStorageEntityRenderer.PROVIDER);
    }

    public static void registerItemColorHandlers() {
        ColorHandlerRegistry.registerItemColors(FluxColorHandler.INSTANCE,
                RegistryBlocks.FLUX_CONTROLLER,
                RegistryBlocks.FLUX_POINT,
                RegistryBlocks.FLUX_PLUG);
        ColorHandlerRegistry.registerItemColors(FluxColorHandler::colorMultiplierForConfigurator,
                RegistryItems.FLUX_CONFIGURATOR);
    }

    public static void registerBlockColorHandlers() {
        ColorHandlerRegistry.registerBlockColors(FluxColorHandler.INSTANCE,
                RegistryBlocks.FLUX_CONTROLLER,
                RegistryBlocks.FLUX_POINT,
                RegistryBlocks.FLUX_PLUG,
                RegistryBlocks.BASIC_FLUX_STORAGE,
                RegistryBlocks.HERCULEAN_FLUX_STORAGE,
                RegistryBlocks.GARGANTUAN_FLUX_STORAGE);
    }
}