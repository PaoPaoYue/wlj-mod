package com.github.paopaoyue.wljmod.card;

import com.github.paopaoyue.wljmod.action.ExhaustCardGroupAction;
import com.github.paopaoyue.wljmod.action.RescueAction;
import com.github.paopaoyue.wljmod.patch.AbstractCardEnum;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class Rescue extends AbstractWljCard {
    public static final String ID = "Wlj:Rescue";
    private static final CardStrings cardStrings;

    static {
        cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    }

    public Rescue() {
        super(ID, cardStrings.NAME, Util.getImagePath(ID), 1, cardStrings.DESCRIPTION, CardType.ATTACK,
                AbstractCardEnum.WLJ_COLOR, CardRarity.UNCOMMON, CardTarget.ALL_ENEMY);
        this.baseDamage = 6;
        this.isMultiDamage = true;
        this.cardsToPreview = new Rabble();
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new ExhaustCardGroupAction(p.drawPile, c -> c instanceof Rabble, cards -> {
            for (AbstractCard card : cards) {
                this.addToTop(new RescueAction(p, this.damage, this.damageTypeForTurn, AbstractGameAction.AttackEffect.NONE));
            }
        }));
    }

    public void applyPowers() {
        super.applyPowers();
        this.rawDescription = cardStrings.DESCRIPTION + cardStrings.EXTENDED_DESCRIPTION[0] +
                AbstractDungeon.player.drawPile.group.stream().filter(c -> c instanceof Rabble).count() +
                cardStrings.EXTENDED_DESCRIPTION[1];
        this.initializeDescription();
    }

    public void onMoveToDiscard() {
        this.rawDescription = cardStrings.DESCRIPTION;
        this.initializeDescription();
    }

    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeDamage(2);
        }
    }

    public AbstractCard makeCopy() {
        return new Rescue();
    }
}
