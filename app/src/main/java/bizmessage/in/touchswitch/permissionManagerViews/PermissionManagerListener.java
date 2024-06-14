package bizmessage.in.touchswitch.permissionManagerViews;

public interface PermissionManagerListener {
    void permissionCallback(String[] permissions, Permission[] grantResults, boolean allGranted);
}
