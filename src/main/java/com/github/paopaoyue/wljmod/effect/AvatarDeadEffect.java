package com.github.paopaoyue.wljmod.effect;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.github.paopaoyue.wljmod.character.Wlj;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import com.megacrit.cardcrawl.vfx.combat.EmpowerCircleEffect;

public class AvatarDeadEffect extends AbstractGameEffect {
    private static final float SHAKE_DURATION = 0.25F;

    public AvatarDeadEffect() {
        CardCrawlGame.sound.play("CARD_POWER_IMPACT", 0.1F);

        for (int i = 0; i < 18; ++i) {
            AbstractDungeon.effectList.add(new EmpowerCircleEffect(AbstractDungeon.player.hb.cX, AbstractDungeon.player.hb.cY));
        }

        Wlj player = (Wlj) AbstractDungeon.player;
        player.SwitchCharacterImage(Wlj.CHARACTER_IMG);

        CardCrawlGame.screenShake.rumble(SHAKE_DURATION);
    }

    public void update() {
        this.isDone = true;
    }

    public void render(SpriteBatch sb) {
    }

    public void dispose() {
    }
}

