package com.github.paopaoyue.wljmod.card;

import basemod.abstracts.CustomCard;
import com.github.paopaoyue.wljmod.power.FutureTechPower;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public abstract class AbstractWorkerCard extends CustomCard {


    public AbstractWorkerCard(String id, String name, String img, int cost, String rawDescription, CardType type, CardColor color, CardRarity rarity, CardTarget target) {
        super(id, name, img, cost, rawDescription, type, color, rarity, target);
    }


    @Override
    public void triggerOnExhaust() {
        if (AbstractDungeon.player.hasPower(FutureTechPower.POWER_ID)) {
            AbstractMonster target = AbstractDungeon.getMonsters().getRandomMonster(null, true, AbstractDungeon.cardRandomRng);
            if (target != null) {
                this.use(AbstractDungeon.player, target);
            }
        }
    }

}