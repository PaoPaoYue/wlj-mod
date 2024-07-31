package com.github.paopaoyue.wljmod.component;

import com.github.paopaoyue.wljmod.character.Wlj;
import com.github.paopaoyue.wljmod.effect.AvatarDeadEffect;
import com.megacrit.cardcrawl.actions.common.DiscardSpecificCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import java.util.ArrayList;
import java.util.List;

public class AvatarManager {

    private final List<AbstractAvatar> avatarSequenceInBattle;

    private final CardGroup avatarCardGroup;

    private boolean hasAvatar;

    private int hp;

    private int baseHp;

    public AvatarManager() {
        avatarSequenceInBattle = new ArrayList<>();
        avatarCardGroup = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
    }

    public boolean isHasAvatar() {
        return hasAvatar;
    }

    public CardGroup getAvatarCardGroup() {
        return avatarCardGroup;
    }

    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    public int getBaseHp() {
        return baseHp;
    }

    public void setBaseHp(int baseHp) {
        this.baseHp = baseHp;
    }

    public AbstractAvatar getCurrentAvatar() {
        if (!hasAvatar) {
            return AbstractAvatar.NONE;
        }
        return avatarSequenceInBattle.get(avatarSequenceInBattle.size() - 1);
    }

    public AbstractAvatar getPreviousAvatar() {
        if (avatarSequenceInBattle.size() <= 1) {
            return AbstractAvatar.NONE;
        }
        return avatarSequenceInBattle.get(avatarSequenceInBattle.size() - 2);
    }

    public void transformAvatar(AbstractCard card, AbstractAvatar avatar) {
        if (hasAvatar) {
            this.getCurrentAvatar().onExit(avatar);
        }

        hasAvatar = true;

        avatarSequenceInBattle.add(avatar);

        card.current_x = avatarCardGroup.getTopCard().current_x;
        card.current_y = avatarCardGroup.getTopCard().current_y;
        avatarCardGroup.clear();
        avatarCardGroup.addToBottom(card);

        avatar.onEnter();
    }

    public void switchAvatar(AbstractCard card, AbstractAvatar avatar) {
        if (hasAvatar) {
            this.getCurrentAvatar().onExit(avatar);
        }

        hasAvatar = true;

        avatarSequenceInBattle.add(avatar);

        if (!avatarCardGroup.isEmpty()) {
            AbstractCard discardCard = avatarCardGroup.getTopCard();
            avatarCardGroup.moveToDiscardPile(discardCard);
        }
        if (!card.exhaust && !card.exhaustOnUseOnce && !card.purgeOnUse && !card.exhaustOnFire) {
            avatarCardGroup.addToBottom(card);
            AbstractDungeon.player.hand.empower(card);
        }

        avatar.onEnter();

    }

    public void onDead() {
        if (hasAvatar) {
            this.getCurrentAvatar().onExit(AbstractAvatar.NONE);
        }
        // hack for Zhaoritian to prevent dead effect
        if (getCurrentAvatar().id.equals(Zhaoritian.ID)) {
            return;
        }

        hasAvatar = false;
        baseHp = 0;

        avatarSequenceInBattle.add(AbstractAvatar.NONE);

        if (!avatarCardGroup.isEmpty()) {
            AbstractCard discardCard = avatarCardGroup.getTopCard();
            AbstractDungeon.actionManager.addToBottom(new DiscardSpecificCardAction(discardCard, avatarCardGroup));
        }

        AbstractDungeon.effectList.add(new AvatarDeadEffect());
    }

    public void onBattleEnd() {
        avatarSequenceInBattle.clear();
        avatarCardGroup.clear();

        hasAvatar = false;
        hp = 0;
        baseHp = 0;

        if (AbstractDungeon.player instanceof Wlj) {
            Wlj wlj = (Wlj) AbstractDungeon.player;
            wlj.SwitchCharacterImage(Wlj.CHARACTER_IMG);
        }
    }

}
