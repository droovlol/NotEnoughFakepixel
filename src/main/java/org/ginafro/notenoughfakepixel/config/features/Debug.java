package org.ginafro.notenoughfakepixel.config.features;

import com.google.gson.annotations.Expose;
import org.ginafro.notenoughfakepixel.config.gui.core.config.annotations.*;


public class Debug {

    @Expose
    @ConfigOption(name = "Debug", desc = "Enable debug mode.")
    @ConfigEditorBoolean
    public boolean debug = false;

    @Expose
    @ConfigOption(name = "Show Sounds", desc = "Show Sounds debug.")
    @ConfigEditorBoolean
    public boolean showSounds = false;

}