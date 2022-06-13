package sonar.fluxnetworks.client.gui.tab;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.world.entity.player.Player;
import org.lwjgl.glfw.GLFW;
import sonar.fluxnetworks.api.FluxConstants;
import sonar.fluxnetworks.api.FluxTranslate;
import sonar.fluxnetworks.api.gui.EnumNetworkColor;
import sonar.fluxnetworks.api.network.SecurityLevel;
import sonar.fluxnetworks.client.gui.EnumNavigationTab;
import sonar.fluxnetworks.client.gui.basic.GuiButtonCore;
import sonar.fluxnetworks.client.gui.button.ColorButton;
import sonar.fluxnetworks.client.gui.button.SimpleButton;
import sonar.fluxnetworks.common.connection.FluxDeviceMenu;
import sonar.fluxnetworks.register.ClientMessages;

import javax.annotation.Nonnull;

public class GuiTabCreate extends GuiTabEditAbstract {

    public SimpleButton mCreate;

    public GuiTabCreate(@Nonnull FluxDeviceMenu menu, @Nonnull Player player) {
        super(menu, player);
        mSecurityLevel = SecurityLevel.ENCRYPTED;
    }

    @Override
    public EnumNavigationTab getNavigationTab() {
        return EnumNavigationTab.TAB_CREATE;
    }

    @Override
    public void init() {
        super.init();
        mNetworkName.setValue(FluxTranslate.PLAYERS_NETWORK.format(mPlayer.getGameProfile().getName()));

        // two rows
        for (int i = 0; i < EnumNetworkColor.VALUES.length; i++) {
            final EnumNetworkColor color = EnumNetworkColor.VALUES[i];
            ColorButton button = new ColorButton(minecraft,
                    leftPos + 48 + (i % 7) * 16, topPos + 90 + (i / 7) * 16, color.getRGB());
            if (i == 0) {
                mColorButton = button;
                button.setSelected(true);
            }
            mButtons.add(button);
        }

        mCreate = new SimpleButton(minecraft, leftPos + 70, topPos + 150, 36, 12);
        mCreate.setText(FluxTranslate.CREATE.get());
        mCreate.setClickable(false);
        mButtons.add(mCreate);
    }

    @Override
    protected void drawForegroundLayer(PoseStack poseStack, int mouseX, int mouseY, float deltaTicks) {
        super.drawForegroundLayer(poseStack, mouseX, mouseY, deltaTicks);
        renderNetwork(poseStack, mNetworkName.getValue(), mColorButton.mColor, leftPos + 20, topPos + 129);
    }

    @Override
    public void onButtonClicked(GuiButtonCore button, int mouseX, int mouseY, int mouseButton) {
        super.onButtonClicked(button, mouseX, mouseY, mouseButton);
        if (mouseButton == GLFW.GLFW_MOUSE_BUTTON_LEFT && button == mCreate) {
            //PacketHandler.CHANNEL.sendToServer(new GeneralPacket(GeneralPacketEnum.CREATE_NETWORK,
            // GeneralPacketHandler.getCreateNetworkPacket(name.getText(), color.color, securityType, energyType,
            // password.getText())));

            //C2SNetMsg.createNetwork(nameField.getText(), colorBtn.color, mSecurityLevel, passwordField.getText());

            ClientMessages.createNetwork(menu.containerId, mNetworkName.getValue(),
                    mColorButton.mColor, mSecurityLevel, mPassword.getValue());
        }
    }

    @Override
    public void onEditSettingsChanged() {
        if (mCreate != null) {
            mCreate.setClickable((!mSecurityLevel.isEncrypted() || !mPassword.getValue().isEmpty()) &&
                    !mNetworkName.getValue().isEmpty());
        }
    }

    @Override
    protected void onResponseAction(int key, int code) {
        super.onResponseAction(key, code);
        if (key == FluxConstants.REQUEST_CREATE_NETWORK) {
            if (code == FluxConstants.RESPONSE_SUCCESS) {
                switchTab(EnumNavigationTab.TAB_SELECTION);
            }
        }
    }
}
