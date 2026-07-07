# PermanentArrow

A lightweight Folia-compatible Minecraft plugin that makes arrows named **"permanent"** never disappear after being shot — they stay in the world until someone picks them up.

---

## How It Works

1. Name an arrow item `"permanent"` in an anvil (or via `/give`).
2. Shoot it with a bow or crossbow.
3. The arrow sticks wherever it lands and **never despawns**.
4. Any player can pick it up and gets a reusable `"permanent"` arrow back.

The arrow's display name is checked **case-insensitively**, so `"Permanent"`, `"PERMANENT"`, etc. all work.

## Supported Arrow Types

- Regular arrows
- Tipped arrows (all potion variants — Poison, Healing, Harming, etc.)
- Spectral arrows

## Requirements

- **Server:** Paper / Folia (or any Folia fork)
- **API version:** 26.1.2 (Minecraft 1.21.4)
- **Java:** 21+

The plugin supports Folia and uses no scheduler tasks — purely event-driven, safe on Folia's regionized threading model.

## Installation

1. Download the JAR from [Releases](https://github.com/Alifmis/PermanentArrow/releases).
2. Drop it into your server's `plugins/` folder.
3. Restart the server

## Usage

### In-game (anvil)
1. Place an arrow in the first slot of an anvil.
2. Rename it to `permanent` (or any casing variation).
3. Take the renamed arrow and shoot it.

## Building from Source

```bash
gradle build
```

The JAR is output to `build/libs/PermanentArrow-1.0-SNAPSHOT.jar`.

## Technical Details

The plugin registers two event handlers:

| Event | Purpose |
|---|---|
| `ProjectileLaunchEvent` | Detects when a player shoots an arrow whose item stack is named `"permanent"` and tags the arrow entity with a Persistent Data Container (PDC) key. |
| `ProjectileHitEvent` | For tagged arrows: makes them persistent across chunk loads, sets their lifetime to `Integer.MAX_VALUE` (never age-despawns), enables pickup for all players, and ensures the item stack retains the display name. |

No configuration file — the behaviour is intentionally zero-config. Name an arrow "permanent" and it works.

## License

[MIT](LICENSE)
