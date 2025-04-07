package org.ginafro.notenoughfakepixel.config.features;

import com.google.gson.annotations.Expose;
import org.ginafro.notenoughfakepixel.config.gui.core.config.annotations.ConfigEditorBoolean;
import org.ginafro.notenoughfakepixel.config.gui.core.config.annotations.ConfigOption;

public class Overlays {

    @Expose
    @ConfigOption(name = "Bazaar Overlay", desc= "Redesign of Bazaar GUI")
    @ConfigEditorBoolean
    public boolean bazaarOverlay = true;

}
