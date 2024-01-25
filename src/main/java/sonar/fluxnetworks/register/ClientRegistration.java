package sonar.fluxnetworks.register;

import io.github.fabricators_of_create.porting_lib.event.client.ColorHandlersCallback;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.event.client.player.ClientPickBlockGatherCallback;
import net.minecraft.client.Minecraft;
import net.minecraft.client.color.block.BlockColors;
import net.minecraft.client.color.item.ItemColors;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.BlockHitResult;
import sonar.fluxnetworks.api.FluxConstants;
import sonar.fluxnetworks.client.FluxColorHandler;
import sonar.fluxnetworks.client.gui.GuiFluxAdminHome;
import sonar.fluxnetworks.client.gui.GuiFluxDeviceHome;
import sonar.fluxnetworks.client.render.FluxStorageEntityRenderer;
import sonar.fluxnetworks.common.connection.FluxMenu;
import sonar.fluxnetworks.common.device.TileFluxDevice;

import javax.annotation.Nonnull;

@Environment(EnvType.CLIENT)
public class ClientRegistration {
    public static void init() {
        // TODO: maybe move this to another thread again?
        MenuScreens.register(RegistryMenuTypes.FLUX_MENU,
//                FluxNetworks.isModernUILoaded() ? MUIIntegration.upgradeScreenFactory(getScreenFactory()) : getScreenFactory()
                getScreenFactory()
        );
        registerEntityRenderers();
        ColorHandlersCallback.ITEM.register(ClientRegistration::registerColorHandlers);

        ClientPlayNetworking.registerGlobalReceiver(Channel.CHANNEL_NAME, (client, handler, buf, responseSender) ->
                ClientMessages.msg(buf.readShort(), buf, () -> Minecraft.getInstance().player));
        // onPlayerLoggedOut is now handled by MinecraftClientMixin
        // Add device data to the picked block
        ClientPickBlockGatherCallback.EVENT.register((player, result) -> {
            if (result instanceof BlockHitResult blockHit && player.level.getBlockEntity(blockHit.getBlockPos()) instanceof TileFluxDevice fluxDevice) {
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

    public static void registerColorHandlers(ItemColors itemColors, BlockColors blockColors) {
        // Items
        itemColors.register(FluxColorHandler.INSTANCE,
                RegistryBlocks.FLUX_CONTROLLER,
                RegistryBlocks.FLUX_POINT,
                RegistryBlocks.FLUX_PLUG);
        itemColors.register(FluxColorHandler::colorMultiplierForConfigurator,
                RegistryItems.FLUX_CONFIGURATOR);

        // Blocks
        blockColors.register(FluxColorHandler.INSTANCE,
                RegistryBlocks.FLUX_CONTROLLER,
                RegistryBlocks.FLUX_POINT,
                RegistryBlocks.FLUX_PLUG,
                RegistryBlocks.BASIC_FLUX_STORAGE,
                RegistryBlocks.HERCULEAN_FLUX_STORAGE,
                RegistryBlocks.GARGANTUAN_FLUX_STORAGE);
    }
}