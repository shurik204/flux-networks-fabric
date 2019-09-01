package fluxnetworks.common.core;

import fluxnetworks.common.registry.RegistryBlocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class FluxGuiStack {

    public static ItemStack FLUX_PLUG;
    public static ItemStack FLUX_POINT;

    static {
        NBTTagCompound tag = new NBTTagCompound();
        tag.setBoolean("GuiColor", true);
        ItemStack stack1 = new ItemStack(RegistryBlocks.FLUX_PLUG);
        ItemStack stack2 = new ItemStack(RegistryBlocks.FLUX_POINT);
        stack1.setTagCompound(tag);
        stack2.setTagCompound(tag);
        FLUX_PLUG = stack1;
        FLUX_POINT = stack2;
    }
}
