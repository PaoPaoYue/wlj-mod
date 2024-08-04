package com.github.paopaoyue.wljmod.card;

import com.github.paopaoyue.wljmod.action.LayoffAction;
import com.github.paopaoyue.wljmod.patch.AbstractCardEnum;
import com.github.paopaoyue.wljmod.power.AlarmPower;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class Alarm extends AbstractLayoffCard {
    public static final String ID = "Wlj:Alarm";
    private static final CardStrings cardStrings;

    static {
        cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    }

    public Alarm() {
        super(ID, cardStrings.NAME, Util.getImagePath(ID), 1, cardStrings.DESCRIPTION, CardType.SKILL,
                AbstractCardEnum.WLJ_COLOR, CardRarity.UNCOMMON, CardTarget.SELF, 2);
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new LayoffAction(this.getLayoffAmount()));
        this.addToBot(new ApplyPowerAction(p, p, new AlarmPower(p, 1), 1));
    }


    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeLayoffAmount(1);
        }
    }

    public AbstractCard makeCopy() {
        return new Alarm();
    }
}
