package com.github.paopaoyue.wljmod.relic;

import basemod.abstracts.CustomRelic;
import com.github.paopaoyue.wljmod.power.LewisPower;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.EnergyManager;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.RelicStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.relics.AbstractRelic;

public class Culture extends CustomRelic {
    public static final String ID = "Wlj:Culture";
    private static final RelicStrings strings = CardCrawlGame.languagePack.getRelicStrings(ID);
    private static final RelicTier TIER = RelicTier.BOSS;
    private static final LandingSound SOUND = LandingSound.MAGICAL;

    public Culture() {
        super(ID, ImageMaster.loadImage("image/icon/culture.png"), TIER, SOUND);
    }

    public String getUpdatedDescription() {
        return strings.DESCRIPTIONS[0];
    }

    @Override
    public void atBattleStart() {
        for (final AbstractMonster m : AbstractDungeon.getMonsters().monsters) {
            m.addPower(new LewisPower(m, 1));
        }
        AbstractDungeon.onModifyPower();
    }

    @Override
    public void onSpawnMonster(final AbstractMonster monster) {
        monster.addPower(new LewisPower(monster, 1));
        AbstractDungeon.onModifyPower();
    }

    @Override
    public void onEquip() {
        final EnergyManager energy = AbstractDungeon.player.energy;
        ++energy.energyMaster;
    }

    @Override
    public void onUnequip() {
        final EnergyManager energy = AbstractDungeon.player.energy;
        --energy.energyMaster;
    }

    @Override
    public AbstractRelic makeCopy() {
        return new Culture();
    }
}
