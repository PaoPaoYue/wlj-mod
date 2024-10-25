package com.github.paopaoyue.wljmod.effect;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Interpolation;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;

public class FrogDefendEffect extends AbstractGameEffect {
    private static Texture img = ImageMaster.loadImage("image/icon/shield.png");
    private float x;
    private float y;
    private float sY;
    private float tY;
    private static final float DURATION = 0.6F;
    private boolean triggered;

    public FrogDefendEffect() {
        this.triggered = false;
        this.duration = 0.6F;
        this.startingDuration = 0.6F;
    }


    public void update() {
        if (this.duration == DURATION) {
            this.x = AbstractDungeon.player.hb.cX - (float)img.getWidth() / 2.0F;
            float y = AbstractDungeon.player.hb.cY - (float)img.getHeight() / 2.0F;

            this.color = Color.WHITE.cpy();
            this.scale = Settings.scale;
            this.y = y + 80.0F * Settings.scale;
            this.sY = this.y;
            this.tY = y;

            CardCrawlGame.sound.play("BLOCK_GAIN_1");
        }
        this.duration -= Gdx.graphics.getDeltaTime();
        if (this.duration < 0.0F) {
            this.isDone = true;
            this.color.a = 0.0F;
        } else if (this.duration < 0.2F) {
            this.color.a = this.duration * 5.0F;
        } else {
            this.color.a = Interpolation.fade.apply(1.0F, 0.0F, this.duration * 0.75F / 0.6F);
        }

        this.y = Interpolation.exp10In.apply(this.tY, this.sY, this.duration / 0.6F);
        if (this.duration < 0.4F && !this.triggered) {
            this.triggered = true;
        }

    }

    public void render(SpriteBatch sb) {
        sb.setColor(this.color);
        sb.draw(img, this.x, this.y);

    }

    public void dispose() {
    }
}