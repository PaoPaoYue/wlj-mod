package com.github.paopaoyue.wljmod.card;

import com.github.paopaoyue.wljmod.action.LapseAction;
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
import com.megacrit.cardcrawl.vfx.WallopEffect;

public class Wand extends AbstractWljCard {
    public static final String ID = "Wlj:Wand";
    private static final CardStrings cardStrings;

    static {
        cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    }

    public Wand() {
        this(0);
    }

    public Wand(int timesUpgraded) {
        super(ID, cardStrings.NAME, Util.getImagePath(ID), 0, cardStrings.DESCRIPTION, CardType.ATTACK,
                AbstractCardEnum.WLJ_COLOR, CardRarity.SPECIAL, CardTarget.ENEMY);
        this.timesUpgraded = timesUpgraded;
        this.upgraded = timesUpgraded > 0;
        this.baseDamage = 4 + 6 * timesUpgraded + timesUpgraded * (timesUpgraded + 1) / 2;
        this.updateCost(timesUpgraded);
        this.configureNameAndImage();
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new DamageAction(m, new DamageInfo(p, damage, this.damageTypeForTurn), AbstractGameAction.AttackEffect.BLUNT_HEAVY));
        this.addToBot(new VFXAction(new WallopEffect(this.damage, m.hb.cX, m.hb.cY)));
        this.addToBot(new LapseAction(this));
    }

    @Override
    public boolean canUpgrade() {
        return true;
    }

    @Override
    public void upgrade() {
        ++this.timesUpgraded;
        this.upgraded = true;
        this.upgradeBaseCost(this.timesUpgraded);
        this.upgradeDamage(6 + this.timesUpgraded);
        this.configureNameAndImage();
    }

    @Override
    protected void upgradeBaseCost(int newBaseCost) {
        int diff = this.costForTurn - this.cost;
        this.cost = newBaseCost;
        if (this.costForTurn >= 0) {
            this.costForTurn = this.cost + diff;
        }

        if (this.costForTurn < 0) {
            this.costForTurn = 0;
        }

        this.upgradedCost = true;
    }


    @Override
    public void triggerOnGlowCheck() {
        if (LapseAction.lastLapseCard != null && !LapseAction.lastLapseCard.name.equals(this.name)) {
            this.glowColor = AbstractCard.GOLD_BORDER_GLOW_COLOR.cpy();
        } else {
            this.glowColor = AbstractCard.BLUE_BORDER_GLOW_COLOR.cpy();
        }
    }

    private void configureNameAndImage() {
        switch (timesUpgraded) {
            case 0:
                this.name = cardStrings.EXTENDED_DESCRIPTION[0];
                this.textureImg = "image/card/wand.png";
                break;
            case 1:
                this.name = cardStrings.EXTENDED_DESCRIPTION[1];
                this.textureImg = "image/card/wand_lightening.png";
                break;
            case 2:
                this.name = cardStrings.EXTENDED_DESCRIPTION[2];
                this.textureImg = "image/card/wand_l.png";
                break;
            case 3:
                this.name = cardStrings.EXTENDED_DESCRIPTION[3];
                this.textureImg = "image/card/wand_xl.png";
                break;
            case 4:
                this.name = cardStrings.EXTENDED_DESCRIPTION[4];
                this.textureImg = "image/card/wand_xxl.png";
                break;
            case 5:
                this.name = cardStrings.EXTENDED_DESCRIPTION[5];
                this.textureImg = "image/card/wand_xxxl.png";
                break;
            default:
                this.name = cardStrings.EXTENDED_DESCRIPTION[5] + "+" + (this.timesUpgraded - 5);
                this.textureImg = "image/card/wand_xxxl.png";
                break;
        }
        this.initializeTitle();
        this.initializeDescription();
        if (this.textureImg != null) {
            this.loadCardImage(this.textureImg);
        }
    }

    public AbstractCard makeCopy() {
        return new Wand();
    }

}
