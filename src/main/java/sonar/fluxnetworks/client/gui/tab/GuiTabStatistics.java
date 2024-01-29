package sonar.fluxnetworks.client.gui.tab;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import it.unimi.dsi.fastutil.floats.FloatArrayList;
import it.unimi.dsi.fastutil.floats.FloatList;
import it.unimi.dsi.fastutil.longs.LongArrayList;
import it.unimi.dsi.fastutil.longs.LongList;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.world.entity.player.Player;
import org.joml.Matrix4f;
import sonar.fluxnetworks.api.FluxConstants;
import sonar.fluxnetworks.api.FluxTranslate;
import sonar.fluxnetworks.api.energy.EnergyType;
import sonar.fluxnetworks.client.gui.EnumNavigationTab;
import sonar.fluxnetworks.client.gui.basic.GuiTabCore;
import sonar.fluxnetworks.common.connection.*;
import sonar.fluxnetworks.common.util.FluxUtils;
import sonar.fluxnetworks.register.ClientMessages;

import javax.annotation.Nonnull;
import java.util.List;

public class GuiTabStatistics extends GuiTabCore {

    private LineChart mChart;
    private int timer = 0;

    public GuiTabStatistics(@Nonnull FluxMenu menu, @Nonnull Player player) {
        super(menu, player);
        if (getNetwork().isValid()) {
            ClientMessages.updateNetwork(getToken(), getNetwork(), FluxConstants.NBT_NET_STATISTICS);
        }
    }

    @Nonnull
    public EnumNavigationTab getNavigationTab() {
        return EnumNavigationTab.TAB_STATISTICS;
    }

    @Override
    protected void drawForegroundLayer(GuiGraphics gr, int mouseX, int mouseY, float deltaTicks) {
        super.drawForegroundLayer(gr, mouseX, mouseY, deltaTicks);
        final FluxNetwork network = getNetwork();
        if (network.isValid()) {
            int color = network.getNetworkColor();
            renderNetwork(gr, network.getNetworkName(), color, topPos + 8);

            gr.pose().pushPose();
            gr.pose().translate(leftPos, topPos, 0);
            final NetworkStatistics stats = network.getStatistics();
            gr.drawString(font, ChatFormatting.GRAY + FluxTranslate.PLUGS.get() + ChatFormatting.GRAY + ": " +
                    ChatFormatting.RESET + stats.fluxPlugCount, 12, 24, color);
            gr.drawString(font, ChatFormatting.GRAY + FluxTranslate.POINTS.get() + ChatFormatting.GRAY + ": " +
                    ChatFormatting.RESET + stats.fluxPointCount, 12, 36, color);
            gr.drawString(font, ChatFormatting.GRAY + FluxTranslate.STORAGES.get() + ChatFormatting.GRAY + ": " +
                    ChatFormatting.RESET + stats.fluxStorageCount, 82, 24, color);
            gr.drawString(font, ChatFormatting.GRAY + FluxTranslate.CONTROLLERS.get() + ChatFormatting.GRAY + ": " +
                    ChatFormatting.RESET + stats.fluxControllerCount, 82, 36, color);
            gr.drawString(font,
                    ChatFormatting.GRAY + FluxTranslate.INPUT.get() + ChatFormatting.GRAY + ": " + ChatFormatting.RESET +
                            EnergyType.E.getUsage(stats.energyInput), 12, 48, color);
            gr.drawString(font,
                    ChatFormatting.GRAY + FluxTranslate.OUTPUT.get() + ChatFormatting.GRAY + ": " + ChatFormatting.RESET +
                            EnergyType.E.getUsage(stats.energyOutput), 12, 60, color);
            gr.drawString(font,
                    ChatFormatting.GRAY + FluxTranslate.BUFFER.get() + ChatFormatting.GRAY + ": " + ChatFormatting.RESET +
                            EnergyType.E.getStorage(stats.totalBuffer), 12, 72, color);
            gr.drawString(font,
                    ChatFormatting.GRAY + FluxTranslate.ENERGY.get() + ChatFormatting.GRAY + ": " + ChatFormatting.RESET +
                            EnergyType.E.getStorage(stats.totalEnergy), 12, 84, color);
            gr.pose().scale(0.75f, 0.75f, 1);
            gr.drawCenteredString(font,
                    FluxTranslate.AVERAGE_TICK.get() + ": " + stats.averageTickMicro + " µs/t",
                    (int) ((imageWidth / 2f) * (1 / 0.75f)), (int) ((imageHeight - 2f) * (1 / 0.75f)), color);
            gr.pose().popPose();
        } else {
            renderNavigationPrompt(gr, FluxTranslate.ERROR_NO_SELECTED, EnumNavigationTab.TAB_SELECTION);
        }
    }

    @Override
    protected void drawBackgroundLayer(GuiGraphics gr, int mouseX, int mouseY, float deltaTicks) {
        super.drawBackgroundLayer(gr, mouseX, mouseY, deltaTicks);
        if (getNetwork().isValid() && mChart != null) {
            mChart.drawChart(Minecraft.getInstance(), gr, deltaTicks);
        }
    }

    @Override
    public void init() {
        super.init();
        if (getNetwork().isValid()) {
            mChart = new LineChart(width / 2 - 48, height / 2 + 20, 50, NetworkStatistics.CHANGE_COUNT, "s",
                    EnergyType.E.getStorageSuffix());
            mChart.updateData(getNetwork().getStatistics().energyChange);
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
    protected void containerTick() {
        super.containerTick();
        if (getNetwork().isValid()) {
            timer = (timer + 1) % 20;
            if (timer == 0) {
                ClientMessages.updateNetwork(getToken(), getNetwork(), FluxConstants.NBT_NET_STATISTICS);
            }
        }
    }

    @Override
    protected void onResponseAction(int key, int code) {
        super.onResponseAction(key, code);
        if (code == FluxConstants.RESPONSE_REJECT) {
            switchTab(EnumNavigationTab.TAB_HOME, false);
            return;
        }
        if (key == FluxConstants.REQUEST_UPDATE_NETWORK) {
            mChart.updateData(getNetwork().getStatistics().energyChange);
        }
    }

    /**
     * Simple line chart.
     */
    public static class LineChart {

        private final int x, y;
        private final int height;

        private final int linePoints;

        private final String displayUnitX;
        private String displayUnitY;

        private long maxUnitY;
        private final String suffixUnitY;

        private LongList data = new LongArrayList();

        private final FloatList currentHeight;
        private final FloatList targetHeight;

        public LineChart(int x, int y, int height, int linePoints, String displayUnitX, String suffixUnitY) {
            this.x = x;
            this.y = y;
            this.height = height;
            this.linePoints = linePoints;
            this.displayUnitX = displayUnitX;
            this.suffixUnitY = suffixUnitY;

            this.currentHeight = new FloatArrayList(linePoints);
            for (int i = 0; i < linePoints; i++) {
                currentHeight.add(y + height);
            }
            this.targetHeight = new FloatArrayList(linePoints);
            for (int i = 0; i < linePoints; i++) {
                targetHeight.add(y + height);
            }
        }

        public void drawChart(Minecraft mc, GuiGraphics gr, float deltaTicks) {
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();

            Tesselator tesselator = Tesselator.getInstance();
            BufferBuilder builder = tesselator.getBuilder();

            RenderSystem.setShader(GameRenderer::getPositionColorShader);

            builder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);
            float hw = 1;
            for (int i = 0; i < currentHeight.size() - 1; i++) {
                float lx = x + 20 * i;
                float ly = currentHeight.getFloat(i);
                float rx = x + 20 * (i + 1);
                float ry = currentHeight.getFloat(i + 1);
                Matrix4f matrix = gr.pose().last().pose();
                builder.vertex(matrix, rx, ry - hw, 0)
                        .color(255, 255, 255, 255).endVertex();
                builder.vertex(matrix, lx, ly - hw, 0)
                        .color(255, 255, 255, 255).endVertex();
                builder.vertex(matrix, lx, ly + hw, 0)
                        .color(255, 255, 255, 255).endVertex();
                builder.vertex(matrix, rx, ry + hw, 0)
                        .color(255, 255, 255, 255).endVertex();
            }
            tesselator.end();

            builder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);
            hw = 2;
            for (int i = 0; i < currentHeight.size(); i++) {
                float cx = x + 20 * i;
                float cy = currentHeight.getFloat(i);
                Matrix4f matrix = gr.pose().last().pose();
                builder.vertex(matrix, cx + hw, cy - hw, 0)
                        .color(255, 255, 255, 255).endVertex();
                builder.vertex(matrix, cx - hw, cy - hw, 0)
                        .color(255, 255, 255, 255).endVertex();
                builder.vertex(matrix, cx - hw, cy + hw, 0)
                        .color(255, 255, 255, 255).endVertex();
                builder.vertex(matrix, cx + hw, cy + hw, 0)
                        .color(255, 255, 255, 255).endVertex();
            }
            tesselator.end();

            gr.fill(x - 16, y + height, x + 116, y + height + 1, 0xcfffffff);
            gr.fill(x - 14, y - 6, x - 13, y + height + 3, 0xcfffffff);

            gr.pose().pushPose();
            gr.pose().scale(0.75f, 0.75f, 1);
            // TODO: is casting to int correct?
            gr.drawString(mc.font, suffixUnitY,
                    (int) ((x - 15) / 0.75f - mc.font.width(suffixUnitY)),
                    (int) ((y - 7.5f) / 0.75f), 0xffffff, true);
            gr.drawString(mc.font, displayUnitY,
                    (int) ((x - 15) / 0.75f - mc.font.width(displayUnitY)),
                    (int) ((y - 2) / 0.75f), 0xffffff, true);
            gr.drawString(mc.font, displayUnitX,
                    (int) ((x + 118) / 0.75f - mc.font.width(displayUnitX)),
                    (int) ((y + height + 1.5f) / 0.75f), 0xffffff, true);
            for (int i = 0; i < data.size(); i++) {
                String d = FluxUtils.compact(data.getLong(i));
                gr.drawString(mc.font, d,
                        (int) (((x + 20 * i) / 0.75f) - (mc.font.width(d) * 0.5f)),
                        (int) ((currentHeight.getFloat(i) - 8) / 0.75f), 0xffffff, true);
                String c = String.valueOf((5 - i) * 5);
                gr.drawString(mc.font, c,
                        (int) (((x + 20 * i) / 0.75f) - (mc.font.width(c) * 0.5f)),
                        (int) ((y + height + 2) / 0.75f), 0xffffff, true);
            }
            gr.pose().popPose();

            updateHeight(deltaTicks);
        }

        public void updateData(LongList newData) {
            this.data = newData;
            calculateUnitY(newData);
            calculateTargetHeight(newData);
        }

        private void updateHeight(float deltaTicks) {
            if (currentHeight.isEmpty()) {
                return;
            }
            for (int i = 0; i < currentHeight.size(); i++) {
                float diff = targetHeight.getFloat(i) - currentHeight.getFloat(i);
                if (diff == 0) {
                    continue;
                }
                float r;
                float p = deltaTicks / 16;
                if (Math.abs(diff) <= p) {
                    r = targetHeight.getFloat(i);
                } else {
                    if (diff > 0)
                        r = currentHeight.getFloat(i) + Math.max(Math.min(diff, diff / 4 * deltaTicks), p);
                    else
                        r = currentHeight.getFloat(i) + Math.min(Math.max(diff, diff / 4 * deltaTicks), -p);
                }
                currentHeight.set(i, r);
            }
        }

        private void calculateUnitY(@Nonnull List<Long> data) {
            long maxValue = 0;
            for (long v : data) {
                maxValue = Math.max(maxValue, v);
            }
            if (maxValue <= 0) {
                displayUnitY = "1";
                maxUnitY = 1;
                return;
            }
            long maxUnitY;
            int exp = (int) Math.log10(maxValue); // 0 = 10, 3 = 10000
            if (exp <= 0) {
                maxUnitY = maxValue + 1;
            } else if (exp <= 1) {
                maxUnitY = ((maxValue / 5) + 1) * 5;
            } else if (exp <= 2) {
                maxUnitY = ((maxValue / 50) + 1) * 50;
            } else {
                int unit = 10;
                for (int i = 1; i < exp; i++) {
                    unit *= 10;
                }
                maxUnitY = ((maxValue / unit) + 1) * unit;
            }
            displayUnitY = FluxUtils.compact(maxUnitY);
            this.maxUnitY = maxUnitY;
        }

        private void calculateTargetHeight(@Nonnull List<Long> data) {
            if (data.size() != linePoints) {
                return;
            }
            int i = 0;
            for (Long value : data) {
                targetHeight.set(i, (float) (y + height * (1 - ((double) value / maxUnitY))));
                i++;
            }
        }
    }
}
