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
     * @return String with the description
     */
    public String getChildDescription(AccessibilityNodeInfo node, int index);

    /**
     * Given a AccessibilityEvent returns its event type
     * @param event accessibilityEvent
     * @return the type of accessibility event
     */
    public String getEventType(AccessibilityEvent event);

    /**
     * Search for a node on screen
     * @param description description to search
     * @return Returns a list of all nodes wich description contains the param given
     */
    public List<AccessibilityNodeInfo> searchNode(String description);

    /**
     * Sweeps the tree of visible nodes and fill the three lists: scrollableNodes, clickableNodes and visibleNodes
     * @param node Root of the window
     */
    public void listTree(AccessibilityNodeInfo node);

    /**
     * Sweeps the entire window including the non-visible nodes using listTree
     */
    public void populateLists();

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

    /**
     * Cleans the three lists
     */
    public void cleanLists();

    /**
     * Perform the action click on the node
     * @param node the node to be clicked
     * @return true if the click was sucessfull false if not
     */
    public boolean click(AccessibilityNodeInfo node);

    /**
     * Perform the action scroll forward on the node
     * @param node the node to be scrolled
     * @return true if the scroll was sucessfull false if not
     */
    public boolean scrollForward(AccessibilityNodeInfo node);

    /**
     * Perform the action  scroll backward on the node
     * @param node the node to be scrolled
     * @return true if the scroll was sucessfull false if not
     */
    public boolean scrollBackward(AccessibilityNodeInfo node);

    /**
     * Returns the description of the node on the index at clickableNodes
     * @param index the index of the desired node
     * @return the description of the node
     */
    public String getClickable(int index);

    /**
     * Returns the size of clickableNodes
     * @return the size of clickableNodes
     */
    public int clickablesSize();

    /**
     * Returns the node with the given description
     * @param s description of the node
     * @return the node with given description
     */
    public AccessibilityNodeInfo getScrollable(String s);

    /**
     * Return the list of descriptions of clickableNodes
     * @return the list of descriptions of clickableNodes
     */
    public List<String> getClickableNodes();

    /**
     * Returns a list with all scrollable nodes
     * @return list with all scrollable nodes
     */
    public List<AccessibilityNodeInfo> getAllScrollable();

    /**
     * Compare the nodes n1 and n2
     * @param n1
     * @param n2
     * @return true if they are similar false if not
     */
    public boolean compareNodes(AccessibilityNodeInfo n1, AccessibilityNodeInfo n2);


}
