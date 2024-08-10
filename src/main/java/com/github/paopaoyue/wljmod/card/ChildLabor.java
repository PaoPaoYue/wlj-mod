package com.github.paopaoyue.wljmod.card;

import com.github.paopaoyue.wljmod.action.PurchaseAction;
import com.github.paopaoyue.wljmod.effect.GoldTextOnPlayerEffect;
import com.github.paopaoyue.wljmod.patch.AbstractCardEnum;
import com.github.paopaoyue.wljmod.patch.CardTagEnum;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class ChildLabor extends AbstractWljCard {
    public static final String ID = "Wlj:Child Labor";
    private static final CardStrings cardStrings;

    private static final int DRAW_AMOUNT = 3;

    static {
        cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    }

    public ChildLabor() {
        super(ID, cardStrings.NAME, Util.getImagePath(ID), 1, cardStrings.DESCRIPTION, CardType.SKILL,
                AbstractCardEnum.WLJ_COLOR, CardRarity.COMMON, CardTarget.SELF);
        this.baseMagicNumber = 2;
        this.magicNumber = this.baseMagicNumber;
        this.cardsToPreview = new Rabble();
        this.tags.add(CardTagEnum.PAY);
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new DrawCardAction(DRAW_AMOUNT, new AbstractGameAction() {
            @Override
            public void update() {
                int gold = 0;
                for (AbstractCard c : DrawCardAction.drawnCards) {
                    if (!(c instanceof Rabble)) {
                        gold += magicNumber;
                    }
                }
                if (gold > 0) {
                    PurchaseAction.sfxUtil.playSFX();
                    AbstractDungeon.effectList.add(new GoldTextOnPlayerEffect(-gold, true));
                    AbstractDungeon.player.loseGold(gold);
                }
                isDone = true;
            }
        }));
    }

    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeMagicNumber(-1);
        }
    }

    public AbstractCard makeCopy() {
        return new ChildLabor();
    }
}
