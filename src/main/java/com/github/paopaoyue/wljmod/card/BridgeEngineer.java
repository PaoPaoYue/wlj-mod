package com.github.paopaoyue.wljmod.card;

import com.github.paopaoyue.wljmod.effect.GoldTextOnPlayerEffect;
import com.github.paopaoyue.wljmod.patch.AbstractCardEnum;
import com.github.paopaoyue.wljmod.patch.CardTagEnum;
import com.github.paopaoyue.wljmod.power.CorruptionPower;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainGoldAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class BridgeEngineer extends AbstractWljCard {
    public static final String ID = "Wlj:Bridge Engineer";
    private static final CardStrings cardStrings;

    static {
        cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    }

    public BridgeEngineer() {
        super(ID, cardStrings.NAME, Util.getImagePath(ID), 1, cardStrings.DESCRIPTION, CardType.POWER,
                AbstractCardEnum.WLJ_COLOR, CardRarity.UNCOMMON, CardTarget.SELF);
        this.baseMagicNumber = 12;
        this.magicNumber = this.baseMagicNumber;
        this.tags.add(CardTagEnum.PAY);
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.effectList.add(new GoldTextOnPlayerEffect(this.magicNumber));
        this.addToBot(new GainGoldAction(this.magicNumber));
        this.addToBot(new ApplyPowerAction(p, p, new CorruptionPower(p, 2), 2));
    }

    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeMagicNumber(4);
        }
    }

    public AbstractCard makeCopy() {
        return new BridgeEngineer();
    }
}
