package com.github.paopaoyue.wljmod.relic;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.evacipated.cardcrawl.mod.stslib.patches.FlavorText;
import com.github.paopaoyue.wljmod.patch.relic.ExtraFlavorTextPatch;

public abstract class AbstractWljRelic extends CustomRelic {
    public AbstractWljRelic(String id, Texture texture, RelicTier tier, LandingSound sfx) {
        super(id, texture, tier, sfx);
        ExtraFlavorTextPatch.RelicExtraFlavorFields.textColor.set(this, Color.MAROON);
        ExtraFlavorTextPatch.RelicExtraFlavorFields.boxColor.set(this, Color.GOLD);
        ExtraFlavorTextPatch.RelicExtraFlavorFields.flavorBoxType.set(this, FlavorText.boxType.WHITE);
    }
}
