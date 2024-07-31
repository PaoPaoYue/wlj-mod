package com.github.paopaoyue.wljmod.component;

import com.evacipated.cardcrawl.mod.stslib.Keyword;
import com.github.paopaoyue.wljmod.WljMod;
import com.github.paopaoyue.wljmod.action.AllTaiwuToHandAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class Xiangdangdang extends AbstractAvatar {

    public static final String ID = "Wlj:Xiangdangdang";

    public static final String CHARACTER_IMG = "image/character/xiangdangdang.png";
    private static final Keyword avatarString = WljMod.MOD_DICTIONARY.get(ID);

    public Xiangdangdang() {
        this.id = avatarString.ID;
        this.name = avatarString.NAMES[0];
        this.updateDescription();
    }

    @Override
    public void onEnter() {
        AbstractDungeon.actionManager.addToTop(new AllTaiwuToHandAction(this.upgraded));
    }

    @Override
    public String getCharacterImage() {
        return CHARACTER_IMG;
    }

    @Override
    public void updateDescription() {
        String[] description = avatarString.DESCRIPTION.split("\\|");
        if (this.upgraded) {
            this.description = description[1];
        } else {
            this.description = description[0];
        }
    }

}
