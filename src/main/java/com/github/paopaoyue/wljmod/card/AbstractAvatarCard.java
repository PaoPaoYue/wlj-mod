package com.github.paopaoyue.wljmod.card;

import basemod.abstracts.CustomCard;
import com.github.paopaoyue.wljmod.component.AbstractAvatar;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public abstract class AbstractAvatarCard extends CustomCard {

    private static final Logger logger = LogManager.getLogger(AbstractAvatarCard.class.getName());

    protected AbstractAvatar avatar;
    private int baseHp;
    private int hp;
    private boolean hpUpgraded;
    private boolean hpModified;

    public AbstractAvatarCard(String id, String name, String img, int cost, String rawDescription, CardType type, CardColor color, CardRarity rarity, CardTarget target, AbstractAvatar avatar, int baseAvatarHp) {
        super(id, name, img, cost, rawDescription, type, color, rarity, target);
        this.avatar = avatar;
        this.baseHp = baseAvatarHp;
        this.hp = this.baseHp;
    }

    public AbstractAvatar getAvatar() {
        return avatar;
    }

    public int getBaseHp() {
        return baseHp;
    }

    public void setBaseHp(int baseHp) {
        this.baseHp = baseHp;
    }

    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    public boolean isHpUpgraded() {
        return hpUpgraded;
    }

    public void setHpUpgraded(boolean hpUpgraded) {
        this.hpUpgraded = hpUpgraded;
    }

    public boolean isHpModified() {
        return hpModified;
    }

    public void setHpModified(boolean v) {
        this.hpModified = v;
    }

    public void modifyBaseHp(int amount) {
        this.baseHp = this.baseHp + amount;
        this.hp = this.baseHp;
        this.hpModified = true;
    }

    public void modifyHp(int amount) {
        this.hp = this.baseHp + amount;
        this.hpModified = true;
    }

    public void upgradeAvatar() {
        this.avatar.upgrade();
    }

    public void upgradeHp(int amount) {
        this.baseHp += amount;
        this.hp = this.baseHp;
        this.hpUpgraded = true;
    }

}