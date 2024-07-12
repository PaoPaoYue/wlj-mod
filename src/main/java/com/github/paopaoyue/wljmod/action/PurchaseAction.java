package com.github.paopaoyue.wljmod.action;

import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import java.util.function.Consumer;

public class PurchaseAction extends AbstractGameAction {

    private int amount;

    private Consumer<Boolean> callback;

    private static float SFX_PROB = 1.0f;

    private static int played_voice = 0;

    public static void resetSFX() {
        played_voice = 0;
        SFX_PROB = 1.0f;
    }

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
        boolean canPlay = false;
        if (played_voice == 0) {
            canPlay = MathUtils.random(1.0f) < SFX_PROB;
        }
        played_voice = canPlay ? played_voice : 0;

        if (canPlay) {
            int random = MathUtils.random(0, 2);
            played_voice = ((random + 1 == played_voice) ? ((random + 1) % 3) : random) + 1;
            if (played_voice == 1) {
                CardCrawlGame.sound.play("Wlj:PAY_1", 0f);
            } else if (played_voice == 2) {
                CardCrawlGame.sound.play("Wlj:PAY_2", 0f);
            } else {
                CardCrawlGame.sound.play("Wlj:PAY_3", 0f);
            }
            SFX_PROB = Math.max(0.5f, SFX_PROB - 0.1f);
        }
    }
}
