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
package org.apache.commons.lang3.builder;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.junit.Test;

/**
 * Unit tests {@link org.apache.commons.lang3.builder.EqualsBuilder}.
 *
 * @version $Id: EqualsBuilderTest.java 1535653 2013-10-25 08:19:29Z bayard $
 */
public class EqualsBuilderTest {

    //-----------------------------------------------------------------------

    static class TestObject {
        private int a;
        public TestObject() {
        }
        public TestObject(final int a) {
            this.a = a;
        }
        @Override
        public boolean equals(final Object o) {
            if (o == null) { return false; }
            if (o == this) { return true; }
            if (o.getClass() != getClass()) {
                return false;
            }

            final TestObject rhs = (TestObject) o;
            return a == rhs.a;
        }

        @Override
        public int hashCode() {
            return a;
        }

        public void setA(final int a) {
            this.a = a;
        }

        public int getA() {
            return a;
        }
    }

    static class TestSubObject extends TestObject {
        private int b;
        public TestSubObject() {
            super(0);
        }
        public TestSubObject(final int a, final int b) {
            super(a);
            this.b = b;
        }
        @Override
        public boolean equals(final Object o) {
            if (o == null) { return false; }
            if (o == this) { return true; }
            if (o.getClass() != getClass()) {
                return false;
            }

            final TestSubObject rhs = (TestSubObject) o;
            return super.equals(o) && b == rhs.b;
        }

        @Override
        public int hashCode() {
            return b *17 + super.hashCode();
        }

        public void setB(final int b) {
            this.b = b;
        }

        public int getB() {
            return b;
        }
    }
    
    static class TestEmptySubObject extends TestObject {
        public TestEmptySubObject(final int a) {
            super(a);
        }
    }

    static class TestTSubObject extends TestObject {
        @SuppressWarnings("unused")
        private transient int t;
        public TestTSubObject(final int a, final int t) {
            super(a);
            this.t = t;
        }
    }

    static class TestTTSubObject extends TestTSubObject {
        @SuppressWarnings("unused")
        private transient int tt;
        public TestTTSubObject(final int a, final int t, final int tt) {
            super(a, t);
            this.tt = tt;
        }
    }

    static class TestTTLeafObject extends TestTTSubObject {
        @SuppressWarnings("unused")
        private final int leafValue;
        public TestTTLeafObject(final int a, final int t, final int tt, final int leafValue) {
            super(a, t, tt);
            this.leafValue = leafValue;
        }
    }

    static class TestTSubObject2 extends TestObject {
        private transient int t;
        public TestTSubObject2(final int a, final int t) {
            super(a);
        }
        public int getT() {
            return t;
        }
        public void setT(final int t) {
            this.t = t;
        }
    }

    @Test
    public void testReflectionEquals() {
        final TestObject o1 = new TestObject(4);
        final TestObject o2 = new TestObject(5);
        assertTrue(EqualsBuilder.reflectionEquals(o1, o1));
        assertTrue(!EqualsBuilder.reflectionEquals(o1, o2));
        o2.setA(4);
        assertTrue(EqualsBuilder.reflectionEquals(o1, o2));

        assertTrue(!EqualsBuilder.reflectionEquals(o1, this));

        assertTrue(!EqualsBuilder.reflectionEquals(o1, null));
        assertTrue(!EqualsBuilder.reflectionEquals(null, o2));
        assertTrue(EqualsBuilder.reflectionEquals((Object) null, (Object) null));
    }
    
    @Test
    public void testReflectionHierarchyEquals() {
        testReflectionHierarchyEquals(false);
        testReflectionHierarchyEquals(true);
        // Transients
        assertTrue(EqualsBuilder.reflectionEquals(new TestTTLeafObject(1, 2, 3, 4), new TestTTLeafObject(1, 2, 3, 4), true));
        assertTrue(EqualsBuilder.reflectionEquals(new TestTTLeafObject(1, 2, 3, 4), new TestTTLeafObject(1, 2, 3, 4), false));
        assertTrue(!EqualsBuilder.reflectionEquals(new TestTTLeafObject(1, 0, 0, 4), new TestTTLeafObject(1, 2, 3, 4), true));
        assertTrue(!EqualsBuilder.reflectionEquals(new TestTTLeafObject(1, 2, 3, 4), new TestTTLeafObject(1, 2, 3, 0), true));
        assertTrue(!EqualsBuilder.reflectionEquals(new TestTTLeafObject(0, 2, 3, 4), new TestTTLeafObject(1, 2, 3, 4), true));
    }

    private void testReflectionHierarchyEquals(final boolean testTransients) {
        final TestObject to1 = new TestObject(4);
        final TestObject to1Bis = new TestObject(4);
        final TestObject to1Ter = new TestObject(4);
        final TestObject to2 = new TestObject(5);
        final TestEmptySubObject teso = new TestEmptySubObject(4);
        final TestTSubObject ttso = new TestTSubObject(4, 1);
        final TestTTSubObject tttso = new TestTTSubObject(4, 1, 2);
        final TestTTLeafObject ttlo = new TestTTLeafObject(4, 1, 2, 3);
        final TestSubObject tso1 = new TestSubObject(1, 4);
        final TestSubObject tso1bis = new TestSubObject(1, 4);
        final TestSubObject tso1ter = new TestSubObject(1, 4);
        final TestSubObject tso2 = new TestSubObject(2, 5);

        testReflectionEqualsEquivalenceRelationship(to1, to1Bis, to1Ter, to2, new TestObject(), testTransients);
        testReflectionEqualsEquivalenceRelationship(tso1, tso1bis, tso1ter, tso2, new TestSubObject(), testTransients);

        // More sanity checks:

        // same values
        assertTrue(EqualsBuilder.reflectionEquals(ttlo, ttlo, testTransients));
        assertTrue(EqualsBuilder.reflectionEquals(new TestSubObject(1, 10), new TestSubObject(1, 10), testTransients));
        // same super values, diff sub values
        assertTrue(!EqualsBuilder.reflectionEquals(new TestSubObject(1, 10), new TestSubObject(1, 11), testTransients));
        assertTrue(!EqualsBuilder.reflectionEquals(new TestSubObject(1, 11), new TestSubObject(1, 10), testTransients));
        // diff super values, same sub values
        assertTrue(!EqualsBuilder.reflectionEquals(new TestSubObject(0, 10), new TestSubObject(1, 10), testTransients));
        assertTrue(!EqualsBuilder.reflectionEquals(new TestSubObject(1, 10), new TestSubObject(0, 10), testTransients));

        // mix super and sub types: equals
        assertTrue(EqualsBuilder.reflectionEquals(to1, teso, testTransients));
        assertTrue(EqualsBuilder.reflectionEquals(teso, to1, testTransients));

        assertTrue(EqualsBuilder.reflectionEquals(to1, ttso, false)); // Force testTransients = false for this assert
        assertTrue(EqualsBuilder.reflectionEquals(ttso, to1, false)); // Force testTransients = false for this assert

        assertTrue(EqualsBuilder.reflectionEquals(to1, tttso, false)); // Force testTransients = false for this assert
        assertTrue(EqualsBuilder.reflectionEquals(tttso, to1, false)); // Force testTransients = false for this assert

        assertTrue(EqualsBuilder.reflectionEquals(ttso, tttso, false)); // Force testTransients = false for this assert
        assertTrue(EqualsBuilder.reflectionEquals(tttso, ttso, false)); // Force testTransients = false for this assert

        // mix super and sub types: NOT equals
        assertTrue(!EqualsBuilder.reflectionEquals(new TestObject(0), new TestEmptySubObject(1), testTransients));
        assertTrue(!EqualsBuilder.reflectionEquals(new TestEmptySubObject(1), new TestObject(0), testTransients));

        assertTrue(!EqualsBuilder.reflectionEquals(new TestObject(0), new TestTSubObject(1, 1), testTransients));
        assertTrue(!EqualsBuilder.reflectionEquals(new TestTSubObject(1, 1), new TestObject(0), testTransients));

        assertTrue(!EqualsBuilder.reflectionEquals(new TestObject(1), new TestSubObject(0, 10), testTransients));
        assertTrue(!EqualsBuilder.reflectionEquals(new TestSubObject(0, 10), new TestObject(1), testTransients));

        assertTrue(!EqualsBuilder.reflectionEquals(to1, ttlo));
        assertTrue(!EqualsBuilder.reflectionEquals(tso1, this));
    }

    /**
     * Equivalence relationship tests inspired by "Effective Java":
     * <ul>
     * <li>reflection</li>
     * <li>symmetry</li>
     * <li>transitive</li>
     * <li>consistency</li>
     * <li>non-null reference</li>
     * </ul>
     * @param to a TestObject
     * @param toBis a TestObject, equal to to and toTer
     * @param toTer Left hand side, equal to to and toBis
     * @param to2 a different TestObject
     * @param oToChange a TestObject that will be changed
     * @param testTransients whether to test transient instance variables 
     */
    private void testReflectionEqualsEquivalenceRelationship(
        final TestObject to,
        final TestObject toBis,
        final TestObject toTer,
        final TestObject to2,
        final TestObject oToChange,
        final boolean testTransients) {

        // reflection test
        assertTrue(EqualsBuilder.reflectionEquals(to, to, testTransients));
        assertTrue(EqualsBuilder.reflectionEquals(to2, to2, testTransients));

        // symmetry test
        assertTrue(EqualsBuilder.reflectionEquals(to, toBis, testTransients) && EqualsBuilder.reflectionEquals(toBis, to, testTransients));

        // transitive test
        assertTrue(
            EqualsBuilder.reflectionEquals(to, toBis, testTransients)
                && EqualsBuilder.reflectionEquals(toBis, toTer, testTransients)
                && EqualsBuilder.reflectionEquals(to, toTer, testTransients));

        // consistency test
        oToChange.setA(to.getA());
        if (oToChange instanceof TestSubObject) {
            ((TestSubObject) oToChange).setB(((TestSubObject) to).getB());
        }
        assertTrue(EqualsBuilder.reflectionEquals(oToChange, to, testTransients));
        assertTrue(EqualsBuilder.reflectionEquals(oToChange, to, testTransients));
        oToChange.setA(to.getA() + 1);
        if (oToChange instanceof TestSubObject) {
            ((TestSubObject) oToChange).setB(((TestSubObject) to).getB() + 1);
        }
        assertTrue(!EqualsBuilder.reflectionEquals(oToChange, to, testTransients));
        assertTrue(!EqualsBuilder.reflectionEquals(oToChange, to, testTransients));

        // non-null reference test
        assertTrue(!EqualsBuilder.reflectionEquals(to, null, testTransients));
        assertTrue(!EqualsBuilder.reflectionEquals(to2, null, testTransients));
        assertTrue(!EqualsBuilder.reflectionEquals(null, to, testTransients));
        assertTrue(!EqualsBuilder.reflectionEquals(null, to2, testTransients));
        assertTrue(EqualsBuilder.reflectionEquals((Object) null, (Object) null, testTransients));
    }

    @Test
    public void testSuper() {
        final TestObject o1 = new TestObject(4);
        final TestObject o2 = new TestObject(5);
        assertTrue(new EqualsBuilder().appendSuper(true).append(o1, o1).isEquals());
        assertFalse(new EqualsBuilder().appendSuper(false).append(o1, o1).isEquals());
        assertFalse(new EqualsBuilder().appendSuper(true).append(o1, o2).isEquals());
        assertFalse(new EqualsBuilder().appendSuper(false).append(o1, o2).isEquals());
    }

    @Test
    public void testObject() {
        final TestObject o1 = new TestObject(4);
        final TestObject o2 = new TestObject(5);
        assertTrue(new EqualsBuilder().append(o1, o1).isEquals());
        assertTrue(!new EqualsBuilder().append(o1, o2).isEquals());
        o2.setA(4);
        assertTrue(new EqualsBuilder().append(o1, o2).isEquals());

        assertTrue(!new EqualsBuilder().append(o1, this).isEquals());
        
        assertTrue(!new EqualsBuilder().append(o1, null).isEquals());
        assertTrue(!new EqualsBuilder().append(null, o2).isEquals());
        assertTrue(new EqualsBuilder().append((Object) null, (Object) null).isEquals());
    }
    
    @Test
    public void testObjectBuild() {
        final TestObject o1 = new TestObject(4);
        final TestObject o2 = new TestObject(5);
        assertEquals(Boolean.TRUE, new EqualsBuilder().append(o1, o1).build());
        assertEquals(Boolean.FALSE, new EqualsBuilder().append(o1, o2).build());
        o2.setA(4);
        assertEquals(Boolean.TRUE, new EqualsBuilder().append(o1, o2).build());

        assertEquals(Boolean.FALSE, new EqualsBuilder().append(o1, this).build());
        
        assertEquals(Boolean.FALSE, new EqualsBuilder().append(o1, null).build());
        assertEquals(Boolean.FALSE, new EqualsBuilder().append(null, o2).build());
        assertEquals(Boolean.TRUE, new EqualsBuilder().append((Object) null, (Object) null).build());
    }

    @Test
    public void testLong() {
        final long o1 = 1L;
        final long o2 = 2L;
        assertTrue(new EqualsBuilder().append(o1, o1).isEquals());
        assertTrue(!new EqualsBuilder().append(o1, o2).isEquals());
    }

    @Test
    public void testInt() {
        final int o1 = 1;
        final int o2 = 2;
        assertTrue(new EqualsBuilder().append(o1, o1).isEquals());
        assertTrue(!new EqualsBuilder().append(o1, o2).isEquals());
    }

    @Test
    public void testShort() {
        final short o1 = 1;
        final short o2 = 2;
        assertTrue(new EqualsBuilder().append(o1, o1).isEquals());
        assertTrue(!new EqualsBuilder().append(o1, o2).isEquals());
    }

    @Test
    public void testChar() {
        final char o1 = 1;
        final char o2 = 2;
        assertTrue(new EqualsBuilder().append(o1, o1).isEquals());
        assertTrue(!new EqualsBuilder().append(o1, o2).isEquals());
    }

    @Test
    public void testByte() {
        final byte o1 = 1;
        final byte o2 = 2;
        assertTrue(new EqualsBuilder().append(o1, o1).isEquals());
        assertTrue(!new EqualsBuilder().append(o1, o2).isEquals());
    }

    @Test
    public void testDouble() {
        final double o1 = 1;
        final double o2 = 2;
        assertTrue(new EqualsBuilder().append(o1, o1).isEquals());
        assertTrue(!new EqualsBuilder().append(o1, o2).isEquals());
        assertTrue(!new EqualsBuilder().append(o1, Double.NaN).isEquals());
        assertTrue(new EqualsBuilder().append(Double.NaN, Double.NaN).isEquals());
        assertTrue(new EqualsBuilder().append(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY).isEquals());
    }

    @Test
    public void testFloat() {
        final float o1 = 1;
        final float o2 = 2;
        assertTrue(new EqualsBuilder().append(o1, o1).isEquals());
        assertTrue(!new EqualsBuilder().append(o1, o2).isEquals());
        assertTrue(!new EqualsBuilder().append(o1, Float.NaN).isEquals());
        assertTrue(new EqualsBuilder().append(Float.NaN, Float.NaN).isEquals());
        assertTrue(new EqualsBuilder().append(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY).isEquals());
    }

    @Test
    public void testAccessors() {
        final EqualsBuilder equalsBuilder = new EqualsBuilder();
        assertTrue(equalsBuilder.isEquals());
        equalsBuilder.setEquals(true);
        assertTrue(equalsBuilder.isEquals());
        equalsBuilder.setEquals(false);
        assertFalse(equalsBuilder.isEquals());
    }

    @Test
    public void testReset() {
        final EqualsBuilder equalsBuilder = new EqualsBuilder();
        assertTrue(equalsBuilder.isEquals());
        equalsBuilder.setEquals(false);
        assertFalse(equalsBuilder.isEquals());
        equalsBuilder.reset();
        assertTrue(equalsBuilder.isEquals());
    }
    
    @Test
    public void testBoolean() {
        final boolean o1 = true;
        final boolean o2 = false;
        assertTrue(new EqualsBuilder().append(o1, o1).isEquals());
        assertTrue(!new EqualsBuilder().append(o1, o2).isEquals());
    }

    @Test
    public void testObjectArray() {
        TestObject[] obj1 = new TestObject[3];
        obj1[0] = new TestObject(4);
        obj1[1] = new TestObject(5);
        obj1[2] = null;
        TestObject[] obj2 = new TestObject[3];
        obj2[0] = new TestObject(4);
        obj2[1] = new TestObject(5);
        obj2[2] = null;
        
        assertTrue(new EqualsBuilder().append(obj1, obj1).isEquals());
        assertTrue(new EqualsBuilder().append(obj2, obj2).isEquals());
        assertTrue(new EqualsBuilder().append(obj1, obj2).isEquals());
        obj1[1].setA(6);
        assertTrue(!new EqualsBuilder().append(obj1, obj2).isEquals());
        obj1[1].setA(5);
        assertTrue(new EqualsBuilder().append(obj1, obj2).isEquals());
        obj1[2] = obj1[1];
        assertTrue(!new EqualsBuilder().append(obj1, obj2).isEquals());
        obj1[2] = null;
        assertTrue(new EqualsBuilder().append(obj1, obj2).isEquals());
                       
        obj2 = null;
        assertTrue(!new EqualsBuilder().append(obj1, obj2).isEquals());
        obj1 = null;
        assertTrue(new EqualsBuilder().append(obj1, obj2).isEquals());
    }

    @Test
    public void testLongArray() {
        long[] obj1 = new long[2];
        obj1[0] = 5L;
        obj1[1] = 6L;
        long[] obj2 = new long[2];
        obj2[0] = 5L;
        obj2[1] = 6L;
        assertTrue(new EqualsBuilder().append(obj1, obj1).isEquals());
        assertTrue(new EqualsBuilder().append(obj1, obj2).isEquals());
        obj1[1] = 7;
        assertTrue(!new EqualsBuilder().append(obj1, obj2).isEquals());

        obj2 = null;
        assertTrue(!new EqualsBuilder().append(obj1, obj2).isEquals());
        obj1 = null;
        assertTrue(new EqualsBuilder().append(obj1, obj2).isEquals());
    }

    @Test
    public void testIntArray() {
        int[] obj1 = new int[2];
        obj1[0] = 5;
        obj1[1] = 6;
        int[] obj2 = new int[2];
        obj2[0] = 5;
        obj2[1] = 6;
        assertTrue(new EqualsBuilder().append(obj1, obj1).isEquals());
        assertTrue(new EqualsBuilder().append(obj1, obj2).isEquals());
        obj1[1] = 7;
        assertTrue(!new EqualsBuilder().append(obj1, obj2).isEquals());

        obj2 = null;
        assertTrue(!new EqualsBuilder().append(obj1, obj2).isEquals());
        obj1 = null;
        assertTrue(new EqualsBuilder().append(obj1, obj2).isEquals());
    }

    @Test
    public void testShortArray() {
        short[] obj1 = new short[2];
        obj1[0] = 5;
        obj1[1] = 6;
        short[] obj2 = new short[2];
        obj2[0] = 5;
        obj2[1] = 6;
        assertTrue(new EqualsBuilder().append(obj1, obj1).isEquals());
        assertTrue(new EqualsBuilder().append(obj1, obj2).isEquals());
        obj1[1] = 7;
        assertTrue(!new EqualsBuilder().append(obj1, obj2).isEquals());

        obj2 = null;
        assertTrue(!new EqualsBuilder().append(obj1, obj2).isEquals());
        obj1 = null;
        assertTrue(new EqualsBuilder().append(obj1, obj2).isEquals());
    }

    @Test
    public void testCharArray() {
        char[] obj1 = new char[2];
        obj1[0] = 5;
        obj1[1] = 6;
        char[] obj2 = new char[2];
        obj2[0] = 5;
        obj2[1] = 6;
        assertTrue(new EqualsBuilder().append(obj1, obj1).isEquals());
        assertTrue(new EqualsBuilder().append(obj1, obj2).isEquals());
        obj1[1] = 7;
        assertTrue(!new EqualsBuilder().append(obj1, obj2).isEquals());

        obj2 = null;
        assertTrue(!new EqualsBuilder().append(obj1, obj2).isEquals());
        obj1 = null;
        assertTrue(new EqualsBuilder().append(obj1, obj2).isEquals());
    }

    @Test
    public void testByteArray() {
        byte[] obj1 = new byte[2];
        obj1[0] = 5;
        obj1[1] = 6;
        byte[] obj2 = new byte[2];
        obj2[0] = 5;
        obj2[1] = 6;
        assertTrue(new EqualsBuilder().append(obj1, obj1).isEquals());
        assertTrue(new EqualsBuilder().append(obj1, obj2).isEquals());
        obj1[1] = 7;
        assertTrue(!new EqualsBuilder().append(obj1, obj2).isEquals());

        obj2 = null;
        assertTrue(!new EqualsBuilder().append(obj1, obj2).isEquals());
        obj1 = null;
        assertTrue(new EqualsBuilder().append(obj1, obj2).isEquals());
    }

    @Test
    public void testDoubleArray() {
        double[] obj1 = new double[2];
        obj1[0] = 5;
        obj1[1] = 6;
        double[] obj2 = new double[2];
        obj2[0] = 5;
        obj2[1] = 6;
        assertTrue(new EqualsBuilder().append(obj1, obj1).isEquals());
        assertTrue(new EqualsBuilder().append(obj1, obj2).isEquals());
        obj1[1] = 7;
        assertTrue(!new EqualsBuilder().append(obj1, obj2).isEquals());

        obj2 = null;
        assertTrue(!new EqualsBuilder().append(obj1, obj2).isEquals());
        obj1 = null;
        assertTrue(new EqualsBuilder().append(obj1, obj2).isEquals());
    }

    @Test
    public void testFloatArray() {
        float[] obj1 = new float[2];
        obj1[0] = 5;
        obj1[1] = 6;
        float[] obj2 = new float[2];
        obj2[0] = 5;
        obj2[1] = 6;
        assertTrue(new EqualsBuilder().append(obj1, obj1).isEquals());
        assertTrue(new EqualsBuilder().append(obj1, obj2).isEquals());
        obj1[1] = 7;
        assertTrue(!new EqualsBuilder().append(obj1, obj2).isEquals());

        obj2 = null;
        assertTrue(!new EqualsBuilder().append(obj1, obj2).isEquals());
        obj1 = null;
        assertTrue(new EqualsBuilder().append(obj1, obj2).isEquals());
    }

    @Test
    public void testBooleanArray() {
        boolean[] obj1 = new boolean[2];
        obj1[0] = true;
        obj1[1] = false;
        boolean[] obj2 = new boolean[2];
        obj2[0] = true;
        obj2[1] = false;
        assertTrue(new EqualsBuilder().append(obj1, obj1).isEquals());
        assertTrue(new EqualsBuilder().append(obj1, obj2).isEquals());
        obj1[1] = true;
        assertTrue(!new EqualsBuilder().append(obj1, obj2).isEquals());

        obj2 = null;
        assertTrue(!new EqualsBuilder().append(obj1, obj2).isEquals());
        obj1 = null;
        assertTrue(new EqualsBuilder().append(obj1, obj2).isEquals());
    }

    @Test
    public void testMultiLongArray() {
        final long[][] array1 = new long[2][2];
        final long[][] array2 = new long[2][2];
        for (int i = 0; i < array1.length; ++i) {
            for (int j = 0; j < array1[0].length; j++) {
                array1[i][j] = (i + 1) * (j + 1);
                array2[i][j] = (i + 1) * (j + 1);
            }
        }
        assertTrue(new EqualsBuilder().append(array1, array1).isEquals());
        assertTrue(new EqualsBuilder().append(array1, array2).isEquals());
        array1[1][1] = 0;
        assertTrue(!new EqualsBuilder().append(array1, array2).isEquals());
    }

    @Test
    public void testMultiIntArray() {
        final int[][] array1 = new int[2][2];
        final int[][] array2 = new int[2][2];
        for (int i = 0; i < array1.length; ++i) {
            for (int j = 0; j < array1[0].length; j++) {
                array1[i][j] = (i + 1) * (j + 1);
                array2[i][j] = (i + 1) * (j + 1);
            }
        }
        assertTrue(new EqualsBuilder().append(array1, array1).isEquals());
        assertTrue(new EqualsBuilder().append(array1, array2).isEquals());
        array1[1][1] = 0;
        assertTrue(!new EqualsBuilder().append(array1, array2).isEquals());
    }

    @Test
    public void testMultiShortArray() {
        final short[][] array1 = new short[2][2];
        final short[][] array2 = new short[2][2];
        for (short i = 0; i < array1.length; ++i) {
            for (short j = 0; j < array1[0].length; j++) {
                array1[i][j] = i;
                array2[i][j] = i;
            }
        }
        assertTrue(new EqualsBuilder().append(array1, array1).isEquals());
        assertTrue(new EqualsBuilder().append(array1, array2).isEquals());
        array1[1][1] = 0;
        assertTrue(!new EqualsBuilder().append(array1, array2).isEquals());
    }

    @Test
    public void testMultiCharArray() {
        final char[][] array1 = new char[2][2];
        final char[][] array2 = new char[2][2];
        for (char i = 0; i < array1.length; ++i) {
            for (char j = 0; j < array1[0].length; j++) {
                array1[i][j] = i;
                array2[i][j] = i;
            }
        }
        assertTrue(new EqualsBuilder().append(array1, array1).isEquals());
        assertTrue(new EqualsBuilder().append(array1, array2).isEquals());
        array1[1][1] = 0;
        assertTrue(!new EqualsBuilder().append(array1, array2).isEquals());
    }

    @Test
    public void testMultiByteArray() {
        final byte[][] array1 = new byte[2][2];
        final byte[][] array2 = new byte[2][2];
        for (byte i = 0; i < array1.length; ++i) {
            for (byte j = 0; j < array1[0].length; j++) {
                array1[i][j] = i;
                array2[i][j] = i;
            }
        }
        assertTrue(new EqualsBuilder().append(array1, array1).isEquals());
        assertTrue(new EqualsBuilder().append(array1, array2).isEquals());
        array1[1][1] = 0;
        assertTrue(!new EqualsBuilder().append(array1, array2).isEquals());
    }

    @Test
    public void testMultiFloatArray() {
        final float[][] array1 = new float[2][2];
        final float[][] array2 = new float[2][2];
        for (int i = 0; i < array1.length; ++i) {
            for (int j = 0; j < array1[0].length; j++) {
                array1[i][j] = (i + 1) * (j + 1);
                array2[i][j] = (i + 1) * (j + 1);
            }
        }
        assertTrue(new EqualsBuilder().append(array1, array1).isEquals());
        assertTrue(new EqualsBuilder().append(array1, array2).isEquals());
        array1[1][1] = 0;
        assertTrue(!new EqualsBuilder().append(array1, array2).isEquals());
    }

    @Test
    public void testMultiDoubleArray() {
        final double[][] array1 = new double[2][2];
        final double[][] array2 = new double[2][2];
        for (int i = 0; i < array1.length; ++i) {
            for (int j = 0; j < array1[0].length; j++) {
                array1[i][j] = (i + 1) * (j + 1);
                array2[i][j] = (i + 1) * (j + 1);
            }
        }
        assertTrue(new EqualsBuilder().append(array1, array1).isEquals());
        assertTrue(new EqualsBuilder().append(array1, array2).isEquals());
        array1[1][1] = 0;
        assertTrue(!new EqualsBuilder().append(array1, array2).isEquals());
    }

    @Test
    public void testMultiBooleanArray() {
        final boolean[][] array1 = new boolean[2][2];
        final boolean[][] array2 = new boolean[2][2];
        for (int i = 0; i < array1.length; ++i) {
            for (int j = 0; j < array1[0].length; j++) {
                array1[i][j] = i == 1 || j == 1;
                array2[i][j] = i == 1 || j == 1;
            }
        }
        assertTrue(new EqualsBuilder().append(array1, array1).isEquals());
        assertTrue(new EqualsBuilder().append(array1, array2).isEquals());
        array1[1][1] = false;
        assertTrue(!new EqualsBuilder().append(array1, array2).isEquals());
        
        // compare 1 dim to 2.
        final boolean[] array3 = new boolean[]{true, true};
        assertFalse(new EqualsBuilder().append(array1, array3).isEquals());
        assertFalse(new EqualsBuilder().append(array3, array1).isEquals());
        assertFalse(new EqualsBuilder().append(array2, array3).isEquals());
        assertFalse(new EqualsBuilder().append(array3, array2).isEquals());
    }

    @Test
    public void testRaggedArray() {
        final long array1[][] = new long[2][];
        final long array2[][] = new long[2][];
        for (int i = 0; i < array1.length; ++i) {
            array1[i] = new long[2];
            array2[i] = new long[2];
            for (int j = 0; j < array1[i].length; ++j) {
                array1[i][j] = (i + 1) * (j + 1);
                array2[i][j] = (i + 1) * (j + 1);
            }
        }
        assertTrue(new EqualsBuilder().append(array1, array1).isEquals());
        assertTrue(new EqualsBuilder().append(array1, array2).isEquals());
        array1[1][1] = 0;
        assertTrue(!new EqualsBuilder().append(array1, array2).isEquals());
    }

    @Test
    public void testMixedArray() {
        final Object array1[] = new Object[2];
        final Object array2[] = new Object[2];
        for (int i = 0; i < array1.length; ++i) {
            array1[i] = new long[2];
            array2[i] = new long[2];
            for (int j = 0; j < 2; ++j) {
                ((long[]) array1[i])[j] = (i + 1) * (j + 1);
                ((long[]) array2[i])[j] = (i + 1) * (j + 1);
            }
        }
        assertTrue(new EqualsBuilder().append(array1, array1).isEquals());
        assertTrue(new EqualsBuilder().append(array1, array2).isEquals());
        ((long[]) array1[1])[1] = 0;
        assertTrue(!new EqualsBuilder().append(array1, array2).isEquals());
    }

    @Test
    public void testObjectArrayHiddenByObject() {
        final TestObject[] array1 = new TestObject[2];
        array1[0] = new TestObject(4);
        array1[1] = new TestObject(5);
        final TestObject[] array2 = new TestObject[2];
        array2[0] = new TestObject(4);
        array2[1] = new TestObject(5);
        final Object obj1 = array1;
        final Object obj2 = array2;
        assertTrue(new EqualsBuilder().append(obj1, obj1).isEquals());
        assertTrue(new EqualsBuilder().append(obj1, array1).isEquals());
        assertTrue(new EqualsBuilder().append(obj1, obj2).isEquals());
        assertTrue(new EqualsBuilder().append(obj1, array2).isEquals());
        array1[1].setA(6);
        assertTrue(!new EqualsBuilder().append(obj1, obj2).isEquals());
    }

    @Test
    public void testLongArrayHiddenByObject() {
        final long[] array1 = new long[2];
        array1[0] = 5L;
        array1[1] = 6L;
        final long[] array2 = new long[2];
        array2[0] = 5L;
        array2[1] = 6L;
        final Object obj1 = array1;
        final Object obj2 = array2;
        assertTrue(new EqualsBuilder().append(obj1, obj1).isEquals());
        assertTrue(new EqualsBuilder().append(obj1, array1).isEquals());
        assertTrue(new EqualsBuilder().append(obj1, obj2).isEquals());
        assertTrue(new EqualsBuilder().append(obj1, array2).isEquals());
        array1[1] = 7;
        assertTrue(!new EqualsBuilder().append(obj1, obj2).isEquals());
    }

    @Test
    public void testIntArrayHiddenByObject() {
        final int[] array1 = new int[2];
        array1[0] = 5;
        array1[1] = 6;
        final int[] array2 = new int[2];
        array2[0] = 5;
        array2[1] = 6;
        final Object obj1 = array1;
        final Object obj2 = array2;
        assertTrue(new EqualsBuilder().append(obj1, obj1).isEquals());
        assertTrue(new EqualsBuilder().append(obj1, array1).isEquals());
        assertTrue(new EqualsBuilder().append(obj1, obj2).isEquals());
        assertTrue(new EqualsBuilder().append(obj1, array2).isEquals());
        array1[1] = 7;
        assertTrue(!new EqualsBuilder().append(obj1, obj2).isEquals());
    }

    @Test
    public void testShortArrayHiddenByObject() {
        final short[] array1 = new short[2];
        array1[0] = 5;
        array1[1] = 6;
        final short[] array2 = new short[2];
        array2[0] = 5;
        array2[1] = 6;
        final Object obj1 = array1;
        final Object obj2 = array2;
        assertTrue(new EqualsBuilder().append(obj1, obj1).isEquals());
        assertTrue(new EqualsBuilder().append(obj1, array1).isEquals());
        assertTrue(new EqualsBuilder().append(obj1, obj2).isEquals());
        assertTrue(new EqualsBuilder().append(obj1, array2).isEquals());
        array1[1] = 7;
        assertTrue(!new EqualsBuilder().append(obj1, obj2).isEquals());
    }

    @Test
    public void testCharArrayHiddenByObject() {
        final char[] array1 = new char[2];
        array1[0] = 5;
        array1[1] = 6;
        final char[] array2 = new char[2];
        array2[0] = 5;
        array2[1] = 6;
        final Object obj1 = array1;
        final Object obj2 = array2;
        assertTrue(new EqualsBuilder().append(obj1, obj1).isEquals());
        assertTrue(new EqualsBuilder().append(obj1, array1).isEquals());
        assertTrue(new EqualsBuilder().append(obj1, obj2).isEquals());
        assertTrue(new EqualsBuilder().append(obj1, array2).isEquals());
        array1[1] = 7;
        assertTrue(!new EqualsBuilder().append(obj1, obj2).isEquals());
    }

    @Test
    public void testByteArrayHiddenByObject() {
        final byte[] array1 = new byte[2];
        array1[0] = 5;
        array1[1] = 6;
        final byte[] array2 = new byte[2];
        array2[0] = 5;
        array2[1] = 6;
        final Object obj1 = array1;
        final Object obj2 = array2;
        assertTrue(new EqualsBuilder().append(obj1, obj1).isEquals());
        assertTrue(new EqualsBuilder().append(obj1, array1).isEquals());
        assertTrue(new EqualsBuilder().append(obj1, obj2).isEquals());
        assertTrue(new EqualsBuilder().append(obj1, array2).isEquals());
        array1[1] = 7;
        assertTrue(!new EqualsBuilder().append(obj1, obj2).isEquals());
    }

    @Test
    public void testDoubleArrayHiddenByObject() {
        final double[] array1 = new double[2];
        array1[0] = 5;
        array1[1] = 6;
        final double[] array2 = new double[2];
        array2[0] = 5;
        array2[1] = 6;
        final Object obj1 = array1;
        final Object obj2 = array2;
        assertTrue(new EqualsBuilder().append(obj1, obj1).isEquals());
        assertTrue(new EqualsBuilder().append(obj1, array1).isEquals());
        assertTrue(new EqualsBuilder().append(obj1, obj2).isEquals());
        assertTrue(new EqualsBuilder().append(obj1, array2).isEquals());
        array1[1] = 7;
        assertTrue(!new EqualsBuilder().append(obj1, obj2).isEquals());
    }

    @Test
    public void testFloatArrayHiddenByObject() {
        final float[] array1 = new float[2];
        array1[0] = 5;
        array1[1] = 6;
        final float[] array2 = new float[2];
        array2[0] = 5;
        array2[1] = 6;
        final Object obj1 = array1;
        final Object obj2 = array2;
        assertTrue(new EqualsBuilder().append(obj1, obj1).isEquals());
        assertTrue(new EqualsBuilder().append(obj1, array1).isEquals());
        assertTrue(new EqualsBuilder().append(obj1, obj2).isEquals());
        assertTrue(new EqualsBuilder().append(obj1, array2).isEquals());
        array1[1] = 7;
        assertTrue(!new EqualsBuilder().append(obj1, obj2).isEquals());
    }

    @Test
    public void testBooleanArrayHiddenByObject() {
        final boolean[] array1 = new boolean[2];
        array1[0] = true;
        array1[1] = false;
        final boolean[] array2 = new boolean[2];
        array2[0] = true;
        array2[1] = false;
        final Object obj1 = array1;
        final Object obj2 = array2;
        assertTrue(new EqualsBuilder().append(obj1, obj1).isEquals());
        assertTrue(new EqualsBuilder().append(obj1, array1).isEquals());
        assertTrue(new EqualsBuilder().append(obj1, obj2).isEquals());
        assertTrue(new EqualsBuilder().append(obj1, array2).isEquals());
        array1[1] = true;
        assertTrue(!new EqualsBuilder().append(obj1, obj2).isEquals());
    }
    
    public static class TestACanEqualB {
        private final int a;

        public TestACanEqualB(final int a) {
            this.a = a;
        }

        @Override
        public boolean equals(final Object o) {
            if (o == this) {
                return true;
            }
            if (o instanceof TestACanEqualB) {
                return this.a == ((TestACanEqualB) o).getA();
            }
            if (o instanceof TestBCanEqualA) {
                return this.a == ((TestBCanEqualA) o).getB();
            }
            return false;
        }

        @Override
        public int hashCode() {
            return a;
        }

        public int getA() {
            return this.a;
        }
    }

    public static class TestBCanEqualA {
        private final int b;

        public TestBCanEqualA(final int b) {
            this.b = b;
        }

        @Override
        public boolean equals(final Object o) {
            if (o == this) {
                return true;
            }
            if (o instanceof TestACanEqualB) {
                return this.b == ((TestACanEqualB) o).getA();
            }
            if (o instanceof TestBCanEqualA) {
                return this.b == ((TestBCanEqualA) o).getB();
            }
            return false;
        }

        @Override
        public int hashCode() {
            return b;
        }

        public int getB() {
            return this.b;
        }
    }
    
    /**
     * Tests two instances of classes that can be equal and that are not "related". The two classes are not subclasses
     * of each other and do not share a parent aside from Object.
     * See http://issues.apache.org/bugzilla/show_bug.cgi?id=33069
     */
    @Test
    public void testUnrelatedClasses() {
        final Object[] x = new Object[]{new TestACanEqualB(1)};
        final Object[] y = new Object[]{new TestBCanEqualA(1)};

        // sanity checks:
        assertTrue(Arrays.equals(x, x));
        assertTrue(Arrays.equals(y, y));
        assertTrue(Arrays.equals(x, y));
        assertTrue(Arrays.equals(y, x));
        // real tests:
        assertTrue(x[0].equals(x[0]));
        assertTrue(y[0].equals(y[0]));
        assertTrue(x[0].equals(y[0]));
        assertTrue(y[0].equals(x[0]));
        assertTrue(new EqualsBuilder().append(x, x).isEquals());
        assertTrue(new EqualsBuilder().append(y, y).isEquals());
        assertTrue(new EqualsBuilder().append(x, y).isEquals());
        assertTrue(new EqualsBuilder().append(y, x).isEquals());
    }
    
    /**
     * Test from http://issues.apache.org/bugzilla/show_bug.cgi?id=33067
     */
    @Test
    public void testNpeForNullElement() {
        final Object[] x1 = new Object[] { Integer.valueOf(1), null, Integer.valueOf(3) };
        final Object[] x2 = new Object[] { Integer.valueOf(1), Integer.valueOf(2), Integer.valueOf(3) };

        // causes an NPE in 2.0 according to:
        // http://issues.apache.org/bugzilla/show_bug.cgi?id=33067
        new EqualsBuilder().append(x1, x2);
    }

    @Test
    public void testReflectionEqualsExcludeFields() throws Exception {
        final TestObjectWithMultipleFields x1 = new TestObjectWithMultipleFields(1, 2, 3);
        final TestObjectWithMultipleFields x2 = new TestObjectWithMultipleFields(1, 3, 4);

        // not equal when including all fields
        assertTrue(!EqualsBuilder.reflectionEquals(x1, x2));

        // doesn't barf on null, empty array, or non-existent field, but still tests as not equal
        assertTrue(!EqualsBuilder.reflectionEquals(x1, x2, (String[]) null));
        assertTrue(!EqualsBuilder.reflectionEquals(x1, x2, new String[] {}));
        assertTrue(!EqualsBuilder.reflectionEquals(x1, x2, new String[] {"xxx"}));

        // not equal if only one of the differing fields excluded
        assertTrue(!EqualsBuilder.reflectionEquals(x1, x2, new String[] {"two"}));
        assertTrue(!EqualsBuilder.reflectionEquals(x1, x2, new String[] {"three"}));

        // equal if both differing fields excluded
        assertTrue(EqualsBuilder.reflectionEquals(x1, x2, new String[] {"two", "three"}));

        // still equal as long as both differing fields are among excluded
        assertTrue(EqualsBuilder.reflectionEquals(x1, x2, new String[] {"one", "two", "three"}));
        assertTrue(EqualsBuilder.reflectionEquals(x1, x2, new String[] {"one", "two", "three", "xxx"}));
    }

    static class TestObjectWithMultipleFields {
        @SuppressWarnings("unused")
        private final TestObject one;
        @SuppressWarnings("unused")
        private final TestObject two;
        @SuppressWarnings("unused")
        private final TestObject three;

        public TestObjectWithMultipleFields(final int one, final int two, final int three) {
            this.one = new TestObject(one);
            this.two = new TestObject(two);
            this.three = new TestObject(three);
        }
    }
    
    /**
     * Test cyclical object references which cause a StackOverflowException if
     * not handled properly. s. LANG-606
     */
    @Test
    public void testCyclicalObjectReferences() {
        final TestObjectReference refX1 = new TestObjectReference(1);
        final TestObjectReference x1 = new TestObjectReference(1);
        x1.setObjectReference(refX1);
        refX1.setObjectReference(x1);

        final TestObjectReference refX2 = new TestObjectReference(1);
        final TestObjectReference x2 = new TestObjectReference(1);
        x2.setObjectReference(refX2);
        refX2.setObjectReference(x2);

        final TestObjectReference refX3 = new TestObjectReference(2);
        final TestObjectReference x3 = new TestObjectReference(2);
        x3.setObjectReference(refX3);
        refX3.setObjectReference(x3);

        assertTrue(x1.equals(x2));
        assertNull(EqualsBuilder.getRegistry());
        assertFalse(x1.equals(x3));
        assertNull(EqualsBuilder.getRegistry());
        assertFalse(x2.equals(x3));
        assertNull(EqualsBuilder.getRegistry());
    }

    static class TestObjectReference {
        @SuppressWarnings("unused")
        private TestObjectReference reference;
        @SuppressWarnings("unused")
        private final TestObject one;

        public TestObjectReference(final int one) {
            this.one = new TestObject(one);
        }

        public void setObjectReference(final TestObjectReference reference) {
            this.reference = reference;
        }

        @Override
        public boolean equals(final Object obj) {
            return EqualsBuilder.reflectionEquals(this, obj);
        }
    }
    
    @Test
    public void testReflectionArrays() throws Exception {

        final TestObject one = new TestObject(1);
        final TestObject two = new TestObject(2);

        Object[] o1 = new Object[] { one };
        Object[] o2 = new Object[] { two };
        Object[] o3 = new Object[] { one };

        assertTrue(!EqualsBuilder.reflectionEquals(o1, o2));
        assertTrue(EqualsBuilder.reflectionEquals(o1, o1));
        assertTrue(EqualsBuilder.reflectionEquals(o1, o3));
        
        double[] d1 = { 0, 1 };
        double[] d2 = { 2, 3 };
        double[] d3 = { 0, 1 };
        
        assertTrue(!EqualsBuilder.reflectionEquals(d1, d2));
        assertTrue(EqualsBuilder.reflectionEquals(d1, d1));
        assertTrue(EqualsBuilder.reflectionEquals(d1, d3));
    }

}

