package sonar.fluxnetworks.common.integration;

import dev.emi.trinkets.api.SlotReference;
import dev.emi.trinkets.api.TrinketComponent;
import dev.emi.trinkets.api.TrinketsApi;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Tuple;
import net.minecraft.world.item.ItemStack;
import sonar.fluxnetworks.FluxNetworks;

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
    @Nonnull
    public static Iterable<ItemStack> getFlatStacks(ServerPlayer player) {
        final Optional<TrinketComponent> trinkets = TrinketsApi.getTrinketComponent(player);
        if (trinkets.isPresent()) {
            final TrinketComponent peek = trinkets.orElseThrow(RuntimeException::new);
            return () -> new FlatIterator(peek);
        }
        return Collections.emptyList();
    }

    private static class FlatIterator implements Iterator<ItemStack> {

        private final Iterator<Tuple<SlotReference, ItemStack>> mIterator;

        FlatIterator(TrinketComponent trinket) {
            mIterator = trinket.getAllEquipped().iterator();
        }

        @Override
        public boolean hasNext() {
            return mIterator.hasNext();
        }

        @Override
        public ItemStack next() {
            return mIterator.next().getB();
        }
    }
}
