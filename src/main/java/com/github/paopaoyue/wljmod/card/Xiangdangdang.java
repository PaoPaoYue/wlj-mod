package com.github.paopaoyue.wljmod.card;

import com.github.paopaoyue.wljmod.action.UseAvatarAction;
import com.github.paopaoyue.wljmod.patch.AbstractCardEnum;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class Xiangdangdang extends AbstractAvatarCard {
    public static final String ID = "Wlj:Xiangdangdang";
    private static final CardStrings cardStrings;

    static {
        cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    }

    public Xiangdangdang() {
        super(ID, cardStrings.NAME, Util.getImagePath(""), 2, cardStrings.DESCRIPTION, CardType.SKILL,
                AbstractCardEnum.WLJ_COLOR, CardRarity.RARE, CardTarget.SELF,
                new com.github.paopaoyue.wljmod.component.Xiangdangdang(), 2);
        this.cardsToPreview = new Love();
        this.baseMagicNumber = 1;
        this.magicNumber = this.baseMagicNumber;
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new UseAvatarAction(this, this.getAvatar(), this.getHp()));
    }


    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeMagicNumber(1);
            this.getAvatar().upgrade();
        }
    }

    public AbstractCard makeCopy() {
        return new Xiangdangdang();
    }
}