package bizmessage.in.touchswitch.enums;

public enum MediaType {
    IMAGE(0),
    VIDEO(1),
    OTHER(2);

    public int value;

    MediaType(int value) {
        this.value = value;
    }
}
