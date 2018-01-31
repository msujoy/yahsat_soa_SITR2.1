/**
 * Author   : Sujoy Mondal
 * Version  : 
 * Purpose  :
 */
package com.techm.nms.util;

import java.io.Serializable;
import java.util.Random;

/**
 * @author sm0015566
 *
 */
public class CorrelationGenerator implements Serializable {

	
	
	public static byte[] getUUID(){
		int size = 16;
		byte[] bytes = new byte[size];
        new Random().nextBytes(bytes);
        return bytes;
	}

}
