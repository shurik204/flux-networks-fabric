package sonar.fluxnetworks.register;

import io.netty.buffer.Unpooled;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.game.ClientboundCustomPayloadPacket;
import net.minecraft.network.protocol.game.ServerboundCustomPayloadPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.chunk.LevelChunk;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import sonar.fluxnetworks.FluxNetworks;

import javax.annotation.Nonnull;

public class Channel {
    public static final ResourceLocation CHANNEL_NAME = FluxNetworks.location("network");

    /**
     * Note: Increment this if any packet is changed.
     */
    static final String PROTOCOL = "707";
    static Channel sChannel;

    @Nonnull
    static FriendlyByteBuf buffer(int index) {
        return new FriendlyByteBuf(Unpooled.copyShort(index));
    }

    public static Channel get() {
        return sChannel;
    }

    public final void sendToPlayer(@Nonnull FriendlyByteBuf payload, @Nonnull Player player) {
        sendToPlayer(payload, (ServerPlayer) player);
    }

    @Environment(EnvType.CLIENT)
    public void sendToServer(@Nonnull FriendlyByteBuf payload) {
        ClientPacketListener connection = Minecraft.getInstance().getConnection();
        if (connection != null) {
            connection.send(new ServerboundCustomPayloadPacket(CHANNEL_NAME, payload));
        } else {
            payload.release();
        }
    }

    public void sendToPlayer(@Nonnull FriendlyByteBuf payload, @Nonnull ServerPlayer player) {
        player.connection.send(new ClientboundCustomPayloadPacket(CHANNEL_NAME, payload));
    }

    public void sendToAll(@Nonnull FriendlyByteBuf payload) {
        FluxNetworks.getServer().getPlayerList()
                .broadcastAll(new ClientboundCustomPayloadPacket(CHANNEL_NAME, payload));
    }

    public void sendToTrackingChunk(@Nonnull FriendlyByteBuf payload, @Nonnull LevelChunk chunk) {
        final ClientboundCustomPayloadPacket packet = new ClientboundCustomPayloadPacket(CHANNEL_NAME, payload);
        ((ServerLevel) chunk.getLevel()).getChunkSource().chunkMap.getPlayers(
                chunk.getPos(), /* boundaryOnly */ false).forEach(p -> p.connection.send(packet));
    }

    public static void init() {
        sChannel = new Channel();
    }
}
