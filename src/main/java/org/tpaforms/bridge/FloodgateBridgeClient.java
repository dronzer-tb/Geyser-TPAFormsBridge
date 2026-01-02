package org.tpaforms.bridge;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.bukkit.entity.Player;
import org.tpaforms.TPAFormsBridgePlugin;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

/**
 * TCP client for sending form requests to FormsAPI extension on Geyser Standalone.
 */
public class FloodgateBridgeClient {

    private final TPAFormsBridgePlugin plugin;
    private final Gson gson;
    private final String host;
    private final int port;

    public FloodgateBridgeClient(TPAFormsBridgePlugin plugin, String host, int port) {
        this.plugin = plugin;
        this.gson = new GsonBuilder().create();
        this.host = host;
        this.port = port;
    }

    /**
     * Send a modal form request to FormsAPI via TCP.
     *
     * @param player Target Bedrock player
     * @param title Form title
     * @param content Form content/body
     * @param button1 Accept button text
     * @param button2 Deny button text
     * @param commandAccept Command to run when accepted
     * @param commandDeny Command to run when denied
     */
    public void sendModalForm(Player player, String title, String content,
                              String button1, String button2,
                              String commandAccept, String commandDeny) {
        FormRequest request = new FormRequest();
        request.player_uuid = player.getUniqueId().toString();
        request.form_type = "modal";
        request.title = title;
        request.content = content;
        request.button1 = button1;
        request.button2 = button2;
        request.command_accept = commandAccept;
        request.command_deny = commandDeny;

        String json = gson.toJson(request);

        // Send asynchronously
        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> {
            try (Socket socket = new Socket(host, port);
                 OutputStream out = socket.getOutputStream()) {

                out.write((json + "\n").getBytes(StandardCharsets.UTF_8));
                out.flush();

                plugin.getLogger().info("Sent form request via TCP to " + host + ":" + port);
            } catch (IOException e) {
                plugin.getLogger().warning("Failed to send form request via TCP: " + e.getMessage());
            }
        });
    }

    /**
     * Shutdown (nothing to do for TCP client).
     */
    public void shutdown() {
        // Nothing to clean up
    }

    /**
     * Simple form request data structure.
     */
    private static class FormRequest {
        String player_uuid;
        String form_type;
        String title;
        String content;
        String button1;
        String button2;
        String command_accept;
        String command_deny;
    }
}
