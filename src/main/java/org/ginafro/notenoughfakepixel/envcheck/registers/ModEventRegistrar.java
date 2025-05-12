package org.ginafro.notenoughfakepixel.envcheck.registers;

import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import org.reflections.Reflections;
import org.reflections.scanners.FieldAnnotationsScanner;
import org.reflections.util.ConfigurationBuilder;

import java.lang.reflect.Field;
import java.util.Set;

public class ModEventRegistrar {

    public static void registerModEvents() {
        new Reflections("org.ginafro.notenoughfakepixel")
                .getTypesAnnotatedWith(RegisterEvents.class)
                .forEach(clazz -> {
                    try {
                        MinecraftForge.EVENT_BUS.register(clazz.newInstance());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
    }

    public static void registerKeybinds() {
        Reflections reflections = new Reflections(new ConfigurationBuilder()
                .forPackages("org.ginafro.notenoughfakepixel")
                .addScanners(new FieldAnnotationsScanner()));

        reflections.getTypesAnnotatedWith(RegisterKeybind.class);

        Set<Field> fields = reflections.getFieldsAnnotatedWith(RegisterKeybind.class);
        for (Field field : fields) {
            try {
                if (KeyBinding.class.isAssignableFrom(field.getType())) {
                    KeyBinding key = (KeyBinding) field.get(null); // static field, so null instance
                    ClientRegistry.registerKeyBinding(key);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
