package sonar.fluxnetworks.common.crafting;

import com.mojang.serialization.Codec;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapelessRecipe;
import sonar.fluxnetworks.FluxNetworks;

import javax.annotation.Nonnull;

public class NBTWipeRecipeSerializer implements RecipeSerializer<ShapelessRecipe> {

    public static final NBTWipeRecipeSerializer INSTANCE = new NBTWipeRecipeSerializer();

    private NBTWipeRecipeSerializer() {}

    @Override
    public Codec<ShapelessRecipe> codec() {
        return RecipeSerializer.SHAPELESS_RECIPE.codec();
    }

    @Override
    public ShapelessRecipe fromNetwork(FriendlyByteBuf buffer) {
        return RecipeSerializer.SHAPELESS_RECIPE.fromNetwork(buffer);
    }

    @Override
    public void toNetwork(@Nonnull FriendlyByteBuf buffer, @Nonnull ShapelessRecipe recipe) {
        try {
            RecipeSerializer.SHAPELESS_RECIPE.toNetwork(buffer, recipe);
        } catch (Exception e) {
            FluxNetworks.LOGGER.error("Error writing NBT Wipe Recipe to packet.", e);
        }
    }
}
