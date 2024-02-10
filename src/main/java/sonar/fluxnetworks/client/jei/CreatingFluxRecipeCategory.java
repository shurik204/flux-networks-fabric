package sonar.fluxnetworks.client.jei;

import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.ITickTimer;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import sonar.fluxnetworks.FluxNetworks;
import sonar.fluxnetworks.api.FluxTranslate;
import sonar.fluxnetworks.client.render.FluxCreationAnimationRenderer;
import sonar.fluxnetworks.register.RegistryBlocks;
import sonar.fluxnetworks.register.RegistryItems;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CreatingFluxRecipeCategory implements IRecipeCategory<CreatingFluxRecipe> {

    public static final ResourceLocation TEXTURES = FluxNetworks.location(
            "textures/gui/gui_creating_flux_recipe.png");

    public static final RecipeType<CreatingFluxRecipe> RECIPE_TYPE =
            RecipeType.create(FluxNetworks.MODID, "creating_flux", CreatingFluxRecipe.class);

    private final IDrawable background;
    private final IDrawable icon;
    private final ITickTimer timer;

    public CreatingFluxRecipeCategory(@Nonnull IGuiHelper guiHelper) {
        this.background = guiHelper.createDrawable(TEXTURES, 0, -20, 128, 80);
        this.icon = guiHelper.createDrawableIngredient(VanillaTypes.ITEM_STACK,
                new ItemStack(RegistryItems.FLUX_DUST));
        this.timer = guiHelper.createTickTimer(60, 320, false);
    }

    @Nonnull
    public static List<CreatingFluxRecipe> getRecipes() {
        List<CreatingFluxRecipe> recipes = new ArrayList<>();
        recipes.add(new CreatingFluxRecipe(Blocks.BEDROCK, Blocks.OBSIDIAN,
                new ItemStack(Items.REDSTONE), new ItemStack(RegistryItems.FLUX_DUST)));
        recipes.add(new CreatingFluxRecipe(RegistryBlocks.FLUX_BLOCK, Blocks.OBSIDIAN,
                new ItemStack(Items.REDSTONE), new ItemStack(RegistryItems.FLUX_DUST)));
        return recipes;
    }

    @Nonnull
    public static List<ItemStack> getCatalysts() {
        return List.of(new ItemStack(RegistryItems.FLUX_DUST));
    }

    @Nonnull
    @Override
    public RecipeType<CreatingFluxRecipe> getRecipeType() {
        return RECIPE_TYPE;
    }

    @Nonnull
    @Override
    public Component getTitle() {
        return FluxTranslate.CREATING_FLUX.getComponent();
    }

    @Nonnull
    @Override
    public IDrawable getBackground() {
        return background;
    }

    @Nonnull
    @Override
    public IDrawable getIcon() {
        return icon;
    }

    @Override
    public void setRecipe(@Nonnull IRecipeLayoutBuilder builder, @Nonnull CreatingFluxRecipe recipe,
                          @Nonnull IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 8 + 1, 24 + 1)
                .addItemStack(recipe.input());
        builder.addSlot(RecipeIngredientRole.OUTPUT, 102 + 1, 24 + 1)
                .addItemStack(recipe.output());
    }

    @Nonnull
    @Override
    public List<Component> getTooltipStrings(@Nonnull CreatingFluxRecipe recipe,
                                             @Nonnull IRecipeSlotsView recipeSlotsView, double mouseX, double mouseY) {
        if (mouseX >= 40 && mouseX < 80 && mouseY >= 10 && mouseY < 64) {
            return List.of(
                    Component.literal("Y+2 = ").append(recipe.crusher().getName()),
                    Component.literal("Y+1 = ").append(recipe.input().getHoverName()),
                    Component.literal("Y+0 = ").append(recipe.base().getName())
            );
        }
        return Collections.emptyList();
    }

    @Override
    public void draw(@Nonnull CreatingFluxRecipe recipe, @Nonnull IRecipeSlotsView recipeSlotsView,
                     @Nonnull GuiGraphics guiGraphics, double mouseX, double mouseY) {
        FluxCreationAnimationRenderer.render(guiGraphics, recipe.base(), recipe.crusher(), recipe.input(), recipe.output(),
                0xff404040, timer.getValue(), timer.getMaxValue());
    }
}
