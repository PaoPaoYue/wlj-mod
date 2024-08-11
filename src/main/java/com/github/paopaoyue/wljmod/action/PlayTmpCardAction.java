package com.github.paopaoyue.wljmod.action;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.utility.NewQueueCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class PlayTmpCardAction extends AbstractGameAction {

    private AbstractCard card;
    private boolean purgeOnUse;
    private boolean exhaust;
    private AbstractCreature target;

    public PlayTmpCardAction(AbstractCard card, boolean purgeOnUse, boolean exhaust) {
        this.duration = Settings.ACTION_DUR_MED;
        this.actionType = ActionType.WAIT;
        this.source = AbstractDungeon.player;
        this.card = card;
        this.purgeOnUse = purgeOnUse;
        this.exhaust = exhaust;
    }

    public PlayTmpCardAction(AbstractCard card, AbstractCreature target, boolean purgeOnUse, boolean exhaust) {
        this(card, purgeOnUse, exhaust);
        this.target = target;
    }

    @Override
    public void update() {
        if (this.duration == Settings.ACTION_DUR_MED) {
            if (AbstractDungeon.getCurrRoom().monsters.areMonstersBasicallyDead()) {
                this.isDone = true;
                return;
            }
            if (card == null) {
                this.isDone = true;
                return;
            }
            if (purgeOnUse) {
                card.purgeOnUse = this.purgeOnUse;
            } else {
                card.exhaustOnUseOnce = this.exhaust;
            }
            AbstractDungeon.player.limbo.group.add(card);
            card.target_x = (float) Settings.WIDTH / 2 - 300F * Settings.scale;
            card.target_y = (float) Settings.HEIGHT / 2;
            card.targetAngle = 0.0f;
            card.drawScale = 0.01F;
            card.targetDrawScale = 0.75F;
            card.lighten(false);
            card.applyPowers();
            this.addToTop(new ForcedWaitAction(0.25F));
            if (this.target != null && !this.target.isDying) {
                this.addToTop(new NewQueueCardAction(card, target, true, true));
            } else {
                this.addToTop(new NewQueueCardAction(card, true, true, true));
            }
            this.isDone = true;
        }
    }
}
