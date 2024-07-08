package com.github.paopaoyue.wljmod.card;

import com.github.paopaoyue.wljmod.action.UseAvatarAction;
import com.github.paopaoyue.wljmod.patch.AbstractCardEnum;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class Zhaoritian extends AbstractAvatarCard {
    public static final String ID = "Wlj:Zhaoritian";
    private static final CardStrings cardStrings;

    static {
        cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    }

    public Zhaoritian() {
        super(ID, cardStrings.NAME, Util.getImagePath(""), 3, cardStrings.DESCRIPTION, CardType.SKILL,
                AbstractCardEnum.WLJ_COLOR, CardRarity.RARE, CardTarget.SELF,
                new com.github.paopaoyue.wljmod.component.Zhaoritian(), 10);
        this.cardsToPreview = new Dick();
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new UseAvatarAction(this, this.getAvatar(), this.getHp()));
    }


    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.getAvatar().upgrade();
            this.cardsToPreview.upgrade();
            this.rawDescription = cardStrings.UPGRADE_DESCRIPTION;
            this.initializeDescription();
        }
    }

    public AbstractCard makeCopy() {
        return new Zhaoritian();
    }
}