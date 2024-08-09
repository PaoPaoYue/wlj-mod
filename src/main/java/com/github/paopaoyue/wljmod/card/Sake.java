package com.github.paopaoyue.wljmod.card;

import com.github.paopaoyue.wljmod.patch.AbstractCardEnum;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import java.util.Arrays;
import java.util.List;

public class Sake extends AbstractWljCard {
    public static final String ID = "Wlj:Sake";
    public static final byte MOCKED_MOVE = (byte) 233; // Mock the move to make the monster do nothing for a turn
    private static final CardStrings cardStrings;
    private static final List<AbstractMonster.Intent> allowedIntents = Arrays.asList(
            AbstractMonster.Intent.ATTACK,
            AbstractMonster.Intent.ATTACK_BUFF,
            AbstractMonster.Intent.ATTACK_DEBUFF,
            AbstractMonster.Intent.ATTACK_DEFEND,
            AbstractMonster.Intent.BUFF,
            AbstractMonster.Intent.DEBUFF,
            AbstractMonster.Intent.DEFEND,
            AbstractMonster.Intent.DEFEND_DEBUFF,
            AbstractMonster.Intent.DEFEND_BUFF,
            AbstractMonster.Intent.ESCAPE
    );

    static {
        cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    }

    public Sake() {
        super(ID, cardStrings.NAME, Util.getImagePath(ID), 2, cardStrings.DESCRIPTION, CardType.SKILL,
                AbstractCardEnum.WLJ_COLOR, CardRarity.UNCOMMON, CardTarget.ENEMY);
        this.baseMagicNumber = 8;
        this.magicNumber = this.baseMagicNumber;
        this.exhaust = true;
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        if (m != null && m.intent != null && allowedIntents.contains(m.intent)) {
            m.setMove(MOCKED_MOVE, AbstractMonster.Intent.ATTACK, this.magicNumber);
            m.createIntent();
        }
    }

    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeMagicNumber(-4);
        }
    }

    public AbstractCard makeCopy() {
        return new Sake();
    }
}
