package com.github.paopaoyue.wljmod.card;

import com.github.paopaoyue.wljmod.action.LapseAction;
import com.github.paopaoyue.wljmod.patch.AbstractCardEnum;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.unique.WhirlwindAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class Spear extends AbstractWljCard {
    public static final String ID = "Wlj:Spear";
    public static final int UPDATE_LIMIT = 6;
    private static final CardStrings cardStrings;

    static {
        cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    }

    public Spear() {
        this(0);
    }

    public Spear(int timesUpgraded) {
        super(ID, cardStrings.NAME, Util.getImagePath(ID), 1, cardStrings.DESCRIPTION, CardType.ATTACK,
                AbstractCardEnum.WLJ_COLOR, CardRarity.UNCOMMON, CardTarget.ENEMY);
        if (timesUpgraded < UPDATE_LIMIT) {
            this.baseDamage = 8;
            this.baseMagicNumber = UPDATE_LIMIT;
            this.magicNumber = this.baseMagicNumber;
            this.cardsToPreview = new Spear(UPDATE_LIMIT);
        } else {
            this.name = cardStrings.EXTENDED_DESCRIPTION[0];
            this.rawDescription = cardStrings.UPGRADE_DESCRIPTION;
            this.cost = -1;
            this.baseDamage = 14;
            this.target = CardTarget.ALL_ENEMY;
            this.isMultiDamage = true;
            this.initializeTitle();
            this.initializeDescription();
        }
        this.upgraded = timesUpgraded > 0;
        this.timesUpgraded = timesUpgraded;
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        if (this.timesUpgraded < UPDATE_LIMIT) {
            this.addToBot(new DamageAction(m, new DamageInfo(p, damage, this.damageTypeForTurn), AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
            this.addToBot(new LapseAction(this));
        } else {
            this.addToBot(new WhirlwindAction(p, this.multiDamage, this.damageType, this.freeToPlayOnce, this.energyOnUse));
            this.addToBot(new LapseAction(this));
        }
    }

    @Override
    public boolean canUpgrade() {
        return this.timesUpgraded < UPDATE_LIMIT;
    }

    @Override
    public void triggerOnGlowCheck() {
        if (LapseAction.lastLapseCard != null && !LapseAction.lastLapseCard.name.equals(this.name)) {
            this.glowColor = AbstractCard.GOLD_BORDER_GLOW_COLOR.cpy();
        } else {
            this.glowColor = AbstractCard.BLUE_BORDER_GLOW_COLOR.cpy();
        }
    }

    public void upgrade() {
        if (!canUpgrade()) return;
        ++this.timesUpgraded;
        this.upgraded = true;
        if (this.timesUpgraded < UPDATE_LIMIT) {
            this.upgradeMagicNumber(-1);
            this.name = cardStrings.NAME + "+" + this.timesUpgraded;
        } else {
            this.name = cardStrings.EXTENDED_DESCRIPTION[0];
            this.rawDescription = cardStrings.UPGRADE_DESCRIPTION;
            this.upgradeBaseCost(-1);
            this.upgradeDamage(6);
            this.isMultiDamage = true;
            this.target = CardTarget.ALL_ENEMY;
            this.cardsToPreview = null;
        }
        this.initializeTitle();
        this.initializeDescription();
        if (AbstractDungeon.isPlayerInDungeon() && AbstractDungeon.player != null && AbstractDungeon.player.masterDeck != null) {
            for (final AbstractCard c : AbstractDungeon.player.masterDeck.group) {
                if (c.uuid.equals(this.uuid) && c != this) {
                    c.upgrade();
                }
            }
        }
    }

    public AbstractCard makeCopy() {
        return new Spear();
    }
}
