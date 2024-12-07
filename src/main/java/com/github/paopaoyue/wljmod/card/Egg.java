package com.github.paopaoyue.wljmod.card;

import com.github.paopaoyue.wljmod.action.EggAction;
import com.github.paopaoyue.wljmod.patch.AbstractCardEnum;
import com.github.paopaoyue.wljmod.sfx.SfxUtil;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.ArtifactPower;

public class Egg extends AbstractWljCard {
    public static final String ID = "Wlj:Egg";
    private static final CardStrings cardStrings;

    static {
        cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    }

    public static SfxUtil sfxUtil = SfxUtil.createInstance(new String[]{"Wlj:EGG_1", "Wlj:EGG_2", "Wlj:EGG_3"}, true, 1.0f, 0.0f, 1.0f);

    public Egg() {
        super(ID, cardStrings.NAME, Util.getImagePath(ID), 2, cardStrings.DESCRIPTION, CardType.SKILL,
                AbstractCardEnum.WLJ_COLOR, CardRarity.UNCOMMON, CardTarget.ALL_ENEMY);
        this.baseMagicNumber = 2;
        this.magicNumber = this.baseMagicNumber;
        this.exhaust = true;
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        sfxUtil.playSFX(1.3f);
        this.addToBot(new EggAction(this.magicNumber));
    }

    @Override
    public void triggerOnGlowCheck() {
        boolean glow = false;
        for (AbstractMonster m : AbstractDungeon.getCurrRoom().monsters.monsters) {
            if (!m.isDeadOrEscaped() && m.hasPower(ArtifactPower.POWER_ID)) {
                glow = true;
                break;
            }
        }
        if (glow) {
            this.glowColor = AbstractCard.GOLD_BORDER_GLOW_COLOR.cpy();
        } else {
            this.glowColor = AbstractCard.BLUE_BORDER_GLOW_COLOR.cpy();
        }
    }

    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeMagicNumber(1);
        }
    }

    public AbstractCard makeCopy() {
        return new Egg();
    }
}
