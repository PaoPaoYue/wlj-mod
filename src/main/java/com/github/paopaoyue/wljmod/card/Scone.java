package com.github.paopaoyue.wljmod.card;

import com.github.paopaoyue.wljmod.action.PurchaseAction;
import com.github.paopaoyue.wljmod.patch.AbstractCardEnum;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class Scone extends AbstractWljCard {
    public static final String ID = "Wlj:Scone";
    private static final CardStrings cardStrings;

    static {
        cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    }

    public Scone() {
        super(ID, cardStrings.NAME, Util.getImagePath(ID), 1, cardStrings.DESCRIPTION, CardType.SKILL,
                AbstractCardEnum.WLJ_COLOR, CardRarity.COMMON, CardTarget.SELF);
        this.baseBlock = 13;
        this.baseMagicNumber = 4;
        this.magicNumber = this.baseMagicNumber;
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new PurchaseAction(this.magicNumber, ok -> {
            if (ok) {
                this.addToTop(new GainBlockAction(p, p, this.block));
            }
        }));
    }

    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeMagicNumber(-2);
        }
    }

    public AbstractCard makeCopy() {
        return new Scone();
    }
}
