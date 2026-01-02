package org.tpaforms;

import org.bukkit.plugin.java.JavaPlugin;
import org.tpaforms.bridge.FloodgateBridgeClient;
import org.tpaforms.essentials.EssentialsHook;
import org.tpaforms.floodgate.FloodgateHook;

/**
 * TPA Forms Bridge - Bridges EssentialsX TPA requests to Bedrock forms.
 *
 * This plugin listens for TPA requests from EssentialsX and sends form
 * requests to FormsAPI on Geyser Standalone via TCP.
 */
public class TPAFormsBridgePlugin extends JavaPlugin {

    private static TPAFormsBridgePlugin instance;
    private FloodgateHook floodgateHook;
    private EssentialsHook essentialsHook;
    private FloodgateBridgeClient bridgeClient;

    @Override
    public void onEnable() {
        instance = this;

        getLogger().info("=== TPA Forms Bridge Starting ===");

        // Save default config
        saveDefaultConfig();

        // Load config values
        String formsApiHost = getConfig().getString("formsapi-host", "localhost");
        int formsApiPort = getConfig().getInt("formsapi-port", 9876);

        // Initialize Floodgate hook (for Bedrock player detection)
        this.floodgateHook = new FloodgateHook(this);
        if (!floodgateHook.isAvailable()) {
            getLogger().severe("Floodgate not found! This plugin requires Floodgate.");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        getLogger().info("Floodgate hook initialized!");

        // Initialize FormsAPI TCP client
        this.bridgeClient = new FloodgateBridgeClient(this, formsApiHost, formsApiPort);
        getLogger().info("FormsAPI TCP client initialized (" + formsApiHost + ":" + formsApiPort + ")");

        // Initialize EssentialsX hook
        this.essentialsHook = new EssentialsHook(this);
        if (!essentialsHook.isAvailable()) {
            getLogger().severe("EssentialsX not found! This plugin requires EssentialsX.");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        getLogger().info("EssentialsX hook initialized!");

        getLogger().info("TPA Forms Bridge ready!");
    }

    @Override
    public void onDisable() {
        if (bridgeClient != null) {
            bridgeClient.shutdown();
        }
        getLogger().info("TPA Forms Bridge disabled.");
    }

    public static TPAFormsBridgePlugin getInstance() {
        return instance;
    }

    public FloodgateHook getFloodgateHook() {
        return floodgateHook;
    }

    public EssentialsHook getEssentialsHook() {
        return essentialsHook;
    }

    public FloodgateBridgeClient getBridgeClient() {
        return bridgeClient;
    }
}
