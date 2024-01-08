package sonar.fluxnetworks.common.util;

import net.minecraft.world.entity.player.Player;

import java.util.Iterator;

public class SlotIterator implements Iterator<ItemReference> {
    private final Player player;
    private int index;
    private final int end;

    public SlotIterator(Player player, int end) {
        this(player, 0, end);
    }

    public SlotIterator(Player player, int start, int end) {
        this.player = player;
        this.index = start;
        this.end = end;
    }

    @Override
    public boolean hasNext() {
        return index < end;
    }

    @Override
    public ItemReference next() {
        return new ItemReference(player, index++);
    }
}