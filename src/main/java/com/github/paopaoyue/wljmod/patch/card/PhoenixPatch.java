package com.github.paopaoyue.wljmod.patch.card;

import com.badlogic.gdx.graphics.Color;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.github.paopaoyue.wljmod.action.ForcedWaitAction;
import com.github.paopaoyue.wljmod.card.Phoenix;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.InstantKillAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.WeightyImpactEffect;
import javassist.CtBehavior;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class PhoenixPatch {

    @SpirePatch(
            clz = AbstractPlayer.class,
            method = "applyStartOfCombatPreDrawLogic"
    )
    public static class StartOfCombatPatch {
        public static void Postfix(AbstractPlayer __instance) {
            List<AbstractCard> tmpGroup = __instance.drawPile.group.stream().filter(card -> card instanceof Phoenix).collect(Collectors.toList());
            __instance.discardPile.group.addAll(tmpGroup);
            __instance.drawPile.group.removeAll(tmpGroup);
        }
    }

    @SpirePatch(
            clz = AbstractPlayer.class,
            method = "onCardDrawOrDiscard"
    )
    public static class OnCardDrawOrDiscardPatch {

        @SpireInsertPatch(
                locator = Locator.class
        )
        public static void Insert(AbstractPlayer __instance) {
            if (AbstractDungeon.player.hand.group.stream()
                    .mapToInt(c -> (c instanceof Phoenix && !((Phoenix) c).isMainBody()) ? 1 : 0).sum() >= 10) {
                AbstractDungeon.actionManager.addToBottom(new ForcedWaitAction(0.5f));
                for (AbstractMonster m : AbstractDungeon.getCurrRoom().monsters.monsters) {
                    if (!m.isDeadOrEscaped()) {
                        AbstractDungeon.actionManager.addToBottom(new VFXAction(new WeightyImpactEffect(m.hb.cX, m.hb.cY, Color.GOLD.cpy())));
                    }
                }
                AbstractDungeon.actionManager.addToBottom(new WaitAction(0.8f));
                for (AbstractMonster m : AbstractDungeon.getCurrRoom().monsters.monsters) {
                    if (!m.isDeadOrEscaped()) {
                        AbstractDungeon.actionManager.addToBottom(new InstantKillAction(m));
                    }
                }
            }
        }

        private static class Locator extends SpireInsertLocator {
            private Locator() {
            }

            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
                Matcher finalMatcher = new Matcher.MethodCallMatcher(CardGroup.class, "applyPowers");
                return LineFinder.findAllInOrder(ctMethodToPatch, new ArrayList<>(), finalMatcher);
            }
        }
    }
}
