package com.github.paopaoyue.wljmod.patch.avatar;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.github.paopaoyue.wljmod.card.AbstractAvatarCard;
import com.github.paopaoyue.wljmod.character.Wlj;
import com.megacrit.cardcrawl.cards.Soul;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import javassist.CtBehavior;

import java.util.ArrayList;

@SpirePatch(
        clz = Soul.class,
        method = "update"
)
public class SwitchAnimePatch {
    @SpireInsertPatch(
            locator = Locator.class
    )
    public static void Insert(Soul __instance) {
        if (__instance.card instanceof AbstractAvatarCard && AbstractDungeon.player instanceof Wlj) {
            Wlj player = (Wlj) AbstractDungeon.player;
            player.SwitchCharacterImage(((AbstractAvatarCard) __instance.card).getAvatar().getCharacterImage());
        }
    }

    private static class Locator extends SpireInsertLocator {
        private Locator() {
        }

        public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
            Matcher finalMatcher = new Matcher.FieldAccessMatcher(AbstractDungeon.class, "effectList");
            return LineFinder.findInOrder(ctMethodToPatch, new ArrayList<>(), finalMatcher);
        }
    }
}
