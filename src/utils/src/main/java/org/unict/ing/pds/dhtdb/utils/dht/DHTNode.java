/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.unict.ing.pds.dhtdb.utils.dht;

import java.util.List;
import org.unict.ing.pds.dhtdb.utils.model.GenericValue;

/**
 *
 */
public interface DHTNode {

     /**
     *
     * @param k |
     * @return |
     */
    public List<GenericValue> get(Key k);

    /**
     *
     * @param elem |
     * @return |
     */
    public Boolean put(GenericValue elem);


}
