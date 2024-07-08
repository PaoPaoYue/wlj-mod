package com.github.paopaoyue.wljmod.component;

import com.evacipated.cardcrawl.mod.stslib.Keyword;
import com.github.paopaoyue.wljmod.WljMod;
import com.github.paopaoyue.wljmod.action.ReduceHandCostAction;
import com.github.paopaoyue.wljmod.card.Love;
import com.github.paopaoyue.wljmod.patch.CardTagEnum;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class Xiangdangdang extends AbstractAvatar {

    public static final String ID = "Wlj:Xiangdangdang";

    public static final String CHARACTER_IMG = "image/character/xiangdangdang.png";
    private static final Keyword avatarString = WljMod.MOD_DICTIONARY.get(ID);
    private int amount = 1;

    public Xiangdangdang() {
        this.id = avatarString.ID;
        this.name = avatarString.NAMES[0];
        this.updateDescription();
    }

    @Override
    public void onEnter() {
        AbstractDungeon.actionManager.addToTop(new ReduceHandCostAction(this.amount, c -> c.hasTag(CardTagEnum.TAIWU)));
        AbstractDungeon.actionManager.addToTop(new MakeTempCardInHandAction(new Love(), this.amount));
    }

    @Override
    public void upgrade() {
        this.amount += 1;
        super.upgrade();
    }

    @Override
    public String getCharacterImage() {
        return CHARACTER_IMG;
    }

    @Override
    public void updateDescription() {
        this.description = String.format(avatarString.DESCRIPTION, this.amount);
    }

}
