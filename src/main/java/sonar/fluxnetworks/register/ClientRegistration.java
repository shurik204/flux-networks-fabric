package sonar.fluxnetworks.register;

import io.github.fabricators_of_create.porting_lib.event.client.ColorHandlersCallback;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.event.client.player.ClientPickBlockGatherCallback;
import net.minecraft.client.Minecraft;
import net.minecraft.client.color.block.BlockColors;
import net.minecraft.client.color.item.ItemColors;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.RenderType;
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

import static sonar.fluxnetworks.register.RegistryBlocks.*;

@Environment(EnvType.CLIENT)
public class ClientRegistration {
    public static void init() {
        // TODO: maybe move this to another thread again?
        MenuScreens.register(RegistryMenuTypes.FLUX_MENU,
                FluxNetworks.isModernUILoaded() ? MUIIntegration.upgradeScreenFactory(getScreenFactory()) : getScreenFactory()
        );
        registerEntityRenderers();
        ColorHandlersCallback.ITEM.register(ClientRegistration::registerColorHandlers);
        BlockRenderLayerMap.INSTANCE.putBlocks(RenderType.cutout(), FLUX_PLUG,
                FLUX_POINT,
                FLUX_CONTROLLER,
                BASIC_FLUX_STORAGE,
                HERCULEAN_FLUX_STORAGE,
                GARGANTUAN_FLUX_STORAGE,
                BOTTOMLESS_FLUX_STORAGE);

        ClientPlayNetworking.registerGlobalReceiver(Channel.CHANNEL_NAME, (client, handler, buf, responseSender) ->
                ClientMessages.msg(buf.readShort(), buf, () -> Minecraft.getInstance().player));
        // onPlayerLoggedOut is now handled by MinecraftClientMixin
        // Add device data to the picked block
        ClientPickBlockGatherCallback.EVENT.register((player, result) -> {
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
                // Do not store the color
                tag.remove(FluxConstants.CLIENT_COLOR);

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
        BlockEntityRenderers.register(RegistryBlockEntityTypes.BOTTOMLESS_FLUX_STORAGE, FluxStorageEntityRenderer.PROVIDER);
    }

    public static void registerColorHandlers(ItemColors itemColors, BlockColors blockColors) {
        // Items
        itemColors.register(FluxColorHandler.INSTANCE, FLUX_CONTROLLER, FLUX_POINT, FLUX_PLUG,
                BASIC_FLUX_STORAGE, HERCULEAN_FLUX_STORAGE, GARGANTUAN_FLUX_STORAGE, BOTTOMLESS_FLUX_STORAGE);
        itemColors.register(FluxColorHandler::colorMultiplierForConfigurator,
                RegistryItems.FLUX_CONFIGURATOR);

        // Blocks
        blockColors.register(FluxColorHandler.INSTANCE, FLUX_CONTROLLER, FLUX_POINT, FLUX_PLUG,
                BASIC_FLUX_STORAGE, HERCULEAN_FLUX_STORAGE, GARGANTUAN_FLUX_STORAGE, BOTTOMLESS_FLUX_STORAGE);
    }
}