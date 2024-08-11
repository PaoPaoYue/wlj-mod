package com.github.paopaoyue.wljmod.card;

import com.github.paopaoyue.wljmod.WljMod;
import com.github.paopaoyue.wljmod.action.PurchaseAction;
import com.github.paopaoyue.wljmod.patch.AbstractCardEnum;
import com.github.paopaoyue.wljmod.power.CapitalistPower;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class Capitalist extends AbstractWljCard {
    public static final String ID = "Wlj:Capitalist";
    private static final CardStrings cardStrings;

    static {
        cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    }

    public Capitalist() {
        super(ID, cardStrings.NAME, Util.getImagePath(ID), 2, cardStrings.DESCRIPTION, CardType.POWER,
                AbstractCardEnum.WLJ_COLOR, CardRarity.RARE, CardTarget.SELF);
        this.baseMagicNumber = 8;
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        PurchaseAction.sfxUtil.playSFX();
        WljMod.tempGold += this.baseMagicNumber;
        this.addToBot(new ApplyPowerAction(p, p, new CapitalistPower(p, 1)));
    }

    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeMagicNumber(12);
        }
    }

    public AbstractCard makeCopy() {
        return new Capitalist();
    }
}
