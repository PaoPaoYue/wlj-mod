package com.github.paopaoyue.wljmod.card;

import com.github.paopaoyue.wljmod.patch.AbstractCardEnum;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.PoisonPower;
import com.megacrit.cardcrawl.vfx.combat.ThrowDaggerEffect;

public class PoisonedArrow extends AbstractWljCard {
    public static final String ID = "Wlj:Poisoned Arrow";
    private static final CardStrings cardStrings;

    static {
        cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    }

    public PoisonedArrow() {
        super(ID, cardStrings.NAME, Util.getImagePath(ID), 1, cardStrings.DESCRIPTION, CardType.ATTACK,
                AbstractCardEnum.WLJ_COLOR, CardRarity.UNCOMMON, CardTarget.ENEMY);
        this.baseDamage = 4;
        this.baseMagicNumber = 3;
        this.magicNumber = this.baseMagicNumber;
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new VFXAction(new ThrowDaggerEffect(m.hb.cX, m.hb.cY)));
        this.addToBot(new DamageAction(m, new DamageInfo(p, this.damage, this.damageTypeForTurn), AbstractGameAction.AttackEffect.POISON));
        this.addToBot(new AbstractGameAction() {
            @Override
            public void update() {
                int amount = 0;
                if (!m.isDeadOrEscaped())
                    amount = m.powers.stream()
                            .filter(power -> power.type == AbstractPower.PowerType.DEBUFF && !power.ID.equals(PoisonPower.POWER_ID))
                            .mapToInt(power -> Math.abs(power.amount) * magicNumber).sum();
                if (amount > 0) {
                    this.addToTop(new ApplyPowerAction(m, p, new PoisonPower(m, p, amount), amount));
                }
                this.isDone = true;
            }
        });

    }

    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeDamage(1);
            this.upgradeMagicNumber(2);
        }
    }

    public AbstractCard makeCopy() {
        return new PoisonedArrow();
    }
}
