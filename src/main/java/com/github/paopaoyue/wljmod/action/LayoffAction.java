package com.github.paopaoyue.wljmod.action;

import com.github.paopaoyue.wljmod.WljMod;
import com.github.paopaoyue.wljmod.card.AbstractWorkerCard;
import com.github.paopaoyue.wljmod.card.Erwen;
import com.github.paopaoyue.wljmod.card.Keel;
import com.github.paopaoyue.wljmod.card.Rabble;
import com.github.paopaoyue.wljmod.effect.ShowCardAndExhaustEffect;
import com.github.paopaoyue.wljmod.power.PrisonPower;
import com.github.paopaoyue.wljmod.relic.Dog;
import com.github.paopaoyue.wljmod.sfx.SfxUtil;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class LayoffAction extends AbstractGameAction {
    public static final String[] TEXT;
    private static final UIStrings uiStrings;

    static {
        uiStrings = CardCrawlGame.languagePack.getUIString("Wlj:LayoffAction");
        TEXT = uiStrings.TEXT;
    }

    public static SfxUtil sfxUtil = SfxUtil.createInstance(new String[]{"Wlj:LAYOFF_1", "Wlj:LAYOFF_2", "Wlj:LAYOFF_3", "Wlj:LAYOFF_4", "Wlj:LAYOFF_5", "Wlj:LAYOFF_6", "Wlj:LAYOFF_DOUBLE"}, false, 1.0f, 0.08f, 0.5f);

    private boolean auto;
    private boolean anyNumber;
    private List<AbstractCard> selectedCards;
    private Predicate<AbstractCard> predicate;

    public LayoffAction(int amount) {
        this(amount, false, true, null);
    }

    public LayoffAction(int amount, Predicate<AbstractCard> predicate) {
        this(amount, false, true, predicate);
    }

    public LayoffAction(int amount, boolean auto, Predicate<AbstractCard> predicate) {
        this(amount, auto, true, predicate);
    }

    public LayoffAction(int amount, boolean auto, boolean anyNumber, Predicate<AbstractCard> predicate) {
        setValues(AbstractDungeon.player, AbstractDungeon.player, amount);
        this.duration = this.startDuration = Settings.ACTION_DUR_FAST;
        this.actionType = ActionType.EXHAUST;
        this.auto = auto;
        this.anyNumber = anyNumber;
        this.predicate = predicate;
        this.selectedCards = new ArrayList<>();
    }

    public void update() {
        if (this.duration == this.startDuration) {

            if (AbstractDungeon.player.drawPile.group.stream().anyMatch(c -> c instanceof Erwen)) {
                AbstractDungeon.actionManager.addToTop(new LayoffAction(this.amount, this.auto, this.anyNumber, this.predicate));
                AbstractDungeon.actionManager.addToTop(new DiscardErwenAction());
                this.isDone = true;
                return;
            }

            PrisonPower prisonPower = (PrisonPower) AbstractDungeon.player.getPower(PrisonPower.POWER_ID);
            if (prisonPower != null) {
                prisonPower.onLayoff(this.amount);
            }

            Dog dog = (Dog) AbstractDungeon.player.getRelic(Dog.ID);
            if (dog != null) {
                dog.onLayoff(this.amount);
            }

            CardGroup tmpGroup = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
            for (AbstractCard card : AbstractDungeon.player.discardPile.group) {
                if (predicate == null || predicate.test(card)) {
                    tmpGroup.group.add(card);
                }
            }

            if (tmpGroup.group.isEmpty()) {
                this.isDone = true;
                return;
            }

            int amount = Math.min(this.amount, tmpGroup.size());

            if (!auto) {
                tmpGroup.group.sort((a, b) -> {
                    int priorityA = (a instanceof AbstractWorkerCard) ? WljMod.workerManager.getWorkerPriority((AbstractWorkerCard) a) : 0;
                    int priorityB = (b instanceof AbstractWorkerCard) ? WljMod.workerManager.getWorkerPriority((AbstractWorkerCard) b) : 0;
                    return priorityA - priorityB;
                });
                if (anyNumber) {
                    AbstractDungeon.gridSelectScreen.open(tmpGroup, amount, true, amount == 1 ? TEXT[0] : TEXT[1] + amount + TEXT[2]);
                } else {
                    AbstractDungeon.gridSelectScreen.open(tmpGroup, amount, amount == 1 ? TEXT[3] : TEXT[4] + amount + TEXT[5], false);
                }
            } else {
                for (int i = 0; i < amount; i++) {
                    AbstractCard card = tmpGroup.getRandomCard(AbstractDungeon.cardRandomRng);
                    tmpGroup.removeCard(card);
                    selectedCards.add(card);
                }
            }
        }

        if (!auto && !AbstractDungeon.gridSelectScreen.selectedCards.isEmpty()) {
            selectedCards.addAll(AbstractDungeon.gridSelectScreen.selectedCards);
            AbstractDungeon.gridSelectScreen.selectedCards.clear();
        }

        if (!selectedCards.isEmpty()) {

            sfxUtil.playLayoffSFX(selectedCards.size());

            for (AbstractCard card : selectedCards) {

                if (auto) {
                    this.showAndMoveToExhaustPile(card);
                } else {
                    AbstractDungeon.player.discardPile.moveToExhaustPile(card);
                }

                if (card instanceof AbstractWorkerCard) {
                    AbstractCard tempCard = card.makeStatEquivalentCopy();
                    if (card instanceof Keel) {
                        ((Keel) tempCard).triggerOnLayoff();
                    }
                    AbstractDungeon.player.drawPile.addToRandomSpot(tempCard);
                } else {
                    AbstractDungeon.player.drawPile.addToRandomSpot(new Rabble());
                }
            }

            CardCrawlGame.dungeon.checkForPactAchievement();
            AbstractDungeon.actionManager.addToTop(new DrawCardAction(selectedCards.size()));

            this.isDone = true;
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
        AbstractDungeon.player.discardPile.group.remove(c);
        AbstractDungeon.effectList.add(new ShowCardAndExhaustEffect(c));
        AbstractDungeon.player.exhaustPile.addToTop(c);
        AbstractDungeon.player.onCardDrawOrDiscard();
        CardCrawlGame.dungeon.checkForPactAchievement();
    }

}
