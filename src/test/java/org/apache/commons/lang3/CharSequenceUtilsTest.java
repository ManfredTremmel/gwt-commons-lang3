/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.commons.lang3;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;

import org.junit.Assert;

import org.junit.Test;

/**
 * Tests CharSequenceUtils
 */
public class CharSequenceUtilsTest {

    //-----------------------------------------------------------------------
    @Test
    public void testConstructor() {
        assertNotNull(new CharSequenceUtils());
        final Constructor<?>[] cons = CharSequenceUtils.class.getDeclaredConstructors();
        assertEquals(1, cons.length);
        assertTrue(Modifier.isPublic(cons[0].getModifiers()));
        assertTrue(Modifier.isPublic(CharSequenceUtils.class.getModifiers()));
        assertFalse(Modifier.isFinal(CharSequenceUtils.class.getModifiers()));
    }
    
    //-----------------------------------------------------------------------
    @Test
    public void testSubSequence() {
        //
        // null input
        //
        Assert.assertEquals(null, CharSequenceUtils.subSequence(null, -1));
        Assert.assertEquals(null, CharSequenceUtils.subSequence(null, 0));
        Assert.assertEquals(null, CharSequenceUtils.subSequence(null, 1));
        //
        // non-null input
        //
        Assert.assertEquals(StringUtils.EMPTY, CharSequenceUtils.subSequence(StringUtils.EMPTY, 0));
        Assert.assertEquals("012", CharSequenceUtils.subSequence("012", 0));
        Assert.assertEquals("12", CharSequenceUtils.subSequence("012", 1));
        Assert.assertEquals("2", CharSequenceUtils.subSequence("012", 2));
        Assert.assertEquals(StringUtils.EMPTY, CharSequenceUtils.subSequence("012", 3));
    }

    @Test(expected=IndexOutOfBoundsException.class)
    public void testSubSequenceNegativeStart() {
        Assert.assertEquals(null, CharSequenceUtils.subSequence(StringUtils.EMPTY, -1));
    }

    @Test(expected=IndexOutOfBoundsException.class)
    public void testSubSequenceTooLong() {
        Assert.assertEquals(null, CharSequenceUtils.subSequence(StringUtils.EMPTY, 1));
    }

    static class TestData{
        final String source;
        final boolean ignoreCase;
        final int toffset;
        final String other;
        final int ooffset;
        final int len;
        final boolean expected;
        final Class<?> throwable;
        TestData(String source, boolean ignoreCase, int toffset,
                String other, int ooffset, int len, boolean expected){
            this.source = source;
            this.ignoreCase = ignoreCase;
            this.toffset = toffset;
            this.other = other;
            this.ooffset = ooffset;
            this.len = len;
            this.expected = expected;
            this.throwable = null;
        }
        TestData(String source, boolean ignoreCase, int toffset,
                String other, int ooffset, int len, Class<?> throwable){
            this.source = source;
            this.ignoreCase = ignoreCase;
            this.toffset = toffset;
            this.other = other;
            this.ooffset = ooffset;
            this.len = len;
            this.expected = false;
            this.throwable = throwable;
        }
        public String toString(){
            StringBuilder sb = new StringBuilder();
            sb.append(source).append("[").append(toffset).append("]");
            sb.append(ignoreCase? " caseblind ":" samecase ");
            sb.append(other).append("[").append(ooffset).append("]");
            sb.append(" ").append(len).append(" => ");
            if (throwable != null) {
                sb.append(throwable);
            } else {
                sb.append(expected);
            }
            return sb.toString();
        }
    }

    private static final TestData[] TEST_DATA = {
            //          Source  IgnoreCase Offset Other  Offset Length Result
            new TestData("",    true,      -1,    "",    -1,    -1,    false),
            new TestData("",    true,      0,     "",    0,     1,     false),
            new TestData("a",   true,      0,     "abc", 0,     0,     true),
            new TestData("a",   true,      0,     "abc", 0,     1,     true),
            new TestData("a",   true,      0,     null,  0,     0,     NullPointerException.class),
            new TestData(null,  true,      0,     null,  0,     0,     NullPointerException.class),
            new TestData(null,  true,      0,     "",    0,     0,     NullPointerException.class),
            new TestData("Abc", true,      0,     "abc", 0,     3,     true),
            new TestData("Abc", false,     0,     "abc", 0,     3,     false),
            new TestData("Abc", true,      1,     "abc", 1,     2,     true),
            new TestData("Abc", false,     1,     "abc", 1,     2,     true),
            new TestData("Abcd",true,      1,     "abcD",1,     2,     true),
            new TestData("Abcd",false,     1,     "abcD",1,     2,     true),
    };

    private static abstract class RunTest {
        
        abstract boolean invoke();
        
        void run(TestData data, String id) {
            if (data.throwable != null) {
                try {
                    invoke();
                    Assert.fail(id + " Expected " + data.throwable);
                } catch (Exception e) {
                    if (!e.getClass().equals(data.throwable)) {
                        Assert.fail(id + " Expected " + data.throwable + " got " + e.getClass());
                    }
                }
            } else {
                boolean stringCheck = invoke();
                Assert.assertEquals(id + " Failed test " + data, data.expected, stringCheck);                
            }
        }
        
    }

    @Test
    public void testRegionMatches() {
        for (final TestData data : TEST_DATA) {
            new RunTest() {
                @Override
                boolean invoke() {
                    return data.source.regionMatches(data.ignoreCase, data.toffset, data.other, data.ooffset, data.len);                        
                }
            }.run(data, "String");
            new RunTest() {
                @Override
                boolean invoke() {
                    return CharSequenceUtils.regionMatches(data.source, data.ignoreCase, data.toffset, data.other, data.ooffset, data.len);                        
                }
            }.run(data, "CSString");
            new RunTest() {
                @Override
                boolean invoke() {
                    return CharSequenceUtils.regionMatches(new StringBuilder(data.source), data.ignoreCase, data.toffset, data.other, data.ooffset, data.len);             
                }
            }.run(data, "CSNonString");
        }
    }

}
