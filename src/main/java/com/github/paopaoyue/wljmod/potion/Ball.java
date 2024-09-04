package com.github.paopaoyue.wljmod.potion;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.localization.PotionStrings;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import com.megacrit.cardcrawl.rewards.RewardItem;

import java.util.ArrayList;
import java.util.List;

public class Ball extends AbstractWljPotion {

    public static final String POTION_ID = "Wlj:Ball";
    private static final PotionStrings potionStrings;
    private static final int DEFAULT_POTENCY = 1;

    static {
        potionStrings = CardCrawlGame.languagePack.getPotionString(POTION_ID);
    }

    public Ball() {
        super(potionStrings.NAME, POTION_ID, PotionRarity.PLACEHOLDER, PotionSize.SPHERE, PotionColor.WHITE);
        this.labOutlineColor = Settings.LIGHT_YELLOW_COLOR;
        this.isThrown = false;
        this.targetRequired = false;
    }

    @Override
    public void initializeData() {
        this.potency = this.getPotency();
        this.description = potionStrings.DESCRIPTIONS[0] + potency + potionStrings.DESCRIPTIONS[1];
        this.tips.clear();
        this.tips.add(new PowerTip(this.name, this.description));
    }

    @Override
    public void use(AbstractCreature target) {
    }

    public void onRewardDrop(List<RewardItem> rewards) {
        this.flash();
        List<RewardItem> replicates = new ArrayList<>();
        for (RewardItem reward : rewards) {
            for (int i = 0; i < potency; i++) {
                if (reward.type == RewardItem.RewardType.CARD) {
                    if (reward.cards.stream().allMatch(card -> card.color == AbstractCard.CardColor.COLORLESS)) {
                        replicates.add(new RewardItem(AbstractCard.CardColor.COLORLESS));
                    } else {
                        replicates.add(new RewardItem());
                    }
                } else if (reward.type == RewardItem.RewardType.STOLEN_GOLD) {
                    replicates.add(new RewardItem(reward.goldAmt, true));
                } else if (reward.type == RewardItem.RewardType.GOLD) {
                    replicates.add(new RewardItem(reward.goldAmt));
                }
            }
        }
        rewards.addAll(replicates);
    }

    @Override
    public int getPotency(final int ascensionLevel) {
        return DEFAULT_POTENCY;
    }

    @Override
    public AbstractPotion makeCopy() {
        return new Ball();
    }

}