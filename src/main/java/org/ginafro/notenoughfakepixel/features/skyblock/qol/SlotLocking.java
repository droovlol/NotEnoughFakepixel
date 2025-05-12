package org.ginafro.notenoughfakepixel.features.skyblock.qol;

import net.minecraft.inventory.Slot;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.event.entity.item.ItemTossEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;
import java.util.List;

public class SlotLocking {

    public static List<Slot> lockedSlots = new ArrayList<>();

    @SubscribeEvent
    public void onInput(GuiScreenEvent.KeyboardInputEvent e) {
//        if (e.gui instanceof GuiContainer) {
//           if(Keyboard.isKeyDown(NotEnoughFakepixel.slotLocking.getKeyCode())){
//                GuiContainer container = (GuiContainer) e.gui;
//                Slot slot = container.getSlotUnderMouse();
//                System.out.println("slot: " + slot.slotNumber);
//                if(lockedSlots.contains(slot)){
//                    System.out.println("contains");
//                    lockedSlots.add(slot);
//                    Minecraft.getMinecraft().getTextureManager().bindTexture(new ResourceLocation("notenoughfakepixel:skyblock/lock.png"));
//                    e.gui.drawTexturedModalRect(slot.xDisplayPosition, slot.yDisplayPosition, 16,16,16,16);
//                }else{
//                    System.out.println("contains");
//                    lockedSlots.add(slot);
//                    Minecraft.getMinecraft().getTextureManager().bindTexture(new ResourceLocation("notenoughfakepixel:skyblock/lock.png"));
//                    e.gui.drawTexturedModalRect(slot.xDisplayPosition, slot.yDisplayPosition, 16,16,16,16);
//                }
//           }
//        }
    }

    @SubscribeEvent
    public void onOpen(GuiScreenEvent.DrawScreenEvent e) {
//        if(e.gui instanceof GuiContainer){
//            GuiContainer container = (GuiContainer) e.gui;
//            for(Slot s : container.inventorySlots.inventorySlots){
//                for(Slot s1 : lockedSlots){
//                    if(s1 == s){
//                        Minecraft.getMinecraft().getTextureManager().bindTexture(new ResourceLocation("notenoughfakepixel:skyblock/lock.png"));
//                        e.gui.drawTexturedModalRect(s.xDisplayPosition, s.yDisplayPosition, 16,16,16,16);
//                    }
//                }
//            }
//        }
    }

    @SubscribeEvent
    public void onDrop(ItemTossEvent e) {
        for (Slot s : lockedSlots) {
            if (s.getStack() == e.entityItem.getEntityItem()) {
                if (e.isCancelable()) {
                    e.setCanceled(true);
                }
            }
        }
    }

}
