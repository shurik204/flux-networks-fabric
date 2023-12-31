package sonar.fluxnetworks.common.block;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import sonar.fluxnetworks.api.FluxTranslate;
import sonar.fluxnetworks.common.device.TileFluxController;
import sonar.fluxnetworks.common.device.TileFluxDevice;
import sonar.fluxnetworks.common.util.FluxShapes;
import sonar.fluxnetworks.register.RegistryBlockEntityTypes;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

@ParametersAreNonnullByDefault
public class FluxControllerBlock extends FluxDeviceBlock {

    public FluxControllerBlock(Properties props) {
        super(props);
    }

    @Nonnull
    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return FluxShapes.FLUX_CONTROLLER_VOXEL;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable BlockGetter level, List<Component> tooltip,
                                TooltipFlag flag) {
        super.appendHoverText(stack, level, tooltip, flag);
        tooltip.add(FluxTranslate.FLUX_CONTROLLER_TOOLTIP.getComponent());
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new TileFluxController(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state,
                                                                  BlockEntityType<T> type) {
        if (type == RegistryBlockEntityTypes.FLUX_CONTROLLER) {
            return TileFluxDevice.getTicker(level);
        }
        return null;
    }
}
