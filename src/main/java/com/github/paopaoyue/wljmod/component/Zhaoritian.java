package com.github.paopaoyue.wljmod.component;

import com.github.paopaoyue.wljmod.WljMod;
import com.github.paopaoyue.wljmod.action.UseAvatarAction;
import com.github.paopaoyue.wljmod.card.AbstractAvatarCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class Zhaoritian extends AbstractAvatar {

    public static final String ID = "Wlj:Zhaoritian";

    public static final String CHARACTER_IMG = "image/character/zhaoritian.png";
    private static final AvatarStrings avatarString = WljMod.AVATAR_DICTIONARY.get(ID);

    private final AbstractAvatarCard dickCard;

    public Zhaoritian() {
        this.id = avatarString.ID;
        this.name = avatarString.NAME;
        this.dickCard = new com.github.paopaoyue.wljmod.card.Dick();
        this.updateDescription();
    }

    public void onDamaged(int damage, DamageInfo.DamageType damageType) {
        if (damageType != DamageInfo.DamageType.HP_LOSS && damage > 0) {
            AbstractDungeon.actionManager.addToTop(new UseAvatarAction(this.dickCard, new Dick(), this.dickCard.getHp(), true));
        }
    }

    @Override
    public void upgrade() {
        super.upgrade();
        this.dickCard.upgrade();
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
