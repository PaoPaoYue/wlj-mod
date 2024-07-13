package com.github.paopaoyue.wljmod.card;

import basemod.abstracts.CustomCard;
import com.github.paopaoyue.wljmod.patch.AbstractCardEnum;
import com.github.paopaoyue.wljmod.patch.CardTagEnum;
import com.github.paopaoyue.wljmod.sfx.SfxUtil;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class Invite extends CustomCard {
    public static final String ID = "Wlj:Invite";
    private static final CardStrings cardStrings;

    static {
        cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    }

    public static SfxUtil sfxUtil = SfxUtil.createInstance(new String[]{"Wlj:INVITE_1", "Wlj:INVITE_2", "Wlj:INVITE_3", "Wlj:INVITE_4", "Wlj:INVITE_5", "Wlj:INVITE_6", "Wlj:INVITE_7"}, true, 1.0f, 0.0f, 1.0f);

    public Invite() {
        super(ID, cardStrings.NAME, Util.getImagePath(ID), 0, cardStrings.DESCRIPTION, CardType.ATTACK,
                AbstractCardEnum.WLJ_COLOR, CardRarity.SPECIAL, CardTarget.ENEMY);
        this.baseDamage = 2;
        this.baseMagicNumber = 1;
        this.magicNumber = this.baseMagicNumber;
        this.exhaust = true;
        this.cardsToPreview = new Rabble();
        this.tags.add(CardTagEnum.TAIWU);
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        sfxUtil.playSFX(1.6f);
        this.addToBot(new DamageAction(m, new DamageInfo(p, this.damage, this.damageTypeForTurn), AbstractGameAction.AttackEffect.BLUNT_LIGHT));
        this.addToBot(new MakeTempCardInHandAction(new Rabble(), this.magicNumber));
    }


    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.exhaust = false;
            this.rawDescription = cardStrings.UPGRADE_DESCRIPTION;
            this.initializeDescription();
        }
    }

    public AbstractCard makeCopy() {
        return new Invite();
    }
}
