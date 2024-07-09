package com.github.paopaoyue.wljmod.effect;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;

public class RollDiceEffect extends AbstractGameEffect {

    private static final Texture[] DICE_IMG = new Texture[]{
            ImageMaster.loadImage("image/vfx/dice/1.png"),
            ImageMaster.loadImage("image/vfx/dice/2.png"),
            ImageMaster.loadImage("image/vfx/dice/3.png"),
            ImageMaster.loadImage("image/vfx/dice/4.png"),
            ImageMaster.loadImage("image/vfx/dice/5.png"),
            ImageMaster.loadImage("image/vfx/dice/6.png"),
    };
    private static final float DURATION = 0.8F;
    private static final float INITIAL_ANGULAR_VELOCITY = 720F;
    private static final float ANGULAR_VELOCITY_LOSS = 900F;

    private Texture img;
    private float x;
    private float y;
    private float sx;
    private float sy;
    private float tx;
    private float ty;
    private float rotationSpeed;

    private boolean soundPlayed;

    public RollDiceEffect(float x, float y, int dice) {
        if (dice <= 0 || dice > 6) {
            throw new RuntimeException("dice value invalid");
        }

        this.duration = 1.0F;
        this.startingDuration = 0.6F;
        this.img = DICE_IMG[dice - 1];
        this.color = Color.WHITE.cpy();
        this.scale = Settings.scale;
        this.x = x;
        this.y = y;
        this.rotation = -90.0F;
        float distX = MathUtils.random(50.0F, 100.0F) * scale;
        float distY = MathUtils.random(150.0F, 200.0F) * scale;
        this.sx = x - distX;
        this.sy = y + distY;
        this.tx = x;
        this.ty = y;
        this.rotationSpeed = INITIAL_ANGULAR_VELOCITY;
    }


    public void update() {
        this.duration -= Gdx.graphics.getDeltaTime();
        if (this.duration < 0.8F && !soundPlayed) {
            soundPlayed = true;
            CardCrawlGame.sound.play("Wlj:ROLL_DICE");
        }

        if (this.duration > 0.2F) {
            this.x = Interpolation.pow3Out.apply(this.sx, this.tx, 1 - (this.duration - 0.2F) / 0.8F);
            this.y = Interpolation.bounceOut.apply(this.sy, this.ty, 1 - (this.duration - 0.2F) / 0.8F);
            this.rotation -= this.rotationSpeed * Gdx.graphics.getDeltaTime();
            this.rotationSpeed -= ANGULAR_VELOCITY_LOSS * Gdx.graphics.getDeltaTime();
        }

        if (this.duration > 0.8F) {
            this.color.a = Interpolation.fade.apply(0.0F, 1.0F, 1 - (this.duration - 0.8F) / 0.2F);
        } else if (this.duration < 0.1F) {
            this.color.a = Interpolation.fade.apply(1.0F, 0.0F, 1 - this.duration / 0.1F);
        } else {
            this.color.a = 1;
        }


        if (this.duration < 0.0F) {
            this.isDone = true;
            this.color.a = 0.0F;
        }

    }

    public void render(SpriteBatch sb) {
        if (this.img != null) {
            sb.setColor(this.color);
            sb.draw(this.img, this.x - (float) this.img.getWidth() / 4, this.y - (float) this.img.getHeight() / 4, (float) this.img.getWidth() / 4, (float) this.img.getHeight() / 4, (float) this.img.getWidth() / 2, (float) this.img.getHeight() / 2, this.scale, this.scale, this.rotation, 0, 0, this.img.getWidth(), this.img.getHeight(), false, false);
        }

    }

    public void dispose() {
    }
}
