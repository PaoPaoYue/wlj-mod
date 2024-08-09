package com.github.paopaoyue.wljmod.potion;

import com.badlogic.gdx.graphics.Color;
import com.evacipated.cardcrawl.mod.stslib.patches.FlavorText;
import com.megacrit.cardcrawl.potions.AbstractPotion;

public abstract class AbstractWljPotion extends AbstractPotion {
    public AbstractWljPotion(String name, String id, PotionRarity rarity, PotionSize size, PotionEffect effect, Color liquidColor, Color hybridColor, Color spotsColor) {
        super(name, id, rarity, size, effect, liquidColor, hybridColor, spotsColor);
        FlavorText.PotionFlavorFields.textColor.set(this, Color.MAROON);
        FlavorText.PotionFlavorFields.boxColor.set(this, Color.GOLD);
        FlavorText.PotionFlavorFields.flavorBoxType.set(this, FlavorText.boxType.WHITE);
    }

    public AbstractWljPotion(String name, String id, PotionRarity rarity, PotionSize size, PotionColor color) {
        super(name, id, rarity, size, color);
        FlavorText.PotionFlavorFields.textColor.set(this, Color.MAROON);
        FlavorText.PotionFlavorFields.boxColor.set(this, Color.GOLD);
        FlavorText.PotionFlavorFields.flavorBoxType.set(this, FlavorText.boxType.WHITE);
    }
}
