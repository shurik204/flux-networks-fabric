package sonar.fluxnetworks.common.integration;

import dev.emi.trinkets.api.SlotReference;
import dev.emi.trinkets.api.TrinketComponent;
import dev.emi.trinkets.api.TrinketsApi;
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.base.SingleSlotStorage;
import net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext;
import net.fabricmc.fabric.api.transfer.v1.transaction.base.SnapshotParticipant;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Tuple;
import net.minecraft.world.item.ItemStack;
import sonar.fluxnetworks.FluxNetworks;
import sonar.fluxnetworks.common.util.ItemReference;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.Iterator;
import java.util.Optional;

/**
 * A holder class that prevents class-loading when Trinkets is not available.
 *
 * @see FluxNetworks#isTrinketsLoaded()
 */
public class TrinketsIntegration {

    // NOTE: the return value will be no longer valid when the player dies (isAlive() == false)
    // TODO: does it apply to Trinkets?
    @Nonnull
    public static Iterable<ItemReference> getItemRefs(ServerPlayer player) {
        final Optional<TrinketComponent> trinkets = TrinketsApi.getTrinketComponent(player);
        if (trinkets.isPresent()) {
            final TrinketComponent peek = trinkets.orElseThrow(RuntimeException::new);
            return () -> new RefIterator(peek);
        }
        return Collections.emptyList();
    }

    private static class RefIterator implements Iterator<ItemReference> {
        private final Iterator<Tuple<SlotReference, ItemStack>> mIterator;

        RefIterator(TrinketComponent trinket) {
            mIterator = trinket.getAllEquipped().iterator();
        }

        @Override
        public boolean hasNext() {
            return mIterator.hasNext();
        }

        @Override
        public ItemReference next() {
            Tuple<SlotReference, ItemStack> itemTuple = mIterator.next();
            return new ItemReference(itemTuple.getB(), new SlotWrapper(itemTuple));
        }
    }

    @SuppressWarnings("UnstableApiUsage")
    public static class SlotWrapper extends SnapshotParticipant<ItemVariant> implements SingleSlotStorage<ItemVariant> {
        private final SlotReference slotRef;
        private final int itemCount;
        private ItemVariant variant;

        public SlotWrapper(Tuple<SlotReference, ItemStack> tuple) {
            this.slotRef = tuple.getA();
            this.itemCount = tuple.getB().getCount();
            this.variant = ItemVariant.of(tuple.getB());
        }

        @Override
        public long insert(ItemVariant resource, long maxAmount, TransactionContext transaction) {
            updateSnapshots(transaction);
            variant = resource;
            return 1L;
        }

        @Override
        public long extract(ItemVariant resource, long maxAmount, TransactionContext transaction) {
            updateSnapshots(transaction);
            return 1L;
        }

        @Override
        public boolean isResourceBlank() {
            return variant.isBlank();
        }

        @Override
        public ItemVariant getResource() {
            return variant;
        }

        @Override
        public long getAmount() {
            return itemCount;
        }

        @Override
        public long getCapacity() {
            return variant.getItem().getMaxStackSize();
        }

        @Override
        protected ItemVariant createSnapshot() {
            return variant;
        }

        @Override
        protected void readSnapshot(ItemVariant snapshot) {
            variant = snapshot;
        }

        @Override
        public void onFinalCommit() {
            slotRef.inventory().setItem(slotRef.index(), variant.toStack(itemCount));
        }
    }
}