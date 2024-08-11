package com.github.paopaoyue.wljmod.card;

import com.github.paopaoyue.wljmod.power.FutureTechPower;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;

public abstract class AbstractWorkerCard extends AbstractWljCard {


    public AbstractWorkerCard(String id, String name, String img, int cost, String rawDescription, CardType type, CardColor color, CardRarity rarity, CardTarget target) {
        super(id, name, img, cost, rawDescription, type, color, rarity, target);
    }


    @Override
    public void triggerOnExhaust() {
        super.triggerOnExhaust();
        AbstractPower power = AbstractDungeon.player.getPower(FutureTechPower.POWER_ID);
        if (power != null) {
            power.flash();
            AbstractMonster target = AbstractDungeon.getMonsters().getRandomMonster(null, true, AbstractDungeon.cardRandomRng);
            if (target != null) {
                this.calculateCardDamage(target);
                this.use(AbstractDungeon.player, target);
            }
        }
    }

}