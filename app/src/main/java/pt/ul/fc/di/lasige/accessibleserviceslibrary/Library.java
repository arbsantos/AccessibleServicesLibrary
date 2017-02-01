package pt.ul.fc.di.lasige.accessibleserviceslibrary;

import android.accessibilityservice.AccessibilityService;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Library implements ILibrary {

    private AccessibilityService service;
    private Map<String, AccessibilityNodeInfo> scrollableNodes;
    private List<String> clickableNodes;
    private List<AccessibilityNodeInfo> visibleNodes;
    //private int clickablesSize;

    public Library(AccessibilityService service){
        this.service=service;
        scrollableNodes = new HashMap<>();
        clickableNodes = new ArrayList<>();
        visibleNodes = new ArrayList<>();
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

        if(node!=null && node.getChildCount()>0 && index < node.getChildCount())
           return getDescription(node.getChild(index));

        return null;
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

        populateLists();

        AccessibilityNodeInfo root = service.getRootInActiveWindow();

        while(root==null){
            root = service.getRootInActiveWindow();
        }

        while(root.findAccessibilityNodeInfosByText(description).size()==0){
            if(scrollableNodes.get(root.getPackageName().toString())==null)
                break;
            scrollableNodes.get(root.getPackageName().toString()).performAction(AccessibilityNodeInfo.ACTION_SCROLL_BACKWARD);

        }

        return root.findAccessibilityNodeInfosByText(description);

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

        if(node.isVisibleToUser())
            visibleNodes.add(node);


        for (int i = 0; i < node.getChildCount(); i++) {
            listTree(node.getChild(i));
        }
    }

    @Override
    public void populateLists(){

        AccessibilityNodeInfo root = service.getRootInActiveWindow();

        while(root==null){
            root = service.getRootInActiveWindow();
        }

        //clean variables
        cleanLists();
        //this.index=-1;

        //collect all the nodes on screen
        int timesScrolled = 0;
        int clickablesSize = clickableNodes.size();
        ArrayList<AccessibilityNodeInfo> aux= getAllScrollable();
        listTree(root);


        //reset page to the top Scroll (up/back)
        if(aux.size()>0) {
            while (clickablesSize != clickableNodes.size()) {
                aux.get(0).performAction(AccessibilityNodeInfo.ACTION_SCROLL_BACKWARD);
                timesScrolled++;
                clickablesSize = clickableNodes.size();
                listTree(root);
            }
        }

        //clean variables
        cleanLists();
        //this.index=-1;

        clickablesSize = clickableNodes.size();
        listTree(root);


        //after reset start scroll down
        if(aux.size()>0) {
            while (clickablesSize!=clickableNodes.size()) {
                aux.get(aux.size() - 1).performAction(AccessibilityNodeInfo.ACTION_SCROLL_FORWARD);
                clickablesSize = clickableNodes.size();
                listTree(root);
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
    public List<AccessibilityNodeInfo> getVisibleNodes() {
        return visibleNodes;
    }

    @Override
    public void cleanLists() {
        clickableNodes.clear();
    }

    @Override
    public boolean click(AccessibilityNodeInfo node) {
        if(!node.performAction(AccessibilityNodeInfo.ACTION_CLICK))
            return node.getParent().performAction(AccessibilityNodeInfo.ACTION_CLICK);
        else
            return true;
    }

    @Override
    public boolean scrollForward(AccessibilityNodeInfo node) {
        return node.performAction(AccessibilityNodeInfo.ACTION_SCROLL_FORWARD);
    }

    @Override
    public boolean scrollBackward(AccessibilityNodeInfo node) {
        return node.performAction(AccessibilityNodeInfo.ACTION_SCROLL_BACKWARD);
    }

    @Override
    public String getClickable(int index){
        return clickableNodes.get(index);
    }

    @Override
    public int clickablesSize(){
        return clickableNodes.size();
    }

    @Override
    public AccessibilityNodeInfo getScrollable(String s){
        return scrollableNodes.get(s);
    }

    @Override
    public List<String> getClickableNodes(){
        return clickableNodes;
    }

    @Override
    public ArrayList<AccessibilityNodeInfo> getAllScrollable() {
        return new ArrayList<>(scrollableNodes.values());
    }

    @Override
    public double compareNodes(AccessibilityNodeInfo n1, AccessibilityNodeInfo n2) {

        double similarity = 0.0;
        double total = 10.0;

        if(n1.getText().equals(n2.getText()))
            similarity+=2;
        if(n1.getContentDescription().equals(n2.getContentDescription()))
            similarity+=2;
        if(n1.getPackageName().equals(n2.getPackageName()))
            similarity+=1;
        if(n1.getChildCount()==n2.getChildCount())
            similarity+=1;
        if(n1.getClassName().equals(n2.getClassName()))
            similarity+=1;
        if(n1.isScrollable() && n2.isScrollable())
            similarity+=1;
        if(n1.isClickable() && n2.isClickable())
            similarity+=1;
        if(n1.isFocusable() && n2.isFocusable())
            similarity+=1;

        return similarity/total;
    }
}
