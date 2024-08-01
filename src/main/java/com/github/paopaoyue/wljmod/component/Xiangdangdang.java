package com.github.paopaoyue.wljmod.component;

import com.evacipated.cardcrawl.mod.stslib.Keyword;
import com.github.paopaoyue.wljmod.WljMod;
import com.github.paopaoyue.wljmod.card.Invite;
import com.github.paopaoyue.wljmod.card.Rabble;
import com.megacrit.cardcrawl.actions.common.ExhaustSpecificCardAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class Xiangdangdang extends AbstractAvatar {

    public static final String ID = "Wlj:Xiangdangdang";

    public static final String CHARACTER_IMG = "image/character/xiangdangdang.png";
    private static final Keyword avatarString = WljMod.MOD_DICTIONARY.get(ID);

    private int gainHpAmount;

    public Xiangdangdang() {
        this.id = avatarString.ID;
        this.name = avatarString.NAMES[0];
        this.updateDescription();
        this.gainHpAmount = 3;
    }

    @Override
    public void onEnter() {
        for (AbstractCard c : AbstractDungeon.player.hand.group) {
            if (c instanceof Rabble) {
                AbstractDungeon.actionManager.addToTop(new ExhaustSpecificCardAction(c, AbstractDungeon.player.hand, Settings.FAST_MODE));
            }
        }
    }

    @Override
    public void onUseCard(AbstractCard card) {
        if (card.costForTurn > 0 && card.type == AbstractCard.CardType.ATTACK && !(card instanceof Invite)) {
            Invite.sfxUtil.playSFX(1.6f);
            AbstractDungeon.actionManager.addToBottom(new MakeTempCardInHandAction(new Rabble(), 1));
        }
    }

    @Override
    public void upgrade() {
        super.upgrade();
        this.gainHpAmount += 2;
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

    public int getGainHpAmount() {
        return gainHpAmount;
    }

}
