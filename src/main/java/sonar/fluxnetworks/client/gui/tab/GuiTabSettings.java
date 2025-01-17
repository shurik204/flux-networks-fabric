package sonar.fluxnetworks.client.gui.tab;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.entity.player.Player;
import org.lwjgl.glfw.GLFW;
import sonar.fluxnetworks.api.FluxConstants;
import sonar.fluxnetworks.api.FluxTranslate;
import sonar.fluxnetworks.api.network.SecurityLevel;
import sonar.fluxnetworks.client.gui.EnumNavigationTab;
import sonar.fluxnetworks.client.gui.basic.GuiButtonCore;
import sonar.fluxnetworks.client.gui.button.SimpleButton;
import sonar.fluxnetworks.common.connection.FluxMenu;
import sonar.fluxnetworks.register.ClientMessages;

import javax.annotation.Nonnull;

public class GuiTabSettings extends GuiTabEditAbstract {

    public SimpleButton mDelete;
    public SimpleButton mApply;
    public int mDeleteCount;

    public GuiTabSettings(@Nonnull FluxMenu menu, @Nonnull Player player) {
        super(menu, player);
        mSecurityLevel = getNetwork().getSecurityLevel();
    }

    public EnumNavigationTab getNavigationTab() {
        return EnumNavigationTab.TAB_SETTING;
    }

    @Override
    protected void drawForegroundLayer(GuiGraphics gr, int mouseX, int mouseY, float deltaTicks) {
        super.drawForegroundLayer(gr, mouseX, mouseY, deltaTicks);
        if (getNetwork().isValid()) {
            if (mDelete.isMouseHovered(mouseX, mouseY)) {
                if (mDelete.isClickable()) {
                    gr.drawCenteredString(font,
                            ChatFormatting.BOLD + FluxTranslate.DELETE_NETWORK.get(),
                            mDelete.x + mDelete.width / 2, mDelete.y - 12, 0xff0000);
                } else {
                    gr.drawCenteredString(font,
                            FluxTranslate.DOUBLE_SHIFT.get(),
                            mDelete.x + mDelete.width / 2, mDelete.y - 12, 0xffffff);
                }
            }
        } else {
            renderNavigationPrompt(gr, FluxTranslate.ERROR_NO_SELECTED, EnumNavigationTab.TAB_SELECTION);
        }
    }

    @Override
    public void init() {
        super.init();
        if (getNetwork().isValid()) {
            mNetworkName.setValue(getNetwork().getNetworkName());

            mDelete = new SimpleButton(this, leftPos + (imageWidth / 2) - 12 - 48, topPos + 150, 48, 12,
                    FluxTranslate.DELETE.get(), 0xFFFF5555);
            mDelete.setClickable(false);
            mButtons.add(mDelete);

            mApply = new SimpleButton(this, leftPos + (imageWidth / 2) + 12, topPos + 150, 48, 12,
                    FluxTranslate.APPLY.get());
            mApply.setClickable(false);
            mButtons.add(mApply);

            initColorSelector(leftPos + 48, topPos + 87, false);
        }
    }

    @Override
    public void onEditSettingsChanged() {
        if (mApply != null) {
            boolean clickable = true;
            if (getNetwork().getSecurityLevel() != SecurityLevel.ENCRYPTED) {
                if (mSecurityLevel == SecurityLevel.ENCRYPTED) {
                    clickable = !mPassword.getValue().isEmpty();
                }
            }
            mApply.setClickable(clickable && !mNetworkName.getValue().isEmpty());
        }
    }

    @Override
    public void onButtonClicked(GuiButtonCore button, float mouseX, float mouseY, int mouseButton) {
        super.onButtonClicked(button, mouseX, mouseY, mouseButton);
        if (mouseButton == GLFW.GLFW_MOUSE_BUTTON_LEFT) {
            if (button == mApply) {
                ClientMessages.editNetwork(getToken(), getNetwork(),
                        mNetworkName.getValue(), mColorButton.mColor, mSecurityLevel, mPassword.getValue());
                mApply.setClickable(false);
            } else if (button == mDelete) {
                ClientMessages.deleteNetwork(getToken(), getNetwork());
                mDeleteCount = 0;
                mDelete.setClickable(false);
            }
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
    public boolean onKeyPressed(int keyCode, int scanCode, int modifiers) {
        if (mDelete != null && getFocused() == null) {
            // Replaced 'modifiers' check to 'keyCode' check
            if (keyCode == GLFW.GLFW_KEY_LEFT_SHIFT) {
                mDeleteCount++;
                if (mDeleteCount > 1) {
                    mDelete.setClickable(true);
                }
            } else {
                mDeleteCount = 0;
                mDelete.setClickable(false);
            }
        }
        return super.onKeyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    protected void onResponseAction(int key, int code) {
        super.onResponseAction(key, code);
        if (code == FluxConstants.RESPONSE_REJECT) {
            switchTab(EnumNavigationTab.TAB_HOME, false);
            return;
        }
        if (code == FluxConstants.RESPONSE_SUCCESS) {
            if (key == FluxConstants.REQUEST_DELETE_NETWORK) {
                switchTab(EnumNavigationTab.TAB_HOME, false);
            } /*else if (key == FluxConstants.REQUEST_EDIT_NETWORK) {
                // ignored
            }*/
        }
    }
}
