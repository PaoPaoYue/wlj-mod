package com.github.paopaoyue.wljmod.component;

import com.megacrit.cardcrawl.cards.DamageInfo;

public abstract class AbstractAvatar {

    public static final AbstractAvatar NONE = null;

    protected String id;
    protected String name;

    protected String description;

    protected boolean upgraded = false;

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public boolean isUpgraded() {
        return upgraded;
    }


    public void onEnter() {
    }

    public void onExit(AbstractAvatar nextKami) {
    }

    public void onDamaged(int damage, DamageInfo.DamageType damageType) {
    }

    public abstract String getCharacterImage();

    public abstract void updateDescription();

    public void upgrade() {
        this.upgraded = true;
        updateDescription();
    }

    @Override
    public String toString() {
        return name;
    }
}
