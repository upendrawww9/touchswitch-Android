package bizmessage.in.touchswitch.enums;

public enum UserType {
    MEMBER(1),
    GUEST(2);

    public int value;

    UserType(int value) {
        this.value = value;
    }
}
