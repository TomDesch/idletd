package io.github.stealingdapenta.idletd.service.utils;

import lombok.RequiredArgsConstructor;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.TextColor;

@RequiredArgsConstructor
public class Coloring {

    /**
     * @param input text containing the old (pre 1.16) color codes
     * @return the formatted text, taking out the color codes and transforming them
     * into TextComponent using TextColor
     * Todo: Remove this method & update all uses to be modern.
     */
    public TextComponent formatString(String input) {
        String formattedString = input;

        for (String colorCode : getAllColorCodes()) {
            formattedString = formattedString.replaceAll(colorCode, mapColorCode(colorCode).toString());
        }

        return Component.text(formattedString);
    }

    /**
     * @return an array containing all old 2 char color codes
     * For example: &5
     */
    private String[] getAllColorCodes() {
        return new String[]{"&0", "&1", "&2", "&3", "&4", "&5", "&6", "&7", "&8", "&9", "&a", "&b", "&c", "&d", "&e", "&f"};
    }

    /**
     * @param colorCode a 2 character old string color code
     * @return the matching TextColor
     */
    private TextColor mapColorCode(String colorCode) {
        return switch (colorCode.toLowerCase()) {
            case "&0" -> TextColor.color(0x000000); // BLACK
            case "&1" -> TextColor.color(0x0000AA); // DARK BLUE
            case "&2" -> TextColor.color(0x00AA00); // DARK GREEN
            case "&3" -> TextColor.color(0x00AAAA); // DARK AQUA
            case "&4" -> TextColor.color(0xAA0000); // DARK RED
            case "&5" -> TextColor.color(0xAA00AA); // DARK PURPLE
            case "&6" -> TextColor.color(0xFFAA00); // GOLD
            case "&7" -> TextColor.color(0xAAAAAA); // GRAY
            case "&8" -> TextColor.color(0x555555); // DARK GRAY
            case "&9" -> TextColor.color(0x5555FF); // BLUE
            case "&a" -> TextColor.color(0x55FF55); // GREEN
            case "&b" -> TextColor.color(0x55FFFF); // AQUA
            case "&c" -> TextColor.color(0xFF5555); // RED
            case "&d" -> TextColor.color(0xFF55FF); // LIGHT PURPLE
            case "&e" -> TextColor.color(0xFFFF55); // YELLOW
            case "&f" -> TextColor.color(0xFFFFFF); // WHITE
            default -> TextColor.color(0x000000); // Default to black if not recognized
        };
    }
}
