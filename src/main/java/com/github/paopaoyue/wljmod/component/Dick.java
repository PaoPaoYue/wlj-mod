package com.github.paopaoyue.wljmod.component;

import com.evacipated.cardcrawl.mod.stslib.Keyword;
import com.github.paopaoyue.wljmod.WljMod;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.IntangiblePower;

public class Dick extends AbstractAvatar {

    public static final String ID = "Wlj:Dick";

    public static final String CHARACTER_IMG = "image/character/benti.png";
    private static final Keyword avatarString = WljMod.MOD_DICTIONARY.get(ID);

    public Dick() {
        this.id = avatarString.ID;
        this.name = avatarString.NAMES[0];
        this.updateDescription();
    }

    @Override
    public void onEnter() {
        AbstractDungeon.actionManager.addToTop(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new IntangiblePower(AbstractDungeon.player, 1), 1));
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
