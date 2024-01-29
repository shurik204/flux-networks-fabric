package sonar.fluxnetworks.api.gui;

public enum EnumNetworkColor {
    BLUE(0x295E8A),
    INDIGO(0x343477),
    PURPLE(0x582A72),
    PINK(0x882D60),
    RED(0xAA3939),
    BROWN(0xAA6F39),
    YELLOW(0xC6B900),
    GREEN(0x609732),
    LIGHT_BLUE(0x87CEFA),
    LILAC(0x86608E),
    LIGHT_CORAL(0xF08080),
    LIGHT_PINK(0xFFC0CB),
    PEACH(0xFFDAB9),
    FLAX(0xEEDC82);

    public static final EnumNetworkColor[] VALUES = values();

    private final int color;

    EnumNetworkColor(int color) {
        this.color = color;
    }

    public int getRGB() {
        return color;
    }
    
    public static int getColorIndex(int color) {
        for (int i = 0; i < VALUES.length; i++) {
            if (VALUES[i].getRGB() == color) {
                return i;
            }
        }
        return -1;
    }
}
