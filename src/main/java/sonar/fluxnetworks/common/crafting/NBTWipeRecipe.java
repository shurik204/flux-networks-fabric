package sonar.fluxnetworks.common.crafting;

import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapelessRecipe;
import net.minecraft.world.level.block.Block;
import sonar.fluxnetworks.api.FluxConstants;
import sonar.fluxnetworks.common.block.FluxStorageBlock;

/**
 * Save Flux Storage energy when wiping NBT
 */
public class NBTWipeRecipe extends ShapelessRecipe {
    public NBTWipeRecipe(String group, CraftingBookCategory category, ItemStack result, NonNullList<Ingredient> ingredients) {
        super(group, category, result, ingredients);
    }

    public NBTWipeRecipe(ShapelessRecipe recipe) {
        super(recipe.getGroup(), recipe.category(), recipe.getResultItem(RegistryAccess.EMPTY), recipe.getIngredients());
    }

    @Override
    public ItemStack assemble(CraftingContainer container, RegistryAccess registryAccess) {
        ItemStack originalStack = null;

        for (int i = 0; i < container.getContainerSize(); i++) {
            ItemStack stack = container.getItem(i);
            if (!stack.isEmpty()) {
                originalStack = stack;
                break;
            }
        }
        if (originalStack != null) {
            ItemStack output = getResultItem(registryAccess).copy();
            if (Block.byItem(output.getItem()) instanceof FluxStorageBlock) {
                CompoundTag subTag = originalStack.getTagElement(FluxConstants.TAG_FLUX_DATA);
                long energy = 0;
                if (subTag != null) {
                    energy = subTag.getLong(FluxConstants.ENERGY);
                }
                if (energy != 0) {
                    CompoundTag newTag = output.getOrCreateTagElement(FluxConstants.TAG_FLUX_DATA);
                    newTag.putLong(FluxConstants.ENERGY, energy);
                }
            }
            return output;
        }
        return super.assemble(container, registryAccess);
    }
    @Override
    public RecipeSerializer<?> getSerializer() {
        return NBTWipeRecipeSerializer.INSTANCE;
    }
}