package org.ginafro.notenoughfakepixel.variables;

import java.util.Arrays;

public enum Skins {

    ENDERMAN_HEAD("e3RleHR1cmVzOntTS0lOOnt1cmw6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvN2E1OWJiMGE3YTMyOTY1YjNkOTBkOGVhZmE4OTlkMTgzNWY0MjQ1MDllYWRkNGU2YjcwOWFkYTUwYjljZiJ9fX0="),
    RED_RELIC("e3RleHR1cmVzOntTS0lOOnt1cmw6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYzIwZWYwNmRkNjA0OTk3NjZhYzhjZTE1ZDJiZWE0MWQyODEzZmU1NTcxODg2NGI1MmRjNDFjYmFhZTFlYTkxMyJ9fX0="),
    GREEN_RELIC("e3RleHR1cmVzOntTS0lOOnt1cmw6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMzQwNjNiYTViMTZiNzAzMGEyMGNlNmYwZWE5NmRjZDI0YjA2NDgzNmY1NzA0NTZjZGJmYzllODYxYTc1ODVhNSJ9fX0="),
    ORANGE_RELIC("e3RleHR1cmVzOntTS0lOOnt1cmw6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYWFjZTZiYjNhYTRjY2FjMDMxMTY4MjAyZjZkNDUzMjU5N2JjYWM2MzUxMDU5YWJkOWQxMGIyODYxMDQ5M2FlYiJ9fX0="),
    PURPLE_RELIC("e3RleHR1cmVzOntTS0lOOnt1cmw6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNTZiMTBkMDkzMzBlMGY3MDA4MTVlNGQwNmU2NmI1ZDc2Nzc2NThiOWNhZDA4NTU1YmE3ZDg3YzEzYjI4OGQwZCJ9fX0="),
    BLUE_RELIC("e3RleHR1cmVzOntTS0lOOnt1cmw6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZTRlNzE2NzFkYjVmNjlkMmM0NmEwZDcyNzY2YjI0OWMxMjM2ZDcyNjc4MmMwMGEwZTIyNjY4ZGY1NzcyZDRiOSJ9fX0="),
    TEST("");

    private final String s;

    Skins(String value) {
        this.s = value;
    }

    public String getValue() {
        return this.s;
    }

    public static Skins getSkinByName(String name) {
        return Arrays.stream(Skins.values())
                .filter(skin -> skin.name().equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);
    }

    public static Skins getSkinByValue(String value) {
        return Arrays.stream(Skins.values())
                .filter(skin -> skin.getValue().equals(value))
                .findFirst()
                .orElse(null);
    }

}
