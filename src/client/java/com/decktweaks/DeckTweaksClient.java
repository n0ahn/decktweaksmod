package com.decktweaks;

import org.lwjgl.glfw.GLFW;

import com.mojang.blaze3d.platform.InputConstants;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.KeyMapping;
import net.minecraft.resources.Identifier;

public class DeckTweaksClient implements ClientModInitializer {

    private static final int    MIN_RENDER_DISTANCE     = 2;
    private static final int    MAX_RENDER_DISTANCE     = 32;
    private static final int    MIN_SIMULATION_DISTANCE = 5;
    private static final int    MAX_SIMULATION_DISTANCE = 32;
    private static final double VOLUME_STEP             = 0.05;

    private static final KeyMapping.Category CATEGORY = new KeyMapping.Category(
        Identifier.fromNamespaceAndPath(DeckTweaks.MOD_ID, "general")
    );

    private static KeyMapping keyRenderUp;
    private static KeyMapping keyRenderDown;
    private static KeyMapping keySimUp;
    private static KeyMapping keySimDown;
    private static KeyMapping keyVolumeUp;
    private static KeyMapping keyVolumeDown;

    @Override
    public void onInitializeClient() {
        ToastHud.register();

        keyRenderUp   = KeyBindingHelper.registerKeyBinding(new KeyMapping("key.decktweaks.render_up",   InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_PERIOD,        CATEGORY));
        keyRenderDown = KeyBindingHelper.registerKeyBinding(new KeyMapping("key.decktweaks.render_down", InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_COMMA,         CATEGORY));
        keySimUp      = KeyBindingHelper.registerKeyBinding(new KeyMapping("key.decktweaks.sim_up",      InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_RIGHT_BRACKET, CATEGORY));
        keySimDown    = KeyBindingHelper.registerKeyBinding(new KeyMapping("key.decktweaks.sim_down",    InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_LEFT_BRACKET,  CATEGORY));
        keyVolumeUp   = KeyBindingHelper.registerKeyBinding(new KeyMapping("key.decktweaks.volume_up",   InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_EQUAL,         CATEGORY));
        keyVolumeDown = KeyBindingHelper.registerKeyBinding(new KeyMapping("key.decktweaks.volume_down", InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_MINUS,         CATEGORY));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.player == null || client.screen != null) return;

            while (keyRenderUp.consumeClick()) {
                int current = client.options.renderDistance().get();
                int next = Math.min(MAX_RENDER_DISTANCE, current + 1);
                if (next != current) {
                    client.options.renderDistance().set(next);
                    client.levelRenderer.needsUpdate();
                    ToastHud.show("Render distance", next + " chunks", ToastHud.Icon.RENDER, 0x55AAFF);
                } else {
                    ToastHud.show("Render distance", "Max (" + MAX_RENDER_DISTANCE + ")", ToastHud.Icon.RENDER, 0xFF5555);
                }
            }

            while (keyRenderDown.consumeClick()) {
                int current = client.options.renderDistance().get();
                int next = Math.max(MIN_RENDER_DISTANCE, current - 1);
                if (next != current) {
                    client.options.renderDistance().set(next);
                    client.levelRenderer.needsUpdate();
                    ToastHud.show("Render distance", next + " chunks", ToastHud.Icon.RENDER, 0x55AAFF);
                } else {
                    ToastHud.show("Render distance", "Min (" + MIN_RENDER_DISTANCE + ")", ToastHud.Icon.RENDER, 0xFF5555);
                }
            }

            while (keySimUp.consumeClick()) {
                int current = client.options.simulationDistance().get();
                int next = Math.min(MAX_SIMULATION_DISTANCE, current + 1);
                if (next != current) {
                    client.options.simulationDistance().set(next);
                    ToastHud.show("Simulation distance", next + " chunks", ToastHud.Icon.SIMULATION, 0x55FF55);
                } else {
                    ToastHud.show("Simulation distance", "Max (" + MAX_SIMULATION_DISTANCE + ")", ToastHud.Icon.SIMULATION, 0xFF5555);
                }
            }

            while (keySimDown.consumeClick()) {
                int current = client.options.simulationDistance().get();
                int next = Math.max(MIN_SIMULATION_DISTANCE, current - 1);
                if (next != current) {
                    client.options.simulationDistance().set(next);
                    ToastHud.show("Simulation distance", next + " chunks", ToastHud.Icon.SIMULATION, 0x55FF55);
                } else {
                    ToastHud.show("Simulation distance", "Min (" + MIN_SIMULATION_DISTANCE + ")", ToastHud.Icon.SIMULATION, 0xFF5555);
                }
            }

            while (keyVolumeUp.consumeClick()) {
                var volumeOption = client.options.getSoundSourceOptionInstance(net.minecraft.sounds.SoundSource.MASTER);
                double current = volumeOption.get();
                double next = Math.min(1.0, Math.round((current + VOLUME_STEP) * 100) / 100.0);
                volumeOption.set(next);
                ToastHud.show("Master volume", (int) Math.round(next * 100) + "%", ToastHud.Icon.VOLUME, 0xFFFF55);
            }

            while (keyVolumeDown.consumeClick()) {
                var volumeOption = client.options.getSoundSourceOptionInstance(net.minecraft.sounds.SoundSource.MASTER);
                double current = volumeOption.get();
                double next = Math.max(0.0, Math.round((current - VOLUME_STEP) * 100) / 100.0);
                volumeOption.set(next);
                ToastHud.show("Master volume", (int) Math.round(next * 100) + "%", ToastHud.Icon.VOLUME, 0xFFFF55);
            }
        });
    }
}