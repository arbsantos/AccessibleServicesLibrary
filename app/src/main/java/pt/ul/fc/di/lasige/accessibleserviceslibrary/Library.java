package pt.ul.fc.di.lasige.accessibleserviceslibrary;

import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import java.util.List;


public interface Library {

    /**
     * Given a AccessibilityNodeInfo returns its description
     * if any
     * @param node
     * @return String with the descr
     */
    public String getDescription(AccessibilityNodeInfo node);

    /**
     * Given a AccessibilityNodeInfo returns its  parent description
     * if any
     * @param node
     * @return String with the descr
     */
    public String getParentDescription(AccessibilityNodeInfo node);

    /**
     * Given a AccessibilityNodeInfo returns its child description
     * if any
     * @param node
     * @return String with the descr
     */
    public String getChildDescription(AccessibilityNodeInfo node);

    /**
     * Given a AccessibilityNodeInfo returns its package
     * if any
     * @param node
     * @return String with the package
     */
    public String getPackageName(AccessibilityNodeInfo node);

    /**
     * Given a AccessibilityNodeInfo returns its class
     * if any
     * @param node
     * @return String with the package
     */
    public String getClassName(AccessibilityNodeInfo node);

    /**
     *
     * @param event
     * @return
     */
    public String getEventType(AccessibilityEvent event);

    /**
     * Search for a node on screen
     * @param description
     * @return
     */
    public AccessibilityNodeInfo searchNode(String description);

    /**
     * Log on loogcat all nodes on the screen
     * @param node
     */
    public void logNodeTree(AccessibilityNodeInfo node);

    /**
     * Given a AccessibilityNodeInfo returns a list of all descriptions of visible AccessibilityNodeInfo
     * @param node
     * @return List of all descriptions of visible AccessibilityNodeInfo
     */
    public List<String> getVisibleNodes(AccessibilityNodeInfo node);


}
