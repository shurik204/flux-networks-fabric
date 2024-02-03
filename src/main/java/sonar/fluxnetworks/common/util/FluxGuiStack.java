package sonar.fluxnetworks.common.util;

import net.minecraft.world.item.ItemStack;
import sonar.fluxnetworks.api.FluxConstants;
import sonar.fluxnetworks.register.RegistryBlocks;

public class FluxGuiStack {
    public static final ItemStack FLUX_PLUG;
    public static final ItemStack FLUX_POINT;
    public static final ItemStack FLUX_CONTROLLER;

    public static final ItemStack BASIC_STORAGE;
    public static final ItemStack HERCULEAN_STORAGE;
    public static final ItemStack GARGANTUAN_STORAGE;
    public static final ItemStack BOTTOMLESS_STORAGE;

    static {
        FLUX_PLUG = new ItemStack(RegistryBlocks.FLUX_PLUG);
        FLUX_POINT = new ItemStack(RegistryBlocks.FLUX_POINT);
        FLUX_CONTROLLER = new ItemStack(RegistryBlocks.FLUX_CONTROLLER);
        BASIC_STORAGE = new ItemStack(RegistryBlocks.BASIC_FLUX_STORAGE);
        HERCULEAN_STORAGE = new ItemStack(RegistryBlocks.HERCULEAN_FLUX_STORAGE);
        GARGANTUAN_STORAGE = new ItemStack(RegistryBlocks.GARGANTUAN_FLUX_STORAGE);
        BOTTOMLESS_STORAGE = new ItemStack(RegistryBlocks.BOTTOMLESS_FLUX_STORAGE);

        FLUX_PLUG.getOrCreateTag().putBoolean(FluxConstants.FLUX_COLOR, true);
        FLUX_PLUG.getOrCreateTag().putBoolean(FluxConstants.FLUX_COLOR, true);
        FLUX_CONTROLLER.getOrCreateTag().putBoolean(FluxConstants.FLUX_COLOR, true);
    }
}