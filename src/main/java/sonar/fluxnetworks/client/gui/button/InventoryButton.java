package sonar.fluxnetworks.client.gui.button;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import sonar.fluxnetworks.FluxNetworks;
import sonar.fluxnetworks.api.network.WirelessType;
import sonar.fluxnetworks.client.gui.basic.GuiButtonCore;
import sonar.fluxnetworks.client.gui.tab.GuiTabWireless;
import sonar.fluxnetworks.common.util.FluxUtils;

import javax.annotation.Nonnull;

public class InventoryButton extends GuiButtonCore {

    public static final ResourceLocation INVENTORY = FluxNetworks.location(
            "textures/gui/inventory_configuration.png");

    public WirelessType mType;
    private final int mU0;
    private final int mV0;
    public GuiTabWireless mHost;
    private final String mText;

    public InventoryButton(GuiTabWireless screen, int x, int y, int width, int height, @Nonnull WirelessType type,
                           int u0, int v0) {
        super(screen, x, y, width, height);
        mHost = screen;
        mType = type;
        mText = type.getTranslatedName();
        mU0 = u0;
        mV0 = v0;
    }

    @Override
    protected void drawButtonInternal(GuiGraphics gr, int mouseX, int mouseY, float deltaTicks) {
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        int color = mHost.getNetwork().getNetworkColor();
        RenderSystem.setShaderColor(FluxUtils.getRed(color), FluxUtils.getGreen(color), FluxUtils.getBlue(color), 1.0f);

        gr.blit(INVENTORY, x, y, mU0, mV0 + height * (mType.isActivated(mHost.mWirelessMode) ? 1 : 0), width, height);

        if (isMouseHovered(mouseX, mouseY)) {
            mHost.drawCenteredStringWithBackground(gr, x + (width) / 2, y - 8, mText);
        }
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
    }
}
