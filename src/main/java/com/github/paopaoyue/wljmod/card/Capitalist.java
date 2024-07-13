package com.github.paopaoyue.wljmod.card;

import basemod.abstracts.CustomCard;
import com.github.paopaoyue.wljmod.action.PurchaseAction;
import com.github.paopaoyue.wljmod.patch.AbstractCardEnum;
import com.github.paopaoyue.wljmod.power.CapitalistPower;
import com.github.paopaoyue.wljmod.power.CorruptionPower;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class Capitalist extends CustomCard {
    public static final String ID = "Wlj:Capitalist";
    private static final CardStrings cardStrings;

    static {
        cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    }

    public Capitalist() {
        super(ID, cardStrings.NAME, Util.getImagePath(ID), 3, cardStrings.DESCRIPTION, CardType.POWER,
                AbstractCardEnum.WLJ_COLOR, CardRarity.RARE, CardTarget.SELF);
        this.baseMagicNumber = 3;
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        PurchaseAction.sfxUtil.playSFX();
        this.addToBot(new ApplyPowerAction(p, p, new CapitalistPower(p)));
        this.addToBot(new ApplyPowerAction(p, p, new CorruptionPower(p, this.magicNumber)));
    }

    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeMagicNumber(-1);
        }
    }

    public AbstractCard makeCopy() {
        return new Capitalist();
    }
}
