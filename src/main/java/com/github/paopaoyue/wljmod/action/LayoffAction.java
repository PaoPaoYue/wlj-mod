package com.github.paopaoyue.wljmod.action;

import com.github.paopaoyue.wljmod.WljMod;
import com.github.paopaoyue.wljmod.card.*;
import com.github.paopaoyue.wljmod.component.SunKnight;
import com.github.paopaoyue.wljmod.effect.ShowCardAndExhaustEffect;
import com.github.paopaoyue.wljmod.power.AlarmPower;
import com.github.paopaoyue.wljmod.power.PrisonPower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
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

    private boolean auto;
    private List<AbstractCard> selectedCards;
    private Predicate<AbstractCard> predicate;

    public LayoffAction(int amount) {
        this(amount, false, null);
    }

    public LayoffAction(int amount, Predicate<AbstractCard> predicate) {
        this(amount, false, predicate);
    }

    public LayoffAction(int amount, boolean auto, Predicate<AbstractCard> predicate) {
        this.target = AbstractDungeon.player;
        this.source = AbstractDungeon.player;
        AlarmPower alarmPower = (AlarmPower) AbstractDungeon.player.getPower(AlarmPower.POWER_ID);
        this.amount = (alarmPower == null ? amount : alarmPower.modifyLayoffAmount(amount));
        this.duration = this.startDuration = Settings.ACTION_DUR_FAST;
        this.actionType = ActionType.EXHAUST;
        this.auto = auto;
        this.predicate = predicate;
        this.selectedCards = new ArrayList<>();
    }

    public void update() {
        if (this.duration == this.startDuration) {

            PrisonPower prisonPower = (PrisonPower) AbstractDungeon.player.getPower(PrisonPower.POWER_ID);
            if (prisonPower != null) {
                prisonPower.onPreLayoff();
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
                AbstractDungeon.gridSelectScreen.open(tmpGroup, amount, true, this.amount == 1 ? TEXT[0] : TEXT[1] + this.amount + TEXT[2]);
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

            boolean randomWorker = AbstractDungeon.player.hasPower("Wlj:Future Tech");

            for (AbstractCard card : selectedCards) {

                if (auto) {
                    this.showAndMoveToExhaustPile(card);
                } else {
                    AbstractDungeon.player.discardPile.moveToExhaustPile(card);
                }

                if (randomWorker) {
                    AbstractCard tempCard = WljMod.workerManager.getRandomWorkerCard();
                    if (card instanceof Gaoshi) {
                        for (int i = 0; i < card.magicNumber; i++) {
                            AbstractDungeon.player.drawPile.addToRandomSpot(tempCard.makeStatEquivalentCopy());
                        }
                    } else {
                        AbstractDungeon.player.drawPile.addToRandomSpot(tempCard.makeStatEquivalentCopy());
                    }
                } else if (card instanceof AbstractWorkerCard) {
                    if (card instanceof Gaoshi) {
                        for (int i = 0; i < card.magicNumber; i++) {
                            AbstractDungeon.player.drawPile.addToRandomSpot(new Performer());
                        }
                    } else if (card instanceof Keel) {
                        AbstractCard tempCard = card.makeStatEquivalentCopy();
                        tempCard.baseDamage *= 2;
                        AbstractDungeon.player.drawPile.addToRandomSpot(tempCard);
                    } else {
                        AbstractDungeon.player.drawPile.addToRandomSpot(card.makeStatEquivalentCopy());
                    }
                } else {
                    AbstractDungeon.player.drawPile.addToRandomSpot(new Rabble());
                }
            }

            CardCrawlGame.dungeon.checkForPactAchievement();
            if (WljMod.avatarManager.getCurrentAvatar() instanceof SunKnight) {
                AbstractDungeon.actionManager.addToTop(new GainEnergyAction(1));
            }
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
