package com.github.paopaoyue.wljmod.card;

import basemod.abstracts.CustomCard;
import com.badlogic.gdx.graphics.Color;
import com.evacipated.cardcrawl.mod.stslib.patches.FlavorText;

public abstract class AbstractWljCard extends CustomCard {
    public AbstractWljCard(String id, String name, String img, int cost, String rawDescription, CardType type, CardColor color, CardRarity rarity, CardTarget target) {
        super(id, name, img, cost, rawDescription, type, color, rarity, target);
        FlavorText.AbstractCardFlavorFields.textColor.set(this, Color.MAROON);
        FlavorText.AbstractCardFlavorFields.boxColor.set(this, Color.GOLD);
        FlavorText.AbstractCardFlavorFields.flavorBoxType.set(this, FlavorText.boxType.WHITE);
    }
}
