package com.ijoomer.caching;

import java.util.ArrayList;

/**
 * This Class Contains Method IjoomerAdvance Database Table List.
 *
 * @author tasol
 */
@SuppressWarnings({"serial", "rawtypes"})
public class IjoomerArrayList extends ArrayList {

    @Override
    public IjoomerTable get(int index) {
        return (IjoomerTable) super.get(index);
    }

    /**
     * This method used to get database table
     *
     * @param tblName represented table name
     * @return {@link IjoomerTable}
     */
    public IjoomerTable get(String tblName) {
        int size = size();
        for (int i = 0; i < size; i++) {
            if (((IjoomerTable) super.get(i)).getTableName().equals(tblName))
                return ((IjoomerTable) super.get(i));
        }
        return null;
    }
}
