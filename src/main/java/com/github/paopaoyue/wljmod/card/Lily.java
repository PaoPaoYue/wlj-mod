package com.github.paopaoyue.wljmod.card;

import basemod.abstracts.CustomCard;
import com.github.paopaoyue.wljmod.patch.AbstractCardEnum;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class Lily extends CustomCard {
    public static final String ID = "Wlj:Lily";
    private static final CardStrings cardStrings;
    private static final int NAMES_COUNT = 5;

    static {
        cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    }

    private int rotateCount = 0;

    public Lily() {
        super(ID, cardStrings.NAME, Util.getImagePath(ID), 0, cardStrings.DESCRIPTION, CardType.ATTACK,
                AbstractCardEnum.WLJ_COLOR, CardRarity.COMMON, CardTarget.ENEMY);
        this.baseDamage = 9;
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        if (rotateCount == NAMES_COUNT - 1) {
            this.addToBot(new DamageAction(m, new DamageInfo(p, damage, this.damageTypeForTurn), AbstractGameAction.AttackEffect.BLUNT_LIGHT));
        }
    }

    @Override
    public void triggerOnCardPlayed(AbstractCard cardPlayed) {
        this.rotateCount = (this.rotateCount + 1) % NAMES_COUNT;
        this.name = cardStrings.EXTENDED_DESCRIPTION[this.rotateCount] + (this.upgraded ? "+" : "");
        this.initializeTitle();

    }

    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeDamage(3);
            this.name = cardStrings.EXTENDED_DESCRIPTION[this.rotateCount] + "+";
            this.initializeTitle();
        }
    }

    public AbstractCard makeCopy() {
        return new Lily();
    }
}
