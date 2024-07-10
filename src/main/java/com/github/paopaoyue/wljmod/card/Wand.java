package com.github.paopaoyue.wljmod.card;

import basemod.abstracts.CustomCard;
import com.github.paopaoyue.wljmod.action.LapseAction;
import com.github.paopaoyue.wljmod.patch.AbstractCardEnum;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.WallopEffect;

public class Wand extends CustomCard {
    public static final String ID = "Wlj:Wand";
    private static final CardStrings cardStrings;

    static {
        cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    }

    public Wand() {
        this(0);
    }

    public Wand(int timesUpgraded) {
        super(ID, cardStrings.NAME, Util.getImagePath(""), 0, cardStrings.DESCRIPTION, CardType.ATTACK,
                AbstractCardEnum.WLJ_COLOR, CardRarity.SPECIAL, CardTarget.ENEMY);
        this.timesUpgraded = timesUpgraded;
        this.upgraded = timesUpgraded > 0;
        this.baseDamage = 6 * (1 + timesUpgraded) + timesUpgraded * (timesUpgraded + 1) / 2;
        this.updateCost(timesUpgraded);
        this.configureNameAndImage();
    }

    public void triggerOnCardPlayed(final AbstractCard c) {
        if (c instanceof AbstractWorkerCard) {
            this.setCostForTurn(-1);
        }
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
    public void triggerOnGlowCheck() {
        if (LapseAction.lastLapseCard != null && !LapseAction.lastLapseCard.name.equals(this.name)) {
            this.glowColor = AbstractCard.GOLD_BORDER_GLOW_COLOR.cpy();
        }
    }

    private void configureNameAndImage() {
        switch (timesUpgraded) {
            case 0:
                this.name = cardStrings.EXTENDED_DESCRIPTION[0];
                break;
            case 1:
                this.name = cardStrings.EXTENDED_DESCRIPTION[1];
                break;
            case 2:
                this.name = cardStrings.EXTENDED_DESCRIPTION[2];
                break;
            case 3:
                this.name = cardStrings.EXTENDED_DESCRIPTION[3];
                break;
            case 4:
                this.name = cardStrings.EXTENDED_DESCRIPTION[4];
                break;
            default:
                this.name = cardStrings.EXTENDED_DESCRIPTION[4] + "+" + (this.timesUpgraded - 4);
                break;
        }
        this.initializeTitle();
        this.initializeDescription();
        // TODO: add img for different wands
        this.textureImg = Util.getImagePath("");
        if (this.textureImg != null) {
            this.loadCardImage(this.textureImg);
        }
    }

    public AbstractCard makeCopy() {
        return new Wand(this.timesUpgraded);
    }
}
