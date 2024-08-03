package com.github.paopaoyue.wljmod.component;

import com.github.paopaoyue.wljmod.WljMod;
import com.github.paopaoyue.wljmod.action.SunKnightAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class SunKnight extends AbstractAvatar {

    public static final String ID = "Wlj:Sun Knight";

    public static final String CHARACTER_IMG = "image/character/sunknight.png";
    private static final AvatarStrings avatarString = WljMod.AVATAR_DICTIONARY.get(ID);

    public SunKnight() {
        this.id = avatarString.ID;
        this.name = avatarString.NAME;
        this.updateDescription();
    }

    @Override
    public void onEnter() {
        AbstractDungeon.actionManager.addToTop(new SunKnightAction(this.upgraded));
    }

    @Override
    public String getCharacterImage() {
        return CHARACTER_IMG;
    }

    @Override
    public void updateDescription() {
        this.description = upgraded ? avatarString.UPGRADE_DESCRIPTION : avatarString.DESCRIPTION;
    }

}
