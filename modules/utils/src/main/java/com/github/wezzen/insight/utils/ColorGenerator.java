package com.github.wezzen.insight.utils;

import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public final class ColorGenerator {

    public String generateSoftColor() {
        Random random = new Random();

        float hue = random.nextFloat() * 360f; // оттенок: 0–360
        float saturation = 0.4f + random.nextFloat() * 0.3f; // насыщенность: 40–70%
        float lightness = 0.6f + random.nextFloat() * 0.2f; // яркость: 60–80%

        return hslToHex(hue, saturation, lightness);
    }

    private static String hslToHex(float h, float s, float l) {
        float c = (1 - Math.abs(2 * l - 1)) * s;
        float x = c * (1 - Math.abs((h / 60) % 2 - 1));
        float m = l - c / 2;

        float r, g, b;
        if (h < 60)      { r = c; g = x; b = 0; }
        else if (h < 120){ r = x; g = c; b = 0; }
        else if (h < 180){ r = 0; g = c; b = x; }
        else if (h < 240){ r = 0; g = x; b = c; }
        else if (h < 300){ r = x; g = 0; b = c; }
        else             { r = c; g = 0; b = x; }

        int R = Math.round((r + m) * 255);
        int G = Math.round((g + m) * 255);
        int B = Math.round((b + m) * 255);

        return String.format("#%02x%02x%02x", R, G, B);
    }
}
