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
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.commons.lang3.SystemUtils;
import org.apache.commons.lang3.builder.ToStringStyleTest.Person;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Unit tests {@link org.apache.commons.lang3.builder.JsonToStringStyleTest}.
 *
 * @version $Id: JsonToStringStyleTest.java 1648925 2015-01-01 19:24:07Z britter $
 */
public class JsonToStringStyleTest {

    private final Integer base = Integer.valueOf(5);

    @Before
    public void setUp() throws Exception {
        ToStringBuilder.setDefaultStyle(ToStringStyle.JSON_STYLE);
    }

    @After
    public void tearDown() throws Exception {
        ToStringBuilder.setDefaultStyle(ToStringStyle.DEFAULT_STYLE);
    }

    // ----------------------------------------------------------------

    @Test
    public void testNull() {
        assertEquals("null", new ToStringBuilder(null).toString());
    }

    @Test
    public void testBlank() {
        assertEquals("{}", new ToStringBuilder(base).toString());
    }

    @Test
    public void testAppendSuper() {
        assertEquals(
                "{}",
                new ToStringBuilder(base).appendSuper(
                        "Integer@8888[" + SystemUtils.LINE_SEPARATOR + "]")
                        .toString());
        assertEquals(
                "{}",
                new ToStringBuilder(base).appendSuper(
                        "Integer@8888[" + SystemUtils.LINE_SEPARATOR + "  null"
                                + SystemUtils.LINE_SEPARATOR + "]").toString());
        assertEquals(
                "{\"a\":\"hello\"}",
                new ToStringBuilder(base)
                        .appendSuper(
                                "Integer@8888[" + SystemUtils.LINE_SEPARATOR
                                        + "]").append("a", "hello").toString());
        assertEquals(
                "{\"a\":\"hello\"}",
                new ToStringBuilder(base)
                        .appendSuper(
                                "Integer@8888[" + SystemUtils.LINE_SEPARATOR
                                        + "  null" + SystemUtils.LINE_SEPARATOR
                                        + "]").append("a", "hello").toString());
        assertEquals("{\"a\":\"hello\"}", new ToStringBuilder(base)
                .appendSuper(null).append("a", "hello").toString());

        assertEquals("{\"a\":\"hello\",\"b\":\"world\"}", new ToStringBuilder(base)
                .appendSuper("{\"a\":\"hello\"}").append("b", "world").toString());
    }

    @Test
    public void testObject() {

        final Integer i3 = Integer.valueOf(3);
        final Integer i4 = Integer.valueOf(4);

        try {
            new ToStringBuilder(base).append((Object) null).toString();
            fail("Should have generated UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
        }

        try {
            new ToStringBuilder(base).append(i3).toString();
            fail("Should have generated UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
        }

        assertEquals("{\"a\":null}",
                new ToStringBuilder(base).append("a", (Object) null).toString());
        assertEquals("{\"a\":3}", new ToStringBuilder(base).append("a", i3)
                .toString());
        assertEquals("{\"a\":3,\"b\":4}",
                new ToStringBuilder(base).append("a", i3).append("b", i4)
                        .toString());

        try {
            new ToStringBuilder(base).append("a", i3, false).toString();
            fail("Should have generated UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
        }

        try {
            new ToStringBuilder(base).append("a", new ArrayList<Object>(), false).toString();
            fail("Should have generated UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
        }

        assertEquals(
                "{\"a\":[]}",
                new ToStringBuilder(base).append("a", new ArrayList<Object>(),
                        true).toString());

        try {
            new ToStringBuilder(base).append("a", new HashMap<Object, Object>(), false).toString();
            fail("Should have generated UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
        }

        assertEquals(
                "{\"a\":{}}",
                new ToStringBuilder(base).append("a",
                        new HashMap<Object, Object>(), true).toString());

        try {
            new ToStringBuilder(base).append("a", (Object) new String[0], false).toString();
            fail("Should have generated UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
        }

        assertEquals(
                "{\"a\":[]}",
                new ToStringBuilder(base).append("a", (Object) new String[0],
                        true).toString());

        try {
            new ToStringBuilder(base).append("a", (Object) new int[]{1, 2, 3}, false).toString();
            fail("Should have generated UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
        }


        assertEquals(
                "{\"a\":[1,2,3]}",
                new ToStringBuilder(base).append("a",
                        (Object) new int[]{1, 2, 3}, true).toString());

        try {
            new ToStringBuilder(base).append("a", (Object) new String[]{"v", "x", "y", "z"}, false).toString();
            fail("Should have generated UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
        }


        assertEquals(
                "{\"a\":[\"v\",\"x\",\"y\",\"z\"]}",
                new ToStringBuilder(base).append("a",
                        (Object) new String[]{"v", "x", "y", "z"}, true)
                        .toString());
    }

    @Test
    public void testPerson() {
        final Person p = new Person();
        p.name = "Jane Doe";
        p.age = 25;
        p.smoker = true;

        assertEquals(
                "{\"name\":\"Jane Doe\",\"age\":25,\"smoker\":true}",
                new ToStringBuilder(p).append("name", p.name)
                        .append("age", p.age).append("smoker", p.smoker)
                        .toString());
    }

    @Test
    public void testLong() {

        try {
            new ToStringBuilder(base).append(3L).toString();
            fail("Should have generated UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
        }

        assertEquals("{\"a\":3}", new ToStringBuilder(base).append("a", 3L)
                .toString());
        assertEquals("{\"a\":3,\"b\":4}",
                new ToStringBuilder(base).append("a", 3L).append("b", 4L)
                        .toString());
    }

    @Test
    public void testObjectArray() {
        Object[] array = new Object[]{null, base, new int[]{3, 6}};

        try {
            new ToStringBuilder(base).append(array).toString();
            fail("Should have generated UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
        }

        try {
            new ToStringBuilder(base).append((Object) array).toString();
            fail("Should have generated UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
        }

        array = null;
        try {
            new ToStringBuilder(base).append(array).toString();
            fail("Should have generated UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
        }

        try {
            new ToStringBuilder(base).append((Object) array).toString();
            fail("Should have generated UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
        }
    }

    @Test
    public void testLongArray() {
        long[] array = new long[]{1, 2, -3, 4};

        try {
            new ToStringBuilder(base).append(array).toString();
            fail("Should have generated UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
        }

        try {
            new ToStringBuilder(base).append((Object) array).toString();
            fail("Should have generated UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
        }

        array = null;

        try {
            new ToStringBuilder(base).append(array).toString();
            fail("Should have generated UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
        }

        try {
            new ToStringBuilder(base).append((Object) array).toString();
            fail("Should have generated UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
        }
    }

    @Test
    public void testLongArrayArray() {
        long[][] array = new long[][]{{1, 2}, null, {5}};

        try {
            new ToStringBuilder(base).append(array).toString();
            fail("Should have generated UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
        }

        try {
            new ToStringBuilder(base).append((Object) array).toString();
            fail("Should have generated UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
        }

        array = null;

        try {
            new ToStringBuilder(base).append(array).toString();
            fail("Should have generated UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
        }

        try {
            new ToStringBuilder(base).append((Object) array).toString();
            fail("Should have generated UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
        }
    }
}
