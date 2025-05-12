package org.ginafro.notenoughfakepixel.features.skyblock.diana;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.network.play.server.S2APacketParticles;
import net.minecraft.util.BlockPos;
import org.ginafro.notenoughfakepixel.NotEnoughFakepixel;
import org.ginafro.notenoughfakepixel.config.gui.Config;
import org.ginafro.notenoughfakepixel.utils.SoundUtils;
import org.ginafro.notenoughfakepixel.utils.Waypoint;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;

public class ParticleProcessor {
    private final String waypointSound = "random.pop";
    private final String waypointTreasureSound = "random.pop";
    private final float volumeWaypointSound = 4.0f;
    private final float volumeWaypointTreasureSound = 3.0f;
    private final float distanceThreshold = 2.0f;
    private final int particleThreshold = 6;

    private final Queue<S2APacketParticles> particleEnchantmentTableQueue = new ConcurrentLinkedQueue<>();
    private final Queue<S2APacketParticles> particleCritQueue = new ConcurrentLinkedQueue<>();
    private final Queue<S2APacketParticles> particleMagicCritQueue = new ConcurrentLinkedQueue<>();
    private final Queue<S2APacketParticles> particleDripLavaQueue = new ConcurrentLinkedQueue<>();

    private final List<Waypoint> waypoints = Collections.synchronizedList(new ArrayList<>());

    public void addParticle(S2APacketParticles particle) {
        if (particle == null) return;

        switch (particle.getParticleType().getParticleName()) {
            case "crit":
                particleCritQueue.add(particle);
                break;
            case "magicCrit":
                particleMagicCritQueue.add(particle);
                break;
            case "enchantmenttable":
                particleEnchantmentTableQueue.add(particle);
                break;
            case "dripLava":
                particleDripLavaQueue.add(particle);
                break;
            default:
                return;
        }

        processParticles();
    }

    public void processParticles() {
        if (particleEnchantmentTableQueue.isEmpty()) {
            return;
        }

        synchronized (waypoints) {
            int[] playerPos = new int[]{
                    Minecraft.getMinecraft().thePlayer.getPosition().getX(),
                    Minecraft.getMinecraft().thePlayer.getPosition().getY(),
                    Minecraft.getMinecraft().thePlayer.getPosition().getZ()
            };
            waypoints.removeIf(waypoint -> !waypoint.getType().equals("MINOS") &&
                    !areCoordinatesClose(playerPos, waypoint.getCoordinates(), 64));

            Waypoint result = detectResult();
            if (result == null) return;

            BlockPos block = new BlockPos(result.getCoordinates()[0], result.getCoordinates()[1] - 1, result.getCoordinates()[2]);
            if (!isDuplicateResult(result) && !Minecraft.getMinecraft().theWorld.isAirBlock(block)) {
                waypoints.add(result);

                if (Config.feature.diana.dianaWaypointSounds) {
                    if (result.getType().equals("EMPTY") || result.getType().equals("MOB")) {
                        SoundUtils.playSound(result.getCoordinates(), waypointSound, volumeWaypointSound, 2.0f);
                    } else if (result.getType().equals("TREASURE")) {
                        SoundUtils.playSound(result.getCoordinates(), waypointTreasureSound, volumeWaypointTreasureSound, 0.5f);
                    }
                }
            }
        }
    }

    private Waypoint detectResult() {
        if (particleEnchantmentTableQueue.isEmpty()) {
            return null;
        }

        Queue<S2APacketParticles> largestQueue = getLargestQueue();
        if (largestQueue == null || largestQueue.isEmpty() || largestQueue.size() < particleThreshold) {
            return null;
        }

        List<S2APacketParticles> combinedParticles = new ArrayList<>();
        S2APacketParticles enchParticle = particleEnchantmentTableQueue.peek();

        combinedParticles.add(particleEnchantmentTableQueue.poll());
        particleEnchantmentTableQueue.clear();

        while (!largestQueue.isEmpty()) {
            S2APacketParticles particle = largestQueue.poll();
            if (particle == null) continue;
            if (areCoordinatesClose(
                    roundToCoords(particle.getXCoordinate(), particle.getYCoordinate(), particle.getZCoordinate()),
                    roundToCoords(enchParticle.getXCoordinate(), enchParticle.getYCoordinate(), enchParticle.getZCoordinate()),
                    distanceThreshold)) {
                combinedParticles.add(particle);
            }
        }

        particleCritQueue.clear();
        particleMagicCritQueue.clear();
        particleDripLavaQueue.clear();

        Waypoint waypoint = classifyGroup(combinedParticles);

        if (waypoint != null && isDevPetClose(waypoint.getCoordinates())) {
            return null;
        }

        return waypoint;
    }

    private Queue<S2APacketParticles> getLargestQueue() {
        Queue<S2APacketParticles> largestQueue = null;
        int maxSize = 0;

        for (Queue<S2APacketParticles> queue : Arrays.asList(particleCritQueue, particleMagicCritQueue, particleDripLavaQueue)) {
            int size = queue.size();
            if (size > maxSize) {
                largestQueue = queue;
                maxSize = size;
            }
        }

        return largestQueue;
    }

    private Waypoint classifyGroup(List<S2APacketParticles> group) {
        if (group == null || group.isEmpty()) return null;
        Set<String> groupTypes = new HashSet<>();
        double sumX = 0, sumY = 0, sumZ = 0;

        for (S2APacketParticles particle : group) {
            if (particle == null || particle.getParticleType() == null || particle.getParticleType().getParticleName() == null) {
                continue;
            }
            groupTypes.add(particle.getParticleType().getParticleName());
            sumX += particle.getXCoordinate();
            sumY += particle.getYCoordinate();
            sumZ += particle.getZCoordinate();
        }

        if (groupTypes.isEmpty()) {
            return null;
        }

        int size = group.size();
        int[] avgCoordinates = new int[]{
                (int) Math.floor(sumX / size),
                (int) Math.floor(sumY / size),
                (int) Math.floor(sumZ / size)
        };

        if (groupTypes.contains("magicCrit") && groupTypes.contains("enchantmenttable")) {
            return new Waypoint("EMPTY", avgCoordinates);
        }
        if (groupTypes.contains("crit") && groupTypes.contains("enchantmenttable")) {
            return new Waypoint("MOB", avgCoordinates);
        }
        if (groupTypes.contains("dripLava") && groupTypes.contains("enchantmenttable")) {
            return new Waypoint("TREASURE", avgCoordinates);
        }

        return null;
    }

    private boolean isDuplicateResult(Waypoint result) {
        if (result == null) return false;
        synchronized (waypoints) {
            for (Waypoint waypoint : waypoints) {
                if (waypoint == null) continue;
                if (areCoordinatesClose(waypoint.getCoordinates(), result.getCoordinates(), distanceThreshold)) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean areCoordinatesClose(int[] coords1, int[] coords2, float threshold) {
        double distance = Math.sqrt(
                Math.pow(coords1[0] - coords2[0], 2) +
                        Math.pow(coords1[1] - coords2[1], 2) +
                        Math.pow(coords1[2] - coords2[2], 2)
        );
        return distance < threshold;
    }

    public static float getDistance(int[] coords1, int[] coords2) {
        return (float) Math.sqrt(
                Math.pow(coords1[0] - coords2[0], 2) +
                        Math.pow(coords1[1] - coords2[1], 2) +
                        Math.pow(coords1[2] - coords2[2], 2)
        );
    }

    private int[] roundToCoords(double x, double y, double z) {
        return new int[]{(int) Math.floor(x), (int) Math.floor(y), (int) Math.floor(z)};
    }

    public List<Waypoint> getWaypoints() {
        return waypoints;
    }

    public void addWaypoint(Waypoint waypoint) {
        if (waypoint != null) {
            synchronized (waypoints) {
                waypoints.add(waypoint);
            }
        }
    }

    public void deleteWaypoint(Waypoint result) {
        synchronized (waypoints) {
            waypoints.remove(result);
        }
    }

    public void clearWaypoints() {
        synchronized (waypoints) {
            waypoints.clear();
        }
    }

    public Waypoint getClosestWaypoint(int[] coords) {
        synchronized (waypoints) {
            Waypoint result = null;
            float distance = Float.MAX_VALUE;
            for (Waypoint res : waypoints) {
                float dist = getDistance(coords, res.getCoordinates());
                if (dist < distance) {
                    distance = dist;
                    result = res;
                }
            }
            return result;
        }
    }

    private boolean isDevPetClose(int[] coords) {
        for (Entity entity : Minecraft.getMinecraft().theWorld.loadedEntityList) {
            if (entity == null || entity.getName() == null || !(entity instanceof EntityArmorStand)) {
                continue;
            }
            if (entity.getName().contains("Developer's Pet")) {
                int[] entityCoords = new int[]{entity.getPosition().getX(), entity.getPosition().getY(), entity.getPosition().getZ()};
                if (getDistance(coords, entityCoords) <= 5) {
                    return true;
                }
            }
        }
        return false;
    }
}