# DeckTweaks

A Minecraft 1.21.11 Fabric mod that lets you control render distance, simulation distance, and master volume with keyboard shortcuts — designed for use with a Stream Deck or any macro device.

---

## Installation

1. Install [Fabric Loader](https://fabricmc.net/use/installer/) for Minecraft 1.21.11
2. Download [Fabric API](https://modrinth.com/mod/fabric-api) for 1.21.11 and place it in your `mods/` folder
3. Download `decktweaks-1.0.0.jar` from the [Releases](../../releases/latest) tab
4. Place the jar in your Minecraft `mods/` folder:

| OS | Mods folder location |
|---|---|
| **Windows** | `%AppData%\.minecraft\mods` |
| **macOS** | `~/Library/Application Support/minecraft/mods` |
| **Linux** | `~/.minecraft/mods` |

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

> **macOS note:** `Ctrl` refers to the Control key (⌃), not Command (⌘).

All changes are shown as a small toast notification in the bottom-right corner of the screen.

---

## Requirements

- Minecraft 1.21.11
- Fabric Loader ≥ 0.18.4
- Fabric API
- Java 21

---

## Building from source

**macOS**
```bash
git clone https://github.com/n0ahn/decktweaks
cd decktweaks
export JAVA_HOME=$(/usr/libexec/java_home -v 21)
./gradlew build
```

**Linux**
```bash
git clone https://github.com/n0ahn/decktweaks
cd decktweaks
./gradlew build
```

**Windows**
```cmd
git clone https://github.com/n0ahn/decktweaks
cd decktweaks
gradlew.bat build
```

The built jar will be at `build/libs/decktweaks-1.0.0.jar`.

---

## Preview
<img width="520" height="204" alt="image" src="https://github.com/user-attachments/assets/b7ec0c4b-7bff-42c1-bff8-c7c3df193c54" />
<img width="520" height="204" alt="image" src="https://github.com/user-attachments/assets/ab5d6eb6-beb1-448e-af98-2b4891fe3b8c" />
<img width="520" height="204" alt="image" src="https://github.com/user-attachments/assets/c673a36a-5655-4cf6-b8a1-5dcd39056307" />



