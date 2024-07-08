package com.github.paopaoyue.wljmod.patch.card;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.github.paopaoyue.wljmod.card.Sake;
import com.github.paopaoyue.wljmod.utility.Reflect;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.actions.common.ChangeStateAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.RollMoveAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import javassist.CtBehavior;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;

@SpirePatch(
        clz = GameActionManager.class,
        method = "getNextAction"
)
public class SakePatch {

    private static final Logger logger = LogManager.getLogger(SakePatch.class);

    @SpireInsertPatch(
            locator = Locator.class,
            localvars = {"m"}
    )
    public static void Insert(AbstractMonster m) {
        if (m.nextMove == Sake.MOCKED_MOVE) {
            Integer damage = Reflect.getPrivate(AbstractMonster.class, m, "intentDmg", Integer.class);
            if (damage == null) {
                logger.error("Failed to get intent damage");
                damage = 0;
            }
            AbstractDungeon.actionManager.addToBottom(new ChangeStateAction(m, "ATTACK"));
            AbstractDungeon.actionManager.addToBottom(new WaitAction(0.5f));
            AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, new DamageInfo(m, damage), AbstractGameAction.AttackEffect.BLUNT_LIGHT));
            AbstractDungeon.actionManager.addToBottom(new RollMoveAction(m));
        }
    }

    private static class Locator extends SpireInsertLocator {
        private Locator() {
        }

        public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
            Matcher finalMatcher = new Matcher.MethodCallMatcher(AbstractMonster.class, "takeTurn");
            return LineFinder.findInOrder(ctMethodToPatch, new ArrayList<>(), finalMatcher);
        }
    }
}
