package io.github.velaliilunalii.orenucopia.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;

public class ForgeCrushingRecipe implements Recipe<SimpleContainer> {
    private final ResourceLocation id;
    private final NonNullList<ItemStack> results;
    private final Ingredient ingredient;

    public ForgeCrushingRecipe(ResourceLocation id, NonNullList<ItemStack> results, Ingredient ingredient) {
        this.id = id;
        this.results = results;
        this.ingredient = ingredient;
    }

    @Override
    public boolean matches(SimpleContainer container, Level level) {
        return this.ingredient.test(container.getItem(0)); //input slot
    }

    @Override
    public ItemStack assemble(SimpleContainer simpleContainer, RegistryAccess registryAccess) {
        return this.results.get(0).copy();
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return width * height >= 1;
    }

    @Override
    public ItemStack getResultItem(RegistryAccess registryAccess) {
        return this.results.get(0).copy();
    }

    public NonNullList<ItemStack> getResults() {
        return this.results;
    }

    @Override
    public ResourceLocation getId() {
        return this.id;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return Serializer.INSTANCE;
    }

    @Override
    public RecipeType<?> getType() {
        return Type.INSTANCE;
    }

    private static NonNullList<ItemStack> itemsFromJson(JsonArray jsonArray) {
        NonNullList<ItemStack> results = NonNullList.create();
        for (JsonElement element : jsonArray) {
            if (element.isJsonObject()) {
                results.add(ShapedRecipe.itemStackFromJson(element.getAsJsonObject()));
            }
        }
        return results;
    }

    public static class Type implements RecipeType<ForgeCrushingRecipe> {
        private Type() {}
        public static final Type INSTANCE = new Type();
        public static final String ID = "forge_crushing";
    }

    public static class Serializer implements RecipeSerializer<ForgeCrushingRecipe> {
        public static final Serializer INSTANCE = new Serializer();
        public static final String ID = "forge_crushing";

        @Override
        public ForgeCrushingRecipe fromJson(ResourceLocation id, JsonObject json) {
            JsonElement ingredientElement = GsonHelper.isArrayNode(json, "ingredient")
                    ? GsonHelper.getAsJsonArray(json, "ingredient")
                    : GsonHelper.getAsJsonObject(json, "ingredient");
            Ingredient ingredient = Ingredient.fromJson(ingredientElement);

            JsonArray resultsArray = GsonHelper.getAsJsonArray(json, "results");
            NonNullList<ItemStack> results = itemsFromJson(resultsArray);

            if (results.isEmpty()) {
                throw new JsonParseException("Recipe must have at least one result");
            }

            return new ForgeCrushingRecipe(id, results, ingredient);
        }

        @Override
        public ForgeCrushingRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buffer) {
            int size = buffer.readVarInt();
            NonNullList<ItemStack> results = NonNullList.withSize(size, ItemStack.EMPTY);
            for (int i = 0; i < size; i++) {
                results.set(i, buffer.readItem());
            }
            Ingredient ingredient = Ingredient.fromNetwork(buffer);
            return new ForgeCrushingRecipe(id, results, ingredient);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buffer, ForgeCrushingRecipe recipe) {
            buffer.writeVarInt(recipe.results.size());
            for (ItemStack result : recipe.results) {
                buffer.writeItem(result);
            }
            recipe.ingredient.toNetwork(buffer);
        }
    }
}