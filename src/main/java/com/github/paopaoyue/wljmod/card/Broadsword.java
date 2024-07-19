package com.github.paopaoyue.wljmod.card;

import basemod.abstracts.CustomCard;
import com.github.paopaoyue.wljmod.action.DrawCardByWorkerAction;
import com.github.paopaoyue.wljmod.patch.AbstractCardEnum;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.watcher.ExpungeVFXAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class Broadsword extends CustomCard {
    public static final String ID = "Wlj:Broadsword";
    private static final CardStrings cardStrings;

    static {
        cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    }

    public Broadsword() {
        super(ID, cardStrings.NAME, Util.getImagePath(ID), 1, cardStrings.DESCRIPTION, CardType.ATTACK,
                AbstractCardEnum.WLJ_COLOR, CardRarity.COMMON, CardTarget.ENEMY);
        this.baseDamage = 7;
        this.baseMagicNumber = 1;
        this.magicNumber = this.baseMagicNumber;
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new ExpungeVFXAction(m));
        this.addToBot(new DamageAction(m, new DamageInfo(p, damage, this.damageTypeForTurn), AbstractGameAction.AttackEffect.NONE));
        this.addToBot(new DrawCardByWorkerAction(p, this.magicNumber));
    }

    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeDamage(4);
        }
    }

    public AbstractCard makeCopy() {
        return new Broadsword();
    }
}
