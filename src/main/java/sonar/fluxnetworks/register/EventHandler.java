package sonar.fluxnetworks.register;

import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import sonar.fluxnetworks.FluxConfig;
import sonar.fluxnetworks.api.FluxConstants;
import sonar.fluxnetworks.common.connection.FluxNetwork;
import sonar.fluxnetworks.common.connection.FluxNetworkData;
import sonar.fluxnetworks.common.util.FluxCommands;

import java.util.List;

public class EventHandler {

    //// SERVER EVENTS \\\\

    public static void init() {
        // mainly used to reload data while changing single-player saves, unnecessary on dedicated server
        ServerLifecycleEvents.SERVER_STOPPED.register(server -> FluxNetworkData.release());
        ServerTickEvents.END_SERVER_TICK.register(server -> FluxNetworkData.getAllNetworks().forEach(FluxNetwork::onEndServerTick));
        UseBlockCallback.EVENT.register(EventHandler::onPlayerInteract);
        ServerPlayConnectionEvents.JOIN.register(EventHandler::onPlayerJoined);
        ServerPlayerEvents.COPY_FROM.register(EventHandler::onPlayerClone);
        CommandRegistrationCallback.EVENT.register((dispatcher, registry, selection) -> FluxCommands.register(dispatcher));
    }

//    @SubscribeEvent
//    public static void onServerTick(@Nonnull TickEvent.ServerTickEvent event) {

//        if (event.phase == TickEvent.Phase.END) {
//            FluxNetworkData.getAllNetworks().forEach(FluxNetwork::onEndServerTick);
//        }
//    }

    //// WORLD EVENTS \\\\

    /*@SubscribeEvent(priority = EventPriority.LOW)
    public static void onWorldLoad(@Nonnull WorldEvent.Load event) {
        if (!event.getWorld().isClientSide()) {
            ServerLevel world = (ServerLevel) event.getWorld();
            world.getServer().enqueue(new TickDelayedTask(world.getServer().getTickCounter(), () ->
                    FluxChunkManager.loadWorld(world)));
        }
    }

    @SubscribeEvent
    public static void onWorldTick(@Nonnull TickEvent.WorldTickEvent event) {
        if (!event.world.isRemote && event.phase == TickEvent.Phase.END) {
            FluxChunkManager.tickWorld((ServerWorld) event.world);
        }
    }*/

    //// PLAYER EVENTS \\\\

    private static InteractionResult onPlayerInteract(Player player, Level pLevel, InteractionHand interactionHand, BlockHitResult blockHitResult) {
        if (!FluxConfig.enableFluxRecipe || pLevel.isClientSide || player.isSpectator()) {
            return InteractionResult.PASS;
        }
        ServerLevel level = (ServerLevel) pLevel;
        BlockPos pos = blockHitResult.getBlockPos();
        BlockState crusher = level.getBlockState(pos);
        BlockState base;
        if (crusher.getBlock() == Blocks.OBSIDIAN &&
                ((base = level.getBlockState(pos.below(2))).getBlock() == Blocks.BEDROCK ||
                        base.getBlock() == RegistryBlocks.FLUX_BLOCK)) {
            List<ItemEntity> entities = level.getEntitiesOfClass(ItemEntity.class, new AABB(pos.below()));
            if (entities.isEmpty()) {
                return InteractionResult.PASS;
            }
            int itemCount = 0;
            for (ItemEntity entity : entities) {
                if (entity.getItem().is(Items.REDSTONE)) {
                    itemCount += entity.getItem().getCount();
                    entity.discard();
                    if (itemCount >= 512) {
                        break;
                    }
                }
            }
            if (itemCount == 0) {
                return InteractionResult.PASS;
            }
            ItemStack stack = new ItemStack(RegistryItems.FLUX_DUST, itemCount);
            level.removeBlock(pos, false);
            ItemEntity entity = new ItemEntity(level, pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, stack);
            entity.setNoPickUpDelay();
            entity.setDeltaMovement(0, 0.2, 0);
            level.addFreshEntity(entity);
            // give it a chance to turn into cobbles
            if (level.getRandom().nextDouble() > Math.pow(0.9, itemCount >> 3)) {
                level.setBlock(pos.below(), Blocks.COBBLESTONE.defaultBlockState(), Block.UPDATE_ALL);
                level.playSound(null, pos, SoundEvents.DRAGON_FIREBALL_EXPLODE, SoundSource.BLOCKS, 1.0f, 1.0f);
            } else {
                level.setBlock(pos.below(), crusher, Block.UPDATE_ALL);
                level.playSound(null, pos, SoundEvents.GENERIC_EXPLODE, SoundSource.BLOCKS, 1.0f, 1.0f);
            }
            int particleCount = Mth.clamp(itemCount >> 2, 4, 64);
            level.sendParticles(ParticleTypes.LAVA, pos.getX() + 0.5, pos.getY(),
                    pos.getZ() + 0.5, particleCount, 0, 0, 0, 0);

            // we succeed
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.PASS;
    }

    /*@SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onEntityAdded(EntityJoinWorldEvent event) {
        if (!FluxConfig.enableFluxRecipe || !FluxConfig.enableOldRecipe || event.getWorld().isRemote) {
            return;
        }
        final Entity entity = event.getEntity();
        if (entity instanceof ItemEntity && !(entity instanceof FireItemEntity)) {
            ItemEntity entityItem = (ItemEntity) entity;
            ItemStack stack = entityItem.getItem();
            if (!stack.isEmpty() && stack.getItem() == Items.REDSTONE) {
                FireItemEntity newEntity = new FireItemEntity(entityItem);
                entityItem.remove();
                event.getWorld().addEntity(newEntity);
                event.setCanceled(true);
            }
        }
    }*/

    private static void onPlayerJoined(ServerGamePacketListenerImpl serverGamePacketListener, PacketSender packetSender, MinecraftServer minecraftServer) {
        // this event only fired on server
        Channel.get().sendToPlayer(Messages.updateNetwork(
                FluxNetworkData.getAllNetworks(), FluxConstants.NBT_NET_BASIC), serverGamePacketListener.player);
        Messages.syncCapability(serverGamePacketListener.player);
    }

//    TODO: fix after FluxPlayer capability is reimplemented
//    @SubscribeEvent
//    public static void onAttachCapability(@Nonnull AttachCapabilitiesEvent<Entity> event) {
//        // make server only
//        if (event.getObject() instanceof ServerPlayer) {
//            var provider = new FluxPlayerProvider();
//            event.addCapability(FluxPlayerProvider.CAP_KEY, provider);
//            // XXX: no invalidation should not be a problem
//            //event.addListener(provider);
//        }
//    }

    private static void onPlayerClone(ServerPlayer oldPlayer, ServerPlayer newPlayer, boolean alive) {
//        TODO: fix after FluxPlayer capability is reimplemented
        // server only event
//        event.getOriginal().reviveCaps();
//        FluxPlayer oFluxPlayer = FluxUtils.get(event.getOriginal(), FluxPlayer.FLUX_PLAYER);
//        if (oFluxPlayer != null) {
//            FluxPlayer nFluxPlayer = FluxUtils.get(event.getEntity(), FluxPlayer.FLUX_PLAYER);
//            if (nFluxPlayer != null) {
//                nFluxPlayer.set(oFluxPlayer);
//            }
//        }
//        event.getOriginal().invalidateCaps();
    }

    //// TILE EVENTS \\\\

    /*@SubscribeEvent
    public static void onFluxConnected(@Nonnull FluxConnectionEvent.Connected event) {
        if (!event.flux.getFluxWorld().isRemote) {
            event.flux.connect(event.network);
        }
    }

    @SubscribeEvent
    public static void onFluxDisconnect(@Nonnull FluxConnectionEvent.Disconnected event) {
        if (!event.flux.getFluxWorld().isRemote) {
            event.flux.disconnect(event.network);
        }
    }*/
}
