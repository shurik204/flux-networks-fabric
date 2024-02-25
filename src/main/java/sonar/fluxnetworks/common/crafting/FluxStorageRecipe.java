package sonar.fluxnetworks.common.crafting;

import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapedRecipe;
import sonar.fluxnetworks.api.FluxConstants;

import javax.annotation.Nonnull;

/**
 * Save Flux Storage energy when upgrading storage
 */
public class FluxStorageRecipe extends ShapedRecipe {

    public FluxStorageRecipe(String group, CraftingBookCategory category, int recipeWidthIn, int recipeHeightIn,
                             NonNullList<Ingredient> recipeItemsIn, ItemStack recipeOutputIn) {
        super(group, category,recipeWidthIn, recipeHeightIn, recipeItemsIn, recipeOutputIn);
    }

    public FluxStorageRecipe(@Nonnull ShapedRecipe recipe) {
        super(recipe.getGroup(), recipe.category(), recipe.getWidth(), recipe.getHeight(),
                recipe.getIngredients(), recipe.getResultItem(RegistryAccess.EMPTY));
    }

    @Nonnull
    @Override
    public ItemStack assemble(@Nonnull CraftingContainer container, @Nonnull RegistryAccess registryAccess) {
        long totalEnergy = 0;
        int networkID = -1;
        for (int i = 0; i < container.getContainerSize(); i++) {
            ItemStack stack = container.getItem(i);
            CompoundTag subTag = stack.getTagElement(FluxConstants.TAG_FLUX_DATA);
            if (subTag != null) {
                if (networkID == -1) {
                    networkID = subTag.getInt(FluxConstants.NETWORK_ID);
                }
                totalEnergy += subTag.getLong(FluxConstants.ENERGY);
            }
        }
        ItemStack stack = getResultItem(registryAccess).copy();
        if (totalEnergy > 0 || networkID != -1) {
            CompoundTag subTag = stack.getOrCreateTagElement(FluxConstants.TAG_FLUX_DATA);
            if (networkID != -1)
                subTag.putInt(FluxConstants.NETWORK_ID, networkID);
            if (totalEnergy > 0)
                subTag.putLong(FluxConstants.ENERGY, totalEnergy);
        }
        return stack;
    }

    @Nonnull
    public RecipeSerializer<?> getSerializer() {
        return FluxStorageRecipeSerializer.INSTANCE;
    }
}
