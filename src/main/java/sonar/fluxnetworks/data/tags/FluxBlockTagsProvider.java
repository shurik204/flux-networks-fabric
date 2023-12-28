package sonar.fluxnetworks.data.tags;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Block;
import sonar.fluxnetworks.register.RegistryBlocks;

import javax.annotation.Nonnull;
import java.util.concurrent.CompletableFuture;

public class FluxBlockTagsProvider extends FabricTagProvider.BlockTagProvider {

    public FluxBlockTagsProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void addTags(@Nonnull HolderLookup.Provider provider) {
        tag(BlockTags.MINEABLE_WITH_PICKAXE)
                .add(key(RegistryBlocks.FLUX_BLOCK))
                .add(key(RegistryBlocks.FLUX_PLUG))
                .add(key(RegistryBlocks.FLUX_POINT))
                .add(key(RegistryBlocks.FLUX_CONTROLLER))
                .add(key(RegistryBlocks.BASIC_FLUX_STORAGE))
                .add(key(RegistryBlocks.HERCULEAN_FLUX_STORAGE))
                .add(key(RegistryBlocks.GARGANTUAN_FLUX_STORAGE));
    }

    private static ResourceKey<Block> key(Block block) {
        return BuiltInRegistries.BLOCK.getResourceKey(block).orElseThrow();
    }
}
