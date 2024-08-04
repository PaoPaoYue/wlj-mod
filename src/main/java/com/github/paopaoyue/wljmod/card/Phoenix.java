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
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class Phoenix extends CustomCard {
    public static final String ID = "Wlj:Phoenix";
    public static final int MAX_LEVEL = 10;
    private static final CardStrings cardStrings;

    static {
        cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    }

    public Phoenix() {
        this(MAX_LEVEL);
    }

    public Phoenix(int level) {
        super(ID, cardStrings.NAME, Util.getImagePath(ID), level, cardStrings.DESCRIPTION, CardType.ATTACK,
                AbstractCardEnum.WLJ_COLOR, CardRarity.UNCOMMON, CardTarget.ENEMY);
        this.baseDamage = level * 7;
        this.baseMagicNumber = 1;
        this.magicNumber = this.baseMagicNumber;
        this.misc = level;
        if (isMainBody()) {
            this.purgeOnUse = true;
            this.cardsToPreview = new Phoenix(1);
        } else {
            this.rawDescription = cardStrings.UPGRADE_DESCRIPTION;
            this.initializeDescription();
        }
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new DamageAction(m, new DamageInfo(p, this.damage, this.damageTypeForTurn), AbstractGameAction.AttackEffect.BLUNT_HEAVY));
        this.addToBot(new GainEnergyAction(this.magicNumber));
    }

    public void triggerOnExhaust() {
        if (this.isMainBody()) {
            Phoenix part = new Phoenix(1);
            Phoenix body = new Phoenix(this.misc - 1);
            if (upgraded) {
                part.upgrade();
                body.upgrade();
            }
            this.addToBot(new MakeTempCardInHandAction(part));
            AbstractDungeon.player.discardPile.addToBottom(body);
        }
        super.triggerOnExhaust();
    }

    public boolean isMainBody() {
        return this.misc > 1;
    }

    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            if (isMainBody()) this.cardsToPreview.upgrade();
            this.upgradeDamage(this.misc * 3);
        }
    }

    public AbstractCard makeCopy() {
        return new Phoenix(this.misc);
    }
}
