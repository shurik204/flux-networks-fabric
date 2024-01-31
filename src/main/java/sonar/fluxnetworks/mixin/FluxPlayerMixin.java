package sonar.fluxnetworks.mixin;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import sonar.fluxnetworks.api.FluxConstants;
import sonar.fluxnetworks.common.access.FluxPlayer;

/**
 * Reimplementation of FluxPlayer capability.
 */
@Unique
@Mixin(Player.class)
@SuppressWarnings({"MissingUnique", "override", "AddedMixinMembersNamePattern"})
public class FluxPlayerMixin implements FluxPlayer {
    private static final String FN$SUPER_ADMIN_KEY = "superAdmin";
    private static final String FN$WIRELESS_MODE_KEY = "wirelessMode";
    private static final String FN$WIRELESS_NETWORK_KEY = "wirelessNetwork";

    private boolean fn$mSuperAdmin = false;

    private int fn$mWirelessMode = 0;

    private int fn$mWirelessNetwork = FluxConstants.INVALID_NETWORK_ID;

    public boolean isSuperAdmin() {
        return fn$mSuperAdmin;
    }

    @Override
    public boolean setSuperAdmin(boolean superAdmin) {
        if (fn$mSuperAdmin != superAdmin) {
            fn$mSuperAdmin = superAdmin;
            return true;
        }
        return false;
    }

    @Override
    public int getWirelessMode() {
        return fn$mWirelessMode;
    }

    @Override
    public void setWirelessMode(int wirelessMode) {
        fn$mWirelessMode = wirelessMode;
    }

    @Override
    public int getWirelessNetwork() {
        return fn$mWirelessNetwork;
    }

    @Override
    public void setWirelessNetwork(int wirelessNetwork) {
        fn$mWirelessNetwork = wirelessNetwork;
    }

    @Override
    public void fn$set(FluxPlayer other) {
        fn$mSuperAdmin = other.isSuperAdmin();
        fn$mWirelessMode = other.getWirelessMode();
        fn$mWirelessNetwork = other.getWirelessNetwork();
    }

    @Inject(method = "addAdditionalSaveData", at = @At("TAIL"))
    public void writeNBT(CompoundTag tag, CallbackInfo ci) {
        CompoundTag fluxTag = new CompoundTag();
        fluxTag.putBoolean(FN$SUPER_ADMIN_KEY, fn$mSuperAdmin);
        fluxTag.putInt(FN$WIRELESS_MODE_KEY, fn$mWirelessMode);
        fluxTag.putInt(FN$WIRELESS_NETWORK_KEY, fn$mWirelessNetwork);

        tag.put(FluxConstants.TAG_FLUX_DATA, fluxTag);
    }

    @Inject(method = "readAdditionalSaveData", at = @At("TAIL"))
    public void readNBT(CompoundTag tag, CallbackInfo ci) {
        CompoundTag fluxTag = tag.getCompound(FluxConstants.TAG_FLUX_DATA);
        fn$mSuperAdmin = fluxTag.getBoolean(FN$SUPER_ADMIN_KEY);
        fn$mWirelessMode = fluxTag.getInt(FN$WIRELESS_MODE_KEY);
        fn$mWirelessNetwork = fluxTag.getInt(FN$WIRELESS_NETWORK_KEY);
    }
}