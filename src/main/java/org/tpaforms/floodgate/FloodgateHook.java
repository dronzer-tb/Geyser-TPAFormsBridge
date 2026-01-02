package org.tpaforms.floodgate;

import org.bukkit.entity.Player;
import org.geysermc.floodgate.api.FloodgateApi;
import org.tpaforms.TPAFormsBridgePlugin;

import java.util.UUID;

/**
 * Hook for Floodgate to detect Bedrock players.
 */
public class FloodgateHook {

    private final TPAFormsBridgePlugin plugin;
    private FloodgateApi floodgateApi;
    private boolean available = false;

    public FloodgateHook(TPAFormsBridgePlugin plugin) {
        this.plugin = plugin;
        checkAvailability();
    }

    private void checkAvailability() {
        try {
            Class.forName("org.geysermc.floodgate.api.FloodgateApi");
            floodgateApi = FloodgateApi.getInstance();
            available = floodgateApi != null;
        } catch (ClassNotFoundException | NoClassDefFoundError e) {
            available = false;
        }
    }

    /**
     * Check if Floodgate is available.
     */
    public boolean isAvailable() {
        return available;
    }

    /**
     * Check if a player is from Bedrock Edition.
     */
    public boolean isBedrockPlayer(Player player) {
        if (!available || floodgateApi == null) {
            return false;
        }
        return floodgateApi.isFloodgatePlayer(player.getUniqueId());
    }

    /**
     * Check if a player UUID belongs to a Bedrock player.
     */
    public boolean isBedrockPlayer(UUID uuid) {
        if (!available || floodgateApi == null) {
            return false;
        }
        return floodgateApi.isFloodgatePlayer(uuid);
    }
}
