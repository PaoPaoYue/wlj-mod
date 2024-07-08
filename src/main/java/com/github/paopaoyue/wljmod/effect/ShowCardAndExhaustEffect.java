package com.github.paopaoyue.wljmod.effect;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import com.megacrit.cardcrawl.vfx.ExhaustBlurEffect;
import com.megacrit.cardcrawl.vfx.ExhaustEmberEffect;

public class ShowCardAndExhaustEffect extends AbstractGameEffect {
    private static final float DUR = 1.5f;
    private static final float PADDING = 30.0f * Settings.scale;
    private static final int MAX_CONCURRENT_EFFECTS = 20;
    private final AbstractCard c;
    private boolean exhaustedEffect = false;

    public ShowCardAndExhaustEffect(AbstractCard c) {
        this.c = c;

        int effectCount = 0;
        for (final AbstractGameEffect e : AbstractDungeon.effectList) {
            if (e instanceof ShowCardAndExhaustEffect) {
                ++effectCount;
            }
        }
        if (effectCount >= MAX_CONCURRENT_EFFECTS) {
            this.duration = 0.0f;
            this.isDone = true;
            return;
        }

        this.duration = DUR;
        c.drawScale = 0.01f;
        c.targetDrawScale = 0.75f;
        this.identifySpawnLocation(effectCount, Settings.WIDTH - 96.0f * Settings.scale, Settings.HEIGHT - 32.0f * Settings.scale);
    }

    private void identifySpawnLocation(int effectCount, float x, float y) {
        this.c.target_y = Settings.HEIGHT * 0.5f;
        switch (effectCount) {
            case 0: {
                this.c.target_x = Settings.WIDTH * 0.5f;
                break;
            }
            case 1: {
                this.c.target_x = Settings.WIDTH * 0.5f - PADDING - AbstractCard.IMG_WIDTH * 0.75f;
                break;
            }
            case 2: {
                this.c.target_x = Settings.WIDTH * 0.5f + PADDING + AbstractCard.IMG_WIDTH * 0.75f;
                break;
            }
            case 3: {
                this.c.target_x = Settings.WIDTH * 0.5f - (PADDING + AbstractCard.IMG_WIDTH * 0.75f) * 2.0f;
                break;
            }
            case 4: {
                this.c.target_x = Settings.WIDTH * 0.5f + (PADDING + AbstractCard.IMG_WIDTH * 0.75f) * 2.0f;
                break;
            }
            default: {
                this.c.target_x = MathUtils.random(Settings.WIDTH * 0.1f, Settings.WIDTH * 0.9f);
                this.c.target_y = MathUtils.random(Settings.HEIGHT * 0.2f, Settings.HEIGHT * 0.8f);
                break;
            }
        }
    }

    @Override
    public void update() {
        this.duration -= Gdx.graphics.getDeltaTime();
        if (!this.exhaustedEffect && this.duration < 0.8) {
            CardCrawlGame.sound.play("CARD_EXHAUST", 0.2f);
            for (int i = 0; i < 90; ++i) {
                AbstractDungeon.topLevelEffectsQueue.add(new ExhaustBlurEffect(this.c.target_x, this.c.target_y));
            }
            for (int i = 0; i < 50; ++i) {
                AbstractDungeon.topLevelEffectsQueue.add(new ExhaustEmberEffect(this.c.target_x, this.c.target_y));
            }
            this.exhaustedEffect = true;
        }
        if (!this.c.fadingOut && this.duration < 0.7) {
            this.c.fadingOut = true;
        }
        if (this.duration < 0.0f) {
            this.isDone = true;
            this.c.resetAttributes();
        }
    }

    @Override
    public void render(final SpriteBatch sb) {
        this.c.render(sb);
    }

    @Override
    public void dispose() {
    }
}
