package com.lostAndFind.project.Utils;

import com.lostAndFind.project.model.entity.Lost;
import com.lostAndFind.project.model.entity.Pick;

/**
 * @author yc_
 * @version 1.0
 */
public class ThingFactory {

    public static Object getThing(String type) {
        if (type.equals("pick")) {
            return createPick();
        }
        if (type.equals("lost")){
            return createLost();
        }
        return null;
    }

    public static Pick createPick() {
        return new Pick();
    }
    public static Lost createLost() {
        return new Lost();
    }
}
