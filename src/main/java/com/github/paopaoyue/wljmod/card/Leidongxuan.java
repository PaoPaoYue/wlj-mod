package com.github.paopaoyue.wljmod.card;

import com.github.paopaoyue.wljmod.patch.AbstractCardEnum;
import com.github.paopaoyue.wljmod.patch.CardTagEnum;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class Leidongxuan extends AbstractWorkerCard {
    public static final String ID = "Wlj:Leidongxuan";
    private static final CardStrings cardStrings;

    private static final int DEFAULT_BLOCK = 6;
    private static final int UPGRADE_BLOCK = 4;

    static {
        cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    }

    public Leidongxuan() {
        super(ID, cardStrings.NAME, Util.getImagePath(ID), 2, cardStrings.DESCRIPTION, CardType.SKILL,
                AbstractCardEnum.WLJ_COLOR, CardRarity.UNCOMMON, CardTarget.SELF);
        this.baseBlock = DEFAULT_BLOCK;
        this.baseMagicNumber = 1;
        this.magicNumber = this.baseMagicNumber;
        this.tags.add(CardTagEnum.TAIWU);
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new GainBlockAction(p, this.block));
    }

    public void onBlockDecrement(int blockedDamage) {
        this.baseBlock += blockedDamage * this.magicNumber;
        this.applyPowers();
    }

    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeBlock(UPGRADE_BLOCK);
        }
    }

    public AbstractCard makeCopy() {
        return new Leidongxuan();
    }

    public AbstractCard makeStatEquivalentCopy() {
        AbstractCard card = super.makeStatEquivalentCopy();
        card.baseBlock = (DEFAULT_BLOCK + (upgraded ? UPGRADE_BLOCK : 0));
        return card;
    }

}
