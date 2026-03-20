package com.decktweaks;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import org.lwjgl.glfw.GLFW;

public class DeckTweaksClient implements ClientModInitializer {

    private static final int    MIN_RENDER_DISTANCE     = 2;
    private static final int    MAX_RENDER_DISTANCE     = 32;
    private static final int    MIN_SIMULATION_DISTANCE = 5;
    private static final int    MAX_SIMULATION_DISTANCE = 32;
    private static final double VOLUME_STEP             = 0.05;

    private boolean wasCommaPressed        = false;
    private boolean wasPeriodPressed       = false;
    private boolean wasMinusPressed        = false;
    private boolean wasPlusPressed         = false;
    private boolean wasLeftBracketPressed  = false;
    private boolean wasRightBracketPressed = false;

    @Override
    public void onInitializeClient() {
        ToastHud.register();

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.player == null || client.screen != null) {
                wasCommaPressed        = false;
                wasPeriodPressed       = false;
                wasMinusPressed        = false;
                wasPlusPressed         = false;
                wasLeftBracketPressed  = false;
                wasRightBracketPressed = false;
                return;
            }

            long window = GLFW.glfwGetCurrentContext();

            boolean ctrl = GLFW.glfwGetKey(window, GLFW.GLFW_KEY_LEFT_CONTROL)  == GLFW.GLFW_PRESS
                        || GLFW.glfwGetKey(window, GLFW.GLFW_KEY_RIGHT_CONTROL) == GLFW.GLFW_PRESS;

            boolean commaNow         = GLFW.glfwGetKey(window, GLFW.GLFW_KEY_COMMA)         == GLFW.GLFW_PRESS;
            boolean periodNow        = GLFW.glfwGetKey(window, GLFW.GLFW_KEY_PERIOD)        == GLFW.GLFW_PRESS;
            boolean minusNow         = GLFW.glfwGetKey(window, GLFW.GLFW_KEY_MINUS)         == GLFW.GLFW_PRESS;
            boolean plusNow          = GLFW.glfwGetKey(window, GLFW.GLFW_KEY_EQUAL)         == GLFW.GLFW_PRESS;
            boolean leftBracketNow   = GLFW.glfwGetKey(window, GLFW.GLFW_KEY_LEFT_BRACKET)  == GLFW.GLFW_PRESS;
            boolean rightBracketNow  = GLFW.glfwGetKey(window, GLFW.GLFW_KEY_RIGHT_BRACKET) == GLFW.GLFW_PRESS;

            // --- Render distance ---

            // Ctrl+. -> increase
            if (periodNow && !wasPeriodPressed && ctrl) {
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

            // Ctrl+, -> decrease
            if (commaNow && !wasCommaPressed && ctrl) {
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

            // --- Simulation distance ---

            // Ctrl+] -> increase
            if (rightBracketNow && !wasRightBracketPressed && ctrl) {
                int current = client.options.simulationDistance().get();
                int next = Math.min(MAX_SIMULATION_DISTANCE, current + 1);
                if (next != current) {
                    client.options.simulationDistance().set(next);
                    ToastHud.show("Simulation distance", next + " chunks", ToastHud.Icon.SIMULATION, 0x55FF55);
                } else {
                    ToastHud.show("Simulation distance", "Max (" + MAX_SIMULATION_DISTANCE + ")", ToastHud.Icon.SIMULATION, 0xFF5555);
                }
            }

            // Ctrl+[ -> decrease
            if (leftBracketNow && !wasLeftBracketPressed && ctrl) {
                int current = client.options.simulationDistance().get();
                int next = Math.max(MIN_SIMULATION_DISTANCE, current - 1);
                if (next != current) {
                    client.options.simulationDistance().set(next);
                    ToastHud.show("Simulation distance", next + " chunks", ToastHud.Icon.SIMULATION, 0x55FF55);
                } else {
                    ToastHud.show("Simulation distance", "Min (" + MIN_SIMULATION_DISTANCE + ")", ToastHud.Icon.SIMULATION, 0xFF5555);
                }
            }

            // --- Master volume ---

            // Ctrl+- -> decrease
            if (minusNow && !wasMinusPressed && ctrl) {
                var volumeOption = client.options.getSoundSourceOptionInstance(net.minecraft.sounds.SoundSource.MASTER);
                double current = volumeOption.get();
                double next = Math.max(0.0, Math.round((current - VOLUME_STEP) * 100) / 100.0);
                volumeOption.set(next);
                int pct = (int) Math.round(next * 100);
                ToastHud.show("Master volume", pct + "%", ToastHud.Icon.VOLUME, 0xFFFF55);
            }

            // Ctrl+= -> increase
            if (plusNow && !wasPlusPressed && ctrl) {
                var volumeOption = client.options.getSoundSourceOptionInstance(net.minecraft.sounds.SoundSource.MASTER);
                double current = volumeOption.get();
                double next = Math.min(1.0, Math.round((current + VOLUME_STEP) * 100) / 100.0);
                volumeOption.set(next);
                int pct = (int) Math.round(next * 100);
                ToastHud.show("Master volume", pct + "%", ToastHud.Icon.VOLUME, 0xFFFF55);
            }

            wasCommaPressed        = commaNow        && ctrl;
            wasPeriodPressed       = periodNow       && ctrl;
            wasMinusPressed        = minusNow        && ctrl;
            wasPlusPressed         = plusNow         && ctrl;
            wasLeftBracketPressed  = leftBracketNow  && ctrl;
            wasRightBracketPressed = rightBracketNow && ctrl;
        });
    }
}