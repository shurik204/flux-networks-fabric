package sonar.fluxnetworks.client.render;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import sonar.fluxnetworks.FluxNetworks;


/**
 * Render energy sides.
 */
public class FluxStorageRenderType extends RenderType {

    private static final ResourceLocation ENERGY_TEXTURE = FluxNetworks.location("textures/block/storage/energy.png");

    public static final RenderType ENTITY_TRANSLUCENT;
    public static final RenderType CUSTOM_TYPE;

    static {
        ENTITY_TRANSLUCENT = entityTranslucentCull(ENERGY_TEXTURE);

        CUSTOM_TYPE = create(FluxNetworks.MODID + ":storage_energy", DefaultVertexFormat.NEW_ENTITY, VertexFormat.Mode.QUADS, 256, false, true, CompositeState.builder()
                .setTextureState(new TextureStateShard(ENERGY_TEXTURE, false, false))
                .setShaderState(RENDERTYPE_ENTITY_TRANSLUCENT_CULL_SHADER)
                .setTransparencyState(CRUMBLING_TRANSPARENCY)
                .setLightmapState(LIGHTMAP)
                .setOverlayState(OVERLAY)
                .createCompositeState(true));
    }

    private FluxStorageRenderType(String name, VertexFormat format, VertexFormat.Mode mode, int bufferSize,
                                  boolean affectsCrumbling, boolean sortOnUpload, Runnable setupState,
                                  Runnable clearState) {
        super(name, format, mode, bufferSize, affectsCrumbling, sortOnUpload, setupState, clearState);
    }
}
