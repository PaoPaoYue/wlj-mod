package com.github.paopaoyue.wljmod.patch.card;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.github.paopaoyue.wljmod.card.Sts;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.Soul;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

@SpirePatch(
        clz = Soul.class,
        method = "obtain"
)
public class StsPatch {

    @SpirePostfixPatch
    public static void Postfix() {
        for (AbstractCard c : AbstractDungeon.player.masterDeck.group) {
            if (c instanceof Sts) {
                ((Sts) c).triggerWhenObtainCards();
            }
        }
    }
}
