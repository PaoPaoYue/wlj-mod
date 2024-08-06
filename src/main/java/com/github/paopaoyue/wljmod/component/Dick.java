package com.github.paopaoyue.wljmod.component;

import com.github.paopaoyue.wljmod.WljMod;
import com.github.paopaoyue.wljmod.patch.avatar.ZhaoritianPatch;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.IntangiblePlayerPower;

public class Dick extends AbstractAvatar {

    public static final String ID = "Wlj:Dick";

    public static final String CHARACTER_IMG = "image/character/benti.png";
    private static final AvatarStrings avatarString = WljMod.AVATAR_DICTIONARY.get(ID);

    public Dick() {
        this.id = avatarString.ID;
        this.name = avatarString.NAME;
        this.updateDescription();
    }

    @Override
    public void onEnter() {
        AbstractPower power = new IntangiblePlayerPower(AbstractDungeon.player, 1);
        ZhaoritianPatch.IsAppliedAtMonsterTurnField.value.set(power, AbstractDungeon.actionManager.turnHasEnded);
        AbstractDungeon.actionManager.addToTop(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, power));
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
