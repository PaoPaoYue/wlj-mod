package com.github.paopaoyue.wljmod.card;

import basemod.abstracts.CustomCard;
import com.github.paopaoyue.wljmod.action.DiceAction;
import com.github.paopaoyue.wljmod.patch.AbstractCardEnum;
import com.github.paopaoyue.wljmod.patch.CardTagEnum;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;

public class Dice extends CustomCard {
    public static final String ID = "Wlj:Dice";
    private static final CardStrings cardStrings;

    static {
        cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    }

    public Dice() {
        super(ID, cardStrings.NAME, Util.getImagePath(ID), -1, cardStrings.DESCRIPTION, CardType.ATTACK,
                AbstractCardEnum.WLJ_COLOR, CardRarity.UNCOMMON, CardTarget.ENEMY);
        this.tags.add(CardTagEnum.PAY);
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new DiceAction(m, this.damage, upgraded ? 2 : 3, this.magicNumber, this.energyOnUse, this.freeToPlayOnce));
    }

    public void applyPowers() {
        int effect = EnergyPanel.totalCount;
        if (this.energyOnUse != 0) {
            effect = this.energyOnUse;
        }
        if (AbstractDungeon.player.hasRelic("Chemical X")) {
            effect += 2;
        }
        this.baseDamage = effect * 9;
        this.baseMagicNumber = effect * 5;
        super.applyPowers();
        this.rawDescription = cardStrings.DESCRIPTION + cardStrings.EXTENDED_DESCRIPTION[0];
        this.initializeDescription();
    }

    public void calculateCardDamage(AbstractMonster mo) {
        int effect = EnergyPanel.totalCount;
        if (this.energyOnUse != 0) {
            effect = this.energyOnUse;
        }
        if (AbstractDungeon.player.hasRelic("Chemical X")) {
            effect += 2;
        }
        this.baseDamage = effect * 9;
        this.baseMagicNumber = effect * 5;
        this.magicNumber = this.baseMagicNumber;
        super.calculateCardDamage(mo);
        this.rawDescription = (upgraded ? cardStrings.UPGRADE_DESCRIPTION : cardStrings.DESCRIPTION) + cardStrings.EXTENDED_DESCRIPTION[0];
        this.initializeDescription();
    }

    public void onMoveToDiscard() {
        this.rawDescription = (upgraded ? cardStrings.UPGRADE_DESCRIPTION : cardStrings.DESCRIPTION);
        this.initializeDescription();
    }

    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.rawDescription = cardStrings.UPGRADE_DESCRIPTION;
            this.initializeDescription();
        }
    }

    public AbstractCard makeCopy() {
        return new Dice();
    }
}
