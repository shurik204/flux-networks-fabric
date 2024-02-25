package sonar.fluxnetworks.mixin.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import sonar.fluxnetworks.client.ClientCache;

/**
 * Release the cache when the integrated server is stopped.
 */
@Mixin(Minecraft.class)
public class MinecraftClientMixin {
    @Inject(method = "clearClientLevel", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/GameRenderer;resetData()V", shift = At.Shift.AFTER, by = 1))
    void releaseFNCache(Screen screen, CallbackInfo ci) {
        ClientCache.release();
    }
}