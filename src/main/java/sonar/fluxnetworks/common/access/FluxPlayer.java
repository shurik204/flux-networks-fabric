package sonar.fluxnetworks.common.access;

import net.minecraft.world.entity.player.Player;
import sonar.fluxnetworks.FluxConfig;
import sonar.fluxnetworks.common.util.FluxUtils;

import javax.annotation.Nonnull;

public interface FluxPlayer {
    boolean isSuperAdmin();
    boolean setSuperAdmin(boolean superAdmin);
    int getWirelessMode();
    void setWirelessMode(int wirelessMode);
    int getWirelessNetwork();
    void setWirelessNetwork(int wirelessNetwork);
    void fn$set(FluxPlayer other);
    default void set(FluxPlayer other) {
        fn$set(other);
    }

    static boolean canActivateSuperAdmin(Player player) {
        return FluxConfig.enableSuperAdmin && player.hasPermissions(FluxConfig.superAdminRequiredPermission);
    }
    static boolean isPlayerSuperAdmin(@Nonnull Player player) {
        if (FluxConfig.enableSuperAdmin) {
            FluxPlayer fluxPlayer = FluxUtils.getFluxPlayer(player);
            return fluxPlayer.isSuperAdmin();
        }
        return false;
    }
}