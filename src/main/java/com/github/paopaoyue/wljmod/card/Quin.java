package com.github.paopaoyue.wljmod.card;

import basemod.abstracts.CustomCard;
import com.github.paopaoyue.wljmod.patch.AbstractCardEnum;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;

public class Quin extends CustomCard {
    public static final String ID = "Wlj:Quin";
    private static final CardStrings cardStrings;

    static {
        cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    }

    public Quin() {
        super(ID, cardStrings.NAME, Util.getImagePath(""), 1, cardStrings.DESCRIPTION, CardType.SKILL,
                AbstractCardEnum.WLJ_COLOR, CardRarity.UNCOMMON, CardTarget.SELF);
        this.baseMagicNumber = 1;
        this.magicNumber = this.baseMagicNumber;
        this.cardsToPreview = new Wand();
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new MakeTempCardInHandAction(new Wand(EnergyPanel.getCurrentEnergy() - this.costForTurn)));
    }


    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeBaseCost(0);
        }
    }

    public AbstractCard makeCopy() {
        return new Quin();
    }
}
