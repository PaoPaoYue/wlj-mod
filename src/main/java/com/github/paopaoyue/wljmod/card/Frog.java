package com.github.paopaoyue.wljmod.card;

import basemod.patches.com.megacrit.cardcrawl.dungeons.AbstractDungeon.NoPools;
import com.github.paopaoyue.wljmod.action.UseAvatarAction;
import com.github.paopaoyue.wljmod.patch.AbstractCardEnum;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

@NoPools
public class Frog extends AbstractAvatarCard {
    public static final String ID = "Wlj:Frog";
    private static final CardStrings cardStrings;

    public static final int NUM_FROGS = 6;

    static {
        cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    }

    public Frog() {
        this(0);
    }

    public Frog(int index) {
        super(ID, cardStrings.NAME, Util.getImagePath(ID), 1, cardStrings.DESCRIPTION, CardType.ATTACK,
                AbstractCardEnum.WLJ_COLOR, CardRarity.UNCOMMON, CardTarget.ENEMY,
                null, 3);
        this.baseDamage = 7;
        this.misc = index;
        this.configFrog();
    }

    public void configFrog() {
        this.setDesc(misc);
        this.setAvatar(misc);
    }

    private void setAvatar(int index) {
        switch (index) {
            case 0:
                this.avatar = new com.github.paopaoyue.wljmod.component.Frog.FrogN();
                break;
            case 1:
                this.avatar = new com.github.paopaoyue.wljmod.component.Frog.FrogY();
                break;
            case 2:
                this.avatar = new com.github.paopaoyue.wljmod.component.Frog.FrogC();
                break;
            case 3:
                this.avatar = new com.github.paopaoyue.wljmod.component.Frog.FrogG();
                break;
            case 4:
                this.avatar = new com.github.paopaoyue.wljmod.component.Frog.FrogR();
                break;
            case 5:
                this.avatar = new com.github.paopaoyue.wljmod.component.Frog.FrogO();
                break;
        }
    }

    private void setDesc(int index) {
        if (index == 0) {
            this.rawDescription = cardStrings.DESCRIPTION + (upgraded ? "" : cardStrings.EXTENDED_DESCRIPTION[0]);
        } else {
            this.rawDescription = cardStrings.DESCRIPTION + cardStrings.EXTENDED_DESCRIPTION[index] + (upgraded ? "" : cardStrings.EXTENDED_DESCRIPTION[0]);
        }
        this.initializeDescription();
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        com.github.paopaoyue.wljmod.component.Frog frog = (com.github.paopaoyue.wljmod.component.Frog) getAvatar();
        frog.setTarget(m);
        frog.setCard(this);
        this.addToBot(new UseAvatarAction(this, this.getAvatar(), this.getHp()));
        if (!upgraded) {
            misc = (misc + 1) % NUM_FROGS;
            configFrog();
            for (AbstractCard c : p.masterDeck.group) {
                if (c.uuid.equals(uuid)) {
                    c.misc = misc;
                    ((Frog)c).configFrog();
                }
            }
        }
    }

    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.setDesc(misc);
        }
    }

    public AbstractCard makeCopy() {
        return new Frog(this.misc);
    }
}