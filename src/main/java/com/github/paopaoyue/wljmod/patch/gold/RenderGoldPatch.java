package com.github.paopaoyue.wljmod.patch.gold;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.github.paopaoyue.wljmod.WljMod;
import com.github.paopaoyue.wljmod.utility.Reflect;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.ui.panels.TopPanel;
import javassist.CtBehavior;

import java.util.ArrayList;

@SpirePatch(
        clz = TopPanel.class,
        method = "renderGold"
)
public class RenderGoldPatch {

    private static final float GOLD_NUM_OFFSET_X = Reflect.getStaticPrivate(TopPanel.class, "GOLD_NUM_OFFSET_X", Float.class);

    private static final float INFO_TEXT_Y = Reflect.getStaticPrivate(TopPanel.class, "INFO_TEXT_Y", Float.class);

    private static final float GOLD_ICON_X = Reflect.getPrivate(TopPanel.class, AbstractDungeon.topPanel, "goldIconX", Float.class);

    private static final float DISPLAY_GOLD_TIME = 2.0f;

    private static float displayGoldTimer = 0.0f;

    @SpireInsertPatch(
            locator = Locator.class
    )
    public static SpireReturn<Void> Insert(TopPanel __instance, SpriteBatch sb) {
        if (AbstractDungeon.player.displayGold == AbstractDungeon.player.gold) {
            if ((WljMod.displayTempGold > 0 || WljMod.tempGold > 0) && displayGoldTimer == 0) {
                FontHelper.renderFontLeftTopAligned(sb, FontHelper.topPanelInfoFont, Integer.toString(WljMod.displayTempGold), GOLD_ICON_X + GOLD_NUM_OFFSET_X, INFO_TEXT_Y, Color.CYAN);
            } else {
                FontHelper.renderFontLeftTopAligned(sb, FontHelper.topPanelInfoFont, Integer.toString(AbstractDungeon.player.displayGold), GOLD_ICON_X + GOLD_NUM_OFFSET_X, INFO_TEXT_Y, Settings.GOLD_COLOR);
                displayGoldTimer -= Gdx.graphics.getDeltaTime();
                displayGoldTimer = Math.max(displayGoldTimer, 0);
            }
        } else if (AbstractDungeon.player.displayGold > AbstractDungeon.player.gold) {
            displayGoldTimer = DISPLAY_GOLD_TIME;
            FontHelper.renderFontLeftTopAligned(sb, FontHelper.topPanelInfoFont, Integer.toString(AbstractDungeon.player.displayGold), GOLD_ICON_X + GOLD_NUM_OFFSET_X, INFO_TEXT_Y, Settings.RED_TEXT_COLOR);
        } else {
            displayGoldTimer = DISPLAY_GOLD_TIME;
            FontHelper.renderFontLeftTopAligned(sb, FontHelper.topPanelInfoFont, Integer.toString(AbstractDungeon.player.displayGold), GOLD_ICON_X + GOLD_NUM_OFFSET_X, INFO_TEXT_Y, Settings.GREEN_TEXT_COLOR);
        }
        __instance.goldHb.render(sb);
        return SpireReturn.Return(null);
    }

    private static class Locator extends SpireInsertLocator {
        private Locator() {
        }

        public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
            Matcher finalMatcher = new Matcher.FieldAccessMatcher(AbstractPlayer.class, "displayGold");
            return LineFinder.findInOrder(ctMethodToPatch, new ArrayList<>(), finalMatcher);
        }
    }
}
