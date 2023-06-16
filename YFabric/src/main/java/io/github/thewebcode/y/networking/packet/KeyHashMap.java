package io.github.thewebcode.y.networking.packet;

import net.minecraft.client.util.InputUtil;

import java.util.ArrayList;
import java.util.Random;

public class KeyHashMap {
    private ArrayList<InputUtil.Key> normalLetters;
    private ArrayList<InputUtil.Key> specialLetters;

    public KeyHashMap(){
        this.normalLetters = new ArrayList<>();
        this.specialLetters = new ArrayList<>();

        normalLetters.add(InputUtil.fromTranslationKey("key.keyboard.a"));
        normalLetters.add(InputUtil.fromTranslationKey("key.keyboard.b"));
        normalLetters.add(InputUtil.fromTranslationKey("key.keyboard.c"));
        normalLetters.add(InputUtil.fromTranslationKey("key.keyboard.d"));
        normalLetters.add(InputUtil.fromTranslationKey("key.keyboard.e"));
        normalLetters.add(InputUtil.fromTranslationKey("key.keyboard.f"));
        normalLetters.add(InputUtil.fromTranslationKey("key.keyboard.g"));
        normalLetters.add(InputUtil.fromTranslationKey("key.keyboard.h"));
        normalLetters.add(InputUtil.fromTranslationKey("key.keyboard.i"));
        normalLetters.add(InputUtil.fromTranslationKey("key.keyboard.j"));
        normalLetters.add(InputUtil.fromTranslationKey("key.keyboard.k"));
        normalLetters.add(InputUtil.fromTranslationKey("key.keyboard.l"));
        normalLetters.add(InputUtil.fromTranslationKey("key.keyboard.m"));
        normalLetters.add(InputUtil.fromTranslationKey("key.keyboard.n"));
        normalLetters.add(InputUtil.fromTranslationKey("key.keyboard.o"));
        normalLetters.add(InputUtil.fromTranslationKey("key.keyboard.p"));
        normalLetters.add(InputUtil.fromTranslationKey("key.keyboard.q"));
        normalLetters.add(InputUtil.fromTranslationKey("key.keyboard.r"));
        normalLetters.add(InputUtil.fromTranslationKey("key.keyboard.s"));
        normalLetters.add(InputUtil.fromTranslationKey("key.keyboard.t"));
        normalLetters.add(InputUtil.fromTranslationKey("key.keyboard.u"));
        normalLetters.add(InputUtil.fromTranslationKey("key.keyboard.v"));
        normalLetters.add(InputUtil.fromTranslationKey("key.keyboard.w"));
        normalLetters.add(InputUtil.fromTranslationKey("key.keyboard.x"));
        normalLetters.add(InputUtil.fromTranslationKey("key.keyboard.y"));
        normalLetters.add(InputUtil.fromTranslationKey("key.keyboard.z"));

        specialLetters.add(InputUtil.fromTranslationKey("key.keyboard.left.shift"));
        specialLetters.add(InputUtil.fromTranslationKey("key.keyboard.space"));

        specialLetters.add(InputUtil.fromTranslationKey("key.mouse.left"));
        specialLetters.add(InputUtil.fromTranslationKey("key.mouse.right"));
    }

    public InputUtil.Key getRandom(){
        ArrayList<InputUtil.Key> randomArrayList = getRandomArrayList();
        InputUtil.Key key = randomArrayList.get(new Random().nextInt(randomArrayList.size()));
        if (randomArrayList.contains(key)) {
            randomArrayList.remove(key);
        }
        return key;
    }

    private ArrayList<InputUtil.Key> getRandomArrayList(){
        if(specialLetters.size() <= 1 || specialLetters.isEmpty()) return normalLetters;
        if(normalLetters.size() <= 1 || normalLetters.isEmpty()) return specialLetters;

        Random random = new Random();
        return random.nextBoolean() ? normalLetters : specialLetters;
    }
}
