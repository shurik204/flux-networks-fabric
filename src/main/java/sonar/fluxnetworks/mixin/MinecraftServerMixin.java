package sonar.fluxnetworks.mixin;

import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import sonar.fluxnetworks.FluxNetworks;

/**
 * Store the server instance in FluxNetworks.
 */
@Mixin(value = MinecraftServer.class, priority = Integer.MIN_VALUE)
public class MinecraftServerMixin {
    @Inject(method = "<init>", at = @At("TAIL"))
    private void init(CallbackInfo ci) { FluxNetworks.setServer((MinecraftServer) (Object) this); }
    @Inject(method = "stopServer", at = @At("TAIL"))
    private void shutdown(CallbackInfo ci) { FluxNetworks.setServer(null); }
}