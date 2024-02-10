package sonar.fluxnetworks.client.rei;

import com.google.common.collect.Lists;
import me.shedaniel.math.Point;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.client.gui.Renderer;
import me.shedaniel.rei.api.client.gui.widgets.Widget;
import me.shedaniel.rei.api.client.gui.widgets.Widgets;
import me.shedaniel.rei.api.client.registry.display.DisplayCategory;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.util.EntryStacks;
import net.minecraft.network.chat.Component;
import sonar.fluxnetworks.FluxNetworks;
import sonar.fluxnetworks.api.FluxTranslate;
import sonar.fluxnetworks.register.RegistryItems;

import java.util.List;

public class CreatingFluxRecipeCategory implements DisplayCategory<CreatingFluxRecipeDisplay> {
    public static final CategoryIdentifier<CreatingFluxRecipeDisplay> ID = CategoryIdentifier.of(FluxNetworks.location("creating_flux"));

    @Override
    public CategoryIdentifier<? extends CreatingFluxRecipeDisplay> getCategoryIdentifier() {
        return ID;
    }

    @Override
    public Component getTitle() {
        return FluxTranslate.CREATING_FLUX.getComponent();
    }

    @Override
    public Renderer getIcon() {
        return EntryStacks.of(RegistryItems.FLUX_DUST);
    }

    @Override
    public int getDisplayHeight() {
        return 100;
    }

    @Override
    public int getDisplayWidth(CreatingFluxRecipeDisplay display) {
        return 184;
    }

    @Override
    public List<Widget> setupDisplay(CreatingFluxRecipeDisplay display, Rectangle bounds) {
        List<Widget> widgets = Lists.newArrayList();
        widgets.add(Widgets.createRecipeBase(bounds));
        // Input on the left (Redstone)
        widgets.add(Widgets.createSlot(new Rectangle(bounds.getCenterX() - 78, bounds.getCenterY() - 9, 18, 18)).entries(display.getInputIngredient()).markInput());
        // Output on the right (Flux Dust)
        widgets.add(Widgets.createSlot(new Rectangle(bounds.getCenterX() + 60, bounds.getCenterY() - 9, 18, 18)).entries(display.getOutputIngredient()).markOutput());

        widgets.add(Widgets.createArrow(new Point(bounds.getCenterX() - 45, bounds.getCenterY() - 9)));
        widgets.add(Widgets.createArrow(new Point(bounds.getCenterX() + 45 - 24, bounds.getCenterY() - 9)));

        widgets.add(Widgets.wrapRenderer(new Rectangle(bounds.getCenterX() - 25, bounds.getY() + 10, 100, 100), new CreatingFluxRecipeAnimation(display)));
        return widgets;
    }
}