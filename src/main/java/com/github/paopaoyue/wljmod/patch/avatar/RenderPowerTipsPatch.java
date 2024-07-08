package com.github.paopaoyue.wljmod.patch.avatar;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.github.paopaoyue.wljmod.WljMod;
import com.github.paopaoyue.wljmod.component.AbstractAvatar;
import com.github.paopaoyue.wljmod.component.AvatarManager;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.helpers.PowerTip;
import javassist.CtBehavior;

import java.util.ArrayList;


@SpirePatch(
        clz = AbstractPlayer.class,
        method = "renderPowerTips"
)
public class RenderPowerTipsPatch {
    @SpireInsertPatch(
            locator = Locator.class,
            localvars = {"tips"}
    )
    public static void Insert(AbstractPlayer __instance, ArrayList<PowerTip> tips) {
        AvatarManager avatarManager = WljMod.avatarManager;
        if (avatarManager.isHasAvatar()) {
            AbstractAvatar avatar = avatarManager.getCurrentAvatar();
            avatar.updateDescription();
            tips.add(new PowerTip(avatar.getName(), avatar.getDescription()));
        }
    }

    private static class Locator extends SpireInsertLocator {
        private Locator() {
        }

        public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
            Matcher finalMatcher = new Matcher.FieldAccessMatcher(AbstractPlayer.class, "stance");
            return LineFinder.findInOrder(ctMethodToPatch, new ArrayList<>(), finalMatcher);
        }
    }
}
