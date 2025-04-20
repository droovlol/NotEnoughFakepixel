package org.ginafro.notenoughfakepixel.config.features;

import com.google.gson.annotations.Expose;
import org.ginafro.notenoughfakepixel.config.gui.core.config.annotations.*;

public class Overlays {

    @Expose
    @ConfigOption(name = "Storage", desc = "Storage Overlay Settings")
    @ConfigEditorAccordion(id = 0)
    public boolean storage = false;

    @Expose
    @ConfigOption(name = "Storage Overlay", desc = "Redesign of Storage GUI")
    @ConfigEditorBoolean
    @ConfigAccordionId(id = 0)
    public boolean storageOverlay = true;

    @Expose
    @ConfigOption(name = "Storage Search", desc = "Add a search bar to storage gui(Requires Storage Overlay)")
    @ConfigEditorBoolean
    @ConfigAccordionId(id = 0)
    public boolean storageSearch = true;

    @Expose
    @ConfigOption(name = "Background Color",desc = "Color of the storage overlay")
    @ConfigEditorColour
    @ConfigAccordionId(id = 0)
    public String storageColor = "0:102:32:32:32";

    @Expose
    @ConfigOption(name = "Chest Color",desc = "Color of the storage Chest Background")
    @ConfigEditorColour
    @ConfigAccordionId(id = 0)
    public String chestColor = "0:102:32:32:32";

    @Expose
    @ConfigOption(name = "Search Highlight Color",desc = "Color to highlight search item with")
    @ConfigEditorColour
    @ConfigAccordionId(id = 0)
    public String searchColor = "0:255:0:255:0";

    @Expose
    @ConfigOption(name = "Bazaar Overlay", desc= "Redesign of Bazaar GUI")
    @ConfigEditorBoolean
    public boolean bazaarOverlay = true;

}
