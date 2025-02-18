package com.github.paopaoyue.wljmod.patch.card;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.github.paopaoyue.wljmod.utility.Reflect;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.actions.common.ChangeStateAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.EnemyMoveInfo;
import javassist.CtBehavior;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;

public class ChangeIntentPatch {

    private static final Logger logger = LogManager.getLogger(ChangeIntentPatch.class);

    private static final Map<AbstractMonster, EnemyMoveInfo> moveRecord = new HashMap<>();
    private static final Map<AbstractMonster, String> moveNameRecord = new HashMap<>();

    private static final List<AbstractMonster.Intent> allowedIntents = Arrays.asList(
        AbstractMonster.Intent.ATTACK,
        AbstractMonster.Intent.ATTACK_BUFF,
        AbstractMonster.Intent.ATTACK_DEBUFF,
        AbstractMonster.Intent.ATTACK_DEFEND,
        AbstractMonster.Intent.BUFF,
        AbstractMonster.Intent.DEBUFF,
        AbstractMonster.Intent.STRONG_DEBUFF,
        AbstractMonster.Intent.DEFEND,
        AbstractMonster.Intent.DEFEND_DEBUFF,
        AbstractMonster.Intent.DEFEND_BUFF,
        AbstractMonster.Intent.ESCAPE
    );

    public static void changeIntentAttack(AbstractMonster m, int damage) {
        if (m.intent != null && allowedIntents.contains(m.intent)) {
            if (!moveRecord.containsKey(m)) {
                moveRecord.put(m, Reflect.getPrivate(AbstractMonster.class, m, "move", EnemyMoveInfo.class));
                moveNameRecord.put(m, m.moveName);
            }
            m.setMove(m.nextMove, AbstractMonster.Intent.ATTACK, damage);
            m.createIntent();
        }
    }

    @SpirePatch(
        clz = GameActionManager.class,
        method = "getNextAction"
    )
    public static class PreEnemyTakeTurnPatch {

        @SpireInsertPatch(
            locator = Locator.class,
            localvars = {"m"}
        )
        public static SpireReturn<Void> Insert(GameActionManager __instance, AbstractMonster m) {
            if (moveRecord.containsKey(m) && m.intent == AbstractMonster.Intent.ATTACK) {
                Integer damage = Reflect.getPrivate(AbstractMonster.class, m, "intentDmg", Integer.class);
                if (damage == null) {
                    logger.error("Failed to get intent damage");
                    damage = 0;
                }
                AbstractDungeon.actionManager.addToBottom(new ChangeStateAction(m, "ATTACK"));
                AbstractDungeon.actionManager.addToBottom(new WaitAction(0.5f));
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, new DamageInfo(m, damage), AbstractGameAction.AttackEffect.BLUNT_LIGHT));

                m.applyTurnPowers();
                String moveName = moveNameRecord.get(m);
                EnemyMoveInfo move = moveRecord.get(m);
                m.setMove(moveName, move.nextMove, move.intent, move.baseDamage, move.multiplier, move.isMultiDamage);
                moveRecord.remove(m);
                moveNameRecord.remove(m);
                __instance.monsterQueue.remove(0);
                if (__instance.monsterQueue.isEmpty()) {
                    __instance.addToBottom(new WaitAction(1.5F));
                }
                return SpireReturn.Return(null);
            }
            return SpireReturn.Continue();
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

}
