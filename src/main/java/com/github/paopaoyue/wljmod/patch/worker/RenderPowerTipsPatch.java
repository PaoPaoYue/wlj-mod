package com.github.paopaoyue.wljmod.patch.worker;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.github.paopaoyue.wljmod.WljMod;
import com.github.paopaoyue.wljmod.card.Performer;
import com.github.paopaoyue.wljmod.card.Prisoner;
import com.github.paopaoyue.wljmod.card.Rabble;
import com.github.paopaoyue.wljmod.character.Wlj;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.localization.UIStrings;
import javassist.CtBehavior;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;


@SpirePatch(
        clz = AbstractPlayer.class,
        method = "renderPowerTips"
)
public class RenderPowerTipsPatch {

    public static final String[] TEXT;
    private static final UIStrings uiStrings;

    static {
        uiStrings = CardCrawlGame.languagePack.getUIString("Wlj:WorkerTips");
        TEXT = uiStrings.TEXT;
    }

    @SpireInsertPatch(
            locator = Locator.class,
            localvars = {"tips"}
    )
    public static void Insert(AbstractPlayer __instance, ArrayList<PowerTip> tips) {
        AtomicInteger performer = new AtomicInteger();
        AtomicInteger rabble = new AtomicInteger();
        AtomicInteger prisoner = new AtomicInteger();
        WljMod.workerManager.iterOutsideDiscardPile(card -> {
            if (card instanceof Performer) {
                performer.getAndAdd(card.magicNumber);
            } else if (card instanceof Rabble) {
                rabble.getAndAdd(card.magicNumber);
            } else if (card instanceof Prisoner) {
                prisoner.getAndAdd(card.magicNumber);
            }
        });

        if (AbstractDungeon.player instanceof Wlj || performer.get() > 0 || rabble.get() > 0 || prisoner.get() > 0) {
            tips.add(0, new PowerTip(TEXT[0], TEXT[1] + performer.get() + TEXT[2] + rabble.get() + TEXT[3] + prisoner.get() + TEXT[4]));
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
