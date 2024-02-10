package sonar.fluxnetworks.client.rei;

import me.shedaniel.math.Color;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.client.REIRuntime;
import me.shedaniel.rei.api.client.gui.DisplayRenderer;
import me.shedaniel.rei.api.client.gui.widgets.Tooltip;
import me.shedaniel.rei.api.client.gui.widgets.TooltipContext;
import net.minecraft.client.Timer;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import sonar.fluxnetworks.client.render.FluxCreationAnimationRenderer;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;


@ParametersAreNonnullByDefault
public class CreatingFluxRecipeAnimation extends DisplayRenderer {
    private static final Timer timer = new Timer(1F, 0L);
    private final CreatingFluxRecipeDisplay recipe;

    public CreatingFluxRecipeAnimation(CreatingFluxRecipeDisplay recipe) {
        this.recipe = recipe;
    }

    @Override
    public int getHeight() {
        return 100;
    }

    @Override
    @Nullable
    public Tooltip getTooltip(TooltipContext mouse) {
        return Tooltip.create(List.of(
                Component.literal("Y+2 = ").append(recipe.crusher().getName()),
                Component.literal("Y+1 = ").append(Component.translatable(recipe.input().getDescriptionId())),
                Component.literal("Y+0 = ").append(recipe.base().getName())
        ));
    }

    @Override
    public void render(GuiGraphics graphics, Rectangle bounds, int mouseX, int mouseY, float delta) {
        graphics.pose().pushPose();
        graphics.pose().translate(bounds.x - 39, bounds.y, 0);

        timer.advanceTime(System.currentTimeMillis());

        int textColor = Color.ofTransparent(REIRuntime.getInstance().isDarkThemeEnabled() ? 0xFFBBBBBB : -1).getColor();

        FluxCreationAnimationRenderer.render(graphics, recipe.base(), recipe.crusher(), recipe.input(), recipe.output(), textColor,
                Math.round(timer.partialTick * 320F), 320);

        graphics.pose().popPose();
    }
}