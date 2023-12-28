package sonar.fluxnetworks.register;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import sonar.fluxnetworks.FluxNetworks;
import sonar.fluxnetworks.common.block.FluxControllerBlock;
import sonar.fluxnetworks.common.block.FluxPlugBlock;
import sonar.fluxnetworks.common.block.FluxPointBlock;
import sonar.fluxnetworks.common.block.FluxStorageBlock;

public class RegistryBlocks {
    public static final ResourceLocation FLUX_BLOCK_KEY = FluxNetworks.location("flux_block");
    public static final ResourceLocation FLUX_PLUG_KEY = FluxNetworks.location("flux_plug");
    public static final ResourceLocation FLUX_POINT_KEY = FluxNetworks.location("flux_point");
    public static final ResourceLocation FLUX_CONTROLLER_KEY = FluxNetworks.location("flux_controller");
    public static final ResourceLocation BASIC_FLUX_STORAGE_KEY = FluxNetworks.location("basic_flux_storage");
    public static final ResourceLocation HERCULEAN_FLUX_STORAGE_KEY = FluxNetworks.location("herculean_flux_storage");
    public static final ResourceLocation GARGANTUAN_FLUX_STORAGE_KEY = FluxNetworks.location("gargantuan_flux_storage");

    private static final BlockBehaviour.Properties BLOCK_PROPS = BlockBehaviour.Properties.of().mapColor(MapColor.METAL).sound(SoundType.METAL).strength(1.0F, 1000F);
    private static final BlockBehaviour.Properties DEVICE_PROPS = BlockBehaviour.Properties.of().mapColor(MapColor.METAL).sound(SoundType.METAL).strength(1.0F, 1000F).noOcclusion();

    public static final Block FLUX_BLOCK = register(FLUX_BLOCK_KEY, new Block(BLOCK_PROPS));
    public static final Block FLUX_PLUG = register(FLUX_PLUG_KEY, new FluxPlugBlock(DEVICE_PROPS));
    public static final Block FLUX_POINT = register(FLUX_POINT_KEY, new FluxPointBlock(DEVICE_PROPS));
    public static final Block FLUX_CONTROLLER = register(FLUX_CONTROLLER_KEY, new FluxControllerBlock(DEVICE_PROPS));
    public static final Block BASIC_FLUX_STORAGE = register(BASIC_FLUX_STORAGE_KEY, new FluxStorageBlock.Basic(DEVICE_PROPS));
    public static final Block HERCULEAN_FLUX_STORAGE = register(HERCULEAN_FLUX_STORAGE_KEY, new FluxStorageBlock.Herculean(DEVICE_PROPS));
    public static final Block GARGANTUAN_FLUX_STORAGE = register(GARGANTUAN_FLUX_STORAGE_KEY, new FluxStorageBlock.Gargantuan(DEVICE_PROPS));

    private static Block register(ResourceLocation key, Block block) {
        return Registry.register(BuiltInRegistries.BLOCK, key, block);
    }

    public static void init() {}
}
