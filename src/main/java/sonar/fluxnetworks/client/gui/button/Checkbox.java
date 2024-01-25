package sonar.fluxnetworks.client.gui.button;

import com.mojang.blaze3d.vertex.PoseStack;
import sonar.fluxnetworks.client.gui.basic.GuiButtonCore;
import sonar.fluxnetworks.client.gui.basic.GuiFocusable;

/**
 * Simple checkbox with a filled rectangle or hollow rectangle.
 */
public class Checkbox extends GuiButtonCore {

    // switch on/off
    private boolean mChecked = false;

    public Checkbox(GuiFocusable screen, int x, int y) {
        this(screen, x, y, 6, 6);
    }

    public Checkbox(GuiFocusable screen, int x, int y, int width, int height) {
        super(screen, x, y, width, height);
    }

    @Override
    protected void drawButton(PoseStack poseStack, int mouseX, int mouseY, float deltaTicks) {
        int color = isMouseHovered(mouseX, mouseY) ? 0xccffffff : 0xccb4b4b4;

        drawOuterFrame(poseStack, x, y, width, height, color);
        if (mChecked) {
            fill(poseStack, x + 1, y + 1, x + width - 1, y + height - 1, 0xddffffff);
        }
    }

    public void toggle() {
        mChecked = !mChecked;
    }

    public boolean isChecked() {
        return mChecked;
    }

    public void setChecked(boolean checked) {
        mChecked = checked;
    }
}
