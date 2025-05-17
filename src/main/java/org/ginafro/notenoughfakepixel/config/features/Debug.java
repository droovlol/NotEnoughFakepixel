package org.ginafro.notenoughfakepixel.config.features;

import com.google.gson.annotations.Expose;
import org.ginafro.notenoughfakepixel.config.gui.core.config.annotations.ConfigEditorBoolean;
import org.ginafro.notenoughfakepixel.config.gui.core.config.annotations.ConfigEditorButton;
import org.ginafro.notenoughfakepixel.config.gui.core.config.annotations.ConfigOption;


public class Debug {

    @Expose
    @ConfigOption(name = "Debug Logs", desc = "Enable debug mode for logging.")
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

    @Expose
    @ConfigOption(name = "Enable out of fakepixel", desc = "Enables mod out of fakepixel server.")
    @ConfigEditorBoolean
    public boolean enableOutOfFakepixel = false;

    @Expose
    @ConfigOption(name = "Log location", desc = "Log current location.")
    @ConfigEditorButton(buttonText = "Log", runnableId = "logLocation")
    public String logLocationButton = "";

    @Expose
    @ConfigOption(name = "Log Scoreboard", desc = "Log current scoreboard data.")
    @ConfigEditorButton(buttonText = "Log", runnableId = "logScoreboard")
    public String logScoreboardButton = "";
}