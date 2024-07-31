package com.github.paopaoyue.wljmod.action;

import com.github.paopaoyue.wljmod.WljMod;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.utility.DiscardToHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import static com.github.paopaoyue.wljmod.patch.CardTagEnum.TAIWU;

public class AllTaiwuToHandAction extends AbstractGameAction {
    private AbstractPlayer p;
    private boolean upgraded;

    public AllTaiwuToHandAction(boolean upgraded) {
        this.setValues(this.p = AbstractDungeon.player, AbstractDungeon.player, this.amount);
        this.actionType = ActionType.CARD_MANIPULATION;
        this.upgraded = upgraded;
    }

    @Override
    public void update() {
        String avatarCardID = WljMod.avatarManager.getAvatarCardGroup().getBottomCard() != null ? WljMod.avatarManager.getAvatarCardGroup().getBottomCard().cardID : "";
        if (upgraded && !this.p.discardPile.isEmpty()) {
            for (final AbstractCard card : this.p.discardPile.group) {
                if (card.hasTag(TAIWU) && !(card.cardID.equals(avatarCardID))) {
                    if (card.costForTurn > card.cost - 1) {
                        card.setCostForTurn(card.cost - 1);
                    }
                    this.addToTop(new DiscardToHandAction(card));
                }
            }
        }
        if (!this.p.drawPile.isEmpty()) {
            for (final AbstractCard card : this.p.drawPile.group) {
                if (card.hasTag(TAIWU) && !(card.cardID.equals(avatarCardID))) {
                    if (card.costForTurn > card.cost - 1) {
                        card.setCostForTurn(card.cost - 1);
                    }
                    this.addToTop(new BetterDrawPileToHandAction(card));
                }
            }
        }
        this.p.onCardDrawOrDiscard();
        this.isDone = true;
    }
}

