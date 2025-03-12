package org.ginafro.notenoughfakepixel.config.features;

import com.google.gson.annotations.Expose;
import org.ginafro.notenoughfakepixel.config.gui.core.config.annotations.ConfigEditorBoolean;
import org.ginafro.notenoughfakepixel.config.gui.core.config.annotations.ConfigOption;

public class General {

    @Expose
    @ConfigOption(name = "Debug", desc = "Enable debug mode.")
    @ConfigEditorBoolean
    public static boolean debug = false;

}