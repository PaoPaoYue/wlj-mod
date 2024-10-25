package com.github.paopaoyue.wljmod.component;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.github.paopaoyue.wljmod.WljMod;
import com.github.paopaoyue.wljmod.action.AvatarAttackAction;
import com.github.paopaoyue.wljmod.effect.FrogDefendEffect;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.beyond.*;
import com.megacrit.cardcrawl.monsters.city.*;
import com.megacrit.cardcrawl.monsters.ending.CorruptHeart;
import com.megacrit.cardcrawl.monsters.ending.SpireShield;
import com.megacrit.cardcrawl.monsters.ending.SpireSpear;
import com.megacrit.cardcrawl.monsters.exordium.*;
import com.megacrit.cardcrawl.powers.*;
import com.megacrit.cardcrawl.vfx.combat.FallingIceEffect;
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;
import com.megacrit.cardcrawl.vfx.combat.LightningEffect;

import java.util.Arrays;

public abstract class Frog extends AbstractAvatar {

    public static final String CHARACTER_IMG = "image/character/frog.png";

    private AbstractCard card;
    private AbstractMonster target;

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
    public void onEnter() {
        if (this.target.isDeadOrEscaped()) {
            this.target = AbstractDungeon.getMonsters().getRandomMonster(null, true, AbstractDungeon.cardRandomRng);
        }
        AbstractDungeon.actionManager.addToTop(new AvatarAttackAction(target, AbstractDungeon.player, card));
    }

    @Override
    public String getCharacterImage() {
        return CHARACTER_IMG;
    }

    @Override
    public void updateDescription() {
        AvatarStrings avatarString = WljMod.AVATAR_DICTIONARY.get(this.id);
        this.description = avatarString.DESCRIPTION;
    }

    public static class FrogN extends Frog {
        public static final String ID = "Wlj:Frog_n";
        private static final AvatarStrings avatarString = WljMod.AVATAR_DICTIONARY.get(ID);

        public FrogN() {
            this.id = ID;
            this.name = avatarString.NAME;
            this.updateDescription();
        }
    }

    public static class FrogY extends Frog {
        public static final String ID = "Wlj:Frog_y";
        private static final AvatarStrings avatarString = WljMod.AVATAR_DICTIONARY.get(ID);

        private static final int DAMAGE = 3;

        public FrogY() {
            this.id = ID;
            this.name = avatarString.NAME;
            this.updateDescription();
        }

        @Override
        public void onAttack(AbstractMonster target, DamageInfo info) {
            if (info.type == DamageInfo.DamageType.NORMAL && Arrays.asList(conductiveMonsters).contains(target.id)) {
                AbstractDungeon.actionManager.addToTop(new DamageAction(target, new DamageInfo(AbstractDungeon.player, DAMAGE, DamageInfo.DamageType.THORNS), AbstractGameAction.AttackEffect.NONE, true));
                AbstractDungeon.actionManager.addToTop(new VFXAction(new LightningEffect(target.drawX, target.drawY), 0.1f));
                AbstractDungeon.actionManager.addToTop(new SFXAction("ORB_LIGHTNING_EVOKE"));
            }
        }
    }

    public static class FrogC extends Frog {
        public static final String ID = "Wlj:Frog_c";
        private static final AvatarStrings avatarString = WljMod.AVATAR_DICTIONARY.get(ID);

        public FrogC() {
            this.id = ID;
            this.name = avatarString.NAME;
            this.updateDescription();
        }

        @Override
        public void onAttack(AbstractMonster target, DamageInfo info) {
            if (info.type == DamageInfo.DamageType.NORMAL) {
                AbstractMonster m = target;
                AbstractDungeon.actionManager.addToTop(new AbstractGameAction() {

                    private float duration = 0.25f;

                    @Override
                    public void update() {
                        if (duration == 0.25f) {
                            int frostCount = 3;
                            CardCrawlGame.sound.playA("ORB_FROST_CHANNEL", -0.25f - frostCount / 200.0f);
                            for (int i = 0; i < frostCount; ++i) {
                                AbstractDungeon.effectsQueue.add(new FallingIceEffect(frostCount, AbstractDungeon.getMonsters().shouldFlipVfx()));
                            }
                            this.addToTop(new ApplyPowerAction(m, AbstractDungeon.player, new StrengthPower(m, -1), -1, true));
                            if (!m.hasPower(ArtifactPower.POWER_ID)) {
                                this.addToTop(new ApplyPowerAction(m, AbstractDungeon.player, new GainStrengthPower(m, 1), 1, true));
                            }
                        }
                        duration -= Gdx.graphics.getDeltaTime();
                        if (duration < 0.0F) {
                            this.isDone = true;
                        }
                    }
                });
            }
        }
    }

    public static class FrogG extends Frog {
        public static final String ID = "Wlj:Frog_g";
        private static final AvatarStrings avatarString = WljMod.AVATAR_DICTIONARY.get(ID);

        public FrogG() {
            this.id = ID;
            this.name = avatarString.NAME;
            this.updateDescription();
        }

        @Override
        public void onAttack(AbstractMonster target, DamageInfo info) {
            if (info.type == DamageInfo.DamageType.NORMAL && Arrays.asList(organicMonsters).contains(target.id))
                AbstractDungeon.actionManager.addToTop(new ApplyPowerAction(target, AbstractDungeon.player, new PoisonPower(target, AbstractDungeon.player, 1), 1, true));
        }
    }

    public static class FrogR extends Frog {
        public static final String ID = "Wlj:Frog_r";
        private static final AvatarStrings avatarString = WljMod.AVATAR_DICTIONARY.get(ID);

        private static final int DAMAGE = 3;

        public FrogR() {
            this.id = ID;
            this.name = avatarString.NAME;
            this.updateDescription();
        }

        @Override
        public void onAttack(AbstractMonster target, DamageInfo info) {
            if (info.type == DamageInfo.DamageType.NORMAL && Arrays.asList(FlammableMonsters).contains(target.id))
                AbstractDungeon.actionManager.addToTop(new DamageAction(target, new DamageInfo(AbstractDungeon.player, DAMAGE, DamageInfo.DamageType.THORNS), AbstractGameAction.AttackEffect.FIRE));
        }
    }

    public static class FrogO extends Frog {
        public static final String ID = "Wlj:Frog_o";
        private static final AvatarStrings avatarString = WljMod.AVATAR_DICTIONARY.get(ID);

        private static final Texture effectImg = ImageMaster.loadImage("image/icon/shield.png");

        public FrogO() {
            this.id = ID;
            this.name = avatarString.NAME;
            this.updateDescription();
        }

        @Override
        public void onEnter() {
            super.onEnter();
            AbstractDungeon.actionManager.addToTop(new VFXAction(new FrogDefendEffect()));
        }

        @Override
        public int onDamaged(int damage, DamageInfo.DamageType damageType) {
            return Math.min(damage, WljMod.avatarManager.getHp());
        }

    }

    private static final String[] conductiveMonsters = {
            GremlinTsundere.ID,
            Hexaghost.ID,
            Sentry.ID,
            TheGuardian.ID,
            BronzeAutomaton.ID,
            BronzeOrb.ID,
            Centurion.ID,
            Champ.ID,
            SphericGuardian.ID,
            TorchHead.ID,
            Deca.ID,
            Donu.ID,
            Exploder.ID,
            GiantHead.ID,
            OrbWalker.ID,
            Repulsor.ID,
            Spiker.ID,
            Transient.ID,
    };
    private static final String[] FlammableMonsters = {
            Cultist.ID,
            FungiBeast.ID,
            Looter.ID,
            LouseDefensive.ID,
            LouseNormal.ID,
            SlaverBlue.ID,
            SlaverRed.ID,
            BookOfStabbing.ID,
            BanditBear.ID,
            BanditLeader.ID,
            BanditPointy.ID,
            Byrd.ID,
            Chosen.ID,
            GremlinLeader.ID,
            Healer.ID,
            Mugger.ID,
            SnakePlant.ID,
            Taskmaster.ID,
            TheCollector.ID,
            AwakenedOne.ID,
            Nemesis.ID,
            TimeEater.ID,
    };
    private static final String[] organicMonsters = {
            AcidSlime_L.ID,
            AcidSlime_M.ID,
            AcidSlime_S.ID,
            ApologySlime.ID,
            Cultist.ID,
            FungiBeast.ID,
            GremlinFat.ID,
            GremlinNob.ID,
            GremlinThief.ID,
            GremlinTsundere.ID,
            GremlinWarrior.ID,
            GremlinWizard.ID,
            JawWorm.ID,
            Lagavulin.ID,
            Looter.ID,
            LouseDefensive.ID,
            LouseNormal.ID,
            SlaverBlue.ID,
            SlaverRed.ID,
            SlimeBoss.ID,
            SpikeSlime_L.ID,
            SpikeSlime_M.ID,
            SpikeSlime_S.ID,
            BanditBear.ID,
            BanditLeader.ID,
            BanditPointy.ID,
            Byrd.ID,
            Centurion.ID,
            Champ.ID,
            Chosen.ID,
            GremlinLeader.ID,
            Healer.ID,
            Mugger.ID,
            ShelledParasite.ID,
            SnakePlant.ID,
            Snecko.ID,
            Taskmaster.ID,
            AwakenedOne.ID,
            Darkling.ID,
            Maw.ID,
            Reptomancer.ID,
            SnakeDagger.ID,
            SpireGrowth.ID,
            TimeEater.ID,
            WrithingMass.ID,
            SpireShield.ID,
            SpireSpear.ID,
            CorruptHeart.ID,
    };

}
