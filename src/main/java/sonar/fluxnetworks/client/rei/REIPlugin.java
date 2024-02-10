package sonar.fluxnetworks.client.rei;

import me.shedaniel.rei.api.client.plugins.REIClientPlugin;
import me.shedaniel.rei.api.client.registry.category.CategoryRegistry;
import me.shedaniel.rei.api.client.registry.display.DisplayRegistry;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import sonar.fluxnetworks.register.RegistryBlocks;
import sonar.fluxnetworks.register.RegistryItems;

public class REIPlugin implements REIClientPlugin {
    @Override
    public void registerCategories(CategoryRegistry registry) {
        registry.add(new CreatingFluxRecipeCategory());

        registry.addWorkstations(CreatingFluxRecipeCategory.ID, EntryIngredients.of(RegistryItems.FLUX_DUST));
    }

    @Override
    public void registerDisplays(DisplayRegistry registry) {
        registry.add(new CreatingFluxRecipeDisplay(Blocks.BEDROCK, Blocks.OBSIDIAN, Items.REDSTONE.getDefaultInstance(), RegistryItems.FLUX_DUST.getDefaultInstance()));
        registry.add(new CreatingFluxRecipeDisplay(RegistryBlocks.FLUX_BLOCK, Blocks.OBSIDIAN, Items.REDSTONE.getDefaultInstance(), RegistryItems.FLUX_DUST.getDefaultInstance()));
    }
}