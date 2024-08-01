package com.github.paopaoyue.wljmod.action;

import com.github.paopaoyue.wljmod.WljMod;
import com.github.paopaoyue.wljmod.card.Rabble;
import com.github.paopaoyue.wljmod.character.Wlj;
import com.github.paopaoyue.wljmod.component.AbstractAvatar;
import com.github.paopaoyue.wljmod.component.AvatarManager;
import com.github.paopaoyue.wljmod.component.Xiangdangdang;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class UseAvatarAction extends AbstractGameAction {
    private AbstractAvatar newAvatar;
    private AbstractCard card;
    private boolean transform;

    public UseAvatarAction(AbstractCard card, AbstractAvatar avatar, int avatarHp) {
        this(card, avatar, avatarHp, false);
    }

    public UseAvatarAction(AbstractCard card, AbstractAvatar avatar, int avatarHp, boolean transform) {
        setValues(AbstractDungeon.player, AbstractDungeon.player, avatarHp);
        this.card = card;
        this.newAvatar = avatar;
        this.duration = Settings.ACTION_DUR_MED;
        this.actionType = ActionType.SPECIAL;
        this.transform = transform;
    }

    public void update() {
        if (this.duration == Settings.ACTION_DUR_MED) {
            AvatarManager avatarManager = WljMod.avatarManager;

            if (this.newAvatar instanceof Xiangdangdang) {
                this.amount += AbstractDungeon.player.hand.group.stream().mapToInt(c -> c instanceof Rabble ? ((Xiangdangdang) this.newAvatar).getGainHpAmount() : 0).sum();
            }

            avatarManager.setHp(amount + avatarManager.getHp() <= 0 ? 1 : amount + avatarManager.getHp());
            avatarManager.setBaseHp(amount <= 0 ? 1 : amount);

            if (transform) {
                avatarManager.transformAvatar(card, newAvatar);
            } else {
                avatarManager.switchAvatar(card, newAvatar);
            }
            if (this.amount > 0) {
                this.target.healthBarUpdatedEvent();
            }
        } else if (this.duration <= 0.1F) {
            if ((card.exhaust || transform) && AbstractDungeon.player instanceof Wlj) {
                Wlj player = (Wlj) AbstractDungeon.player;
                player.SwitchCharacterImage(newAvatar.getCharacterImage());
            }
        }

        this.tickDuration();
    }
}
