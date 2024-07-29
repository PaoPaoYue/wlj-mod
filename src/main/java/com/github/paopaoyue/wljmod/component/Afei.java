package com.github.paopaoyue.wljmod.component;

import com.evacipated.cardcrawl.mod.stslib.Keyword;
import com.github.paopaoyue.wljmod.WljMod;
import com.github.paopaoyue.wljmod.action.AfeiAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class Afei extends AbstractAvatar {

    public static final String ID = "Wlj:Afei";

    public static final String CHARACTER_IMG = "image/character/afei.png";
    private static final Keyword avatarString = WljMod.MOD_DICTIONARY.get(ID);

    private AbstractCard card;
    private AbstractMonster target;
    private int incrementAmount;

    public Afei() {
        this.id = avatarString.ID;
        this.name = avatarString.NAMES[0];
        this.incrementAmount = 2;
        this.updateDescription();
    }

    public AbstractCard getCard() {
        return card;
    }

    public void setCard(AbstractCard card) {
        this.card = card;
    }

    public AbstractMonster getTarget() {
        return target;
    }

    public void setTarget(AbstractMonster target) {
        this.target = target;
    }

    @Override
    public void onDamaged(int damage, DamageInfo.DamageType damageType) {
        if (damageType != DamageInfo.DamageType.HP_LOSS && damage > 0) {
            this.card.baseDamage += incrementAmount;
            this.card.misc += incrementAmount;
            for (AbstractCard card : AbstractDungeon.player.masterDeck.group) {
                if (card.uuid.equals(this.card.uuid)) {
                    card.baseDamage += incrementAmount;
                    card.misc += incrementAmount;
                }
            }
        }
    }

    @Override
    public void onEnter() {
        AbstractDungeon.actionManager.addToTop(new AfeiAction(target, AbstractDungeon.player, card));
    }

    @Override
    public void upgrade() {
        super.upgrade();
        this.incrementAmount += 1;
    }

    @Override
    public String getCharacterImage() {
        return CHARACTER_IMG;
    }

    @Override
    public void updateDescription() {
        int insertPosition = avatarString.DESCRIPTION.length() - 5;
        this.description = avatarString.DESCRIPTION.substring(0, insertPosition) + incrementAmount + avatarString.DESCRIPTION.substring(insertPosition);
    }

}
