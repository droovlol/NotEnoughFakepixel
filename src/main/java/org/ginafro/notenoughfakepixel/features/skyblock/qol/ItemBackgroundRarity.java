package org.ginafro.notenoughfakepixel.features.skyblock.qol;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;
import org.ginafro.notenoughfakepixel.config.features.QualityOfLife;
import org.lwjgl.opengl.GL11;


import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ItemBackgroundRarity {
    private static final ResourceLocation RARITY_TEXTURE = new ResourceLocation("notenoughfakepixel:skyblock/textures/gui/rarity.png");
    private static final Pattern RARITY_PATTERN = Pattern.compile("(§[0-9a-f]§l§ka§r )?([§0-9a-fk-or]+)(?<rarity>[A-Z]+)");
    private static final Pattern PET_PATTERN = Pattern.compile("§7\\[Lvl \\d+\\] (?<color>§[0-9a-fk-or]).+");

    public enum ItemRarity {
        COMMON("COMMON", EnumChatFormatting.WHITE),
        UNCOMMON("UNCOMMON", EnumChatFormatting.GREEN),
        RARE("RARE", EnumChatFormatting.BLUE),
        EPIC("EPIC", EnumChatFormatting.DARK_PURPLE),
        LEGENDARY("LEGENDARY", EnumChatFormatting.GOLD),
        MYTHIC("MYTHIC", EnumChatFormatting.LIGHT_PURPLE),
        DIVINE("DIVINE", EnumChatFormatting.AQUA),
        SUPREME("SUPREME", EnumChatFormatting.DARK_RED),
        SPECIAL("SPECIAL", EnumChatFormatting.RED),
        VERY_SPECIAL("VERY SPECIAL", EnumChatFormatting.RED);

        private final String name;
        private final EnumChatFormatting color;

        ItemRarity(String name, EnumChatFormatting color) {
            this.name = name;
            this.color = color;
        }

        public String getName() {
            return name;
        }

        public EnumChatFormatting getColor() {
            return color;
        }

        public static ItemRarity byBaseColor(String colorCode) {
            for (ItemRarity rarity : values()) {
                if (rarity.color.toString().equals(colorCode)) {
                    return rarity;
                }
            }
            return null;
        }
    }

    public static void renderRarityOverlay(ItemStack stack, int x, int y) {
        if (stack == null) return;

        ItemRarity rarity = getItemRarity(stack);
        if (rarity == null) return;

        renderRarityBackground(x, y, rarity);
    }

    private static void renderRarityBackground(int x, int y, ItemRarity rarity) {
        GlStateManager.disableLighting();
        GlStateManager.disableDepth();
        GlStateManager.enableBlend();
        GlStateManager.enableAlpha();

        Minecraft.getMinecraft().getTextureManager().bindTexture(RARITY_TEXTURE);
        setColorFromRarity(rarity);

        GL11.glTexEnvi(GL11.GL_TEXTURE_ENV, GL11.GL_TEXTURE_ENV_MODE, GL11.GL_BLEND);
        Gui.drawModalRectWithCustomSizedTexture(x, y, 0, 0, 16, 16, 16, 16);

        resetRenderStates();
    }

    private static void setColorFromRarity(ItemRarity rarity) {
        EnumChatFormatting color = rarity.getColor();
        int rgb = getColorValue(color);
        float r = ((rgb >> 16) & 0xFF) / 255f;
        float g = ((rgb >> 8) & 0xFF) / 255f;
        float b = (rgb & 0xFF) / 255f;

        GlStateManager.color(r, g, b, QualityOfLife.qolItemRarityOpacity);
    }

    private static int getColorValue(EnumChatFormatting format) {
        switch (format) {
            case WHITE: return 0xFFFFFF;
            case GREEN: return 0x55FF55;
            case BLUE: return 0x5555FF;
            case DARK_PURPLE: return 0xAA00AA;
            case GOLD: return 0xFFAA00;
            case LIGHT_PURPLE: return 0xFF55FF;
            case AQUA: return 0x55FFFF;
            case DARK_RED: return 0xAA0000;
            case RED: return 0xFF5555;
            default: return 0xFFFFFF;
        }
    }

    private static void resetRenderStates() {
        GL11.glTexEnvi(GL11.GL_TEXTURE_ENV, GL11.GL_TEXTURE_ENV_MODE, GL11.GL_MODULATE);
        GlStateManager.enableLighting();
        GlStateManager.enableDepth();
        GlStateManager.disableAlpha();
    }

    public static ItemRarity getItemRarity(ItemStack item) {
        if (item == null || !item.hasTagCompound()) return null;

        NBTTagCompound display = item.getSubCompound("display", false);
        if (display == null) return null;

        String name = getDisplayName(item);
        NBTTagList lore = display.getTagList("Lore", 8); // NBT type String

        // Check for pet items
        Matcher petMatcher = PET_PATTERN.matcher(name);
        if (petMatcher.find()) {
            return ItemRarity.byBaseColor(petMatcher.group("color"));
        }

        // Check regular item lore
        for (int i = 0; i < lore.tagCount(); i++) {
            String line = lore.getStringTagAt(i);
            Matcher rarityMatcher = RARITY_PATTERN.matcher(line);
            if (rarityMatcher.find()) {
                String rarityName = rarityMatcher.group("rarity");
                for (ItemRarity rarity : ItemRarity.values()) {
                    if (rarityName.startsWith(rarity.name)) {
                        return rarity;
                    }
                }
            }
        }
        return null;
    }

    private static String getDisplayName(ItemStack item) {
        if (item.hasTagCompound() && item.getTagCompound().hasKey("display", 10)) {
            NBTTagCompound display = item.getTagCompound().getCompoundTag("display");
            if (display.hasKey("Name", 8)) {
                return display.getString("Name");
            }
        }
        return item.getDisplayName();
    }
}