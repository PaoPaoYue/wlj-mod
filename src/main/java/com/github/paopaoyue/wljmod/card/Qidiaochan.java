package com.github.paopaoyue.wljmod.card;

import com.github.paopaoyue.wljmod.WljMod;
import com.github.paopaoyue.wljmod.action.UseAvatarAction;
import com.github.paopaoyue.wljmod.component.AbstractAvatar;
import com.github.paopaoyue.wljmod.patch.AbstractCardEnum;
import com.github.paopaoyue.wljmod.patch.CardTagEnum;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class Qidiaochan extends AbstractAvatarCard {
    public static final String ID = "Wlj:Qidiaochan";
    private static final CardStrings cardStrings;

    static {
        cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    }

    public Qidiaochan() {
        super(ID, cardStrings.NAME, Util.getImagePath(ID), 1, cardStrings.DESCRIPTION, CardType.SKILL,
                AbstractCardEnum.WLJ_COLOR, CardRarity.UNCOMMON, CardTarget.SELF, null, 3);
        this.tags.add(CardTagEnum.TAIWU);
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        this.avatar = WljMod.avatarManager.getCurrentAvatar();
        if (this.avatar == AbstractAvatar.NONE) {
            this.avatar = WljMod.avatarManager.getPreviousAvatar();
        }
        if (this.avatar == AbstractAvatar.NONE) {
            this.avatar = new com.github.paopaoyue.wljmod.component.Qidiaochan();
        }
        this.addToBot(new UseAvatarAction(this, getAvatar(), getHp()));
    }

    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeHp(2);
        }
    }

    public void applyPowers() {
        super.applyPowers();
        this.avatar = WljMod.avatarManager.getCurrentAvatar();
        if (this.avatar == AbstractAvatar.NONE) {
            this.avatar = WljMod.avatarManager.getPreviousAvatar();
        }
        if (this.avatar != AbstractAvatar.NONE) {
            this.rawDescription = cardStrings.DESCRIPTION;
            this.rawDescription = this.rawDescription + cardStrings.EXTENDED_DESCRIPTION[0] + avatar.getName() + cardStrings.EXTENDED_DESCRIPTION[1];
            this.initializeDescription();
        }
    }

    public void onMoveToDiscard() {
        this.rawDescription = cardStrings.DESCRIPTION;
        this.initializeDescription();
    }

    public AbstractCard makeCopy() {
        return new Qidiaochan();
    }
}
