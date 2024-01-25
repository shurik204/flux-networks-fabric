package sonar.fluxnetworks.client.gui.tab;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.world.entity.player.Player;
import org.lwjgl.glfw.GLFW;
import sonar.fluxnetworks.api.FluxTranslate;
import sonar.fluxnetworks.api.network.WirelessType;
import sonar.fluxnetworks.client.ClientCache;
import sonar.fluxnetworks.client.gui.EnumNavigationTab;
import sonar.fluxnetworks.client.gui.basic.GuiButtonCore;
import sonar.fluxnetworks.client.gui.basic.GuiTabCore;
import sonar.fluxnetworks.client.gui.button.*;
import sonar.fluxnetworks.common.connection.FluxMenu;
import sonar.fluxnetworks.register.ClientMessages;

import javax.annotation.Nonnull;

public class GuiTabWireless extends GuiTabCore {

    public SwitchButton mEnable;
    public SimpleButton mApply;

    public int mWirelessMode;

    public GuiTabWireless(@Nonnull FluxMenu menu, @Nonnull Player player) {
        super(menu, player);
    }

    @Override
    public EnumNavigationTab getNavigationTab() {
        return EnumNavigationTab.TAB_WIRELESS;
    }

    @Override
    protected void drawForegroundLayer(PoseStack poseStack, int mouseX, int mouseY, float deltaTicks) {
        super.drawForegroundLayer(poseStack, mouseX, mouseY, deltaTicks);
        if (getNetwork().isValid()) {
            int color = getNetwork().getNetworkColor();
            drawCenteredString(poseStack, font, FluxTranslate.TAB_WIRELESS.get(), leftPos + 88, topPos + 10, 0xb4b4b4);
            font.draw(poseStack, FluxTranslate.ENABLE_WIRELESS.get(), leftPos + 20, topPos + 148, color);

            if (WirelessType.ENABLE_WIRELESS.isActivated(ClientCache.sWirelessMode) &&
                    ClientCache.sWirelessNetwork == getNetwork().getNetworkID()) {
                drawCenteredString(poseStack, font,
                        '(' + FluxTranslate.EFFECTIVE_WIRELESS_NETWORK.get() + ')',
                        leftPos + 88, topPos + 160, color);
            } else {
                drawCenteredString(poseStack, font,
                        '(' + FluxTranslate.INEFFECTIVE_WIRELESS_NETWORK.get() + ')',
                        leftPos + 88, topPos + 160, 0xb4b4b4);
            }
        } else {
            renderNavigationPrompt(poseStack, FluxTranslate.ERROR_NO_SELECTED, EnumNavigationTab.TAB_SELECTION);
        }
    }

    @Override
    public void init() {
        super.init();
        if (getNetwork().isValid()) {

            mWirelessMode = ClientCache.sWirelessMode;

            mEnable = new SwitchButton(this, leftPos + 140, topPos + 148,
                    WirelessType.ENABLE_WIRELESS.isActivated(mWirelessMode), getNetwork().getNetworkColor());
            mButtons.add(mEnable);
            mButtons.add(new InventoryButton(this, leftPos + 24, topPos + 28, 52, 16,
                    WirelessType.ARMOR, 0, 80));
            mButtons.add(new InventoryButton(this, leftPos + 100, topPos + 28, 52, 16,
                    WirelessType.TRINKETS, 0, 80));
            mButtons.add(new InventoryButton(this, leftPos + 32, topPos + 52, 112, 40,
                    WirelessType.INVENTORY, 0, 0));
            mButtons.add(new InventoryButton(this, leftPos + 32, topPos + 100, 112, 16,
                    WirelessType.HOT_BAR, 112, 0));
            mButtons.add(new InventoryButton(this, leftPos + 136, topPos + 124, 16, 16,
                    WirelessType.MAIN_HAND, 52, 80));
            mButtons.add(new InventoryButton(this, leftPos + 24, topPos + 124, 16, 16,
                    WirelessType.OFF_HAND, 52, 80));

            mApply = new SimpleButton(this, leftPos + (imageWidth / 2) - 24, topPos + 126, 48, 12,
                    FluxTranslate.APPLY.get());
            mApply.setClickable(ClientCache.sWirelessNetwork != getNetwork().getNetworkID());
            mButtons.add(mApply);
        }
    }

    @Override
    public void onButtonClicked(GuiButtonCore button, float mouseX, float mouseY, int mouseButton) {
        super.onButtonClicked(button, mouseX, mouseY, mouseButton);
        if (mouseButton != GLFW.GLFW_MOUSE_BUTTON_LEFT) {
            return;
        }
        if (button instanceof InventoryButton btn) {
            if (btn.mType != WirelessType.INVENTORY) {
                mWirelessMode ^= 1 << btn.mType.ordinal();
                mApply.setClickable(true);
            }
        } else if (button instanceof SwitchButton btn) {
            btn.toggle();
            if (btn.isChecked()) {
                mWirelessMode |= 1 << WirelessType.ENABLE_WIRELESS.ordinal();
            } else {
                mWirelessMode &= ~(1 << WirelessType.ENABLE_WIRELESS.ordinal());
            }
            mApply.setClickable(true);
        } else if (button == mApply) {
            ClientMessages.wirelessMode(getToken(), mWirelessMode, getNetwork().getNetworkID());
            mApply.setClickable(false);
        }
    }

    @Override
    public boolean onMouseClicked(double mouseX, double mouseY, int mouseButton) {
        if (super.onMouseClicked(mouseX, mouseY, mouseButton)) {
            return true;
        }
        if (!getNetwork().isValid()) {
            return redirectNavigationPrompt(mouseX, mouseY, mouseButton, EnumNavigationTab.TAB_SELECTION);
        }
        return false;
    }

    @Override
    protected void onResponseAction(int key, int code) {
        super.onResponseAction(key, code);
        if (code > 0) {
            mWirelessMode = ClientCache.sWirelessMode;
            if (mEnable != null) {
                mEnable.setChecked(WirelessType.ENABLE_WIRELESS.isActivated(mWirelessMode));
            }
        }
    }

    @Override
    protected void containerTick() {
        super.containerTick();
        if (mEnable != null) {
            mEnable.setColor(getNetwork().getNetworkColor());
        }
    }

    /* @Override
    public void onFeedbackAction(@Nonnull FeedbackInfo info) {
        super.onFeedbackAction(info);
        if (apply != null && info == FeedbackInfo.SUCCESS) {
            apply.clickable = false;
        }
    }*/
}
