package com.github.paopaoyue.wljmod.patch.avatar;

import com.evacipated.cardcrawl.mod.stslib.vfx.combat.TempDamageNumberEffect;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.github.paopaoyue.wljmod.WljMod;
import com.github.paopaoyue.wljmod.component.AvatarManager;
import com.github.paopaoyue.wljmod.power.SteepPower;
import com.github.paopaoyue.wljmod.utility.Inject;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ScreenShake.ShakeDur;
import com.megacrit.cardcrawl.helpers.ScreenShake.ShakeIntensity;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.vfx.combat.DamageImpactLineEffect;
import com.megacrit.cardcrawl.vfx.combat.StrikeEffect;
import javassist.CtBehavior;

import java.util.ArrayList;

@SpirePatch(
        clz = AbstractPlayer.class,
        method = "damage"
)
public class PlayerDamagePatch {

    @SpireInsertPatch(
            locator = AfterDecrementLocator.class,
            localvars = {"damageAmount", "hadBlock"}
    )
    public static void Insert(AbstractCreature __instance, DamageInfo info, @ByRef int[] damageAmount, boolean hadBlock) {
        for (AbstractPower p : __instance.powers) {
            if (p instanceof SteepPower) {
                ((SteepPower) p).onDamagedAfterBlockDecrement(damageAmount[0], info.type);
            }
        }

        if (damageAmount[0] > 0 && info.type != DamageInfo.DamageType.HP_LOSS) {

            AvatarManager avatarManager = WljMod.avatarManager;
            int tempHp = avatarManager.getHp();
            if (tempHp > 0) {

                for (int i = 0; i < 18; ++i) {
                    AbstractDungeon.effectsQueue.add(new DamageImpactLineEffect(__instance.hb.cX, __instance.hb.cY));
                }

                CardCrawlGame.screenShake.shake(ShakeIntensity.MED, ShakeDur.SHORT, false);
                if (tempHp > damageAmount[0]) {
                    AbstractDungeon.effectsQueue.add(new TempDamageNumberEffect(__instance, __instance.hb.cX, __instance.hb.cY, damageAmount[0]));
                    int damage = avatarManager.getCurrentAvatar().onDamaged(damageAmount[0], info.type);
                    avatarManager.setHp(tempHp - damage);
                    damageAmount[0] = 0;
                } else {
                    AbstractDungeon.effectsQueue.add(new TempDamageNumberEffect(__instance, __instance.hb.cX, __instance.hb.cY, tempHp));
                    int damage  = avatarManager.getCurrentAvatar().onDamaged(damageAmount[0], info.type);
                    avatarManager.setHp(Math.max(0, tempHp - damage));
                    if (avatarManager.getHp() == 0) {
                        avatarManager.onDead();
                    }
                    damageAmount[0] = (damage - tempHp);
                }
            }
        }
    }

    @SpireInsertPatch(
            locator = StrikeEffectLocator.class
    )
    public static SpireReturn<Void> Insert(AbstractCreature __instance, DamageInfo info) {
        AvatarManager avatarManager = WljMod.avatarManager;
        return avatarManager.getHp() > 0 ? SpireReturn.Return((Void) null) : SpireReturn.Continue();
    }

    private static class AfterDecrementLocator extends SpireInsertLocator {
        private AfterDecrementLocator() {
        }

        public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
            Matcher finalMatcher = new Matcher.MethodCallMatcher(AbstractPlayer.class, "decrementBlock");
            return Inject.insertAfter(LineFinder.findInOrder(ctMethodToPatch, new ArrayList<>(), finalMatcher), 1);
        }
    }

    private static class StrikeEffectLocator extends SpireInsertLocator {
        private StrikeEffectLocator() {
        }

        public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
            Matcher finalMatcher = new Matcher.NewExprMatcher(StrikeEffect.class);
            int[] all = LineFinder.findAllInOrder(ctMethodToPatch, new ArrayList<>(), finalMatcher);
            return new int[]{all[all.length - 1]};
        }
    }
}
