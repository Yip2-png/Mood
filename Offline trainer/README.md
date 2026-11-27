# Offline Trainer (Android floating UI prototype)

This Android project provides:
- overlay permission flow and launch button
- draggable floating panel
- minimize and close controls
- game profile selector for Murim Life Simulator / Ancient Life Simulator
- feature ON/OFF switches
- numeric inputs for Multiplier, Adjustable All, Increase / Adding

## Important implementation boundary
This is a functional floating UI/configuration prototype only. It does not attach to other apps, read/write their process memory, bypass app protections, block another app's ad SDK, or implement game cheats. Those capabilities cannot be made universal across Android games. A real game-specific mod requires source-level integration or an explicitly supported mod API and verified test build.

## Build
Open this folder in Android Studio (AGP 8.7.3, Kotlin 2.0.21, compileSdk 35) and run `assembleDebug`.

## Verified game identifiers (public listings)
- Murim Life Simulator: `com.murimlifesimulator.app`, version 3.0.0 reported by AppBrain.
- Ancient Life: `com.wingjoy.dynastyLife.inter`, Google Play listing by Wingjoy Games.

Engine, internal class names, game save schema, and mod support have not been verified. Do not assume these games share an engine or data model.
