package com.github.paopaoyue.wljmod.event;

import com.github.paopaoyue.wljmod.card.Frog;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.AbstractImageEvent;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.screens.CardRewardScreen;
import com.megacrit.cardcrawl.vfx.FastCardObtainEffect;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FrogEvent extends AbstractImageEvent {

	public static final String ID = "Wlj:Frog";
	private static final EventStrings eventStrings = CardCrawlGame.languagePack.getEventString(ID);
	private static final String[] DESCRIPTIONS = eventStrings.DESCRIPTIONS;
	private static final String[] OPTIONS = eventStrings.OPTIONS;
	private static final String NAME = eventStrings.NAME;

    private static final int FROG_NUM = 6;
	private static final int HEAL_AMOUNT = 4;

	private enum State {
		BEGINNING,
		SELECTED,
		LEAVING
	}

    private int frogId;
	private int correctButton;
	private State state;
	private boolean correct;
	private AbstractCard frogCard;
	private boolean retrieveCard;

	public FrogEvent() {
		super(NAME, DESCRIPTIONS[0], "image/event/event_ceremony.png");

        this.frogId = AbstractDungeon.miscRng.random(0, FROG_NUM - 1);
        this.body = body + DESCRIPTIONS[frogId + 1] + DESCRIPTIONS[7];
        this.imageEventText.loadImage("image/event/event_frog_" + frogId + ".png");
		List<Integer> options = new ArrayList<>();
		for (int i = 0; i < FROG_NUM; i++) {
			if (i != frogId) {
				options.add(i);
			}
		}
		for (int i = 0; i < 3; i++) {
			Collections.shuffle(options);
			options.remove(0);
		}
		options.add(frogId);
		Collections.shuffle(options);
		correctButton = options.indexOf(frogId);
        for (int i = 0; i < 3; i++) {
            this.imageEventText.setDialogOption(OPTIONS[options.get(i)]);
        }
		this.frogCard = new Frog(frogId);
		state = State.BEGINNING;

		AbstractDungeon.getCurrRoom().rewards.clear();
	}

	@Override
	public void update() {
		super.update();
		if (!this.retrieveCard) {
			if (AbstractDungeon.cardRewardScreen.discoveryCard != null) {
				final AbstractCard disCard = AbstractDungeon.cardRewardScreen.discoveryCard;
				AbstractDungeon.effectList.add(new FastCardObtainEffect(disCard, disCard.current_x, disCard.current_y));
				AbstractDungeon.cardRewardScreen.discoveryCard = null;
				this.retrieveCard = true;
			}
		}
	}

	@Override
	protected void buttonEffect(int buttonPressed) {
		switch(state) {
			case BEGINNING:
                this.correct = (buttonPressed == correctButton);
				this.imageEventText.clearAllDialogs();
                if (this.correct) {
                    imageEventText.updateBodyText(DESCRIPTIONS[8] + DESCRIPTIONS[9]);
					this.imageEventText.setDialogOption(OPTIONS[6]);
                } else {
                    imageEventText.updateBodyText(DESCRIPTIONS[8] + DESCRIPTIONS[10]);
                    this.imageEventText.setDialogOption(OPTIONS[7]);
                }
				this.state = State.SELECTED;
				break;

			case SELECTED:
                if (correct)
                    AbstractDungeon.player.increaseMaxHp(HEAL_AMOUNT, true);

				ArrayList<AbstractCard> rewards = new ArrayList<>();
				rewards.add(frogCard);
				AbstractDungeon.cardRewardScreen.customCombatOpen(rewards, CardRewardScreen.TEXT[1], true);

				this.imageEventText.clearAllDialogs();
				this.imageEventText.setDialogOption(OPTIONS[8]);
				this.state = State.LEAVING;

			case LEAVING:
				if (!AbstractDungeon.isScreenUp) {
					openMap();
				}
				break;
		}
	}

}