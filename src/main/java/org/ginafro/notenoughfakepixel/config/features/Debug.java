package org.ginafro.notenoughfakepixel.config.features;

import com.google.gson.annotations.Expose;
import org.ginafro.notenoughfakepixel.config.gui.core.config.annotations.*;


public class Debug {

    @Expose
    @ConfigOption(name = "Debug", desc = "Enable debug mode.")
    @ConfigEditorBoolean
    public boolean debug = false;

    @Expose
    @ConfigOption(name = "Sound Debug", desc = "Show Sounds debug.")
    @ConfigEditorBoolean
    public boolean showSounds = false;

    @Expose
    @ConfigOption(name = "Force Pojav", desc = "Force Pojav detection in the mod.")
    @ConfigEditorBoolean
    public boolean forcePojav = false;

}