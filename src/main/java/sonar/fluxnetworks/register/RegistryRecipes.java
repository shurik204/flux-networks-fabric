package sonar.fluxnetworks.register;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import sonar.fluxnetworks.FluxNetworks;
import sonar.fluxnetworks.common.crafting.FluxStorageRecipe;
import sonar.fluxnetworks.common.crafting.FluxStorageRecipeSerializer;
import sonar.fluxnetworks.common.crafting.NBTWipeRecipe;
import sonar.fluxnetworks.common.crafting.NBTWipeRecipeSerializer;

public class RegistryRecipes {
    public static final ResourceLocation FLUX_STORAGE_RECIPE_KEY = FluxNetworks.location("flux_storage_recipe");
    public static final ResourceLocation NBT_WIPE_RECIPE_KEY = FluxNetworks.location("nbt_wipe_recipe");

    public static final RecipeSerializer<FluxStorageRecipe> FLUX_STORAGE_RECIPE = register(FLUX_STORAGE_RECIPE_KEY, FluxStorageRecipeSerializer.INSTANCE);
    public static final RecipeSerializer<NBTWipeRecipe> NBT_WIPE_RECIPE = register(NBT_WIPE_RECIPE_KEY, NBTWipeRecipeSerializer.INSTANCE);

    private static <S extends RecipeSerializer<T>, T extends Recipe<?>> S register(ResourceLocation id, S recipeSerializer) {
        return Registry.register(BuiltInRegistries.RECIPE_SERIALIZER, id, recipeSerializer);
    }

    public static void init() {}
}
