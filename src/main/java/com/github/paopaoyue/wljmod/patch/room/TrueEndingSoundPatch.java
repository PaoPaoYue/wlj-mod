package com.github.paopaoyue.wljmod.patch.room;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.github.paopaoyue.wljmod.character.Wlj;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

@SpirePatch(
        clz = AbstractMonster.class,
        method = "playBossStinger"
)
public class TrueEndingSoundPatch {

    public static SpireReturn<Void> Prefix() {
        if (AbstractDungeon.id.equals("TheEnding") &&  AbstractDungeon.player instanceof Wlj) {
            return SpireReturn.Return(null);
        }
        return SpireReturn.Continue();
    }
}
