package sonar.fluxnetworks.common.device;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.PlayerList;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import sonar.fluxnetworks.FluxConfig;
import sonar.fluxnetworks.FluxNetworks;
import sonar.fluxnetworks.api.FluxConstants;
import sonar.fluxnetworks.api.energy.IItemEnergyConnector;
import sonar.fluxnetworks.api.network.NetworkMember;
import sonar.fluxnetworks.api.network.WirelessType;
import sonar.fluxnetworks.common.access.FluxPlayer;
import sonar.fluxnetworks.common.connection.TransferHandler;
import sonar.fluxnetworks.common.integration.TrinketsIntegration;
import sonar.fluxnetworks.common.util.EnergyUtils;
import sonar.fluxnetworks.common.util.FluxUtils;
import sonar.fluxnetworks.common.util.ItemReference;
import sonar.fluxnetworks.common.util.SlotIterator;

import javax.annotation.Nonnull;
import java.util.*;
import java.util.function.Predicate;

public class FluxControllerHandler extends TransferHandler {

    private static final Predicate<ItemStack> NOT_EMPTY = s -> !s.isEmpty();

    private final Map<ServerPlayer, Iterable<WirelessHandler>> mPlayers = new HashMap<>();
    private int mTimer;

    private long mDesired;

    final TileFluxController mDevice;

    public FluxControllerHandler(TileFluxController fluxController) {
        super(FluxConfig.defaultLimit);
        mDevice = fluxController;
    }

    @Override
    public void onCycleStart() {
        /*if (!WirelessType.ENABLE_WIRELESS.isActivated(mDevice.getNetwork())) {
            demand = 0;
            clearPlayers();
            return;
        }*/
        if (mTimer == 0) {
            updatePlayers();
        }
        if ((mTimer & 0x3) == 2) {
            // keep demand
            mDesired = chargeAllItems(getLimit(), true);
        }
    }

    @Override
    public void onCycleEnd() {
        mBuffer += mChange = -sendToConsumers(Math.min(mBuffer, getLimit()));
        mTimer = ++mTimer & 0x3f;
    }

    @Override
    public void addToBuffer(long energy) {
        mBuffer += energy;
    }

    @Override
    public long getRequest() {
        return Math.max(mDesired - mBuffer, 0);
    }

    @Override
    public void onNetworkChanged() {
        super.onNetworkChanged();
        mPlayers.clear();
    }

    @Override
    public void writeCustomTag(@Nonnull CompoundTag tag, byte type) {
        super.writeCustomTag(tag, type);
        tag.putLong(FluxConstants.BUFFER, mBuffer);
    }

    private long sendToConsumers(long energy) {
        //if (!mDevice.isActive()) return 0;
        if ((mTimer & 0x3) != 0) return 0;
        //if (!WirelessType.ENABLE_WIRELESS.isActivated(mDevice.getNetwork())) return 0;
        return chargeAllItems(energy, false);
    }

    private long chargeAllItems(long energy, boolean simulate) {
        long remaining = energy;
        for (var player : mPlayers.entrySet()) {
            // dead, or quit game
            if (!player.getKey().isAlive()) {
                continue;
            }
            for (WirelessHandler handler : player.getValue()) {
                remaining = handler.chargeItems(remaining, simulate);
                if (remaining <= 0) {
                    return energy;
                }
            }
        }
        return energy - remaining;
    }

    private void updatePlayers() {
        mPlayers.clear();

        // TODO: shouldn't there be a check for invalid network?

        PlayerList playerList = FluxNetworks.getServer().getPlayerList();
        for (NetworkMember p : mDevice.getNetwork().getAllMembers()) {
            ServerPlayer player = playerList.getPlayer(p.getPlayerUUID());
            if (player == null || !player.isAlive()) {
                continue;
            }
            FluxPlayer fluxPlayer = FluxUtils.getFluxPlayer(player);
            // Skip if player is not in the same network
            if (fluxPlayer.getWirelessNetwork() != mDevice.getNetworkID()) {
                continue;
            }
            // or wireless is not enabled
            int wirelessMode = fluxPlayer.getWirelessMode();
            if (!WirelessType.ENABLE_WIRELESS.isActivated(wirelessMode)) {
                continue;
            }
            if ((wirelessMode & ~(1 << WirelessType.ENABLE_WIRELESS.ordinal())) == 0) {
                continue;
            }
            final Inventory inventory = player.getInventory();
            final List<WirelessHandler> handlers = new ArrayList<>();
            if (WirelessType.MAIN_HAND.isActivated(wirelessMode)) {
                // TODO: there's a small delay when player changes selected item
                handlers.add(new WirelessHandler(player, Collections.singleton(new ItemReference(player, player.getInventory().selected)), NOT_EMPTY));
            }
            if (WirelessType.OFF_HAND.isActivated(wirelessMode)) {
                handlers.add(new WirelessHandler(player, () -> new SlotIterator(player, Inventory.SLOT_OFFHAND, Inventory.SLOT_OFFHAND + 1), NOT_EMPTY));
            }
            if (WirelessType.HOT_BAR.isActivated(wirelessMode)) {
                handlers.add(new WirelessHandler(player, () -> new SlotIterator(player, Inventory.getSelectionSize()),
                stack -> {
                    ItemStack heldItem;
                    return !stack.isEmpty() &&
                            ((heldItem = inventory.getSelected()).isEmpty() || heldItem != stack);
                }));
            }
            if (WirelessType.ARMOR.isActivated(wirelessMode)) {
                handlers.add(new WirelessHandler(player, () -> new SlotIterator(player, Inventory.INVENTORY_SIZE, Inventory.INVENTORY_SIZE + Inventory.ALL_ARMOR_SLOTS.length), NOT_EMPTY));
            }
            if (WirelessType.TRINKETS.isActivated(wirelessMode) && FluxNetworks.isTrinketsLoaded()) {
                handlers.add(new WirelessHandler(player, TrinketsIntegration.getItemRefs(player), NOT_EMPTY));
            }
            if (!handlers.isEmpty()) {
                mPlayers.put(player, handlers);
            }
        }
    }

    private record WirelessHandler(ServerPlayer player, Iterable<ItemReference> items, Predicate<ItemStack> validator) {

        private long chargeItems(long remaining, boolean simulate) {
            for (ItemReference ref : items) {
                IItemEnergyConnector connector;
                if (!validator.test(ref.stack) || (connector = EnergyUtils.getConnector(ref.stack)) == null) {
                    continue;
                }
                if (connector.supportsInsertion(ref.stack)) {
                        remaining -= connector.insert(remaining, player, ref.slot, simulate);
                    if (remaining <= 0) {
                        return 0;
                    }
                }
            }
            return remaining;
        }
    }
}
