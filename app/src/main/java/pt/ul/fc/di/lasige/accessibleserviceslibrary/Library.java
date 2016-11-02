package pt.ul.fc.di.lasige.accessibleserviceslibrary;

import android.accessibilityservice.AccessibilityService;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Library implements ILibrary {

    private AccessibilityService service;
    private Map<String, AccessibilityNodeInfo> scrollableNodes;
    private List<String> clickableNodes;
    //private int clickablesSize;

    public Library(AccessibilityService service){
        this.service=service;
        scrollableNodes = new HashMap<>();
        clickableNodes = new ArrayList<>();
    }

    @Override
    public String getDescription(AccessibilityNodeInfo node) {

        StringBuilder sb = new StringBuilder();

        if(node!=null){

            if (node.getContentDescription() != null) {
                sb.append(String.format("%s",node.getContentDescription()));
            }else if (node.getText() != null) {
                sb.append(String.format("%s",node.getText()));
            }else {
                return null;
            }

        }else{

            return null;

        }
        return sb.toString();

    }

    @Override
    public String getParentDescription(AccessibilityNodeInfo node) {
        return getDescription(node.getParent());
    }

    @Override
    public String getChildDescription(AccessibilityNodeInfo node, int index) {

        if(node!=null && node.getChildCount()>0)
           return getDescription(node.getChild(index));

        return null;
    }

    @Override
    public String getPackageName(AccessibilityNodeInfo node) {
        return String.format("%s", node.getPackageName());
    }

    @Override
    public String getClassName(AccessibilityNodeInfo node) {
        return String.format("%s", node.getPackageName());
    }

    @Override
    public String getEventType(AccessibilityEvent event) {
        switch (event.getEventType()) {
            case AccessibilityEvent.TYPE_ANNOUNCEMENT:
                return "TYPE_ANNOUNCEMENT";
            case AccessibilityEvent.TYPE_GESTURE_DETECTION_END:
                return "TYPE_GESTURE_DETECTION_END";
            case AccessibilityEvent.TYPE_GESTURE_DETECTION_START:
                return "TYPE_GESTURE_DETECTION_START";
            case AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED:
                return "TYPE_NOTIFICATION_STATE_CHANGED";
            case AccessibilityEvent.TYPE_TOUCH_EXPLORATION_GESTURE_END:
                return "TYPE_TOUCH_EXPLORATION_GESTURE_END";
            case AccessibilityEvent.TYPE_TOUCH_EXPLORATION_GESTURE_START:
                return "TYPE_TOUCH_EXPLORATION_GESTURE_START";
            case AccessibilityEvent.TYPE_TOUCH_INTERACTION_END:
                return "TYPE_TOUCH_INTERACTION_END";
            case AccessibilityEvent.TYPE_TOUCH_INTERACTION_START:
                return "TYPE_TOUCH_INTERACTION_START";
            case AccessibilityEvent.TYPE_VIEW_ACCESSIBILITY_FOCUSED:
                return "TYPE_VIEW_ACCESSIBILITY_FOCUSED";
            case AccessibilityEvent.TYPE_VIEW_ACCESSIBILITY_FOCUS_CLEARED:
                return "TYPE_VIEW_ACCESSIBILITY_FOCUS_CLEARED";
            case AccessibilityEvent.TYPE_VIEW_CLICKED:
                return "TYPE_VIEW_CLICKED";
            case AccessibilityEvent.TYPE_VIEW_FOCUSED:
                return "TYPE_VIEW_FOCUSED";
            case AccessibilityEvent.TYPE_VIEW_HOVER_ENTER:
                return "TYPE_VIEW_HOVER_ENTER";
            case AccessibilityEvent.TYPE_VIEW_HOVER_EXIT:
                return "TYPE_VIEW_HOVER_EXIT";
            case AccessibilityEvent.TYPE_VIEW_LONG_CLICKED:
                return "TYPE_VIEW_LONG_CLICKED";
            case AccessibilityEvent.TYPE_VIEW_SCROLLED:
                return "TYPE_VIEW_SCROLLED";
            case AccessibilityEvent.TYPE_VIEW_SELECTED:
                return "TYPE_VIEW_SELECTED";
            case AccessibilityEvent.TYPE_VIEW_TEXT_CHANGED:
                return "TYPE_VIEW_TEXT_CHANGED";
            case AccessibilityEvent.TYPE_VIEW_TEXT_SELECTION_CHANGED:
                return "TYPE_VIEW_TEXT_SELECTION_CHANGED";
            case AccessibilityEvent.TYPE_VIEW_TEXT_TRAVERSED_AT_MOVEMENT_GRANULARITY:
                return "TYPE_VIEW_TEXT_TRAVERSED_AT_MOVEMENT_GRANULARITY";
            case AccessibilityEvent.TYPE_WINDOWS_CHANGED:
                return "TYPE_WINDOWS_CHANGED";
            case AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED:
                return "TYPE_WINDOW_CONTENT_CHANGED";
            case AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED:
                return "TYPE_WINDOW_STATE_CHANGED";
        }
        return String.format("unknown (%d)", event.getEventType());
    }

    @Override
    public List<AccessibilityNodeInfo> searchNode(String description) {

        populate_lists();

        while(service.getRootInActiveWindow().findAccessibilityNodeInfosByText(description).size()==0){
            if(scrollableNodes.get(service.getRootInActiveWindow().getPackageName().toString())==null)
                break;
            scrollableNodes.get(service.getRootInActiveWindow().getPackageName().toString()).performAction(AccessibilityNodeInfo.ACTION_SCROLL_BACKWARD);
        }

        return service.getRootInActiveWindow().findAccessibilityNodeInfosByText(description);

    }

    @Override
    public void listTree(AccessibilityNodeInfo node) {

        if (node == null ) {
            return;
        }

        if(node.isScrollable()){
            scrollableNodes.put(service.getRootInActiveWindow().getPackageName().toString(), node);
        }

        if(node.isClickable()){

            String s = getDescription(node);
            if (s!=null) {
                if (!clickableNodes.contains(s)) {
                    clickableNodes.add(s);
                }
            } else {
                String s2 = getChildDescription(node, 0);
                if(!clickableNodes.contains(s2)){
                    clickableNodes.add(s2);
                }
            }
        }
        for (int i = 0; i < node.getChildCount(); i++) {
            listTree(node.getChild(i));
        }
    }

    @Override
    public void populate_lists(){

        //clean variables
        cleanLists();
        //this.index=-1;

        //collect all the nodes on screen
        int timesScrolled = 0;
        int clickablesSize = clickableNodes.size();
        listTree(service.getRootInActiveWindow());


        //reset page to the top Scroll (up/back)
        if(scrollableNodes.size()>0) {
            while (clickablesSize != clickableNodes.size()) {
                scrollableNodes.get(0).performAction(AccessibilityNodeInfo.ACTION_SCROLL_BACKWARD);
                timesScrolled++;
                clickablesSize = clickableNodes.size();
                listTree(service.getRootInActiveWindow());
            }
        }

        //clean variables
        cleanLists();
        //this.index=-1;

        clickablesSize = clickableNodes.size();
        listTree(service.getRootInActiveWindow());


        //after reset start scroll down
        if(scrollableNodes.size()>0) {
            while (clickablesSize!=clickableNodes.size()) {
                scrollableNodes.get(scrollableNodes.size() - 1).performAction(AccessibilityNodeInfo.ACTION_SCROLL_FORWARD);
                clickablesSize = clickableNodes.size();
                listTree(service.getRootInActiveWindow());
            }
        }
    }

    @Override
    public void logNodeTree(AccessibilityNodeInfo node, int indent) {

            if (node == null ) {
                return;
            }

            String indentStr = new String(new char[indent * 3]).replace('\0', ' ');
            Log.d("ARVORE" , String.format("%s NODE: %s", indentStr, node.toString()));
            if(node.getContentDescription()!=null || node.getText()!=null) {
                Log.d("ARVORE" , String.format("%s DESCRIPTION: %s", indentStr, node.getContentDescription()));
                Log.d("ARVORE" , String.format("%s TEXT: %s", indentStr, node.getText()));
            }
            else {
                if (node.getParent() != null) {
                    Log.d("ARVORE" , String.format("%s DESCRIPTION: %s", indentStr, node.getParent().getContentDescription()));
                    Log.d("ARVORE" , String.format("%s TEXT: %s", indentStr, node.getParent().getText()));
                }
            }

            for (int i = 0; i < node.getChildCount(); i++) {
                logNodeTree(node.getChild(i), indent + 1);
            }

            node.recycle();

    }

    @Override
    public List<String> getVisibleNodes(AccessibilityNodeInfo node) {
        return null;
    }

    @Override
    public void cleanLists() {
        clickableNodes.clear();
    }


}
