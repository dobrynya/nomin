package org.nomin.core;

import org.nomin.util.*;

/**
 * Contains constants.
 * @author Dmitry Dobrynin
 *         Created: 15.04.2010 21:31:46
 */
public interface MappingConsts {
    String DEFAULT = "default";
    Introspector jb = new JbIntrospector();
    Introspector exploding = new ExplodingIntrospector();
//    Introspector asm = new AsmJbIntrospector();
}
