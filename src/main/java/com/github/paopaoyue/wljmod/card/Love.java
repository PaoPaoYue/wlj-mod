package com.github.paopaoyue.wljmod.card;

import com.github.paopaoyue.wljmod.patch.AbstractCardEnum;
import com.github.paopaoyue.wljmod.patch.CardTagEnum;
import com.github.paopaoyue.wljmod.power.LovePower;
import com.github.paopaoyue.wljmod.sfx.SfxUtil;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class Love extends AbstractWljCard {
    public static final String ID = "Wlj:Love";
    private static final CardStrings cardStrings;

    static {
        cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    }

    public static SfxUtil sfxUtil = SfxUtil.createInstance(new String[]{"Wlj:LOVE_1", "Wlj:LOVE_2", "Wlj:LOVE_3", "Wlj:LOVE_4"}, true, 1.0f, 0.0f, 1.0f);

    public Love() {
        super(ID, cardStrings.NAME, Util.getImagePath(ID), 1, cardStrings.DESCRIPTION, CardType.SKILL,
                AbstractCardEnum.WLJ_COLOR, CardRarity.UNCOMMON, CardTarget.ENEMY);
        this.baseMagicNumber = 2;
        this.magicNumber = this.baseMagicNumber;
        this.cardsToPreview = new Invite();
        this.exhaust = true;
        this.tags.add(CardTagEnum.TAIWU);
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        sfxUtil.playSFX(1.6f);
        this.addToBot(new ApplyPowerAction(m, p, new LovePower(m, this.magicNumber), this.magicNumber));
        this.addToBot(new MakeTempCardInHandAction(this.cardsToPreview));
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
        return new Love();
    }
}
