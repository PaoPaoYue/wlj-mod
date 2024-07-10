package com.github.paopaoyue.wljmod.card;

import basemod.abstracts.CustomCard;
import com.github.paopaoyue.wljmod.patch.AbstractCardEnum;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class Phoenix extends CustomCard {
    public static final String ID = "Wlj:Phoenix";
    public static final int MAX_LEVEL = 2;
    private static final CardStrings cardStrings;

    static {
        cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    }

    public Phoenix() {
        this(MAX_LEVEL);
    }

    public Phoenix(int level) {
        super(ID, cardStrings.NAME, Util.getImagePath(ID), 1 << level, cardStrings.DESCRIPTION, CardType.ATTACK,
                AbstractCardEnum.WLJ_COLOR, CardRarity.UNCOMMON, CardTarget.ENEMY);
        this.baseDamage = (1 << level) * 10;
        this.baseMagicNumber = 1;
        this.magicNumber = this.baseMagicNumber;
        this.misc = level;
        if (level > 0) {
            this.purgeOnUse = true;
        } else {
            this.rawDescription = cardStrings.UPGRADE_DESCRIPTION;
            this.initializeDescription();
        }
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new DamageAction(m, new DamageInfo(p, this.damage, this.damageTypeForTurn), AbstractGameAction.AttackEffect.BLUNT_HEAVY));
        this.addToBot(new GainEnergyAction(this.magicNumber));
        if (this.misc > 0) {
            AbstractCard tempCard = new Phoenix(this.misc - 1);
            if (this.upgraded) {
                tempCard.upgrade();
            }
            this.addToBot(new MakeTempCardInHandAction(tempCard, 2, true));
        }
    }

    public void triggerOnExhaust() {
        if (this.misc > 0) {
            AbstractCard tempCard = new Phoenix(this.misc - 1);
            if (this.upgraded) {
                tempCard.upgrade();
            }
            this.addToBot(new MakeTempCardInHandAction(tempCard, 2, true));
        }
    }

    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeDamage((1 << this.misc) * 3);
        }
    }

    public AbstractCard makeCopy() {
        return new Phoenix(this.misc);
    }
}
