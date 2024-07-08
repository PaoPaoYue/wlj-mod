package com.github.paopaoyue.wljmod.patch.avatar;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.github.paopaoyue.wljmod.WljMod;
import com.github.paopaoyue.wljmod.component.AvatarManager;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import javassist.CtBehavior;

import java.util.ArrayList;

import static com.github.paopaoyue.wljmod.utility.Reflect.getStaticPrivate;

@SpirePatch(
        clz = AbstractCreature.class,
        method = "renderHealth"
)
public class RenderHealthBarPatch {
    private static final Texture TEMP_HP_ICON = ImageMaster.loadImage("image/icon/temp_hp.png");
    private static float HEALTH_BAR_HEIGHT = -1.0F;
    private static float HEALTH_BAR_OFFSET_Y = -1.0F;

    @SpireInsertPatch(
            locator = Locator.class,
            localvars = {"x", "y"}
    )
    public static void Insert(AbstractCreature __instance, SpriteBatch sb, float x, float y) {
        if (HEALTH_BAR_HEIGHT == -1.0F) {
            HEALTH_BAR_HEIGHT = 20.0F * Settings.scale;
            HEALTH_BAR_OFFSET_Y = -28.0F * Settings.scale;
        }

        if (!Gdx.input.isKeyPressed(36) && __instance.hbAlpha > 0.0F) {
            AvatarManager avatarManager = WljMod.avatarManager;
            if (avatarManager.isHasAvatar() && AbstractDungeon.player == __instance) {
                sb.setColor(Color.WHITE.cpy());
                sb.draw(TEMP_HP_ICON, x + getStaticPrivate(AbstractCreature.class, "BLOCK_ICON_X", Float.class) - 16.0F + __instance.hb.width, y + getStaticPrivate(AbstractCreature.class, "BLOCK_ICON_Y", Float.class) - 32.0F, 32.0F, 32.0F, 64.0F, 64.0F, Settings.scale, Settings.scale, 0.0F, 0, 0, 64, 64, false, false);
                FontHelper.renderFontCentered(sb, FontHelper.blockInfoFont, String.valueOf(avatarManager.getHp()), x + getStaticPrivate(AbstractCreature.class, "BLOCK_ICON_X", Float.class) + 16.0F + __instance.hb.width, y - 16.0F * Settings.scale, Settings.CREAM_COLOR, 1.0F);
            }
        }

    }

    private static class Locator extends SpireInsertLocator {
        private Locator() {
        }

        public int[] Locate(CtBehavior ctBehavior) throws Exception {
            Matcher finalMatcher = new Matcher.FieldAccessMatcher(AbstractCreature.class, "currentBlock");
            ArrayList<Matcher> matchers = new ArrayList<>();
            matchers.add(finalMatcher);
            return LineFinder.findInOrder(ctBehavior, matchers, finalMatcher);
        }
    }
}

