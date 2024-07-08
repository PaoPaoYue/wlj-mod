package com.github.paopaoyue.wljmod.card;

import basemod.abstracts.DynamicVariable;
import com.megacrit.cardcrawl.cards.AbstractCard;

public class AvatarHp extends DynamicVariable {

    public AvatarHp() {
    }

    @Override
    public String key() {
        return "K";
    }

    @Override
    public boolean isModified(AbstractCard card) {
        if (card instanceof AbstractAvatarCard) {
            return ((AbstractAvatarCard) card).isHpModified();
        }
        return false;
    }

    @Override
    public void setIsModified(AbstractCard card, boolean v) {
        if (card instanceof AbstractAvatarCard) {
            ((AbstractAvatarCard) card).setHpModified(v);
        }
    }

    @Override
    public int value(AbstractCard card) {
        if (card instanceof AbstractAvatarCard) {
            return ((AbstractAvatarCard) card).getHp();
        }
        return 0;
    }

    @Override
    public int baseValue(AbstractCard card) {
        if (card instanceof AbstractAvatarCard) {
            return ((AbstractAvatarCard) card).getBaseHp();
        }
        return 0;
    }

    @Override
    public boolean upgraded(AbstractCard card) {
        if (card instanceof AbstractAvatarCard) {
            return ((AbstractAvatarCard) card).isHpUpgraded();
        }
        return false;
    }
}
