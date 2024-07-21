package com.github.paopaoyue.wljmod.effect;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import com.megacrit.cardcrawl.vfx.GainGoldTextEffect;

public class GoldTextOnPlayerEffect extends AbstractGameEffect {

    private int amount;
    private boolean isNegative;
    private boolean muted;
    private boolean sfxPlayed;
    private int lastAmount;

    public GoldTextOnPlayerEffect(int amount) {
        this(amount, false);
    }

    public GoldTextOnPlayerEffect(int amount, boolean muted) {
        this.amount = Math.abs(amount);
        this.isNegative = amount < 0;
        this.muted = muted;
        this.sfxPlayed = false;

        this.duration = 0.7F;
        this.lastAmount = 0;
    }

    public void update() {
        if (this.duration < 0.0F)
            this.isDone = true;
        this.duration -= Gdx.graphics.getDeltaTime();
        int nextAmount = (int) MathUtils.lerp(this.amount, 0, this.duration / 0.6F);
        int delta = nextAmount - this.lastAmount;
        if (delta > 0) {
            this.lastAmount = nextAmount;
            boolean textEffectFound = false;
            for (final AbstractGameEffect e : AbstractDungeon.effectList) {
                if (e instanceof GainGoldTextEffect && ((GainGoldTextEffect) e).ping(isNegative ? -delta : delta)) {
                    textEffectFound = true;
                    break;
                }
            }
            if (!textEffectFound) {
                for (final AbstractGameEffect e : AbstractDungeon.effectsQueue) {
                    if (e instanceof GainGoldTextEffect && ((GainGoldTextEffect) e).ping(isNegative ? -delta : delta)) {
                        textEffectFound = true;
                    }
                }
            }
            if (!textEffectFound) {
                AbstractDungeon.effectsQueue.add(new GainGoldTextEffect(isNegative ? -delta : delta));
                if (!muted && !sfxPlayed) {
                    sfxPlayed = true;
                    CardCrawlGame.sound.play(isNegative ? "SHOP_PURCHASE" : "GOLD_GAIN", 0F);
                }
            }
        }
    }

    public void render(SpriteBatch sb) {
    }

    public void dispose() {
    }
}
