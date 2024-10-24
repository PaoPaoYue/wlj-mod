package com.github.paopaoyue.wljmod.component;

import com.github.paopaoyue.wljmod.WljMod;
import com.github.paopaoyue.wljmod.patch.avatar.ZhaoritianPatch;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.IntangiblePlayerPower;

public class Qidiaochan extends AbstractAvatar {

    public static final String ID = "Wlj:Qidiaochan";

    public static final String CHARACTER_IMG = "image/character/qidiaochan.png";
    private static final AvatarStrings avatarString = WljMod.AVATAR_DICTIONARY.get(ID);

    public Qidiaochan() {
        this.id = avatarString.ID;
        this.name = avatarString.NAME;
        this.updateDescription();
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
