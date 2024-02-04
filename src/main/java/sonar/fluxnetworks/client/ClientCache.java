package sonar.fluxnetworks.client;

import it.unimi.dsi.fastutil.ints.*;
import net.minecraft.core.GlobalPos;
import net.minecraft.nbt.CompoundTag;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import sonar.fluxnetworks.FluxNetworks;
import sonar.fluxnetworks.api.FluxConstants;
import sonar.fluxnetworks.api.device.IFluxDevice;
import sonar.fluxnetworks.common.connection.ClientFluxNetwork;
import sonar.fluxnetworks.common.connection.FluxNetwork;
import sonar.fluxnetworks.common.util.FluxUtils;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.List;

/**
 * Main thread only.
 */
@Environment(EnvType.CLIENT)
public final class ClientCache {

    private static final int MAX_RECENT_PASSWORD_COUNT = 5;

    private static final Int2ObjectOpenHashMap<FluxNetwork> sNetworks =
            new Int2ObjectOpenHashMap<>();
    private static final Int2ObjectLinkedOpenHashMap<String> sRecentPasswords =
            new Int2ObjectLinkedOpenHashMap<>(MAX_RECENT_PASSWORD_COUNT); // LRU cache

    public static int sLastNetworkID = FluxConstants.INVALID_NETWORK_ID;
    public static boolean sSuperAdmin = false;
    public static int sWirelessMode = 0;
    public static int sWirelessNetwork = FluxConstants.INVALID_NETWORK_ID;
    public static boolean sDetailedNetworkView = false;

    public static int sAdminViewingNetwork = FluxConstants.INVALID_NETWORK_ID;

    private ClientCache() {
    }

    /**
     * Release buffers and view models.
     */
    public static void release() {
        sNetworks.clear();
        sNetworks.trim(); // rehash
        sRecentPasswords.clear(); // preserved memory, no need to rehash
        sAdminViewingNetwork = FluxConstants.INVALID_NETWORK_ID;
        sLastNetworkID = FluxConstants.INVALID_NETWORK_ID;
        FluxNetworks.LOGGER.info("Released client Flux Networks cache");
    }

    /**
     * Cleanup members and connections cache.
     */
    public static void cleanup() {
        sNetworks.values().forEach(FluxNetwork::onDelete);
    }

    public static void updateNetwork(@Nonnull Int2ObjectMap<CompoundTag> map, byte type) {
        for (var e : map.int2ObjectEntrySet()) {
            sNetworks.computeIfAbsent(e.getIntKey(), ClientFluxNetwork::new)
                    .readCustomTag(e.getValue(), type);
        }
    }

    public static void updateConnections(int networkID, @Nonnull List<CompoundTag> tags) {
        final FluxNetwork network = sNetworks.get(networkID);
        if (network != null) {
            for (var tag : tags) {
                final GlobalPos pos = FluxUtils.readGlobalPos(tag);
                final IFluxDevice device = network.getConnectionByPos(pos);
                if (device != null) {
                    device.readCustomTag(tag, FluxConstants.NBT_PHANTOM_UPDATE);
                }
            }
        }
    }

    @Nonnull
    public static FluxNetwork getNetwork(int id) {
        return sNetworks.getOrDefault(id, FluxNetwork.INVALID);
    }

    @Nonnull
    public static Collection<FluxNetwork> getAllNetworks() {
        return sNetworks.values();
    }

    public static void deleteNetwork(int id) {
        sNetworks.remove(id);
    }

    @Nonnull
    public static String getRecentPassword(int id) {
        return sRecentPasswords.getOrDefault(id, "");
    }

    public static void updateRecentPassword(int id, String password) {
        // remember last 5 passwords so that no need to enter password again
        for (int i = MAX_RECENT_PASSWORD_COUNT; i < sRecentPasswords.size(); i++) {
            sRecentPasswords.removeFirst();
        }
        sRecentPasswords.put(id, password);
    }

    /*public String getDisplayName(@Nonnull CompoundTag subTag) {
        FluxNetwork network = getNetwork(subTag.getInt(FluxConstants.NETWORK_ID));
        if (network.isValid()) {
            return network.getNetworkName();
        }
        return "NONE";
    }*/

    /*@Nonnull
    public static String getFeedbackText() {
        return feedback.getText();
    }

    public static int getFeedbackColor() {
        return 16733525 | (int) (Math.min(feedbackTimer, 16) * Math.min(61 - feedbackTimer, 5) * 3.1875f) << 24;
    }

    public static void setFeedbackText(FeedbackInfo info) {
        feedback = info;
        if (feedbackTimer > 0) {
            feedbackTimer = 58;
        } else {
            feedbackTimer = 60;
        }
    }*/

    /*public static void tick() {
        if (feedback != FeedbackInfo.NONE) {
            if (--feedbackTimer <= 0) {
                feedback = FeedbackInfo.NONE;
            }
        }
    }*/
}
