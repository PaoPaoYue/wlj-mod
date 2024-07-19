package com.github.paopaoyue.wljmod.card;

import com.github.paopaoyue.wljmod.patch.AbstractCardEnum;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class Erwen extends AbstractWorkerCard {
    public static final String ID = "Wlj:Erwen";
    private static final CardStrings cardStrings;

    static {
        cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    }

    public Erwen() {
        super(ID, cardStrings.NAME, Util.getImagePath(ID), 1, cardStrings.DESCRIPTION, CardType.SKILL,
                AbstractCardEnum.WLJ_COLOR, CardRarity.COMMON, CardTarget.SELF);
        this.baseBlock = 5;
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new GainBlockAction(AbstractDungeon.player, this.block));
    }

    public void triggerOnExhaust() {
        this.addToBot(new GainBlockAction(AbstractDungeon.player, this.block));
        super.triggerOnExhaust();
    }

    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeBlock(2);
        }
    }

    public AbstractCard makeCopy() {
        return new Erwen();
    }
}
