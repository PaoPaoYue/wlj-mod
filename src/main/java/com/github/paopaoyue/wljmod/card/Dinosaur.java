package com.github.paopaoyue.wljmod.card;

import basemod.abstracts.CustomCard;
import com.badlogic.gdx.graphics.Color;
import com.github.paopaoyue.wljmod.WljMod;
import com.github.paopaoyue.wljmod.action.ExhaustDiscardedWorkerAction;
import com.github.paopaoyue.wljmod.patch.AbstractCardEnum;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.BiteEffect;

public class Dinosaur extends CustomCard {
    public static final String ID = "Wlj:Dinosaur";
    private static final CardStrings cardStrings;

    static {
        cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    }

    public Dinosaur() {
        super(ID, cardStrings.NAME, Util.getImagePath(""), 2, cardStrings.DESCRIPTION, CardType.ATTACK,
                AbstractCardEnum.WLJ_COLOR, CardRarity.UNCOMMON, CardTarget.ENEMY);
        this.baseDamage = 26;
        this.baseMagicNumber = 2;
        this.magicNumber = this.baseMagicNumber;
        this.cardsToPreview = new Prisoner();
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new ExhaustDiscardedWorkerAction(this.magicNumber));
        this.addToBot(new VFXAction(new BiteEffect(m.hb.cX, m.hb.cY, Color.CHARTREUSE.cpy()), 0.2f));
        this.addToBot(new DamageAction(m, new DamageInfo(p, damage, this.damageTypeForTurn), AbstractGameAction.AttackEffect.NONE));
    }

    @Override
    public boolean cardPlayable(AbstractMonster m) {
        if (WljMod.workerManager.getWorkerCountInDiscardPile() < this.magicNumber) {
            return false;
        }
        return super.cardPlayable(m);
    }

    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeMagicNumber(-1);
        }
    }

    public AbstractCard makeCopy() {
        return new Dinosaur();
    }
}
