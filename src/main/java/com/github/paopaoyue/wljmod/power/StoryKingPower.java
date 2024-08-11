package com.github.paopaoyue.wljmod.power;

import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.DamageRandomEnemyAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.vfx.combat.GrandFinalEffect;

public class StoryKingPower extends AbstractPower {
    public static final String POWER_ID = "Wlj:Story King";
    private static final PowerStrings strings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    private static final Texture IMG = ImageMaster.loadImage("image/icon/story_king.png");

    private int count = 0;

    public StoryKingPower(AbstractCreature owner, int amount) {
        this.name = strings.NAME;
        this.ID = POWER_ID;
        this.owner = owner;

        this.img = IMG;
        this.type = PowerType.BUFF;
        this.amount = amount;
        this.canGoNegative = false;
        updateDescription();
    }

    @Override
    public void atEndOfTurn(boolean isPlayer) {
        if (isPlayer && this.owner.isPlayer) {
            this.flash();
            if (Settings.FAST_MODE) {
                this.addToBot(new VFXAction(new GrandFinalEffect(), 0.7f));
            } else {
                this.addToBot(new VFXAction(new GrandFinalEffect(), 1.0f));
            }
            for (int i = 0; i < this.count; i++) {

                AbstractGameAction.AttackEffect effect;
                if (i % 2 == 0) {
                    effect = AbstractGameAction.AttackEffect.SLASH_HORIZONTAL;
                } else {
                    effect = AbstractGameAction.AttackEffect.SLASH_VERTICAL;
                }

                this.addToBot(new DamageRandomEnemyAction(new DamageInfo(this.owner, this.amount, DamageInfo.DamageType.THORNS), effect));
            }
        }
        this.count = 0;
        this.updateDescription();
    }

    @Override
    public void onAfterCardPlayed(AbstractCard usedCard) {
        this.count += 1;
        this.updateDescription();
    }

    @Override
    public void onExhaust(AbstractCard card) {
        this.count += 1;
        this.updateDescription();
    }

    public void updateDescription() {
        this.description = strings.DESCRIPTIONS[0] +
                this.amount +
                strings.DESCRIPTIONS[1] +
                strings.DESCRIPTIONS[2] +
                this.amount +
                strings.DESCRIPTIONS[3] +
                this.count +
                strings.DESCRIPTIONS[4];
    }

}
