package bizmessage.in.touchswitch.enums;

public enum ResponseType {
    USERNOTFOUND(-1),
    FAIL(0),
    SUCCESS(1),
    UNAUTHORIZED(2);

    public int value;

    ResponseType(int value) {
        this.value = value;
    }
}
