package com.github.paopaoyue.wljmod.card;

import com.megacrit.cardcrawl.cards.AbstractCard;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public abstract class AbstractLayoffCard extends AbstractWljCard {

    private static final Logger logger = LogManager.getLogger(AbstractLayoffCard.class.getName());

    private int baseLayoffAmount;
    private int layoffAmount;
    private boolean layoffAmountUpgraded;
    private boolean layoffAmountModified;
    private boolean anyNumber;

    public AbstractLayoffCard(String id, String name, String img, int cost, String rawDescription, CardType type, CardColor color, CardRarity rarity, CardTarget target, int baseLayoffAmount) {
        this(id, name, img, cost, rawDescription, type, color, rarity, target, baseLayoffAmount, true);
    }

    public AbstractLayoffCard(String id, String name, String img, int cost, String rawDescription, CardType type, CardColor color, CardRarity rarity, CardTarget target, int baseLayoffAmount, boolean anyNumber) {
        super(id, name, img, cost, rawDescription, type, color, rarity, target);
        this.baseLayoffAmount = baseLayoffAmount;
        this.layoffAmount = this.baseLayoffAmount;
        this.anyNumber = anyNumber;
    }

    public int getBaseLayoffAmount() {
        return baseLayoffAmount;
    }

    public void setBaseLayoffAmount(int baseLayoffAmount) {
        this.baseLayoffAmount = baseLayoffAmount;
    }

    public int getLayoffAmount() {
        return layoffAmount;
    }

    public void setLayoffAmount(int layoffAmount) {
        this.layoffAmount = layoffAmount;
    }

    public boolean isLayoffAmountUpgraded() {
        return layoffAmountUpgraded;
    }

    public void setLayoffAmountUpgraded(boolean layoffAmountUpgraded) {
        this.layoffAmountUpgraded = layoffAmountUpgraded;
    }

    public boolean isLayoffAmountModified() {
        return layoffAmountModified;
    }

    public void setLayoffAmountModified(boolean v) {
        this.layoffAmountModified = v;
    }

    public void modifyBaseLayoffAmount(int amount) {
        this.baseLayoffAmount = this.baseLayoffAmount + amount;
        this.layoffAmount = this.baseLayoffAmount;
        this.layoffAmountModified = amount != 0;
    }

    public void modifyLayoffAmount(int amount) {
        this.layoffAmount = this.baseLayoffAmount + amount;
        this.layoffAmountModified = amount != 0;
    }

    public void upgradeLayoffAmount(int amount) {
        this.baseLayoffAmount += amount;
        this.layoffAmount = this.baseLayoffAmount;
        this.layoffAmountUpgraded = amount != 0;
    }

    public void applyPowers() {
        super.applyPowers();
        if (anyNumber) {
            this.layoffAmount = LayoffAmount.calculateLayoffAmount(this.baseLayoffAmount);
            this.layoffAmountModified = this.baseLayoffAmount != this.layoffAmount;
        }
    }

    public AbstractCard makeStatEquivalentCopy() {
        AbstractCard card = super.makeStatEquivalentCopy();
        ((AbstractLayoffCard) card).baseLayoffAmount = this.baseLayoffAmount;
        ((AbstractLayoffCard) card).layoffAmountUpgraded = this.layoffAmountUpgraded;
        ((AbstractLayoffCard) card).anyNumber = this.anyNumber;
        return card;
    }
}