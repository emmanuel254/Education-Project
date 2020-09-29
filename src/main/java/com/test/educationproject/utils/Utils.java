/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.test.educationproject.utils;

import java.util.HashMap;

/**
 *
 * @author litem
 */
public class Utils {

    public static String getStringValue_FromHashMap(HashMap hm, String key) {
        String res = null;
        try {
            if (hm.get(key) != null) {
                res = hm.get(key).toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return res;
        }
    }
    
    public static Integer getInteger_FromHashMap(HashMap hm, String key){
        Integer res = null;
        
        try {
            if(hm.get(key) != null){
                res = Integer.valueOf(hm.get(key).toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return res;
        }
    }
}
