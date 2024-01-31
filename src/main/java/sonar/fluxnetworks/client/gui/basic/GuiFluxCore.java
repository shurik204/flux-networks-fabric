package sonar.fluxnetworks.client.gui.basic;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.toasts.SystemToast;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.gui.screens.inventory.tooltip.DefaultTooltipPositioner;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.joml.Vector2ic;
import sonar.fluxnetworks.FluxNetworks;
import sonar.fluxnetworks.api.FluxTranslate;
import sonar.fluxnetworks.api.device.IFluxDevice;
import sonar.fluxnetworks.api.energy.EnergyType;
import sonar.fluxnetworks.api.network.AccessLevel;
import sonar.fluxnetworks.client.ClientCache;
import sonar.fluxnetworks.common.connection.FluxMenu;
import sonar.fluxnetworks.common.connection.FluxNetwork;
import sonar.fluxnetworks.common.device.TileFluxDevice;
import sonar.fluxnetworks.common.integration.MUIIntegration;
import sonar.fluxnetworks.common.item.ItemAdminConfigurator;
import sonar.fluxnetworks.common.util.FluxUtils;
import sonar.fluxnetworks.register.ClientMessages;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

/**
 * Gui that interacts flux networks.
 */
public abstract class GuiFluxCore extends GuiPopupHost {

    protected final List<GuiButtonCore> mButtons = new ArrayList<>();

    public final Player mPlayer; // client player

    private FluxNetwork mNetwork;

    public GuiFluxCore(@Nonnull FluxMenu menu, @Nonnull Player player) {
        super(menu, player);
        mPlayer = player;
        mNetwork = ClientCache.getNetwork(menu.mProvider.getNetworkID());
        menu.mOnResultListener = this::onResponse;
    }

    // this called from main thread
    private void onResponse(FluxMenu menu, int key, int code) {
        final FluxTranslate t = FluxTranslate.fromResponseCode(code);
        if (t != null) {
            if (FluxNetworks.isModernUILoaded()) {
                MUIIntegration.showToastError(t);
            } else {
                Minecraft.getInstance().getToasts().addToast(SystemToast.multiline(Minecraft.getInstance(),
                        SystemToast.SystemToastIds.TUTORIAL_HINT,
                        Component.literal(FluxNetworks.NAME),
                        t.getComponent()));
            }
        }
        onResponseAction(key, code);
    }

    /**
     * @return the menu token
     */
    public int getToken() {
        return menu.containerId;
    }

    /**
     * @return current network
     */
    @Nonnull
    public FluxNetwork getNetwork() {
        return mNetwork;
    }

    /**
     * @return current access
     */
    @Nonnull
    public AccessLevel getAccessLevel() {
        return mNetwork.getPlayerAccess(mPlayer);
    }

    @Override
    public void init() {
        super.init();
        mButtons.clear();
    }

    @Override
    protected void drawForegroundLayer(GuiGraphics gr, int mouseX, int mouseY, float deltaTicks) {
        super.drawForegroundLayer(gr, mouseX, mouseY, deltaTicks);
        for (GuiButtonCore button : mButtons) {
            button.drawButton(gr, mouseX, mouseY, deltaTicks);
        }
    }

    @Override
    protected void drawBackgroundLayer(GuiGraphics gr, int mouseX, int mouseY, float deltaTicks) {
        super.drawBackgroundLayer(gr, mouseX, mouseY, deltaTicks);
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
        RenderSystem.setShaderTexture(0, BACKGROUND);
        blitBackgroundOrFrame(gr);

        int color = mNetwork.getNetworkColor();
        RenderSystem.setShaderColor(FluxUtils.getRed(color), FluxUtils.getGreen(color), FluxUtils.getBlue(color), 1.0f);
        RenderSystem.setShaderTexture(0, FRAME);
        blitBackgroundOrFrame(gr);
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
    }

    public void drawCenteredStringWithBackground(GuiGraphics gr, int x, int y, String text) {
        Font font = Minecraft.getInstance().font;
        int textWidth = font.width(text);
        int textHeight = font.lineHeight;
        x -= textWidth / 2;
        y -= textHeight / 2;

        gr.pose().pushPose();

        // Render outline
        gr.fill(x - 2, y - 2, x + 2 + textWidth, y + 2 + textHeight, 400, 0xB3A0A0A0);
        // Render background
        gr.fill(x - 1, y - 1, x + 1 + textWidth, y + 1 + textHeight, 400, 0xF3000000);

        gr.pose().translate(0.0F, 0.0F, 400.0F);
        gr.drawString(font, text, x, y, 0xFFFFFF);

        gr.pose().popPose();
    }

    public void drawTooltipWithBackground(GuiGraphics gr, int mouseX, int mouseY, String text) {
        drawTooltipWithBackground(gr, mouseX, mouseY, Component.nullToEmpty(text));
    }

    public void drawTooltipWithBackground(GuiGraphics gr, int mouseX, int mouseY, Component text) {
        ClientTooltipComponent tooltipText = ClientTooltipComponent.create(text.getVisualOrderText());

        int textWidth = tooltipText.getWidth(font);
        int textHeight = tooltipText.getHeight();

        Vector2ic vector2ic = DefaultTooltipPositioner.INSTANCE.positionTooltip(this.width, this.height, mouseX, mouseY, textWidth, textHeight);
        int tooltipX = vector2ic.x();
        int tooltipY = vector2ic.y();

        gr.pose().pushPose();

        int backgroundX = tooltipX - 4;
        int backgroundY = tooltipY - 4;

        // Render background
        gr.fill(backgroundX - 2, backgroundY - 2, backgroundX + textWidth + 2, backgroundY + textHeight + 2, 400, 0xB3000000);
        gr.pose().translate(0.0F, 0.0F, 400.0F);
        // Render tooltip text
        gr.drawString(font, text, backgroundX, backgroundY, 0xFFFFFF);

        gr.pose().popPose();
    }

    @Override
    public boolean onMouseClicked(double mouseX, double mouseY, int mouseButton) {
        for (GuiButtonCore button : mButtons) {
            if (button.mClickable && button.isMouseHovered(mouseX, mouseY)) {
                onButtonClicked(button, (float) mouseX, (float) mouseY, mouseButton);
                return true;
            }
        }
        return super.onMouseClicked(mouseX, mouseY, mouseButton);
    }

    public void onButtonClicked(GuiButtonCore button, float mouseX, float mouseY, int mouseButton) {
    }

    @Override
    protected void containerTick() {
        super.containerTick();
        mNetwork = ClientCache.getNetwork(menu.mProvider.getNetworkID());
    }

    @Override
    public void onClose() {
        super.onClose();
    }

    /**
     * Render the network bar on the top.
     */
    protected void renderNetwork(GuiGraphics gr, String name, int color, int y) {
        RenderSystem.enableBlend();
        RenderSystem.setShaderColor(FluxUtils.getRed(color), FluxUtils.getGreen(color), FluxUtils.getBlue(color), 1.0f);
        RenderSystem.setShaderTexture(0, ICON);
        int x = leftPos + 20;
        blitF(gr, x, y, 135, 12, 0, 320, 270, 24);
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
        gr.drawString(font, name, x + 4, y + 2, 0xffffff);
    }

    /**
     * Render the energy change.
     */
    protected void renderTransfer(GuiGraphics gr, IFluxDevice device, int x, int y) {
        RenderSystem.enableBlend();
        gr.drawString(font, FluxUtils.getTransferInfo(device, EnergyType.E), x, y, 0xffffff);

        String text = device.getDeviceType().isStorage() ? FluxTranslate.ENERGY.get() : FluxTranslate.BUFFER.get();
        text += ": " + ChatFormatting.BLUE + EnergyType.E.getStorage(device.getTransferBuffer());
        gr.drawString(font, text, x, y + 10, 0xffffff);

        renderItemStack(gr, device.getDisplayStack(), x - 20, y + 1);
    }

    protected void renderItemStack(GuiGraphics gr, ItemStack stack, int x, int y) {
        gr.pose().pushPose();
        gr.pose().translate(0, 0, 50);
        gr.renderItem(stack, x, y);
        gr.renderItemDecorations(font, stack, x, y);
        gr.pose().popPose();
    }

    public void setConnectedNetwork(FluxNetwork network, String password) {
        if (menu.mProvider instanceof TileFluxDevice) {
            ClientMessages.tileNetwork(getToken(), (TileFluxDevice) menu.mProvider, network, password);
        } /*else if (menu.mProvider instanceof ItemFluxConfigurator.Provider) {
            C2SNetMsg.configuratorNet(networkID, password);
        }*/ else if (menu.mProvider instanceof ItemAdminConfigurator.Provider) {
            ClientCache.sAdminViewingNetwork = network.getNetworkID();
        }
    }

    /**
     * Called when a server response is received.
     *
     * @param key  the request key
     * @param code the response code
     */
    protected void onResponseAction(int key, int code) {
    }
}
