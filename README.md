# DeckTweaks

A Minecraft 1.21.11 Fabric mod that lets you control render distance, simulation distance, and master volume with keyboard shortcuts — designed for use with a Stream Deck or any macro device.

---

## Installation

1. Install [Fabric Loader](https://fabricmc.net/use/installer/) for Minecraft 1.21.11
2. Download [Fabric API](https://modrinth.com/mod/fabric-api) for 1.21.11 and place it in your `mods/` folder
3. Download `decktweaks-1.0.0.jar` from the [Releases](../../releases/latest) tab
4. Place it in your `mods/` folder
5. Launch Minecraft with the Fabric profile

---

## Keybinds

| Keybind | Action |
|---|---|
| `Ctrl` + `.` | Render distance +1 |
| `Ctrl` + `,` | Render distance -1 |
| `Ctrl` + `]` | Simulation distance +1 |
| `Ctrl` + `[` | Simulation distance -1 |
| `Ctrl` + `=` | Master volume +5% |
| `Ctrl` + `-` | Master volume -5% |

All changes are shown as a small toast notification in the bottom-right corner of the screen.

---

## Requirements

- Minecraft 1.21.11
- Fabric Loader ≥ 0.18.4
- Fabric API
- Java 21

---

## Building from source

```bash
git clone https://github.com/yourusername/decktweaks
cd decktweaks
export JAVA_HOME=$(/usr/libexec/java_home -v 21)   # Mac only
./gradlew build
```

The built jar will be at `build/libs/decktweaks-1.0.0.jar`.
