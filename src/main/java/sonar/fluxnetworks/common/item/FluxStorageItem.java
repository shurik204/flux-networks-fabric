package sonar.fluxnetworks.common.item;

import net.minecraft.world.level.block.Block;

public class FluxStorageItem extends FluxDeviceItem {

    public FluxStorageItem(Block block, Properties props) {
        super(block, props);
    }

    // TODO: somehow replace custom item renderer registration
    // @Override
    // public void initializeClient(@Nonnull Consumer<IClientItemExtensions> consumer) {
    //     consumer.accept(new IClientItemExtensions() {
    //         @Override
    //         public BlockEntityWithoutLevelRenderer getCustomRenderer() {
    //             return new FluxStorageItemRenderer();
    //         }
    //     });
    // }
}