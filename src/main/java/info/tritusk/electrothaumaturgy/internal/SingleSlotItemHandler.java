package info.tritusk.electrothaumaturgy.internal;

import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nonnull;

public class SingleSlotItemHandler implements IItemHandler {

    private ItemStack content = ItemStack.EMPTY;

    /**
     * @return The direct reference of its content.
     */
    public ItemStack getRawContent() {
        return content;
    }

    public void setRawContent(ItemStack newContent) {
        this.content = newContent;
    }

    @Override
    public int getSlots() {
        return 1;
    }

    @Nonnull
    @Override
    public ItemStack getStackInSlot(int slot) {
        return slot == 0 ? content : ItemStack.EMPTY;
    }

    @Nonnull
    @Override
    public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
        if (slot == 0) {
            if (this.content.isEmpty()) {
                if (!simulate) {
                    this.content = stack;
                }
                return ItemStack.EMPTY;
            } else {
                if (this.content.isItemEqual(stack)) {
                    int newCount = Math.max(this.content.getMaxStackSize(), this.content.getCount() + stack.getCount());
                    int diff = this.content.getMaxStackSize() - newCount;
                    if (diff > 0) {
                        ItemStack splitted = simulate ? stack.copy() : stack;
                        return splitted.splitStack(diff);
                    } else {
                        if (!simulate) {
                            this.content.grow(stack.getCount());
                        }
                        return ItemStack.EMPTY;
                    }
                } else {
                    return ItemStack.EMPTY;
                }
            }
        } else {
            return stack;
        }
    }

    @Nonnull
    @Override
    public ItemStack extractItem(int slot, int amount, boolean simulate) {
        if (slot == 0) {
            if (this.content.isEmpty()) {
                return ItemStack.EMPTY;
            }
            ItemStack splitted = simulate ? this.content.copy() : this.content;
            return splitted.splitStack(amount);
        } else {
            return ItemStack.EMPTY;
        }
    }

    @Override
    public int getSlotLimit(int slot) {
        return slot == 0 ? content.getMaxStackSize() : 0;
    }
}
