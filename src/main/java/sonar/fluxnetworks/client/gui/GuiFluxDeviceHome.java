package sonar.fluxnetworks.client.gui;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import org.lwjgl.glfw.GLFW;
import sonar.fluxnetworks.FluxConfig;
import sonar.fluxnetworks.api.FluxConstants;
import sonar.fluxnetworks.api.FluxTranslate;
import sonar.fluxnetworks.client.gui.basic.GuiButtonCore;
import sonar.fluxnetworks.client.gui.basic.GuiTabCore;
import sonar.fluxnetworks.client.gui.button.FluxEditBox;
import sonar.fluxnetworks.client.gui.button.SwitchButton;
import sonar.fluxnetworks.common.connection.FluxMenu;
import sonar.fluxnetworks.common.connection.TransferHandler;
import sonar.fluxnetworks.common.device.TileFluxDevice;
import sonar.fluxnetworks.register.ClientMessages;

import javax.annotation.Nonnull;

/**
 * The home page.
 */
public class GuiFluxDeviceHome extends GuiTabCore {

    public FluxEditBox mCustomName;
    public FluxEditBox mPriority;
    public FluxEditBox mLimit;

    public SwitchButton mSurgeMode;
    public SwitchButton mDisableLimit;
    public SwitchButton mChunkLoading;

    //private int timer;

    public GuiFluxDeviceHome(@Nonnull FluxMenu menu, @Nonnull Player player) {
        super(menu, player);
    }

    @Override
    public EnumNavigationTab getNavigationTab() {
        return EnumNavigationTab.TAB_HOME;
    }

    public TileFluxDevice getDevice() {
        return (TileFluxDevice) menu.mProvider;
    }

    @Override
    public void init() {
        super.init();

        int color = getNetwork().getNetworkColor() | 0xFF000000;
        mCustomName = FluxEditBox.create(FluxTranslate.NAME.get() + ": ", font,
                        leftPos + 16, topPos + 28, 144, 12)
                .setOutlineColor(color);
        mCustomName.setMaxLength(TileFluxDevice.MAX_CUSTOM_NAME_LENGTH);
        mCustomName.setValue(getDevice().getCustomName());
        mCustomName.setResponder(string -> {
            CompoundTag tag = new CompoundTag();
            tag.putString(FluxConstants.CUSTOM_NAME, mCustomName.getValue());
            ClientMessages.editTile(getToken(), getDevice(), tag);
        });
        mCustomName.setPlaceholderText(FluxTranslate.getTranslation(getDevice().getBlockState().getBlock().getDescriptionId()));
        addRenderableWidget(mCustomName);

        mPriority = FluxEditBox.create(FluxTranslate.PRIORITY.get() + ": ", font,
                        leftPos + 16, topPos + 45, 144, 12)
                .setOutlineColor(color)
                .setDigitsOnly()
                .setAllowNegatives(true);
        mPriority.setMaxLength(5);
        mPriority.setValue(String.valueOf(getDevice().getRawPriority()));
        mPriority.setResponder(string -> {
            int priority = Mth.clamp(mPriority.getValidInt(),
                    TransferHandler.PRI_USER_MIN, TransferHandler.PRI_USER_MAX);
            CompoundTag tag = new CompoundTag();
            tag.putInt(FluxConstants.PRIORITY, priority);
            ClientMessages.editTile(getToken(), getDevice(), tag);
        });
        addRenderableWidget(mPriority);

        mLimit = FluxEditBox.create(FluxTranslate.TRANSFER_LIMIT.get() + ": ", font,
                        leftPos + 16, topPos + 62, 144, 12)
                .setOutlineColor(color)
                .setDigitsOnly()
                .setMaxValue(Long.MAX_VALUE);
        mLimit.setMaxLength(15);
        mLimit.setValue(String.valueOf(getDevice().getRawLimit()));
        mLimit.setResponder(string -> {
            long limit = mLimit.getValidLong();
            CompoundTag tag = new CompoundTag();
            tag.putLong(FluxConstants.LIMIT, limit);
            ClientMessages.editTile(getToken(), getDevice(), tag);
        });
        addRenderableWidget(mLimit);

        mSurgeMode = new SwitchButton(this, leftPos + 140, topPos + 120,
                getDevice().getSurgeMode(), color);
        mDisableLimit = new SwitchButton(this, leftPos + 140, topPos + 132,
                getDevice().getDisableLimit(), color);
        mButtons.add(mSurgeMode);
        mButtons.add(mDisableLimit);

        if (!getDevice().getDeviceType().isStorage()) {
            mChunkLoading = new SwitchButton(this, leftPos + 140, topPos + 144,
                    getDevice().isForcedLoading(), color);
            mChunkLoading.setClickable(FluxConfig.enableChunkLoading);
            mButtons.add(mChunkLoading);
        }
    }

    @Override
    protected void drawForegroundLayer(GuiGraphics gr, int mouseX, int mouseY, float deltaTicks) {
        super.drawForegroundLayer(gr, mouseX, mouseY, deltaTicks);

        int color = getNetwork().getNetworkColor();
        renderNetwork(gr, getNetwork().getNetworkName(), color, topPos + 8);
        renderTransfer(gr, getDevice(), leftPos + 30, topPos + 90);

        gr.drawString(font, FluxTranslate.SURGE_MODE.get(), 20 + leftPos, 120 + topPos, color);
        gr.drawString(font, FluxTranslate.DISABLE_LIMIT.get(), 20 + leftPos, 132 + topPos, color);

        if (mChunkLoading != null) {
            gr.drawString(font, FluxTranslate.CHUNK_LOADING.get(), 20 + leftPos, 144 + topPos, color);
        }
    }

    @Override
    public void onButtonClicked(GuiButtonCore button, float mouseX, float mouseY, int mouseButton) {
        super.onButtonClicked(button, mouseX, mouseY, mouseButton);
        if (mouseButton == GLFW.GLFW_MOUSE_BUTTON_LEFT && button instanceof SwitchButton switchButton) {
            if (switchButton == mSurgeMode) {
                switchButton.toggle();
                CompoundTag tag = new CompoundTag();
                tag.putBoolean(FluxConstants.SURGE_MODE, mSurgeMode.isChecked());
                ClientMessages.editTile(getToken(), getDevice(), tag);
            } else if (switchButton == mDisableLimit) {
                switchButton.toggle();
                CompoundTag tag = new CompoundTag();
                tag.putBoolean(FluxConstants.DISABLE_LIMIT, mDisableLimit.isChecked());
                ClientMessages.editTile(getToken(), getDevice(), tag);
            } else if (switchButton == mChunkLoading) {
                // delayed toggle, wait for server response
                CompoundTag tag = new CompoundTag();
                tag.putBoolean(FluxConstants.FORCED_LOADING, !mChunkLoading.isChecked());
                ClientMessages.editTile(getToken(), getDevice(), tag);
            }
        }
    }

    /*@Override
    public void tick() {
        super.tick();
        if (timer == 0) {
            C2SNetMsg.requestNetworkUpdate(network, FluxConstants.TYPE_NET_BASIC);
        }
        if (chunkLoading != null) {
            chunkLoading.toggled = tileEntity.isForcedLoading();
        }
        timer++;
        timer %= 100;
    }*/

    @Override
    protected void containerTick() {
        super.containerTick();
        if (mCustomName != null) {
            int color = getNetwork().getNetworkColor() | 0xFF000000;
            mCustomName.setOutlineColor(color);
            mPriority.setOutlineColor(color);
            mLimit.setOutlineColor(color);
            mSurgeMode.setColor(color);
            mDisableLimit.setColor(color);
            if (mChunkLoading != null) {
                mChunkLoading.setColor(color);
                mChunkLoading.setChecked(getDevice().isForcedLoading());
            }
        }
    }

    @Override
    public boolean onMouseClicked(double mouseX, double mouseY, int mouseButton) {
        if (super.onMouseClicked(mouseX, mouseY, mouseButton)) {
            return true;
        }
        if (mouseButton == GLFW.GLFW_MOUSE_BUTTON_LEFT) {
            if (mouseX >= leftPos + 20 && mouseX < leftPos + 155 && mouseY >= topPos + 8 && mouseY < topPos + 20) {
                switchTab(EnumNavigationTab.TAB_SELECTION, false);
                return true;
            }
        }
        return false;
    }
}
