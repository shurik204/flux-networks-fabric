package sonar.fluxnetworks.common.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
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
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import sonar.fluxnetworks.api.FluxTranslate;
import sonar.fluxnetworks.common.device.TileFluxDevice;
import sonar.fluxnetworks.common.device.TileFluxPoint;
import sonar.fluxnetworks.common.util.FluxShapes;
import sonar.fluxnetworks.common.util.FluxUtils;
import sonar.fluxnetworks.register.RegistryBlockEntityTypes;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

@ParametersAreNonnullByDefault
public class FluxPointBlock extends FluxConnectorBlock {

    public FluxPointBlock(Properties props) {
        super(props);
    }

    @Nonnull
    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        VoxelShape shape = FluxShapes.FLUX_POINT_CENTRE_VOXEL;
        for (Direction direction : FluxUtils.DIRECTIONS) {
            if (state.getValue(SIDES_CONNECTED[direction.get3DDataValue()])) {
                shape = Shapes.or(shape, FluxShapes.CONNECTORS_ROTATED_VOXELS[direction.get3DDataValue()]);
            }
        }
        return shape;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable BlockGetter level, List<Component> tooltip,
                                TooltipFlag flag) {
        super.appendHoverText(stack, level, tooltip, flag);
        FluxTranslate.FLUX_POINT_TOOLTIP.getSplit(FluxTranslate.SPLIT_LENGTH).forEach(line -> tooltip.add(Component.literal(line)));
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new TileFluxPoint(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state,
                                                                  BlockEntityType<T> type) {
        if (type == RegistryBlockEntityTypes.FLUX_POINT) {
            return TileFluxDevice.getTicker(level);
        }
        return null;
    }
}
