package org.ginafro.notenoughfakepixel.features.skyblock.overlays.storage;

import net.minecraft.inventory.ContainerChest;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTException;
import net.minecraft.nbt.NBTTagCompound;

import java.util.HashMap;
import java.util.Map;

public class StorageData {
    public String chestName;
    public Map<Integer, String> items;

    public StorageData(ContainerChest chest) {
        String s = chest.getLowerChestInventory().getDisplayName().getUnformattedText();
        if(s.contains("Backpack")){
            String[] a = s.split(" ");
            String bagNumber = a[a.length - 1].split("/")[0];
            chestName = "Backpack" + bagNumber;
        }else {
            chestName = s;
        }
        this.items = new HashMap<>();
        for (int i = 9; i < chest.getLowerChestInventory().getSizeInventory(); i++) {
            ItemStack stack = chest.getLowerChestInventory().getStackInSlot(i);
            if (stack != null) {
                items.put((i-9), itemStackToJson(stack)); // Convert ItemStack to JSON
            }
        }
    }

    public static String itemStackToJson(ItemStack stack) {
        NBTTagCompound nbt = new NBTTagCompound();
        stack.writeToNBT(nbt);
        return nbt.toString();
    }

    public static ItemStack jsonToItemStack(String json) throws NBTException {
        NBTTagCompound nbt = net.minecraft.nbt.JsonToNBT.getTagFromJson(json);
        return ItemStack.loadItemStackFromNBT(nbt);
    }

    public Map<Integer, ItemStack> getItemStacks() throws NBTException {
        Map<Integer, ItemStack> itemStacks = new HashMap<>();
        for (Map.Entry<Integer, String> entry : items.entrySet()) {
            itemStacks.put(entry.getKey(), jsonToItemStack(entry.getValue())); // Convert JSON back to ItemStack
        }
        return itemStacks;
    }

}