package com.github.paopaoyue.wljmod.action;

import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import java.util.function.Consumer;

public class PurchaseAction extends AbstractGameAction {

    private int amount;

    private Consumer<Boolean> callback;

    private static final float SFX_PROB = 0.5f;

    private static boolean played_voice = false;

    public PurchaseAction(int amount, Consumer<Boolean> Callback) {
        this.duration = 0.0f;
        this.actionType = ActionType.WAIT;
        this.amount = amount;
        this.callback = Callback;
    }

    @Override
    public void update() {
        if (AbstractDungeon.player.gold >= amount) {
            playSFX();
            AbstractDungeon.player.loseGold(this.amount);
            callback.accept(true);
        } else {
            callback.accept(false);
        }
        this.isDone = true;
    }

    private void playSFX() {
        CardCrawlGame.sound.play("SHOP_PURCHASE", 0f);
        if (played_voice) {
            played_voice = MathUtils.random(1.0f) < SFX_PROB;
        } else {
            played_voice = true;
        }

        if (played_voice) {
            int random = MathUtils.random(1, 3);
            if (random == 1) {
                CardCrawlGame.sound.play("Wlj:PAY_1", 0f);
            } else if (random == 2) {
                CardCrawlGame.sound.play("Wlj:PAY_2", 0f);
            } else {
                CardCrawlGame.sound.play("Wlj:PAY_3", 0f);
            }
        }
    }
}
