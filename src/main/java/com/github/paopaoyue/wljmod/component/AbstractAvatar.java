package com.github.paopaoyue.wljmod.component;

import com.github.paopaoyue.wljmod.WljMod;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

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

    public void onAttack(AbstractMonster target, DamageInfo info) {
    }

    public int onDamaged(int damage, DamageInfo.DamageType damageType) {
        return damage;
    }

    public void onUseCard(AbstractCard card) {
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
