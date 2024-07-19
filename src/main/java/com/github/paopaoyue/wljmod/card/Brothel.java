package com.github.paopaoyue.wljmod.card;

import basemod.abstracts.CustomCard;
import com.github.paopaoyue.wljmod.action.ExhaustCardGroupAction;
import com.github.paopaoyue.wljmod.patch.AbstractCardEnum;
import com.github.paopaoyue.wljmod.patch.CardTagEnum;
import com.github.paopaoyue.wljmod.power.BrothelPower;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class Brothel extends CustomCard {
    public static final String ID = "Wlj:Brothel";
    private static final CardStrings cardStrings;

    static {
        cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    }

    public Brothel() {
        super(ID, cardStrings.NAME, Util.getImagePath(ID), 1, cardStrings.DESCRIPTION, CardType.POWER,
                AbstractCardEnum.WLJ_COLOR, CardRarity.UNCOMMON, CardTarget.SELF);
        this.cardsToPreview = new Performer();
        this.tags.add(CardTagEnum.TAIWU);
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new ExhaustCardGroupAction(p.discardPile, c -> c instanceof Performer, cards -> {
            if (!cards.isEmpty())
                this.addToTop(new ApplyPowerAction(p, p, new BrothelPower(p, cards.size()), cards.size()));
        }));
    }

    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeBaseCost(0);
        }
    }

    public AbstractCard makeCopy() {
        return new Brothel();
    }
}
