package com.github.paopaoyue.wljmod.effect;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;

import java.util.ArrayList;

public class SparkEffect extends AbstractGameEffect
{
    private Vector2 position;
    private Vector2 velocity;
    private float initialVelocity;
    private float maxVelocity;
    private TextureAtlas.AtlasRegion img;
    private ArrayList<Vector2> prevPositions;

    public SparkEffect(float x, float y, float initialVelocity, float maxVelocity) {
        this.img = null;
        this.prevPositions = new ArrayList<Vector2>();
        this.img = ImageMaster.GLOW_SPARK_2;
        this.duration = 1.5f;
        this.position = new Vector2(x - this.img.packedWidth / 2.0f, y - this.img.packedHeight / 2.0f);
        this.velocity = new Vector2(initialVelocity, 0.0f);
        this.initialVelocity = initialVelocity;
        this.maxVelocity = maxVelocity;
        this.color = new Color(1.0f, 1.0f, 0.2f, 1.0f);
        this.scale = 3.0f * Settings.scale;
    }

    @Override
    public void update() {
        this.prevPositions.add(this.position.cpy());
        this.velocity.x = Interpolation.exp5In.apply(initialVelocity, maxVelocity, MathUtils.clamp(1.5f - duration, 0.0f, 0.5f) / 0.5f);
        this.position.mulAdd(this.velocity, Gdx.graphics.getDeltaTime());
        if (this.prevPositions.size() > 30) {
            this.prevPositions.remove(0);
        }
        this.duration -= Gdx.graphics.getDeltaTime();
        if (this.duration < 0.0f) {
            this.isDone = true;
        }
    }

    @Override
    public void render(final SpriteBatch sb) {
        sb.setBlendFunction(770, 1);
        for (int i = 0; i < this.prevPositions.size(); ++i) {
            sb.setColor(new Color(1.0f, 0.9f, 0.3f, 1.0f));
            sb.draw(this.img, this.prevPositions.get(i).x, this.prevPositions.get(i).y, this.img.packedWidth / 2.0f, this.img.packedHeight / 2.0f, (float)this.img.packedWidth, (float)this.img.packedHeight, this.scale / 8.0f * (i * 0.05f + 1.0f) * MathUtils.random(1.5f, 3.0f), this.scale / 8.0f * (i * 0.05f + 1.0f) * MathUtils.random(0.5f, 2.0f), 0.0f);
        }
        sb.setColor(Color.RED);
        sb.draw(this.img, this.position.x, this.position.y, this.img.packedWidth / 2.0f, this.img.packedHeight / 2.0f, (float)this.img.packedWidth, (float)this.img.packedHeight, this.scale * 2.5f, this.scale * 2.5f, 0.0f);
        sb.setBlendFunction(770, 771);
        sb.setColor(Color.YELLOW);
        sb.draw(this.img, this.position.x, this.position.y, this.img.packedWidth / 2.0f, this.img.packedHeight / 2.0f, (float)this.img.packedWidth, (float)this.img.packedHeight, this.scale, this.scale, 0.0f);
    }

    @Override
    public void dispose() {

    }

}
