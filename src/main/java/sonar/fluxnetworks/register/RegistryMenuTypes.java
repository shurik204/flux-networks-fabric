package sonar.fluxnetworks.register;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import sonar.fluxnetworks.FluxNetworks;
import sonar.fluxnetworks.common.connection.FluxMenu;

/**
 * ContainerType has the function to create container on client side<br>
 * Register the create container function that will be opened on client side from the packet that from the server
 */
public class RegistryMenuTypes {
    public static final ResourceLocation FLUX_MENU_KEY = FluxNetworks.location("flux_menu");
    public static final MenuType<FluxMenu> FLUX_MENU = register(FLUX_MENU_KEY, FluxMenu::new);

    private static <T extends AbstractContainerMenu> MenuType<T> register(ResourceLocation id, MenuType.MenuSupplier<T> factory) {
        return Registry.register(BuiltInRegistries.MENU, id, new MenuType<>(factory, FeatureFlags.VANILLA_SET));
    }

    // TODO: probably has to be reimplemented
//    static void register(RegisterEvent.RegisterHelper<MenuType<?>> helper) {
//        helper.register(FLUX_MENU_KEY, IForgeMenuType.create((containerId, inventory, buffer) -> {
//            // check if it's tile entity
//            if (buffer.readBoolean()) {
//                BlockPos pos = buffer.readBlockPos();
//                if (inventory.player.level().getBlockEntity(pos) instanceof TileFluxDevice device) {
//                    CompoundTag tag = buffer.readNbt();
//                    if (tag != null) {
//                        device.readCustomTag(tag, FluxConstants.NBT_TILE_UPDATE);
//                    }
//                    return new FluxMenu(containerId, inventory, device);
//                }
//            } else {
//                ItemStack stack = inventory.player.getMainHandItem();
//                if (stack.getItem() == RegistryItems.FLUX_CONFIGURATOR.get()) {
//                    return new FluxMenu(containerId, inventory, new ItemFluxConfigurator.Provider(stack));
//                }
//            }
//            return new FluxMenu(containerId, inventory, new ItemAdminConfigurator.Provider());
//        }));
//    }

    public static void init() {}
}
