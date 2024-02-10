package sonar.fluxnetworks.client.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.Mth;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import org.joml.Quaternionf;
import sonar.fluxnetworks.api.FluxTranslate;

public class FluxCreationAnimationRenderer {
    public static void render(GuiGraphics guiGraphics, Block recipeBase, Block recipeCrusher, ItemStack recipeInput, ItemStack recipeOutput, int textColor, int timerValue, int timerMaxValue) {
        MultiBufferSource.BufferSource bufferSource = Minecraft.getInstance().renderBuffers().bufferSource();

        Quaternionf rotationQ = new Quaternionf();
        rotationQ.rotationXYZ(30 * Mth.DEG_TO_RAD, 45 * Mth.DEG_TO_RAD, 0);

        double offset = (timerValue > 160 ? 160 - (timerValue - 160) : timerValue) / 10F;

        //// CRUSHER
        guiGraphics.pose().pushPose();
        guiGraphics.pose().translate(52, 10 + offset, 128);
        guiGraphics.pose().scale(16, 16, 16);
        guiGraphics.pose().mulPose(rotationQ);
        Minecraft.getInstance().getBlockRenderer().renderSingleBlock(recipeCrusher.defaultBlockState(), guiGraphics.pose(), bufferSource,
                LightTexture.FULL_BRIGHT, OverlayTexture.NO_OVERLAY);
        guiGraphics.pose().popPose();

        //// BASE
        guiGraphics.pose().pushPose();
        guiGraphics.pose().translate(52, 40, 128 - 32);
        guiGraphics.pose().scale(16, 16, 16);
        guiGraphics.pose().mulPose(rotationQ);
        Minecraft.getInstance().getBlockRenderer().renderSingleBlock(recipeBase.defaultBlockState(), guiGraphics.pose(), bufferSource,
                LightTexture.FULL_BRIGHT, OverlayTexture.NO_OVERLAY);
        guiGraphics.pose().popPose();

        //// ITEM
        guiGraphics.pose().pushPose();
        guiGraphics.pose().translate(63, 36, 128 - 16);
        guiGraphics.pose().scale(16, -16, 16);
        ItemStack toDisplay = timerValue > 160 ? recipeOutput : recipeInput;
        guiGraphics.pose().mulPose(rotationQ.rotationXYZ(toDisplay.getItem() instanceof BlockItem ? 30 * Mth.DEG_TO_RAD : 0,
                (-90 + 180 * ((float) timerValue / timerMaxValue)) * Mth.DEG_TO_RAD, 0));
        Minecraft.getInstance().getItemRenderer().renderStatic(toDisplay, ItemDisplayContext.FIXED, LightTexture.FULL_BRIGHT,
                OverlayTexture.NO_OVERLAY, guiGraphics.pose(), bufferSource, null, 0);
        guiGraphics.pose().popPose();

        bufferSource.endBatch();

        Font fontRenderer = Minecraft.getInstance().font;
        String help = FluxTranslate.CREATING_FLUX_INTERACT.format(recipeCrusher.getName().getString());
        guiGraphics.drawString(fontRenderer, help, 64 - fontRenderer.width(help) / 2, 68, textColor, true);
    }
}