package com.github.paopaoyue.wljmod.card;

import com.github.paopaoyue.wljmod.action.DiscardWithCallbackAction;
import com.github.paopaoyue.wljmod.action.ReplicateWorkerAction;
import com.github.paopaoyue.wljmod.patch.AbstractCardEnum;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class Joy extends AbstractWljCard {
    public static final String ID = "Wlj:Joy";
    private static final CardStrings cardStrings;

    static {
        cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    }

    public Joy() {
        super(ID, cardStrings.NAME, Util.getImagePath(ID), 2, cardStrings.DESCRIPTION, CardType.SKILL,
                AbstractCardEnum.WLJ_COLOR, CardRarity.RARE, CardTarget.SELF);
        this.baseMagicNumber = 3;
        this.magicNumber = this.baseMagicNumber;
        this.exhaust = true;
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new DiscardWithCallbackAction(p, p, -1, false, cards -> {
            this.addToTop(new ReplicateWorkerAction(cards.size() + this.magicNumber, this.upgraded));
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
        return new Joy();
    }
}
