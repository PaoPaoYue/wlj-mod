package com.github.paopaoyue.wljmod.relic;

import com.github.paopaoyue.wljmod.potion.Ball;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.localization.PotionStrings;
import com.megacrit.cardcrawl.localization.RelicStrings;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rewards.RewardItem;

import java.util.List;

public class Useless extends AbstractWljRelic {
    public static final String ID = "Wlj:Useless";
    private static final RelicStrings strings = CardCrawlGame.languagePack.getRelicStrings(ID);
    private static final PotionStrings potionStrings = CardCrawlGame.languagePack.getPotionString(Ball.POTION_ID);
    private static final RelicTier TIER = RelicTier.UNCOMMON;
    private static final LandingSound SOUND = LandingSound.FLAT;
    private static final float chance = 0.2f;


    public Useless() {
        super(ID, ImageMaster.loadImage("image/icon/useless.png"), TIER, SOUND);
        tips.add(new PowerTip(potionStrings.NAME, potionStrings.DESCRIPTIONS[0] + 1 + potionStrings.DESCRIPTIONS[1]));
    }

    public String getUpdatedDescription() {
        return strings.DESCRIPTIONS[0];
    }

    public void onRewardDrop(List<RewardItem> rewards) {
        if (AbstractDungeon.player.hasPotion(Ball.POTION_ID)) {
            return;
        }
        boolean trigger = rewards.stream()
                .anyMatch(reward -> reward.type == RewardItem.RewardType.GOLD || reward.type == RewardItem.RewardType.CARD) &&
                AbstractDungeon.miscRng.randomBoolean(chance);
        if (trigger) {
            this.flash();
            rewards.removeIf(reward -> reward.type == RewardItem.RewardType.GOLD || reward.type == RewardItem.RewardType.CARD);
            rewards.add(0, new RewardItem(new Ball()));
        }
    }

    @Override
    public AbstractRelic makeCopy() {
        return new Useless();
    }
}
