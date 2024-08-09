package com.github.paopaoyue.wljmod.component;

import com.github.paopaoyue.wljmod.WljMod;
import com.github.paopaoyue.wljmod.action.ExhaustCardGroupAction;
import com.github.paopaoyue.wljmod.card.Invite;
import com.github.paopaoyue.wljmod.card.Rabble;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class Xiangdangdang extends AbstractAvatar {

    public static final String ID = "Wlj:Xiangdangdang";

    public static final String CHARACTER_IMG = "image/character/xiangdangdang.png";
    private static final AvatarStrings avatarString = WljMod.AVATAR_DICTIONARY.get(ID);

    private int gainHpAmount;

    public Xiangdangdang() {
        this.id = avatarString.ID;
        this.name = avatarString.NAME;
        this.updateDescription();
        this.gainHpAmount = 6;
    }

    @Override
    public void onEnter() {
        // exhaust half of rabbles inside discard pile, non-upgraded first if possible
        List<AbstractCard> cards = AbstractDungeon.player.discardPile.group.stream()
                .filter(c -> c instanceof Rabble)
                .sorted(Comparator.comparingInt(c -> c.timesUpgraded))
                .collect(Collectors.toList());
        Set<AbstractCard> cardsToExhaust = cards.stream().limit((cards.size() + 1) / 2).collect(Collectors.toSet());
        AbstractDungeon.actionManager.addToTop(new ExhaustCardGroupAction(AbstractDungeon.player.discardPile, cardsToExhaust::contains, null));
    }

    public int onEnterModifyHp(int hp) {
        return (int) (hp + ((AbstractDungeon.player.discardPile.group.stream().filter(c -> c instanceof Rabble).count() + 1) / 2 * this.gainHpAmount));
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
        updateDescription();
    }

    @Override
    public String getCharacterImage() {
        return CHARACTER_IMG;
    }

    @Override
    public void updateDescription() {
        this.description = String.format(avatarString.DESCRIPTION, gainHpAmount);
    }

}
