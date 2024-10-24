package com.github.paopaoyue.wljmod.patch.color;

import com.badlogic.gdx.graphics.Color;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.github.paopaoyue.wljmod.utility.Reflect;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.ui.DialogWord;
import com.megacrit.cardcrawl.ui.SpeechWord;

import static com.github.paopaoyue.wljmod.patch.color.WordColorEnum.BROWN;
import static com.github.paopaoyue.wljmod.patch.color.WordColorEnum.CYAN;

public class ColorPatch {

    @SpirePatch(
        clz = FontHelper.class,
        method = "identifyColor"
    )
    public static class FontHelperIdentifyColorPatch {

        @SpirePrefixPatch
        public static SpireReturn<Color> Prefix(String word) {
            if (word.length() >= 2 &&  word.charAt(0) == '#') {
                switch (word.charAt(1)) {
                    case 'o': {
                        return SpireReturn.Return(Color.BROWN.cpy());
                    }
                    case 'c': {
                        return SpireReturn.Return(Color.CYAN.cpy());
                    }
                }
            }
            return SpireReturn.Continue();
        }

    }

    @SpirePatch(
        clz = DialogWord.class,
        method = "getColor"
    )
    public static class DialogWordGetColorPatch {
        @SpirePrefixPatch
        public static SpireReturn<Color> Prefix(DialogWord __instance) {
            DialogWord.WordColor wColor = Reflect.getPrivate(DialogWord.class, __instance, "wColor", DialogWord.WordColor.class);
            Color c = getColor(wColor);
            if (c != null) {
                return SpireReturn.Return(c);
            }
            return SpireReturn.Continue();
        }
    }

    @SpirePatch(
        clz = SpeechWord.class,
        method = "getColor"
    )
    public static class SpeechWordGetColorPatch {
        @SpirePrefixPatch
        public static SpireReturn<Color> Prefix(SpeechWord __instance) {
            DialogWord.WordColor wColor = Reflect.getPrivate(SpeechWord.class, __instance, "wColor", DialogWord.WordColor.class);
            Color c = getColor(wColor);
            if (c != null) {
                return SpireReturn.Return(c);
            }
            return SpireReturn.Continue();
        }
    }

    @SpirePatch(
        clz = DialogWord.class,
        method = "identifyWordColor"
    )
    public static class DialogWordIdentifyWordColorPatch {

        @SpirePrefixPatch
        public static SpireReturn<DialogWord.WordColor> Prefix(String word) {
            DialogWord.WordColor color = identifyWordColor(word);
            if (color != null) {
                return SpireReturn.Return(color);
            }
            return SpireReturn.Continue();
        }

    }

    @SpirePatch(
        clz = SpeechWord.class,
        method = "identifyWordColor"
    )
    public static class SpeechWordIdentifyWordColorPatch {

        @SpirePrefixPatch
        public static SpireReturn<DialogWord.WordColor> Prefix(String word) {
            DialogWord.WordColor color = identifyWordColor(word);
            if (color != null) {
                return SpireReturn.Return(color);
            }
            return SpireReturn.Continue();
        }

    }

    private static Color getColor(DialogWord.WordColor color) {
        if (color == BROWN) {
            return Color.BROWN.cpy();
        }
        if (color == CYAN) {
            return Color.CYAN.cpy();
        }
        return null;
    }

    private static DialogWord.WordColor identifyWordColor(String word) {
        if (word.charAt(0) == '#') {
            switch (word.charAt(1)) {
                case 'o': {
                    return BROWN;
                }
                case 'c': {
                    return CYAN;
                }
            }
        }
        return null;
    }

}
