package com.github.paopaoyue.wljmod.component;

import com.github.paopaoyue.wljmod.card.*;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class WorkerManager {

    private final Map<String, Integer> workerCardsWithPriority;

    private int totalWorkerPlayedThisTurn;

    public WorkerManager() {
        workerCardsWithPriority = new HashMap<String, Integer>() {{
            put(Keel.ID, 0);
            put(Leidongxuan.ID, 1);
            put(Crab.ID, 1);
            put(Gaoshi.ID, 2);
            put(Erwen.ID, 2);
            put(Performer.ID, 101);
            put(Rabble.ID, 102);
            put(Prisoner.ID, 200);
        }};
        this.totalWorkerPlayedThisTurn = 0;
    }

    public void reset() {
        this.totalWorkerPlayedThisTurn = 0;
    }

    public void increaseTotalWorkerPlayedThisTurn() {
        this.totalWorkerPlayedThisTurn++;
    }

    public int getTotalWorkerPlayedThisTurn() {
        return this.totalWorkerPlayedThisTurn;
    }

    public void iterOutsideDiscardPile(Consumer<? super AbstractWorkerCard> consumer) {
        AbstractDungeon.player.drawPile.group.forEach(c -> {
            if (c instanceof AbstractWorkerCard) {
                consumer.accept((AbstractWorkerCard) c);
            }
        });
        AbstractDungeon.player.hand.group.forEach(c -> {
            if (c instanceof AbstractWorkerCard) {
                consumer.accept((AbstractWorkerCard) c);
            }
        });
    }

    public int getWorkerCountOutsideDiscardPile() {
        int count = 0;
        for (AbstractCard c : AbstractDungeon.player.drawPile.group) {
            if (c instanceof AbstractWorkerCard) {
                count++;
            }
        }
        for (AbstractCard c : AbstractDungeon.player.hand.group) {
            if (c instanceof AbstractWorkerCard) {
                count++;
            }
        }
        return count;
    }

    public int getWorkerCountInDiscardPile() {
        int count = 0;
        for (AbstractCard c : AbstractDungeon.player.discardPile.group) {
            if (c instanceof AbstractWorkerCard) {
                if (c instanceof Gaoshi) {
                    count += ((Gaoshi) c).magicNumber;
                } else {
                    count++;
                }
            }
        }
        return count;
    }

    public List<AbstractCard> getWorkerInDiscardPile() {
        return AbstractDungeon.player.discardPile.group.stream()
                .filter(c -> c instanceof AbstractWorkerCard)
                .collect(Collectors.toList());
    }

    public List<AbstractCard> getHighestPriorityWorkerInDiscardPile(int amount) {
        return AbstractDungeon.player.discardPile.group.stream()
                .filter(c -> c instanceof AbstractWorkerCard)
                .sorted(Comparator.comparingInt(c -> workerCardsWithPriority.get(((AbstractCard) c).cardID)))
                .limit(amount)
                .collect(Collectors.toList());
    }

    public AbstractCard getRandomWorkerCard() {
        switch (AbstractDungeon.cardRandomRng.random(2)) {
            case 0:
                switch (AbstractDungeon.cardRandomRng.random(5)) {
                    case 0:
                        return new Gaoshi();
                    case 1:
                        return new Erwen();
                    case 2:
                        return new Crab();
                    case 3:
                        return new Leidongxuan();
                    case 4:
                        return new Keel();
                    default:
                        return new Prisoner();
                }
            case 1:
                return new Performer();
            default:
                return new Rabble();
        }
    }
}
