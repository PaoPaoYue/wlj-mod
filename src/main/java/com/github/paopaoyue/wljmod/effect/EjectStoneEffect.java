package com.github.paopaoyue.wljmod.effect;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;

public class EjectStoneEffect extends AbstractGameEffect {
    private float x;
    private float y;
    private float tX;
    private float tY;

    public EjectStoneEffect(final float sX, final float sY, final float tX, final float tY) {
        this.x = sX;
        this.y = sY;
        this.tX = tX;
        this.tY = tY;
        this.scale = 0.12f;
        this.duration = 0.5f;
    }

    @Override
    public void update() {
        this.scale -= Gdx.graphics.getDeltaTime();
        if (this.scale < 0.0f) {
            AbstractDungeon.effectsQueue.add(new StoneParticle(this.x + MathUtils.random(60.0f, -60.0f) * Settings.scale, this.y + MathUtils.random(60.0f, -60.0f) * Settings.scale, this.tX, this.tY, AbstractDungeon.player.flipHorizontal));
            this.scale = 0.04f;
        }
        this.duration -= Gdx.graphics.getDeltaTime();
        if (this.duration < 0.0f) {
            this.isDone = true;
        }
    }

    @Override
    public void render(final SpriteBatch sb) {
    }

    @Override
    public void dispose() {
    }
}