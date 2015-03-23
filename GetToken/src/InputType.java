/*
 * Levels Beyond CONFIDENTIAL
 *
 * Copyright 2003 - 2015 Levels Beyond Incorporated
 * All Rights Reserved.
 *
 * NOTICE:  All information contained herein is, and remains
 * the property of Levels Beyond Incorporated and its suppliers,
 * if any.  The intellectual and technical concepts contained
 * herein are proprietary to Levels Beyond Incorporated
 * and its suppliers and may be covered by U.S. and Foreign Patents,
 * patents in process, and are protected by trade secret or copyright law.
 * Dissemination of this information or reproduction of this material
 * is unlawful and strictly forbidden unless prior written permission is obtained
 * from Levels Beyond Incorporated.
 */

import com.sun.corba.se.impl.encoding.OSFCodeSetRegistry;

import java.util.Map;

/**
 * Created by jessenelson on 3/13/15.
 */
public interface InputType {
    Map<String, Integer> valueMap ();
    boolean isType (InputType ip);
    //Map.Entry<String>
}
