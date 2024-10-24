package com.github.paopaoyue.wljmod.event;

import com.github.paopaoyue.wljmod.card.AbstractWorkerCard;
import com.github.paopaoyue.wljmod.card.Koro;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.AbstractImageEvent;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.screens.CardRewardScreen;
import com.megacrit.cardcrawl.vfx.FastCardObtainEffect;
import com.megacrit.cardcrawl.vfx.RainingGoldEffect;
import com.megacrit.cardcrawl.vfx.cardManip.PurgeCardEffect;

import java.util.ArrayList;
import java.util.List;

public class CeremonyEvent extends AbstractImageEvent {

	public static final String ID = "Wlj:Ceremony";
	private static final EventStrings eventStrings = CardCrawlGame.languagePack.getEventString(ID);
	private static final String[] DESCRIPTIONS = eventStrings.DESCRIPTIONS;
	private static final String[] OPTIONS = eventStrings.OPTIONS;
	private static final String NAME = eventStrings.NAME;

	private static final float GOLD_BOOST = .4f;
	private static final int DAMAGE_AMOUNT = 6;
	private static final int HEAL_AMOUNT = 6;

	private enum State {
		BEGINNING,
		SELECTED,
		LEAVING
	}

	private State state;
	private int option;
	private long soundId;
	private AbstractCard cardToRemove;
	private boolean retrieveCard;
	private float preLeaveDuration = 0.5f;

	public CeremonyEvent() {
		super(NAME, DESCRIPTIONS[0], "image/event/event_ceremony.png");

		this.imageEventText.setDialogOption(OPTIONS[0]);
		this.imageEventText.setDialogOption(OPTIONS[1]);
		this.imageEventText.setDialogOption(OPTIONS[2]);
		this.imageEventText.setDialogOption(OPTIONS[3]);
		state = State.BEGINNING;

		AbstractDungeon.getCurrRoom().rewards.clear();
	}

	@Override
	public void update() {
		super.update();
		if (!this.retrieveCard) {
            if (AbstractDungeon.cardRewardScreen.discoveryCard != null) {
                final AbstractCard disCard = AbstractDungeon.cardRewardScreen.discoveryCard;
				AbstractDungeon.effectsQueue.add(new FastCardObtainEffect(disCard, disCard.current_x, disCard.current_y));
                AbstractDungeon.cardRewardScreen.discoveryCard = null;
				this.retrieveCard = true;
            }
        }
		if (this.preLeaveDuration > 0.0f && this.state == State.LEAVING) {
			this.preLeaveDuration -= com.badlogic.gdx.Gdx.graphics.getDeltaTime();
			if (this.preLeaveDuration < 0.0f) {
				this.preLeaveDuration = 0.0f;
			}
		}
	}

	@Override
	protected void buttonEffect(int buttonPressed) {
		switch(state) {
			case BEGINNING:
				this.option = buttonPressed;
				imageEventText.updateBodyText(DESCRIPTIONS[buttonPressed + 1]);
				this.imageEventText.clearAllDialogs();
				if (buttonPressed < 3) {
					this.imageEventText.setDialogOption(OPTIONS[buttonPressed + 4]);
				} else {
					setCard();
					if (this.cardToRemove != null) {
						this.imageEventText.setDialogOption(OPTIONS[7] + this.cardToRemove.name, this.cardToRemove);
					} else {
						this.imageEventText.setDialogOption(OPTIONS[9]);
					}
				}
				this.state = State.SELECTED;
				this.changeBGM();
				break;
			case SELECTED:
				switch(option) {
				case 0:
					ArrayList<AbstractCard> rewards = new ArrayList<>();
					rewards.add(new Koro());
					AbstractDungeon.cardRewardScreen.customCombatOpen(rewards, CardRewardScreen.TEXT[1], true);
					break;
				case 1:
					int goldAmount = (int)(AbstractDungeon.player.gold * GOLD_BOOST);
					AbstractDungeon.effectList.add(new RainingGoldEffect(10));
					AbstractDungeon.player.gainGold(goldAmount);
					break;
				case 2:
					CardCrawlGame.sound.play("Wlj:EVENT_OH");
					AbstractDungeon.player.damage(new DamageInfo(null, DAMAGE_AMOUNT));
					AbstractDungeon.player.increaseMaxHp(HEAL_AMOUNT, true);
					break;
				case 3:
					AbstractDungeon.effectList.add(new PurgeCardEffect(this.cardToRemove));
					AbstractDungeon.player.masterDeck.removeCard(this.cardToRemove);
					break;
				default:
					break;
				}
				this.imageEventText.clearAllDialogs();
				this.imageEventText.setDialogOption(OPTIONS[8]);
				this.preLeaveDuration = 0.5f;
				this.state = State.LEAVING;

			case LEAVING:
				if (!AbstractDungeon.isScreenUp && this.preLeaveDuration == 0.0f) {
					this.resumeBGM();
					openMap();
				}
				break;
		}
	}

	private void setCard() {
        List<AbstractCard> workers = new ArrayList<>();
		List<AbstractCard> commonCards = new ArrayList<>();
		List<AbstractCard> otherCards = new ArrayList<>();
		for (AbstractCard c : CardGroup.getGroupWithoutBottledCards(AbstractDungeon.player.masterDeck).group) {
			if (c instanceof AbstractWorkerCard) {
				workers.add(c);
			} else if (c.rarity == AbstractCard.CardRarity.COMMON) {
				commonCards.add(c);
			} else {
				otherCards.add(c);
			}
		}
		if (!workers.isEmpty()) {
			this.cardToRemove = workers.get(AbstractDungeon.miscRng.random(workers.size() - 1));
		} else if (!commonCards.isEmpty()) {
			this.cardToRemove = commonCards.get(AbstractDungeon.miscRng.random(commonCards.size() - 1));
		} else {
			this.cardToRemove = otherCards.get(AbstractDungeon.miscRng.random(otherCards.size() - 1));
		}
    }

	private void changeBGM() {
		CardCrawlGame.music.silenceBGMInstantly();
		CardCrawlGame.music.silenceTempBgmInstantly();
        switch (option) {
			case 0:
				this.soundId = CardCrawlGame.sound.play("Wlj:EVENT_LOL");
				break;
			case 1:
				this.soundId = CardCrawlGame.sound.play("Wlj:EVENT_SONG");
				break;
			case 2:
				this.soundId = CardCrawlGame.sound.play("Wlj:EVENT_LOVE");
				break;
			case 3:
				this.soundId = CardCrawlGame.sound.play("Wlj:EVENT_BOX");
				break;
			default:
				break;
		}
    }

	private void resumeBGM() {
		switch (option) {
			case 0:
				CardCrawlGame.sound.fadeOut("Wlj:EVENT_LOL", this.soundId);
				break;
			case 1:
				CardCrawlGame.sound.fadeOut("Wlj:EVENT_SONG", this.soundId);
				break;
			case 2:
				CardCrawlGame.sound.fadeOut("Wlj:EVENT_LOVE", this.soundId);
				break;
			case 3:
				CardCrawlGame.sound.fadeOut("Wlj:EVENT_BOX", this.soundId);
				break;
			default:
				break;
		}
		CardCrawlGame.music.unsilenceBGM();
	}
}
