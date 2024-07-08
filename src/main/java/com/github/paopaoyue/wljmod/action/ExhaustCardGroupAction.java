package com.github.paopaoyue.wljmod.action;

import com.github.paopaoyue.wljmod.effect.ShowCardAndExhaustEffect;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class ExhaustCardGroupAction extends AbstractGameAction {
    private CardGroup group;
    private float startingDuration;
    private Predicate<AbstractCard> predicate;
    private Consumer<List<AbstractCard>> callback;

    public ExhaustCardGroupAction(CardGroup group) {
        this(group, c -> true, null);
    }

    public ExhaustCardGroupAction(CardGroup group, Predicate<AbstractCard> predicate, Consumer<List<AbstractCard>> callback) {
        this.setValues(AbstractDungeon.player, AbstractDungeon.player, this.amount);
        this.actionType = ActionType.EXHAUST;
        this.group = group;
        this.predicate = predicate;
        this.startingDuration = 0.8F;
        this.duration = this.startingDuration;
        this.callback = callback;
    }

    @Override
    public void update() {

        List<AbstractCard> cardToExhaust = this.group.group.stream().filter(this.predicate).collect(Collectors.toList());

        if (this.callback != null) {
            this.callback.accept(cardToExhaust);
        }

        if (cardToExhaust.isEmpty()) {
            this.isDone = true;
            return;
        }

        int effectCount = 0;
        if (this.duration == this.startingDuration) {
            for (AbstractCard c : cardToExhaust) {
                this.showAndMoveToExhaustPile(c);
            }
        }
        this.tickDuration();
    }

    private void showAndMoveToExhaustPile(AbstractCard c) {
        for (final AbstractRelic r : AbstractDungeon.player.relics) {
            r.onExhaust(c);
        }
        for (final AbstractPower p : AbstractDungeon.player.powers) {
            p.onExhaust(c);
        }
        c.triggerOnExhaust();
        if (AbstractDungeon.player.hoveredCard == c) {
            AbstractDungeon.player.releaseCard();
        }
        AbstractDungeon.actionManager.removeFromQueue(c);
        c.unhover();
        c.untip();
        c.stopGlowing();
        this.group.group.remove(c);
        AbstractDungeon.effectList.add(new ShowCardAndExhaustEffect(c));
        AbstractDungeon.player.exhaustPile.addToTop(c);
        AbstractDungeon.player.onCardDrawOrDiscard();
        CardCrawlGame.dungeon.checkForPactAchievement();
    }
}
