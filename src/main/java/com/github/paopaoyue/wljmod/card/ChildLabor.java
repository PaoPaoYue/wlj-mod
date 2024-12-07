package com.github.paopaoyue.wljmod.card;

import com.github.paopaoyue.wljmod.action.PurchaseAction;
import com.github.paopaoyue.wljmod.action.TransformDrawnCardsAction;
import com.github.paopaoyue.wljmod.patch.AbstractCardEnum;
import com.github.paopaoyue.wljmod.patch.CardTagEnum;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class ChildLabor extends AbstractWljCard {
    public static final String ID = "Wlj:Child Labor";
    private static final CardStrings cardStrings;

    static {
        cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    }

    public ChildLabor() {
        super(ID, cardStrings.NAME, Util.getImagePath(ID), 1, cardStrings.DESCRIPTION, CardType.SKILL,
                AbstractCardEnum.WLJ_COLOR, CardRarity.UNCOMMON, CardTarget.SELF);
        this.baseMagicNumber = 3;
        this.magicNumber = this.baseMagicNumber;
        this.cardsToPreview = new Rabble();
        this.tags.add(CardTagEnum.PAY);
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new PurchaseAction(2, ok -> {
            if (ok) {
                addToTop(new DrawCardAction(this.magicNumber, new TransformDrawnCardsAction(new Rabble(), false, !this.upgraded)));
            }
        }));
    }

    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.rawDescription = cardStrings.UPGRADE_DESCRIPTION;
            this.initializeDescription();
        }
    }

    public AbstractCard makeCopy() {
        return new ChildLabor();
    }
}
