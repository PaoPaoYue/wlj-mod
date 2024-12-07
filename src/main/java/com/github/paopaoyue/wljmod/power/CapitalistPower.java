package com.github.paopaoyue.wljmod.power;

import com.badlogic.gdx.graphics.Texture;
import com.github.paopaoyue.wljmod.action.PlayTmpCardAction;
import com.github.paopaoyue.wljmod.card.*;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

import java.util.ArrayList;
import java.util.List;

public class CapitalistPower extends AbstractPower {
    public static final String POWER_ID = "Wlj:Capitalist";
    private static final PowerStrings strings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    private static final Texture IMG = ImageMaster.loadImage("image/icon/capitalist.png");

    private static final List<AbstractCard> cardPool = new ArrayList<>();

    static {
        cardPool.add(new Hire());
        cardPool.add(new ChildLabor());
        cardPool.add(new Startup());
        cardPool.add(new Balcony());
        cardPool.add(new Awards());
        cardPool.add(new Dice());
        cardPool.add(new Box());
        cardPool.add(new BridgeEngineer());
        cardPool.add(new Capitalist());
        cardPool.add(new Scone());
    }

    public CapitalistPower(AbstractCreature owner, int amount) {
        this.name = strings.NAME;
        this.ID = POWER_ID;
        this.owner = owner;

        this.img = IMG;
        this.type = PowerType.BUFF;
        this.amount = amount;
        this.canGoNegative = false;
        updateDescription();
    }

    @Override
    public void atStartOfTurn() {
        this.flash();
        for (int i = 0; i < this.amount; i++) {
            this.addToBot(new AbstractGameAction() {
                @Override
                public void update() {
                    addToTop(new PlayTmpCardAction(cardPool.get(AbstractDungeon.cardRandomRng.random(cardPool.size() - 1)).makeStatEquivalentCopy(), true, false));
                    isDone = true;
                }
            });
        }
    }

    public void updateDescription() {
        this.description = strings.DESCRIPTIONS[0] + amount + strings.DESCRIPTIONS[1];
    }

}
