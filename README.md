# TPAFormsBridge

A Spigot/Paper plugin that shows Bedrock-native forms for EssentialsX TPA requests.

## What It Does

When a player sends a TPA request to a Bedrock player, instead of showing chat messages that require typing `/tpaccept` or `/tpdeny`, this plugin displays a **native Bedrock modal form** with Accept and Deny buttons.

## Requirements

- Spigot/Paper 1.20+
- EssentialsX
- Floodgate (for Bedrock player detection)
- [FormsAPI Extension](https://github.com/dronzer-tb/Geyser-FormsAPI) on Geyser Standalone

## Installation

1. Install [FormsAPI](https://github.com/dronzer-tb/Geyser-FormsAPI) on your Geyser Standalone
2. Download `TPAFormsBridge.jar`
3. Place it in your Spigot/Paper server's `plugins/` folder
4. Start the server
5. Configure `plugins/TPAFormsBridge/config.yml` if Geyser is on a different machine

## Configuration

```yaml
# TPAFormsBridge Configuration

# FormsAPI Connection Settings
# The host where FormsAPI extension is running (Geyser Standalone)
formsapi-host: "localhost"

# The port FormsAPI is listening on
formsapi-port: 9876
```

### Configuration Options

| Option | Default | Description |
|--------|---------|-------------|
| `formsapi-host` | `localhost` | IP/hostname of the machine running Geyser Standalone with FormsAPI |
| `formsapi-port` | `9876` | TCP port that FormsAPI is listening on |

## How It Works

```
┌─────────────────────────────────────────────────────────────┐
│                    SPIGOT SERVER                            │
│  ┌───────────────────────────────────────────────────────┐  │
│  │  TPAFormsBridge                                       │  │
│  │  1. Listens for EssentialsX TPARequestEvent           │  │
│  │  2. Checks if target is Bedrock player (Floodgate)    │  │
│  │  3. Sends form request via TCP to FormsAPI            │  │
│  └───────────────────────────────────────────────────────┘  │
└─────────────────────────────────────────────────────────────┘
                            │
                            │ TCP Connection
                                          ▼
┌─────────────────────────────────────────────────────────────┐
│                    GEYSER STANDALONE                        │
│  ┌───────────────────────────────────────────────────────┐  │
│  │  FormsAPI Extension                                   │  │
│  │  4. Receives form request                             │  │
│  │  5. Shows modal form to Bedrock player                │  │
│  │  6. Executes /tpaccept or /tpdeny on response         │  │
│  └───────────────────────────────────────────────────────┘  │
└─────────────────────────────────────────────────────────────┘
                            │
                                          ▼
                    ┌───────────────┐
                    │ Bedrock Player│
                    │ sees form with│
                    │ Accept / Deny │
                    └───────────────┘
```

## Network Topology Support

TPAFormsBridge works with any network setup:

### Same Machine (Simplest)
```
Spigot + Geyser Standalone on same machine
→ Use default config (localhost:9876)
```

### Velocity/BungeeCord Network
```
Backend Spigot Servers → Velocity → Geyser Standalone
→ Set formsapi-host to Geyser's IP on each backend server
```

### Separate Machines
```
Spigot on Machine A, Geyser on Machine B
→ Set formsapi-host to Machine B's IP
→ Ensure port 9876 is open between machines
```

## Commands

This plugin has no commands. It works automatically in the background.

## Permissions

This plugin has no permissions. All Bedrock players automatically see forms for TPA requests.

## Building from Source

```bash
git clone https://github.com/DronzerStudios/TPAFormsBridge.git
cd TPAFormsBridge
./gradlew build
```

## Troubleshooting

### Forms not showing

1. **Check FormsAPI is running**: Look for `FormsAPI ready! Listening for form requests on TCP port 9876` in Geyser logs
2. **Check connection**: Ensure `formsapi-host` and `formsapi-port` are correct
3. **Check firewall**: If on different machines, ensure the TCP port is open
4. **Check Floodgate**: Ensure Floodgate is installed and detecting Bedrock players

### "Failed to send form request via TCP" error

- FormsAPI extension is not running or not reachable
- Check the host/port configuration
- Check network connectivity between Spigot and Geyser

## License

MIT License with Attribution Requirement

Copyright (c) 2024 DronzerStudios.tech

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

1. The above copyright notice and this permission notice shall be included in all
   copies or substantial portions of the Software.

2. **Attribution Requirement**: Any use, modification, or distribution of this
   software must include visible credit to **DronzerStudios.tech** in:
   - The project's README or documentation
   - Any derivative works or forks
   - Any public releases that include this software

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.

## Credits

Developed by [Dronzer Studios](https://dronzerstudios.tech)


## Support

For issues, feature requests, or contributions, please visit:
- Website: https://dronzerstudios.tech
