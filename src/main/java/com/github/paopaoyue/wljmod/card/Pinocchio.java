package com.github.paopaoyue.wljmod.card;

import basemod.patches.com.megacrit.cardcrawl.dungeons.AbstractDungeon.NoPools;
import com.badlogic.gdx.math.MathUtils;
import com.github.paopaoyue.wljmod.patch.AbstractCardEnum;
import com.megacrit.cardcrawl.actions.animations.TalkAction;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

@NoPools
public class Pinocchio extends AbstractWljCard {
    public static final String ID = "Wlj:Pinocchio";
    private static final CardStrings cardStrings;
    private static final UIStrings uiStrings;

    public static final int MAX_LEVEL = 12;

    static {
        cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
        uiStrings = CardCrawlGame.languagePack.getUIString(ID);
    }

    public Pinocchio() {
        super(ID, cardStrings.NAME, Util.getImagePath(ID), 1, cardStrings.DESCRIPTION, CardType.SKILL,
                AbstractCardEnum.WLJ_COLOR, CardRarity.UNCOMMON, CardTarget.SELF);
        this.baseMagicNumber = 4;
        this.magicNumber = this.baseMagicNumber;
        this.misc = this.baseMagicNumber;
        this.exhaust = true;
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new HealAction(p, p, this.magicNumber));
        if (p.hand.size() >= this.magicNumber) {
            this.misc = Math.min(this.misc + 1, MAX_LEVEL);
            this.lie();
            for (AbstractCard card: p.masterDeck.group) {
                if (card instanceof Pinocchio && card.uuid.equals(this.uuid)) {
                    card.misc = Math.min(card.misc + 1, MAX_LEVEL);
                    ((Pinocchio) card).lie();
                }
            }
            for (AbstractMonster monster: AbstractDungeon.getCurrRoom().monsters.monsters) {
                if (!monster.isDeadOrEscaped()) {
                    this.addToBot(new TalkAction(monster, uiStrings.TEXT[MathUtils.random(0, uiStrings.TEXT.length - 1)], 0.1F, 2.0F));
                }
            }
        }
    }

    public void lie() {
        if (this.misc >= MAX_LEVEL) {
            this.misc = MAX_LEVEL;
        }
        this.baseMagicNumber = this.misc;
        this.magicNumber = this.baseMagicNumber;
        this.textureImg = String.format("image/card/%s_%d.png", "pinocchio", this.misc);
        this.loadCardImage(this.textureImg);
        this.initializeDescription();
    }

    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.selfRetain = true;
            this.rawDescription = cardStrings.UPGRADE_DESCRIPTION;
            this.initializeDescription();
        }
    }

    public AbstractCard makeCopy() {
        return new Pinocchio();
    }

    @Override
    public AbstractCard makeStatEquivalentCopy() {
        Pinocchio card = (Pinocchio) super.makeStatEquivalentCopy();
        card.lie();
        return card;
    }
}
