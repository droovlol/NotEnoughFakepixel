package org.ginafro.notenoughfakepixel.config.features;

import com.google.gson.annotations.Expose;
import org.ginafro.notenoughfakepixel.config.gui.core.config.annotations.ConfigEditorBoolean;
import org.ginafro.notenoughfakepixel.config.gui.core.config.annotations.ConfigOption;

import java.util.HashMap;
import java.util.Map;

public class Experimentation {
    @Expose
    private Map<String, Object> staticFieldValues = new HashMap<>();

    @Expose
    @ConfigOption(name = "Chronomatron Solver", desc = "Enable Chronomatron solver.")
    @ConfigEditorBoolean
    public boolean experimentationChronomatronSolver = true;

    @Expose
    @ConfigOption(name = "Ultrasequencer Solver", desc = "Enable Ultrasequencer solver.")
    @ConfigEditorBoolean
    public boolean experimentationUltraSequencerSolver = true;

    @Expose
    @ConfigOption(name = "Hide Tooltips", desc = "Hide tooltips during experiments.")
    @ConfigEditorBoolean
    public boolean experimentationHideTooltips = true;

    @Expose
    @ConfigOption(name = "Prevent Missclicks", desc = "Prevent missclicks during experiments.")
    @ConfigEditorBoolean
    public boolean experimentationPreventMissclicks = true;
}