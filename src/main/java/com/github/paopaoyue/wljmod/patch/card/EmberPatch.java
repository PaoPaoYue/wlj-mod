package com.github.paopaoyue.wljmod.patch.card;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.github.paopaoyue.wljmod.card.Ember;
import com.github.paopaoyue.wljmod.utility.Inject;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import javassist.CtBehavior;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@SpirePatch(
        clz = AbstractRoom.class,
        method = "update"
)
public class EmberPatch {

    private static final Logger logger = LogManager.getLogger(EmberPatch.class);

    @SpireInsertPatch(
            locator = Locator.class
    )
    public static void Insert() {
        List<AbstractCard> tmpGroup =
                Stream.concat(
                                AbstractDungeon.player.drawPile.group.stream().filter(card -> card instanceof Ember && !card.upgraded),
                                AbstractDungeon.player.drawPile.group.stream().filter(card -> !(card instanceof Ember) || card.upgraded))
                        .collect(Collectors.toList());
        AbstractDungeon.player.drawPile.group.removeAll(tmpGroup);
        tmpGroup.addAll(AbstractDungeon.player.drawPile.group);
        AbstractDungeon.player.drawPile.group = (ArrayList<AbstractCard>) tmpGroup;
    }

    private static class Locator extends SpireInsertLocator {
        private Locator() {
        }

        public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
            Matcher finalMatcher = new Matcher.MethodCallMatcher(AbstractPlayer.class, "applyStartOfCombatPreDrawLogic");
            return Inject.insertAfter(LineFinder.findInOrder(ctMethodToPatch, new ArrayList<>(), finalMatcher), 1);
        }
    }
}
