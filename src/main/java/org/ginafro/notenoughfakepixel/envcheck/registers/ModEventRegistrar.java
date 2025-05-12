package org.ginafro.notenoughfakepixel.envcheck.registers;

import net.minecraftforge.common.MinecraftForge;
import org.reflections.Reflections;

import java.util.Set;

public class ModEventRegistrar {

    public static void registerModEvents(){
        Reflections reflections = new Reflections("org.ginafro.notenoughfakepixel");
        Set<Class<?>> classes = reflections.getTypesAnnotatedWith(RegisterEvents.class);

        for (Class<?> clazz : classes) {
            try {
                Object instance = clazz.newInstance(); // Java 8-style
                MinecraftForge.EVENT_BUS.register(instance);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
