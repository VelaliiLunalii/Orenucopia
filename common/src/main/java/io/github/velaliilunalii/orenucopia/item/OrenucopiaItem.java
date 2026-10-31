package io.github.velaliilunalii.orenucopia.item;

import io.github.velaliilunalii.orenucopia.CommonClass;
import io.github.velaliilunalii.orenucopia.recipe.ForgeCrushingRecipe;
import io.github.velaliilunalii.orenucopia.util.ItemStackUtil;
import io.github.velaliilunalii.orenucopia.util.ModTags;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.core.NonNullList;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.tags.InstrumentTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.inventory.tooltip.BundleTooltip;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

import static io.github.velaliilunalii.orenucopia.CommonClass.ORENUCOPIA;

public class OrenucopiaItem extends Item {
    private static final String TAG_ITEMS = "Items";
    public static final int MAX_WEIGHT = 64*8;
    private static final int BUNDLE_IN_BUNDLE_WEIGHT = 4;
    private static final int BAR_COLOR = Mth.color(0.4F, 0.4F, 1.0F);

    public OrenucopiaItem(Item.Properties properties) {
        super(properties);
    }

    public static boolean hasContent(ItemStack stack) {
        return getContentWeight(stack) > 0;
    }

    public boolean overrideStackedOnOther(ItemStack stack, Slot slot, ClickAction action, Player player) {
        if (action != ClickAction.SECONDARY) {
            return false;
        } else {
            ItemStack itemstack = slot.getItem();
            if (itemstack.isEmpty()) {
                this.playRemoveOneSound(player);
                removeOne(stack).ifPresent((p_150740_) -> add(stack, slot.safeInsert(p_150740_)));
            } else if (itemstack.getItem().canFitInsideContainerItems() && isAllowedIn(itemstack)) {
                int i = (MAX_WEIGHT - getContentWeight(stack)) / getWeight(itemstack);
                int j = add(stack, slot.safeTake(itemstack.getCount(), i, player));
                if (j > 0) {
                    this.playInsertSound(player);
                }
            }

            return true;
        }
    }

    public boolean overrideOtherStackedOnMe(ItemStack stack, ItemStack other, Slot slot, ClickAction action, Player player, SlotAccess access) {
        if (action == ClickAction.SECONDARY && slot.allowModification(player)) {
            if (other.isEmpty()) {
                removeOne(stack).ifPresent((p_186347_) -> {
                    this.playRemoveOneSound(player);
                    access.set(p_186347_);
                });
            } else {
                int i = add(stack, other);
                if (i > 0) {
                    this.playInsertSound(player);
                    other.shrink(i);
                }
            }

            return true;
        } else {
            return false;
        }
    }

    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
        ItemStack itemstack = player.getItemInHand(usedHand);
        if (dropContents(itemstack, player)) {
            this.playDropContentsSound(player);
            player.awardStat(Stats.ITEM_USED.get(this));
            return InteractionResultHolder.sidedSuccess(itemstack, level.isClientSide());
        } else {
            return InteractionResultHolder.fail(itemstack);
        }
    }

    public boolean isBarVisible(ItemStack stack) {
        CompoundTag compoundtag = stack.getOrCreateTag();
        boolean no_display = compoundtag.contains("NoDisplay");
        return getContentWeight(stack) > 0 && !no_display;
    }

    public int getBarWidth(ItemStack stack) {
        return Math.min(1 + 12 * getContentWeight(stack) / MAX_WEIGHT, 13);
    }

    public int getBarColor(ItemStack stack) {
        return BAR_COLOR;
    }

    private static boolean isAllowedIn(ItemStack itemStack){
        return itemStack.is(ModTags.Items.ORENUCOPIA_ITEM_ALLOWED);
    }

    private static int add(ItemStack bundleStack, ItemStack insertedStack) {
        if (!insertedStack.isEmpty() && insertedStack.getItem().canFitInsideContainerItems() && isAllowedIn(insertedStack)) {
            CompoundTag compoundtag = bundleStack.getOrCreateTag();
            if (!compoundtag.contains("Items")) {
                compoundtag.put("Items", new ListTag());
            }

            int i = getContentWeight(bundleStack);
            int j = getWeight(insertedStack);
            int k = Math.min(insertedStack.getCount(), (MAX_WEIGHT - i) / j);
            if (k == 0) {
                return 0;
            } else {
                ListTag listtag = compoundtag.getList("Items", 10);
                Optional<CompoundTag> optional = getMatchingItem(insertedStack, listtag);
                if (optional.isPresent()) {
                    CompoundTag compoundtag1 = (CompoundTag)optional.get();
                    ItemStack itemstack = ItemStack.of(compoundtag1);
                    int remainder = Math.max(itemstack.getCount() + k - itemstack.getMaxStackSize(), 0);
                    if (remainder > 0){
                        itemstack.setCount(itemstack.getMaxStackSize());
                        itemstack.save(compoundtag1);
                        listtag.remove(compoundtag1);
                        listtag.add(0, compoundtag1);
                        ItemStack itemstack1 = insertedStack.copyWithCount(remainder);
                        CompoundTag compoundtag2 = new CompoundTag();
                        itemstack1.save(compoundtag2);
                        listtag.add(0, compoundtag2);
                    }else {
                        itemstack.grow(k);
                        itemstack.save(compoundtag1);
                        listtag.remove(compoundtag1);
                        listtag.add(0, compoundtag1);
                    }
                } else {
                    ItemStack itemstack1 = insertedStack.copyWithCount(k);
                    CompoundTag compoundtag2 = new CompoundTag();
                    itemstack1.save(compoundtag2);
                    listtag.add(0, compoundtag2);
                }

                return k;
            }
        } else {
            return 0;
        }
    }

    private static Optional<CompoundTag> getMatchingItem(ItemStack stack, ListTag list) {
        Optional var10000;
        if (stack.is(Items.BUNDLE)) {
            var10000 = Optional.empty();
        } else {
            Stream var2 = list.stream();
            Objects.requireNonNull(CompoundTag.class);
            var2 = var2.filter(CompoundTag.class::isInstance);
            Objects.requireNonNull(CompoundTag.class);
            var10000 = var2.map(CompoundTag.class::cast).filter((p_186350_) -> ItemStack.isSameItemSameTags(ItemStack.of((CompoundTag) p_186350_), stack)).findFirst();
        }

        return var10000;
    }

    private static int getWeight(ItemStack stack) {
        return 64 / stack.getMaxStackSize();
    }

    private static int getContentWeight(ItemStack stack) {
        return getContents(stack).mapToInt((p_186356_) -> getWeight(p_186356_) * p_186356_.getCount()).sum();
    }

    private static Optional<ItemStack> removeOne(ItemStack stack) {
        CompoundTag compoundtag = stack.getOrCreateTag();
        if (!compoundtag.contains("Items")) {
            return Optional.empty();
        } else {
            ListTag listtag = compoundtag.getList("Items", 10);
            if (listtag.isEmpty()) {
                return Optional.empty();
            } else {
                int i = 0;
                CompoundTag compoundtag1 = listtag.getCompound(0);
                ItemStack itemstack = ItemStack.of(compoundtag1);
                listtag.remove(0);
                if (listtag.isEmpty()) {
                    stack.removeTagKey("Items");
                }

                return Optional.of(itemstack);
            }
        }
    }

    private static boolean dropContents(ItemStack stack, Player player) {
        CompoundTag compoundtag = stack.getOrCreateTag();
        if (!compoundtag.contains("Items")) {
            return false;
        } else {
            if (player instanceof ServerPlayer) {
                Level level = player.level();
                ListTag listtag = compoundtag.getList("Items", 10);

                SimpleContainer inputContainer = new SimpleContainer(MAX_STACK_SIZE);
                SimpleContainer outputContainer = new SimpleContainer(MAX_STACK_SIZE*3);

                for(int i = 0; i < listtag.size(); ++i) {
                    CompoundTag compoundtag1 = listtag.getCompound(i);
                    ItemStack itemstack = ItemStack.of(compoundtag1);

                    for (int k = 0; k < itemstack.getCount(); k++) {
                        inputContainer.addItem(itemstack.copy());

                        Optional<ForgeCrushingRecipe> recipe = level.getRecipeManager()
                                .getRecipeFor(ForgeCrushingRecipe.Type.INSTANCE, inputContainer, level);

                        if (recipe.isPresent()) {
                            ForgeCrushingRecipe forgeRecipe = recipe.get();

                            if (forgeRecipe.matches(inputContainer, level))
                                applyRecipe(inputContainer, outputContainer, itemstack, forgeRecipe);

                            for (int j = 0; j < outputContainer.getContainerSize(); j++) {
                                ItemStack newStack = outputContainer.removeItem(j, outputContainer.getItem(j).getCount());
                                player.drop(newStack, true);
                            }
                        } else if (itemstack.is(CommonClass.CONDENSED_EXPERIENCE)) {
                            ItemStack givenStack = CondensedExperienceItem.getLoot();
                            player.drop(givenStack, true);

                            if (level instanceof ServerLevel serverLevel) {
                                ExperienceOrb.award(serverLevel, player.position(), 1 + player.getRandom().nextInt(2));
                            }
                        } else if (itemstack.is(CommonClass.AURICHALCITE_CHUNK)) {
                            player.drop(new ItemStack(Items.RAW_COPPER, 12), true);
                        }
                    }
                }
            }

            stack.removeTagKey("Items");
            return true;
        }
    }

    private static void applyRecipe(SimpleContainer inputContainer, SimpleContainer outputContainer, ItemStack itemstack, ForgeCrushingRecipe forgeRecipe){
        inputContainer.removeItem(0, 1);

        for (ItemStack result : forgeRecipe.getResults()) {
            for (int j = 0; j < outputContainer.getContainerSize(); j++) {
                ItemStack existing = outputContainer.getItem(j);
                if (existing.isEmpty()) {
                    outputContainer.setItem(j, result.copy());
                    break;
                } else if (ItemStack.isSameItem(existing, result) && existing.getCount() + result.getCount() <= existing.getMaxStackSize()) {
                    existing.grow(result.getCount());
                    break;
                }
            }
        }
    }

    private static Stream<ItemStack> getContents(ItemStack stack) {
        CompoundTag compoundtag = stack.getTag();
        if (compoundtag == null) {
            return Stream.empty();
        } else {
            ListTag listtag = compoundtag.getList("Items", 10);
            Stream var10000 = listtag.stream();
            Objects.requireNonNull(CompoundTag.class);
            return var10000.map((c) -> ItemStack.of((CompoundTag)c));
        }
    }

    public Optional<TooltipComponent> getTooltipImage(ItemStack stack) {
        NonNullList<ItemStack> nonnulllist = NonNullList.create();
        Stream var10000 = getContents(stack);
        Objects.requireNonNull(nonnulllist);
        var10000.forEach((itemStack) -> nonnulllist.add((ItemStack) itemStack));
        return Optional.of(new BundleTooltip(nonnulllist, getContentWeight(stack)));
    }

    public void appendHoverText(ItemStack stack, Level level, List<Component> tooltipComponents, TooltipFlag isAdvanced) {
        tooltipComponents.add(Component.translatable("item.minecraft.bundle.fullness", new Object[]{getContentWeight(stack), MAX_WEIGHT}).withStyle(ChatFormatting.GRAY));
    }

    public void onDestroyed(ItemEntity itemEntity) {
        ItemUtils.onContainerDestroyed(itemEntity, getContents(itemEntity.getItem()));
    }

    private void playRemoveOneSound(Entity entity) {
        entity.playSound(SoundEvents.BUNDLE_REMOVE_ONE, 0.8F, 0.8F + entity.level().getRandom().nextFloat() * 0.4F);
    }

    private void playInsertSound(Entity entity) {
        entity.playSound(CommonClass.METAL_HAMMER, 1F, 0.8F + entity.level().getRandom().nextFloat() * 0.4F);
    }

    private void playDropContentsSound(Player player) {
        Level level = player.level();
        Optional<Holder<Instrument>> optional = BuiltInRegistries.INSTRUMENT.getTag(InstrumentTags.GOAT_HORNS).flatMap((named) -> Optional.of(named.get(1)));
        if (optional.isPresent()) {
            Instrument instrument = optional.get().value();
            SoundEvent soundEvent = (SoundEvent) instrument.soundEvent().value();
            float f = instrument.range() / 16.0F;
            level.playSound(player, player, soundEvent, SoundSource.RECORDS, f, 1.0F);
            level.gameEvent(GameEvent.INSTRUMENT_PLAY, player.position(), GameEvent.Context.of(player));
        }
    }

    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.TOOT_HORN;
    }

    public static ItemStack getFilledOrenucopia(){
        ItemStack orenucopia = new ItemStack(ORENUCOPIA);
        CompoundTag compoundtag = orenucopia.getOrCreateTag();
        compoundtag.put("Items", new ListTag());
        ListTag listtag = compoundtag.getList("Items", 10);
        ItemStack stack = new ItemStack(CommonClass.BLANK_TOURMALINE);
        CompoundTag compoundtag2 = new CompoundTag();
        stack.save(compoundtag2);
        listtag.add(0, compoundtag2);
        compoundtag.put("NoDisplay", StringTag.valueOf(""));
        return orenucopia;
    }
}