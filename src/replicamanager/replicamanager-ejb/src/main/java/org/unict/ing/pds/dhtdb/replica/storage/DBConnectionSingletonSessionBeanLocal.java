/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.unict.ing.pds.dhtdb.replica.storage;

import com.mongodb.DB;
import javax.ejb.Local;

/**
 *
 * @author aleskandro
 */
@Local
public interface DBConnectionSingletonSessionBeanLocal {
    
    public DB getDatabase();
    
}
