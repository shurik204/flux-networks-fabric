package sonar.fluxnetworks.common.connection;

import net.minecraft.world.entity.player.Player;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import sonar.fluxnetworks.api.network.AccessLevel;
import sonar.fluxnetworks.client.ClientCache;
import sonar.fluxnetworks.common.device.TileFluxDevice;

import javax.annotation.Nonnull;
import java.util.List;

@Environment(EnvType.CLIENT)
public class ClientFluxNetwork extends FluxNetwork {

    public ClientFluxNetwork(int ignored) {
    }

    @Override
    public void onEndServerTick() {
        throw new IllegalStateException();
    }

    @Nonnull
    @Override
    public List<TileFluxDevice> getLogicalDevices(int logic) {
        throw new IllegalStateException();
    }

    @Override
    public long getBufferLimiter() {
        throw new IllegalStateException();
    }

    @Override
    public boolean enqueueConnectionAddition(@Nonnull TileFluxDevice device) {
        throw new IllegalStateException();
    }

    @Override
    public void enqueueConnectionRemoval(@Nonnull TileFluxDevice device, boolean unload) {
        throw new IllegalStateException();
    }

    @Override
    public boolean isValid() {
        return true;
    }

    @Nonnull
    @Override
    public AccessLevel getPlayerAccess(@Nonnull Player player) {
        if (ClientCache.sSuperAdmin) {
            return AccessLevel.SUPER_ADMIN;
        }
        return super.getPlayerAccess(player);
    }
}
