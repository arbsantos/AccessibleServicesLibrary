package pt.ul.fc.di.lasige.accessibleserviceslibrary;

import android.accessibilityservice.AccessibilityService;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import java.util.List;

/**
 * Created by andre on 02/11/2016.
 */

public class Library implements ILibrary {

    private AccessibilityService service;

    public Library(AccessibilityService service){
        this.service=service;
    }

    @Override
    public String getDescription(AccessibilityNodeInfo node) {
        return null;
    }

    @Override
    public String getParentDescription(AccessibilityNodeInfo node) {
        return null;
    }

    @Override
    public String getChildDescription(AccessibilityNodeInfo node) {
        return null;
    }

    @Override
    public String getPackageName(AccessibilityNodeInfo node) {
        return null;
    }

    @Override
    public String getClassName(AccessibilityNodeInfo node) {
        return null;
    }

    @Override
    public String getEventType(AccessibilityEvent event) {
        return null;
    }

    @Override
    public AccessibilityNodeInfo searchNode(String description) {
        return null;
    }

    @Override
    public void logNodeTree(AccessibilityNodeInfo node) {

    }

    @Override
    public List<String> getVisibleNodes(AccessibilityNodeInfo node) {
        return null;
    }
}
