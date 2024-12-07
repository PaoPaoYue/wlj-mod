package com.github.paopaoyue.wljmod.action;

import com.github.paopaoyue.wljmod.WljMod;
import com.github.paopaoyue.wljmod.effect.GoldTextOnPlayerEffect;
import com.github.paopaoyue.wljmod.power.GiantKingPower;
import com.github.paopaoyue.wljmod.sfx.SfxUtil;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.vfx.SpeechBubble;

import java.util.function.Consumer;

public class PurchaseAction extends AbstractGameAction {

    public static SfxUtil sfxUtil = SfxUtil.createInstance(new String[]{"Wlj:PAY_1", "Wlj:PAY_2", "Wlj:PAY_3"}, false, 1.0f, 0.1f, 0.5f);

    public static UIStrings uiStrings = CardCrawlGame.languagePack.getUIString("Wlj:PurchaseAction");

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
        if (AbstractDungeon.player.gold + WljMod.tempGold >= amount ||
                (AbstractDungeon.getCurrRoom().monsters != null &&
                        AbstractDungeon.getCurrRoom().monsters.monsters.stream()
                                .anyMatch(monster -> !monster.isDeadOrEscaped() && monster.hasPower(GiantKingPower.POWER_ID)))) {
            sfxUtil.playSFX();
            AbstractDungeon.effectList.add(new GoldTextOnPlayerEffect(-this.amount));
            AbstractDungeon.player.loseGold(this.amount);
            if (callback != null) callback.accept(true);
        } else {
            AbstractDungeon.effectList.add(new SpeechBubble(AbstractDungeon.player.dialogX, AbstractDungeon.player.dialogY, 2.0F, uiStrings.TEXT[0], true));
            if (callback != null) callback.accept(false);
        }
        this.isDone = true;
    }

}
