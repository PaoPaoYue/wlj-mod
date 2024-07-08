package com.github.paopaoyue.wljmod.patch.avatar;

import com.badlogic.gdx.Gdx;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.github.paopaoyue.wljmod.card.AbstractAvatarCard;
import com.github.paopaoyue.wljmod.utility.Reflect;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.utility.HandCheckAction;
import com.megacrit.cardcrawl.actions.utility.ShowCardAndPoofAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.cardManip.ExhaustCardEffect;
import javassist.CtBehavior;

import java.util.ArrayList;

@SpirePatch(
        clz = UseCardAction.class,
        method = "update"
)
public class UseCardActionPatch {

    @SpireInsertPatch(
            locator = Locator.class
    )
    public static SpireReturn<Void> Insert(UseCardAction __instance, AbstractCard ___targetCard, float ___duration) {
        if (___targetCard instanceof AbstractAvatarCard) {
            ___targetCard.exhaustOnUseOnce = false;
            ___targetCard.dontTriggerOnUseCard = false;
            AbstractDungeon.actionManager.addToBottom(new HandCheckAction());
            ___duration -= Gdx.graphics.getDeltaTime();
            Reflect.setPrivate(AbstractGameAction.class, __instance, "duration", ___duration);
            if (___duration < 0.0F) {
                __instance.isDone = true;
            }
            return SpireReturn.Return();
        }

        return SpireReturn.Continue();
    }

    @SpireInsertPatch(
            locator = PurgeOnUseLocator.class
    )
    public static SpireReturn<Void> Insert(UseCardAction __instance, AbstractCard ___targetCard, @ByRef float[] ___duration) {
        AbstractDungeon.actionManager.addToTop(new ShowCardAndPoofAction(___targetCard));
        AbstractDungeon.effectList.add(new ExhaustCardEffect(___targetCard));
        if (AbstractDungeon.player.limbo.contains(___targetCard)) {
            AbstractDungeon.player.limbo.removeCard(___targetCard);
        }
        AbstractDungeon.player.cardInUse = null;
        ___duration[0] -= Gdx.graphics.getDeltaTime();
        if (___duration[0] < 0.0f) {
            __instance.isDone = true;
        }
        return SpireReturn.Return();
    }

    private static class Locator extends SpireInsertLocator {
        private Locator() {
        }

        public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
            Matcher finalMatcher = new Matcher.MethodCallMatcher(CardGroup.class, "moveToDiscardPile");
            return LineFinder.findInOrder(ctMethodToPatch, new ArrayList<>(), finalMatcher);
        }
    }

    private static class PurgeOnUseLocator extends SpireInsertLocator {
        private PurgeOnUseLocator() {
        }

        public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
            Matcher finalMatcher = new Matcher.NewExprMatcher(ShowCardAndPoofAction.class);
            return LineFinder.findInOrder(ctMethodToPatch, new ArrayList<>(), finalMatcher);
        }
    }
}
