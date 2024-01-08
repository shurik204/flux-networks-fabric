package sonar.fluxnetworks.common.util;

import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.api.transfer.v1.item.PlayerInventoryStorage;
import net.fabricmc.fabric.api.transfer.v1.storage.base.SingleSlotStorage;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nonnull;

@SuppressWarnings("UnstableApiUsage")
public class ItemReference {
    public final ItemStack stack;
    public final SingleSlotStorage<ItemVariant> slot;

    public ItemReference(@Nonnull Player player, int index) {
        this(player.getInventory().getItem(index), PlayerInventoryStorage.of(player.getInventory()).getSlot(index));
    }

    public ItemReference(@Nonnull ItemStack stack, @Nonnull SingleSlotStorage<ItemVariant> slot) {
        this.stack = stack;
        this.slot = slot;
    }
}