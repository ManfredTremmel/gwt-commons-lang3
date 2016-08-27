/*
 * Copyright (C) 2012 The Guava Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package java.nio.charset;

import java.util.Collections;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * A minimal GWT emulation of {@link Charset}.
 *
 * @author Gregory Kick
 */
public abstract class Charset implements Comparable<Charset> {
  private static final Charset UTF_8 = new Charset("UTF-8") {};

  private static final SortedMap<String, Charset> AVAILABLE_CHARSETS = new TreeMap<>();
  static {
    Charset.AVAILABLE_CHARSETS.put(Charset.UTF_8.name(), Charset.UTF_8);
  }

  public static SortedMap<String, Charset> availableCharsets() {
    return Collections.unmodifiableSortedMap(Charset.AVAILABLE_CHARSETS);
  }

  public static Charset forName(final String charsetName) {
    if (charsetName == null) {
      throw new IllegalArgumentException("Null charset name");
    }
    final int length = charsetName.length();
    if (length == 0) {
      throw new IllegalCharsetNameException(charsetName);
    }
    for (int i = 0; i < length; i++) {
      final char c = charsetName.charAt(i);
      if (c >= 'A' && c <= 'Z' || c >= 'a' && c <= 'z' || c >= '0' && c <= '9' || c == '-' && i != 0
          || c == ':' && i != 0 || c == '_' && i != 0 || c == '.' && i != 0) {
        continue;
      }
      throw new IllegalCharsetNameException(charsetName);
    }
    final Charset charset = Charset.AVAILABLE_CHARSETS.get(charsetName.toUpperCase());
    if (charset != null) {
      return charset;
    }
    throw new UnsupportedCharsetException(charsetName);
  }

  private final String name;

  private Charset(final String name) {
    this.name = name;
  }

  protected Charset(final String name, final String[] aliasesIgnored) {
    this.name = name;
  }

  public final String name() {
    return this.name;
  }

  @Override
  public final int compareTo(final Charset that) {
    return this.name.compareToIgnoreCase(that.name);
  }

  @Override
  public final int hashCode() {
    return this.name.hashCode();
  }

  @Override
  public final boolean equals(final Object o) {
    if (o == this) {
      return true;
    } else if (o instanceof Charset) {
      final Charset that = (Charset) o;
      return this.name.equals(that.name);
    } else {
      return false;
    }
  }

  @Override
  public final String toString() {
    return this.name;
  }

  public static Charset defaultCharset() {
    return Charset.UTF_8;
  }
}
