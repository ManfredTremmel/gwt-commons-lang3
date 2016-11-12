package java.nio.charset;

import javaemul.internal.EmulatedCharset;

import java.util.Objects;

public class CharsetEncoder {

  private final EmulatedCharset charset;

  public CharsetEncoder(final Charset pcharset) {
    this.charset = (EmulatedCharset) pcharset;
  }

  /**
   * Tells whether or not this encoder can encode the given character sequence.
   *
   * <p>
   * If this method returns <tt>false</tt> for a particular character sequence then more information
   * about why the sequence cannot be encoded may be obtained by performing a full
   * <a href="#steps">encoding operation</a>.
   * </p>
   *
   * <p>
   * This method may modify this encoder's state; it should therefore not be invoked if an encoding
   * operation is already in progress.
   * </p>
   *
   * <p>
   * The default implementation of this method is not very efficient; it should generally be
   * overridden to improve performance.
   * </p>
   *
   * @param cs The given character sequence
   *
   * @return <tt>true</tt> if, and only if, this encoder can encode the given character without
   *         throwing any exceptions and without performing any replacements
   *
   */
  public boolean canEncode(final CharSequence cs) {
    if (cs == null) {
      return true;
    }
    final String cstring = Objects.toString(cs);
    final byte[] stringAsByte = this.charset.getBytes(cstring);
    return Objects.equals(cstring, String.valueOf(
        this.charset.decodeString(this.charset.getBytes(cstring), 0, stringAsByte.length)));
  }
}
