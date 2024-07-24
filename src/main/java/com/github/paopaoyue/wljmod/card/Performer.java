package com.github.paopaoyue.wljmod.card;

import com.github.paopaoyue.wljmod.effect.GoldTextOnPlayerEffect;
import com.github.paopaoyue.wljmod.patch.AbstractCardEnum;
import com.github.paopaoyue.wljmod.relic.Tax;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.GainGoldAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.relics.AbstractRelic;

public class Performer extends AbstractWorkerCard {
    public static final String ID = "Wlj:Performer";
    private static final CardStrings cardStrings;

    static {
        cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    }

    public Performer() {
        super(ID, cardStrings.NAME, Util.getImagePath(ID), 0, cardStrings.DESCRIPTION, CardType.ATTACK,
                AbstractCardEnum.WLJ_COLOR, CardRarity.SPECIAL, CardTarget.ENEMY);
        this.baseDamage = 5;
        this.baseMagicNumber = 1;
        this.magicNumber = this.baseMagicNumber;
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractRelic relic = p.getRelic(Tax.ID);
        if (relic != null) {
            relic.flash();
            this.exhaust = true;
            AbstractDungeon.effectList.add(new GoldTextOnPlayerEffect(Tax.GOLD_GAIN, true));
            this.addToTop(new GainGoldAction(Tax.GOLD_GAIN));
        }
        this.addToBot(new DamageAction(m, new DamageInfo(p, this.damage, this.damageTypeForTurn), AbstractGameAction.AttackEffect.BLUNT_LIGHT));
    }

    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeDamage(2);
        }
    }

    public float atDamageGiveOutsideDiscardPile(float damage, DamageInfo.DamageType type, AbstractCard card) {
        if (type == DamageInfo.DamageType.NORMAL) {
            if (card instanceof Lewis) {
                return damage + this.magicNumber * card.magicNumber;
            }
            return damage + this.magicNumber;
        }
        return damage;
    }

    public AbstractCard makeCopy() {
        return new Performer();
    }
}
