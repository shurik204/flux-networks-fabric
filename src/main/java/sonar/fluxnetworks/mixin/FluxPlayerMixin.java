package sonar.fluxnetworks.mixin;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import sonar.fluxnetworks.common.access.FluxPlayer;
@Unique
@Mixin(Player.class)
public class FluxPlayerMixin implements FluxPlayer {
    private static final String SUPER_ADMIN_KEY = "superAdmin";

    private boolean fn$mSuperAdmin = false;

    private int fn$mWirelessMode = 0;

    private int fn$mWirelessNetwork = -1;

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
        tag.putBoolean(SUPER_ADMIN_KEY, fn$mSuperAdmin);
        tag.putInt("wirelessMode", fn$mWirelessMode);
        tag.putInt("wirelessNetwork", fn$mWirelessNetwork);
    }

    @Inject(method = "readAdditionalSaveData", at = @At("TAIL"))
    public void readNBT(CompoundTag tag, CallbackInfo ci) {
        fn$mSuperAdmin = tag.getBoolean(SUPER_ADMIN_KEY);
        fn$mWirelessMode = tag.getInt("wirelessMode");
        fn$mWirelessNetwork = tag.getInt("wirelessNetwork");
    }
}
