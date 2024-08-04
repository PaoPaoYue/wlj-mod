package com.github.paopaoyue.wljmod.card;

import com.github.paopaoyue.wljmod.action.UseAvatarAction;
import com.github.paopaoyue.wljmod.patch.AbstractCardEnum;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class Afei extends AbstractAvatarCard {
    public static final String ID = "Wlj:Afei";
    private static final CardStrings cardStrings;

    static {
        cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    }

    public Afei() {
        super(ID, cardStrings.NAME, Util.getImagePath(ID), 1, cardStrings.DESCRIPTION, CardType.ATTACK,
                AbstractCardEnum.WLJ_COLOR, CardRarity.RARE, CardTarget.ENEMY,
                new com.github.paopaoyue.wljmod.component.Afei(), 3);
        this.baseDamage = 3;
        this.misc = this.baseDamage;
        this.baseMagicNumber = 2;
        this.magicNumber = this.baseMagicNumber;
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        com.github.paopaoyue.wljmod.component.Afei afei = (com.github.paopaoyue.wljmod.component.Afei) getAvatar();
        afei.setTarget(m);
        afei.setCard(this);
        this.addToBot(new UseAvatarAction(this, this.getAvatar(), this.getHp()));
    }


    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeHp(2);
            this.upgradeMagicNumber(1);
            this.upgradeAvatar();
        }
    }

    public AbstractCard makeCopy() {
        return new Afei();
    }
}