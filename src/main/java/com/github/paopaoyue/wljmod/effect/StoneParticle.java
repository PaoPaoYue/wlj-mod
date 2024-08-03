package com.github.paopaoyue.wljmod.effect;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.CatmullRomSpline;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.ScreenShake;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import com.megacrit.cardcrawl.vfx.combat.DamageImpactLineEffect;

import java.util.ArrayList;

public class StoneParticle extends AbstractGameEffect {
    private TextureAtlas.AtlasRegion img;
    private CatmullRomSpline<Vector2> crs;
    private ArrayList<Vector2> controlPoints;
    private static final int TRAIL_ACCURACY = 60;
    private Vector2[] points;
    private Vector2 pos;
    private Vector2 target;
    private float currentSpeed;
    private static final float MAX_VELOCITY;
    private static final float VELOCITY_RAMP_RATE;
    private static final float DST_THRESHOLD;
    private float rotation;
    private boolean rotateClockwise;
    private boolean stopRotating;
    private boolean facingLeft;
    private float rotationRate;

    public StoneParticle(final float sX, final float sY, final float tX, final float tY, final boolean facingLeft) {
        this.crs = new CatmullRomSpline<Vector2>();
        this.controlPoints = new ArrayList<Vector2>();
        this.points = new Vector2[60];
        this.currentSpeed = 0.0f;
        this.rotateClockwise = true;
        this.stopRotating = false;
        this.img = ImageMaster.GLOW_SPARK_2;
        this.pos = new Vector2(sX, sY);
        if (!facingLeft) {
            this.target = new Vector2(tX + StoneParticle.DST_THRESHOLD, tY);
        } else {
            this.target = new Vector2(tX - StoneParticle.DST_THRESHOLD, tY);
        }
        this.facingLeft = facingLeft;
        this.crs.controlPoints = new Vector2[1];
        this.rotateClockwise = MathUtils.randomBoolean();
        this.rotation = (float) MathUtils.random(0, 359);
        this.controlPoints.clear();
        this.rotationRate = MathUtils.random(600.0f, 650.0f) * Settings.scale;
        this.currentSpeed = 1000.0f * Settings.scale;
        this.color = new Color(0.1f, 0.1f, 0.2f, 0.7f);
        this.duration = 0.7f;
        this.scale = 1.0f * Settings.scale;
        this.renderBehind = MathUtils.randomBoolean();
    }

    @Override
    public void update() {
        this.updateMovement();
    }

    private void updateMovement() {
        final Vector2 tmp = new Vector2(this.pos.x - this.target.x, this.pos.y - this.target.y);
        tmp.nor();
        final float targetAngle = tmp.angle();
        this.rotationRate += Gdx.graphics.getDeltaTime() * 2000.0f;
        this.scale += Gdx.graphics.getDeltaTime() * 1.0f * Settings.scale;
        if (!this.stopRotating) {
            if (this.rotateClockwise) {
                this.rotation += Gdx.graphics.getDeltaTime() * this.rotationRate;
            } else {
                this.rotation -= Gdx.graphics.getDeltaTime() * this.rotationRate;
                if (this.rotation < 0.0f) {
                    this.rotation += 360.0f;
                }
            }
            this.rotation %= 360.0f;
            if (!this.stopRotating && Math.abs(this.rotation - targetAngle) < Gdx.graphics.getDeltaTime() * this.rotationRate) {
                this.rotation = targetAngle;
                this.stopRotating = true;
            }
        }
        tmp.setAngle(this.rotation);
        final Vector2 vector2 = tmp;
        vector2.x *= Gdx.graphics.getDeltaTime() * this.currentSpeed;
        final Vector2 vector3 = tmp;
        vector3.y *= Gdx.graphics.getDeltaTime() * this.currentSpeed;
        this.pos.sub(tmp);
        if (this.stopRotating) {
            this.currentSpeed += Gdx.graphics.getDeltaTime() * StoneParticle.VELOCITY_RAMP_RATE * 3.0f;
        } else {
            this.currentSpeed += Gdx.graphics.getDeltaTime() * StoneParticle.VELOCITY_RAMP_RATE * 1.5f;
        }
        if (this.currentSpeed > StoneParticle.MAX_VELOCITY) {
            this.currentSpeed = StoneParticle.MAX_VELOCITY;
        }
        if (this.target.dst(this.pos) < StoneParticle.DST_THRESHOLD) {
            for (int i = 0; i < 5; ++i) {
                if (this.facingLeft) {
                    AbstractDungeon.effectsQueue.add(new DamageImpactLineEffect(this.target.x + StoneParticle.DST_THRESHOLD, this.target.y));
                } else {
                    AbstractDungeon.effectsQueue.add(new DamageImpactLineEffect(this.target.x - StoneParticle.DST_THRESHOLD, this.target.y));
                }
            }
            CardCrawlGame.sound.playAV("BLUNT_HEAVY", MathUtils.random(0.6f, 0.9f), 0.5f);
            CardCrawlGame.screenShake.shake(ScreenShake.ShakeIntensity.MED, ScreenShake.ShakeDur.SHORT, false);
            this.isDone = true;
        }
        if (!this.controlPoints.isEmpty()) {
            if (!this.controlPoints.get(0).equals(this.pos)) {
                this.controlPoints.add(this.pos.cpy());
            }
        } else {
            this.controlPoints.add(this.pos.cpy());
        }
        if (this.controlPoints.size() > 3) {
            final Vector2[] vec2Array = new Vector2[0];
            this.crs.set(this.controlPoints.toArray(vec2Array), false);
            for (int j = 0; j < 60; ++j) {
                this.points[j] = new Vector2();
                this.crs.valueAt(this.points[j], j / 59.0f);
            }
        }
        if (this.controlPoints.size() > 10) {
            this.controlPoints.remove(0);
        }
        this.duration -= Gdx.graphics.getDeltaTime();
        if (this.duration < 0.0f) {
            this.isDone = true;
        }
    }

    @Override
    public void render(final SpriteBatch sb) {
        if (!this.isDone) {
            sb.setColor(Color.BLACK);
            float scaleCpy = this.scale;
            for (int i = this.points.length - 1; i > 0; --i) {
                if (this.points[i] != null) {
                    sb.draw(this.img, this.points[i].x - this.img.packedWidth / 2, this.points[i].y - this.img.packedHeight / 2, this.img.packedWidth / 2.0f, this.img.packedHeight / 2.0f, (float) this.img.packedWidth, (float) this.img.packedHeight, scaleCpy * 1.5f, scaleCpy * 1.5f, this.rotation);
                    scaleCpy *= 0.98f;
                }
            }
            sb.setBlendFunction(770, 1);
            sb.setColor(this.color);
            scaleCpy = this.scale;
            for (int i = this.points.length - 1; i > 0; --i) {
                if (this.points[i] != null) {
                    sb.draw(this.img, this.points[i].x - this.img.packedWidth / 2, this.points[i].y - this.img.packedHeight / 2, this.img.packedWidth / 2.0f, this.img.packedHeight / 2.0f, (float) this.img.packedWidth, (float) this.img.packedHeight, scaleCpy, scaleCpy, this.rotation);
                    scaleCpy *= 0.98f;
                }
            }
            sb.setBlendFunction(770, 771);
        }
    }

    @Override
    public void dispose() {
    }

    static {
        MAX_VELOCITY = 4000.0f * Settings.scale;
        VELOCITY_RAMP_RATE = 3000.0f * Settings.scale;
        DST_THRESHOLD = 42.0f * Settings.scale;
    }
}
