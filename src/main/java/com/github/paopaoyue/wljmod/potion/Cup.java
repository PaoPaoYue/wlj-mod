package com.github.paopaoyue.wljmod.potion;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.GameDictionary;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.helpers.TipHelper;
import com.megacrit.cardcrawl.localization.PotionStrings;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.PoisonPower;
import com.megacrit.cardcrawl.powers.StrengthPower;

public class Cup extends AbstractPotion {

    public static final String POTION_ID = "Wlj:Cup";
    private static final PotionStrings potionStrings;
    private static final int DEFAULT_POTENCY = 5;

    static {
        potionStrings = CardCrawlGame.languagePack.getPotionString(POTION_ID);
    }

    public Cup() {
        super(potionStrings.NAME, POTION_ID, PotionRarity.RARE, PotionSize.GHOST, PotionColor.GREEN);
        this.labOutlineColor = Settings.LIGHT_YELLOW_COLOR;
        this.isThrown = true;
        this.targetRequired = true;
    }

    @Override
    public void initializeData() {
        this.potency = this.getPotency();
        this.description = potionStrings.DESCRIPTIONS[0] + this.potency + potionStrings.DESCRIPTIONS[1] + (this.potency > DEFAULT_POTENCY ? 2 : 1) + potionStrings.DESCRIPTIONS[2];
        this.tips.clear();
        this.tips.add(new PowerTip(this.name, this.description));
        this.tips.add(new PowerTip(TipHelper.capitalize(GameDictionary.POISON.NAMES[0]), GameDictionary.keywords.get(GameDictionary.POISON.NAMES[0])));
        this.tips.add(new PowerTip(TipHelper.capitalize(GameDictionary.STRENGTH.NAMES[0]), GameDictionary.keywords.get(GameDictionary.STRENGTH.NAMES[0])));

    }

    @Override
    public void use(AbstractCreature target) {
        AbstractCreature m = target;
        this.addToBot(new ApplyPowerAction(target, AbstractDungeon.player, new PoisonPower(target, AbstractDungeon.player, this.potency), this.potency));
        this.addToBot(new AbstractGameAction() {
            @Override
            public void update() {
                int amount = 0;
                if (!m.isDeadOrEscaped())
                    amount = m.powers.stream()
                            .filter(power -> power.type == AbstractPower.PowerType.DEBUFF)
                            .mapToInt(power -> -(potency > DEFAULT_POTENCY ? 2 : 1)).sum();
                if (amount < 0) {
                    this.addToTop(new ApplyPowerAction(m, AbstractDungeon.player, new StrengthPower(m, amount), amount));
                }
                this.isDone = true;
            }
        });
    }

    @Override
    public int getPotency(final int ascensionLevel) {
        return DEFAULT_POTENCY;
    }

    @Override
    public AbstractPotion makeCopy() {
        return new Cup();
    }

}