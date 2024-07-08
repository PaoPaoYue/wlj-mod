package com.github.paopaoyue.wljmod.component;

import com.evacipated.cardcrawl.mod.stslib.Keyword;
import com.github.paopaoyue.wljmod.WljMod;
import com.github.paopaoyue.wljmod.action.SunKnightAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class SunKnight extends AbstractAvatar {

    public static final String ID = "Wlj:Sun Knight";

    public static final String CHARACTER_IMG = "image/character/sunknight.png";
    private static final Keyword avatarString = WljMod.MOD_DICTIONARY.get(ID);

    public SunKnight() {
        this.id = avatarString.ID;
        this.name = avatarString.NAMES[0];
        this.updateDescription();
    }

    @Override
    public void onEnter() {
        AbstractDungeon.actionManager.addToTop(new SunKnightAction());
    }

    @Override
    public String getCharacterImage() {
        return CHARACTER_IMG;
    }

    @Override
    public void updateDescription() {
        this.description = avatarString.DESCRIPTION;
    }

}
