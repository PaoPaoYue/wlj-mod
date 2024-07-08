package com.github.paopaoyue.wljmod.patch.avatar;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.github.paopaoyue.wljmod.character.Wlj;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import javassist.CtBehavior;

import java.util.ArrayList;


@SpirePatch(
        clz = AbstractPlayer.class,
        method = "render"
)
public class RenderCharacterPatch {
    @SpireInsertPatch(
            locator = Locator.class,
            localvars = {"sb"}
    )
    public static SpireReturn<Void> Insert(AbstractPlayer __instance, SpriteBatch sb) {
        if (__instance instanceof Wlj) {
            Wlj instance = (Wlj) __instance;
            float opacity = 1;
            float characterImgSwitchDuration = instance.getCharacterImgSwitchDuration();
            if (characterImgSwitchDuration > 0) {
                opacity = MathUtils.lerp(0F, 1F, 1 - characterImgSwitchDuration / Wlj.CHARACTER_IMAG_SWITCH_MAX_DURATION);
                characterImgSwitchDuration -= Gdx.graphics.getDeltaTime();
                if (characterImgSwitchDuration < 0.01F) {
                    characterImgSwitchDuration = 0;
                }
                instance.setCharacterImgSwitchDuration(characterImgSwitchDuration);
            }
            sb.setColor(1, 1, 1, opacity);
            sb.draw(instance.img, instance.drawX - (float) instance.img.getWidth() * Settings.scale / 2.0F + instance.animX, instance.drawY, (float) instance.img.getWidth() * Settings.scale, (float) instance.img.getHeight() * Settings.scale, 0, 0, instance.img.getWidth(), instance.img.getHeight(), instance.flipHorizontal, instance.flipVertical);
            sb.setColor(Color.WHITE.cpy());
            instance.hb.render(sb);
            instance.healthHb.render(sb);
            return SpireReturn.Return();
        }
        return SpireReturn.Continue();
    }

    private static class Locator extends SpireInsertLocator {
        private Locator() {
        }

        public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
            Matcher finalMatcher = new Matcher.MethodCallMatcher(SpriteBatch.class, "setColor");
            return LineFinder.findInOrder(ctMethodToPatch, new ArrayList<>(), finalMatcher);
        }
    }
}
