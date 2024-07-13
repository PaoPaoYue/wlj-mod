package com.github.paopaoyue.wljmod.action;

import com.github.paopaoyue.wljmod.sfx.SfxUtil;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import java.util.function.Consumer;

public class PurchaseAction extends AbstractGameAction {

    public static SfxUtil sfxUtil = SfxUtil.createInstance(new String[]{"Wlj:PAY_1", "Wlj:PAY_2", "Wlj:PAY_3"}, false, 1.0f, 0.1f, 0.5f);

    private int amount;

    private Consumer<Boolean> callback;

    public PurchaseAction(int amount, Consumer<Boolean> Callback) {
        this.duration = 0.0f;
        this.actionType = ActionType.WAIT;
        this.amount = amount;
        this.callback = Callback;
    }

    @Override
    public void update() {
        if (AbstractDungeon.player.gold >= amount) {
            CardCrawlGame.sound.play("SHOP_PURCHASE", 0f);
            sfxUtil.playSFX();
            AbstractDungeon.player.loseGold(this.amount);
            callback.accept(true);
        } else {
            callback.accept(false);
        }
        this.isDone = true;
    }

}
