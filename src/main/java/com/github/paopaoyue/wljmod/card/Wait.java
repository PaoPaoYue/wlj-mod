package com.github.paopaoyue.wljmod.card;

import com.github.paopaoyue.wljmod.action.EldenWaitAction;
import com.github.paopaoyue.wljmod.action.HandToDrawPileBottomAction;
import com.github.paopaoyue.wljmod.patch.AbstractCardEnum;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class Wait extends AbstractWljCard {
    public static final String ID = "Wlj:Wait";
    private static final CardStrings cardStrings;

    static {
        cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    }

    public Wait() {
        super(ID, cardStrings.NAME, Util.getImagePath(ID), -1, cardStrings.DESCRIPTION, CardType.SKILL,
                AbstractCardEnum.WLJ_COLOR, CardRarity.UNCOMMON, CardTarget.SELF);
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new HandToDrawPileBottomAction(true));
        this.addToBot(new EldenWaitAction(this.block, this.freeToPlayOnce, this.energyOnUse));
    }

    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.isInnate = true;
            this.rawDescription = cardStrings.UPGRADE_DESCRIPTION;
            this.initializeDescription();
        }
    }

    public AbstractCard makeCopy() {
        return new Wait();
    }
}
