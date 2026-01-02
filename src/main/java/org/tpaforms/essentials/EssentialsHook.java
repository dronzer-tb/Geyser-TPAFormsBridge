package org.tpaforms.essentials;

import com.earth2me.essentials.IEssentials;
import net.ess3.api.events.TPARequestEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.tpaforms.TPAFormsBridgePlugin;

/**
 * Hook for EssentialsX TPA events.
 */
public class EssentialsHook implements Listener {

    private final TPAFormsBridgePlugin plugin;
    private IEssentials essentials;
    private boolean available = false;

    public EssentialsHook(TPAFormsBridgePlugin plugin) {
        this.plugin = plugin;
        checkAvailability();

        if (available) {
            // Register TPA event listener
            plugin.getServer().getPluginManager().registerEvents(this, plugin);
        }
    }

    private void checkAvailability() {
        try {
            Plugin essPlugin = Bukkit.getPluginManager().getPlugin("Essentials");
            if (essPlugin != null && essPlugin instanceof IEssentials) {
                this.essentials = (IEssentials) essPlugin;
                available = true;
            }
        } catch (Exception e) {
            available = false;
        }
    }

    public boolean isAvailable() {
        return available;
    }

    public IEssentials getEssentials() {
        return essentials;
    }

    /**
     * Listen for TPA requests and show a form to Bedrock players.
     */
    @EventHandler(priority = EventPriority.MONITOR)
    public void onTPARequest(TPARequestEvent event) {
        if (event.isCancelled()) {
            return;
        }

        Player target = event.getTarget().getBase();

        // Get the requester player from CommandSource
        if (!event.getRequester().isPlayer()) {
            return; // Console TPA requests aren't shown to Bedrock players
        }
        Player requester = event.getRequester().getPlayer();
        if (requester == null) {
            return;
        }

        boolean isTpaHere = event.isTeleportHere();

        // Only handle Bedrock players
        if (!plugin.getFloodgateHook().isBedrockPlayer(target)) {
            return;
        }

        plugin.getLogger().info("TPA request detected: " + requester.getName() + " -> " + target.getName() + " (tpahere: " + isTpaHere + ")");

        // Build the form content
        String title = "Teleport Request";
        String content;
        if (isTpaHere) {
            content = requester.getName() + " wants you to teleport to them.\n\nDo you accept?";
        } else {
            content = requester.getName() + " wants to teleport to you.\n\nDo you accept?";
        }

        // Send form request to FloodgateBridge on Velocity
        // FloodgateBridge will show the form and execute the command when player responds
        plugin.getBridgeClient().sendModalForm(
                target,
                title,
                content,
                "Accept",
                "Deny",
                "tpaccept",  // Command to run when accepted
                "tpdeny"     // Command to run when denied
        );
    }
}
