package com.decktweaks;

import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.DeltaTracker;

public class ToastHud {

    public enum Icon { RENDER, SIMULATION, VOLUME }

    private static final int TOAST_W     = 130;
    private static final int TOAST_H     = 28;
    private static final int ICON_SIZE   = 18;
    private static final int PADDING     = 5;
    private static final int MARGIN      = 3;
    private static final int SLIDE_TICKS = 6;
    private static final int HOLD_TICKS  = 120;
    private static final int FADE_TICKS  = 12;

    private static String title    = "";
    private static String subtitle = "";
    private static Icon   icon     = Icon.RENDER;
    private static int    color    = 0xFF55AAFF;

    private static int   phase     = 0; // 0=idle 1=slide-in 2=hold 3=fade-out
    private static int   phaseTick = 0;
    private static float slideOffset = TOAST_W + MARGIN + 4;

    public static void show(String titleText, String subtitleText, Icon iconType, int rgbColor) {
        title    = titleText;
        subtitle = subtitleText;
        icon     = iconType;
        color    = rgbColor;
        // If already fully visible (hold phase) or fading, just reset the hold timer
        // so the toast stays on screen without replaying the slide-in animation.
        if (phase == 2 || phase == 3) {
            phase     = 2;
            phaseTick = 0;
        } else {
            // Not yet on screen — play the full slide-in
            phase     = 1;
            phaseTick = 0;
            slideOffset = TOAST_W + MARGIN + 4;
        }
    }

    public static void register() {
        HudRenderCallback.EVENT.register(ToastHud::render);
    }

    private static void render(GuiGraphics ctx, DeltaTracker delta) {
        if (phase == 0) return;

        Minecraft client = Minecraft.getInstance();
        int screenW = client.getWindow().getGuiScaledWidth();
        int screenH = client.getWindow().getGuiScaledHeight();

        phaseTick++;
        float alpha = 1.0f;

        if (phase == 1) {
            float progress = Math.min(1.0f, (float) phaseTick / SLIDE_TICKS);
            float eased = 1f - (1f - progress) * (1f - progress);
            slideOffset = (TOAST_W + MARGIN + 4) * (1f - eased);
            if (phaseTick >= SLIDE_TICKS) { phase = 2; phaseTick = 0; slideOffset = 0; }
        } else if (phase == 2) {
            slideOffset = 0;
            if (phaseTick >= HOLD_TICKS) { phase = 3; phaseTick = 0; }
        } else if (phase == 3) {
            float progress = Math.min(1.0f, (float) phaseTick / FADE_TICKS);
            float eased = progress * progress;
            slideOffset = (TOAST_W + MARGIN + 4) * eased;
            alpha = Math.max(0f, 1f - progress);
            if (phaseTick >= FADE_TICKS) { phase = 0; slideOffset = 0; return; }
        }

        int x = (int)(screenW - TOAST_W - MARGIN + slideOffset);
        int y = screenH - TOAST_H - MARGIN - 20;

        int bgAlpha     = (int)(alpha * 0xCC);
        int borderAlpha = (int)(alpha * 0x99);
        int textAlpha   = (int)(alpha * 0xFF);

        // Background
        ctx.fill(x, y, x + TOAST_W, y + TOAST_H, (bgAlpha << 24) | 0x0D0D1E);

        // Border
        drawBorder(ctx, x, y, TOAST_W, TOAST_H, (borderAlpha << 24) | 0x555555);

        // Accent line on left edge
        ctx.fill(x, y, x + 2, y + TOAST_H, (textAlpha << 24) | (color & 0x00FFFFFF));

        // Icon
        int ix = x + PADDING + 2;
        int iy = y + (TOAST_H - ICON_SIZE) / 2;
        drawIcon(ctx, icon, ix, iy, textAlpha);

        // Text
        int tx = ix + ICON_SIZE + PADDING;
        int ty = y + PADDING - 1;

        ctx.drawString(client.font, title,    tx, ty,      (textAlpha << 24) | (color & 0x00FFFFFF), false);
        ctx.drawString(client.font, subtitle, tx, ty + 11, (textAlpha << 24) | 0xAAAAAA,             false);
    }

    private static void drawBorder(GuiGraphics ctx, int x, int y, int w, int h, int col) {
        ctx.fill(x,         y,         x + w,     y + 2,     col);
        ctx.fill(x,         y + h - 2, x + w,     y + h,     col);
        ctx.fill(x,         y,         x + 2,     y + h,     col);
        ctx.fill(x + w - 2, y,         x + w,     y + h,     col);
    }

    private static void drawIcon(GuiGraphics ctx, Icon icon, int x, int y, int alpha) {
        int s = 4;
        if (icon == Icon.RENDER) {
            int[] c = {
                0x7abc4e, 0x7abc4e, 0x5b8a3c, 0x7abc4e,
                0x7abc4e, 0xa0784a, 0xa0784a, 0x7abc4e,
                0x5b8a3c, 0xa0784a, 0xa0784a, 0x5b8a3c,
                0x5b8a3c, 0x5b8a3c, 0x5b8a3c, 0x5b8a3c
            };
            drawPixelGrid(ctx, x, y, s, c, alpha);
        } else if (icon == Icon.SIMULATION) {
            int[] c = {
                0x4466cc, 0x88aaff, 0x4466cc, 0x4466cc,
                0x88aaff, 0xaaccff, 0x88aaff, 0x4466cc,
                0x4466cc, 0x88aaff, 0x4466cc, 0x336699,
                0x336699, 0x4466cc, 0x336699, 0x224488
            };
            drawPixelGrid(ctx, x, y, s, c, alpha);
        } else {
            // Volume icon
            ctx.fill(x,      y + 8,  x + 6,  y + 16, (alpha << 24) | 0xFFEE00);
            ctx.fill(x + 6,  y + 6,  x + 10, y + 18, (alpha << 24) | 0xFFEE00);
            ctx.fill(x + 10, y + 4,  x + 14, y + 20, (alpha << 24) | 0xFFEE00);
            ctx.fill(x + 15, y + 8,  x + 16, y + 16, (alpha << 24) | 0xFFEE00);
            ctx.fill(x + 17, y + 6,  x + 18, y + 18, (alpha << 24) | 0xFFEE00);
            ctx.fill(x + 19, y + 4,  x + 20, y + 20, (alpha << 24) | 0xFFEE00);
        }
    }

    private static void drawPixelGrid(GuiGraphics ctx, int x, int y, int s, int[] colors, int alpha) {
        for (int i = 0; i < 16; i++) {
            int px = x + (i % 4) * s;
            int py = y + (i / 4) * s;
            ctx.fill(px, py, px + s, py + s, (alpha << 24) | colors[i]);
        }
    }
}