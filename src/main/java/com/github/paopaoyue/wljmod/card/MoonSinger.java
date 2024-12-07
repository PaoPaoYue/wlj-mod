package com.github.paopaoyue.wljmod.card;

import com.github.paopaoyue.wljmod.action.ForcedWaitAction;
import com.github.paopaoyue.wljmod.patch.AbstractCardEnum;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.ArtifactPower;
import com.megacrit.cardcrawl.powers.GainStrengthPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.vfx.combat.ShockWaveEffect;

public class MoonSinger extends AbstractWljCard {
    public static final String ID = "Wlj:Moon Singer";
    private static final CardStrings cardStrings;

    static {
        cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    }

    public MoonSinger() {
        super(ID, cardStrings.NAME, Util.getImagePath(ID), 1, cardStrings.DESCRIPTION, CardType.SKILL,
                AbstractCardEnum.WLJ_COLOR, CardRarity.RARE, CardTarget.ALL_ENEMY);
        this.baseMagicNumber = 4;
        this.magicNumber = this.baseMagicNumber;
        this.cardsToPreview = new Rabble();
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new SFXAction("Wlj:MOON_SINGER"));
        this.addToBot(new ForcedWaitAction(0.5F));
        if (Settings.FAST_MODE) {
            this.addToBot(new VFXAction(p, new ShockWaveEffect(p.hb.cX, p.hb.cY, Settings.GREEN_TEXT_COLOR, ShockWaveEffect.ShockWaveType.CHAOTIC), 0.3f));
        } else {
            this.addToBot(new VFXAction(p, new ShockWaveEffect(p.hb.cX, p.hb.cY, Settings.GREEN_TEXT_COLOR, ShockWaveEffect.ShockWaveType.CHAOTIC), 1.5f));
        }
        int amount = (int) (AbstractDungeon.player.hand.group.stream().filter(c -> c instanceof Rabble).count() * this.magicNumber);
        for (final AbstractMonster mo : AbstractDungeon.getCurrRoom().monsters.monsters) {
            this.addToBot(new ApplyPowerAction(mo, p, new StrengthPower(mo, -amount), -amount));
            if (mo != null && !mo.hasPower(ArtifactPower.POWER_ID)) {
                this.addToBot(new ApplyPowerAction(mo, p, new GainStrengthPower(mo, amount), amount));
            }
        }
    }

    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeMagicNumber(2);
        }
    }

    public AbstractCard makeCopy() {
        return new MoonSinger();
    }
}
