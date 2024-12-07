package com.github.paopaoyue.wljmod.card;

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
        this.baseMagicNumber = 6;
        this.magicNumber = this.baseMagicNumber;
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        PurchaseAction.sfxUtil.playSFX();
        if (!upgraded) {
            this.addToBot(new PurchaseAction(this.magicNumber, ok -> {
                if (ok) {
                    this.addToTop(new ApplyPowerAction(p, p, new CapitalistPower(p, 1)));
                }
            }));
        }
        else {
            this.addToBot(new ApplyPowerAction(p, p, new CapitalistPower(p, 1)));
        }
    }

    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.rawDescription = cardStrings.UPGRADE_DESCRIPTION;
            this.initializeDescription();
        }
    }

    public AbstractCard makeCopy() {
        return new Capitalist();
    }
}
