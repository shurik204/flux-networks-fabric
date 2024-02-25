package sonar.fluxnetworks.common.crafting;

import com.mojang.serialization.Codec;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapedRecipe;
import sonar.fluxnetworks.FluxNetworks;

public class FluxStorageRecipeSerializer implements RecipeSerializer<ShapedRecipe> {

    public static final FluxStorageRecipeSerializer INSTANCE = new FluxStorageRecipeSerializer();

    private FluxStorageRecipeSerializer() {
    }

    @Override
    public Codec<ShapedRecipe> codec() {
        return RecipeSerializer.SHAPED_RECIPE.codec();
    }

    @Override
    public ShapedRecipe fromNetwork(FriendlyByteBuf buffer) {
        try {
            return RecipeSerializer.SHAPED_RECIPE.fromNetwork(buffer);
        } catch (Exception e) {
            throw new RuntimeException("Error reading Flux Storage recipe from packet.", e);
        }
    }

    @Override
    public void toNetwork(FriendlyByteBuf buffer, ShapedRecipe recipe) {
        try {
            RecipeSerializer.SHAPED_RECIPE.toNetwork(buffer, recipe);
        } catch (Exception e) {
            FluxNetworks.LOGGER.error("Error writing Flux Storage recipe to packet.", e);
        }
    }
}
