package pt.ul.fc.di.lasige.accessibleserviceslibrary;

import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import java.util.List;


public interface ILibrary {

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
    public String getChildDescription(AccessibilityNodeInfo node, int index);

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
     * @param description description to search
     * @return Returns a list of all nodes wich description contains the param given
     */
    public List<AccessibilityNodeInfo> searchNode(String description);

    /**
     *
     * @param node
     */
    public void listTree(AccessibilityNodeInfo node);

    public void populate_lists();
    /**
     * Log on loogcat all nodes on the screen
     * @param node
     */
    public void logNodeTree(AccessibilityNodeInfo node, int indent);

    /**
     * Given a AccessibilityNodeInfo returns a list of all visible AccessibilityNodeInfo
     * @return List of all  visible AccessibilityNodeInfo
     */
    public List<AccessibilityNodeInfo> getVisibleNodes();

    public void cleanLists();

    public boolean click(AccessibilityNodeInfo node);

    public boolean scrollForward(AccessibilityNodeInfo node);

    public boolean scrollBackward(AccessibilityNodeInfo node);

    public String getClickable(int index);

    public int clickablesSize();

    public AccessibilityNodeInfo getScrollable(String s);

    public List<String> getClickableNodes();
}
