package sonar.fluxnetworks.client.rei;

import com.google.common.collect.Lists;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.Display;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;

import java.util.Collections;
import java.util.List;

public record CreatingFluxRecipeDisplay(Block base, Block crusher, ItemStack input, ItemStack output) implements Display {
    @Override
    public List<EntryIngredient> getInputEntries() {
        return Lists.newArrayList(EntryIngredients.of(base), EntryIngredients.of(crusher), EntryIngredients.of(input));
    }

    @Override
    public List<EntryIngredient> getOutputEntries() {
        return Collections.singletonList(EntryIngredients.of(output));
    }

    @Override
    public CategoryIdentifier<?> getCategoryIdentifier() {
        return CreatingFluxRecipeCategory.ID;
    }

    public EntryIngredient getInputIngredient() {
        return EntryIngredients.of(input);
    }

    public EntryIngredient getOutputIngredient() {
        return EntryIngredients.of(output);
    }
}