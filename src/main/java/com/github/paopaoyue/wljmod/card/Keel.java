package com.github.paopaoyue.wljmod.card;

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
import com.megacrit.cardcrawl.vfx.combat.VerticalImpactEffect;

public class Keel extends AbstractWorkerCard {
    public static final String ID = "Wlj:Keel";
    private static final CardStrings cardStrings;

    private static final int DEFAULT_DAMAGE = 3;

    static {
        cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    }

    public Keel() {
        super(ID, cardStrings.NAME, Util.getImagePath(ID), 1, cardStrings.DESCRIPTION, CardType.ATTACK,
                AbstractCardEnum.WLJ_COLOR, CardRarity.RARE, CardTarget.ENEMY);
        this.baseDamage = DEFAULT_DAMAGE;
        this.misc = 0;
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        if (m != null) {
            this.addToBot(new VFXAction(new VerticalImpactEffect(m.hb.cX + m.hb.width / 4.0f, m.hb.cY - m.hb.height / 4.0f)));
        }
        this.addToBot(new DamageAction(m, new DamageInfo(p, this.damage, this.damageTypeForTurn), AbstractGameAction.AttackEffect.BLUNT_HEAVY));
    }

    public void triggerOnLayoff() {
        this.misc++;
        if (this.misc >= 8) {
            this.name = cardStrings.EXTENDED_DESCRIPTION[0];
            this.rawDescription = cardStrings.UPGRADE_DESCRIPTION;
            this.textureImg = "image/card/moonlight.png";
            this.initializeTitle();
            this.initializeDescription();
            this.loadCardImage(this.textureImg);
        }
        if (this.misc <= 8) {
            this.baseDamage = (int) ((upgraded ? DEFAULT_DAMAGE + 1 : DEFAULT_DAMAGE) * Math.pow(2, this.misc));
        }
    }

    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.baseDamage = (int) ((DEFAULT_DAMAGE + 1) * Math.pow(2, this.misc));
            this.upgradedDamage = true;
        }
    }

    public AbstractCard makeCopy() {
        return new Keel();
    }
}
