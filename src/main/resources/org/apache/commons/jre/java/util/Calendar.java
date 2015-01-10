/**
 * This class gets copy-and-pasted between the eu.future.earth.gwt.emul.java.util (used for
 * development and testing) and com.google.gwt.emul.java.util packages (used for gwt
 * compilation).  Change the comments on the package declaration appropriately.
 */
//package eu.future.earth.gwt.emul.java.util;
package java.util;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public abstract class Calendar implements Cloneable {
	
	protected Calendar(){
		super();
	}

  /**
   * Field number for <code>get</code> and <code>set</code> indicating the
   * era, e.g., AD or BC in the Julian calendar. This is a calendar-specific
   * value; see subclass documentation.
   *
   * @see GregorianCalendar#AD
   * @see GregorianCalendar#BC
   */
  public final static int ERA = 0;

  /**
   * Field number for <code>get</code> and <code>set</code> indicating the
   * year. This is a calendar-specific value; see subclass documentation.
   */
  public final static int YEAR = 1;

  /**
   * Field number for <code>get</code> and <code>set</code> indicating the
   * month. This is a calendar-specific value. The first month of
   * the year in the Gregorian and Julian calendars is
   * <code>JANUARY</code> which is 0; the last depends on the number
   * of months in a year.
   *
   * @see #JANUARY
   * @see #FEBRUARY
   * @see #MARCH
   * @see #APRIL
   * @see #MAY
   * @see #JUNE
   * @see #JULY
   * @see #AUGUST
   * @see #SEPTEMBER
   * @see #OCTOBER
   * @see #NOVEMBER
   * @see #DECEMBER
   * @see #UNDECIMBER
   */
  public final static int MONTH = 2;

  /**
   * Field number for <code>get</code> and <code>set</code> indicating the
   * week number within the current year.  The first week of the year, as
   * defined by <code>getFirstDayOfWeek()</code> and
   * <code>getMinimalDaysInFirstWeek()</code>, has value 1.  Subclasses define
   * the value of <code>WEEK_OF_YEAR</code> for days before the first week of
   * the year.
   *
   * @see #getFirstDayOfWeek
   * @see #getMinimalDaysInFirstWeek
   */
  public final static int WEEK_OF_YEAR = 3;

  /**
   * Field number for <code>get</code> and <code>set</code> indicating the
   * week number within the current month.  The first week of the month, as
   * defined by <code>getFirstDayOfWeek()</code> and
   * <code>getMinimalDaysInFirstWeek()</code>, has value 1.  Subclasses define
   * the value of <code>WEEK_OF_MONTH</code> for days before the first week of
   * the month.
   *
   * @see #getFirstDayOfWeek
   * @see #getMinimalDaysInFirstWeek
   */
  public final static int WEEK_OF_MONTH = 4;

  /**
   * Field number for <code>get</code> and <code>set</code> indicating the
   * day of the month. This is a synonym for <code>DAY_OF_MONTH</code>.
   * The first day of the month has value 1.
   *
   * @see #DAY_OF_MONTH
   */
  public final static int DATE = 5;

  /**
   * Field number for <code>get</code> and <code>set</code> indicating the
   * day of the month. This is a synonym for <code>DATE</code>.
   * The first day of the month has value 1.
   *
   * @see #DATE
   */
  public final static int DAY_OF_MONTH = 5;

  /**
   * Field number for <code>get</code> and <code>set</code> indicating the day
   * number within the current year.  The first day of the year has value 1.
   */
  public final static int DAY_OF_YEAR = 6;

  /**
   * Field number for <code>get</code> and <code>set</code> indicating the day
   * of the week.  This field takes values <code>SUNDAY</code>,
   * <code>MONDAY</code>, <code>TUESDAY</code>, <code>WEDNESDAY</code>,
   * <code>THURSDAY</code>, <code>FRIDAY</code>, and <code>SATURDAY</code>.
   *
   * @see #SUNDAY
   * @see #MONDAY
   * @see #TUESDAY
   * @see #WEDNESDAY
   * @see #THURSDAY
   * @see #FRIDAY
   * @see #SATURDAY
   */
  public final static int DAY_OF_WEEK = 7;

  /**
   * Field number for <code>get</code> and <code>set</code> indicating the
   * ordinal number of the day of the week within the current month. Together
   * with the <code>DAY_OF_WEEK</code> field, this uniquely specifies a day
   * within a month.  Unlike <code>WEEK_OF_MONTH</code> and
   * <code>WEEK_OF_YEAR</code>, this field's value does <em>not</em> depend on
   * <code>getFirstDayOfWeek()</code> or
   * <code>getMinimalDaysInFirstWeek()</code>.  <code>DAY_OF_MONTH 1</code>
   * through <code>7</code> always correspond to <code>DAY_OF_WEEK_IN_MONTH
   * 1</code>; <code>8</code> through <code>14</code> correspond to
   * <code>DAY_OF_WEEK_IN_MONTH 2</code>, and so on.
   * <code>DAY_OF_WEEK_IN_MONTH 0</code> indicates the week before
   * <code>DAY_OF_WEEK_IN_MONTH 1</code>.  Negative values count back from the
   * end of the month, so the last Sunday of a month is specified as
   * <code>DAY_OF_WEEK = SUNDAY, DAY_OF_WEEK_IN_MONTH = -1</code>.  Because
   * negative values count backward they will usually be aligned differently
   * within the month than positive values.  For example, if a month has 31
   * days, <code>DAY_OF_WEEK_IN_MONTH -1</code> will overlap
   * <code>DAY_OF_WEEK_IN_MONTH 5</code> and the end of <code>4</code>.
   *
   * @see #DAY_OF_WEEK
   * @see #WEEK_OF_MONTH
   */
  public final static int DAY_OF_WEEK_IN_MONTH = 8;

  /**
   * Field number for <code>get</code> and <code>set</code> indicating
   * whether the <code>HOUR</code> is before or after noon.
   * E.g., at 10:04:15.250 PM the <code>AM_PM</code> is <code>PM</code>.
   *
   * @see #AM
   * @see #PM
   * @see #HOUR
   */
  public final static int AM_PM = 9;

  /**
   * Field number for <code>get</code> and <code>set</code> indicating the
   * hour of the morning or afternoon. <code>HOUR</code> is used for the
   * 12-hour clock (0 - 11). Noon and midnight are represented by 0, not by 12.
   * E.g., at 10:04:15.250 PM the <code>HOUR</code> is 10.
   *
   * @see #AM_PM
   * @see #HOUR_OF_DAY
   */
  public final static int HOUR = 10;

  /**
   * Field number for <code>get</code> and <code>set</code> indicating the
   * hour of the day. <code>HOUR_OF_DAY</code> is used for the 24-hour clock.
   * E.g., at 10:04:15.250 PM the <code>HOUR_OF_DAY</code> is 22.
   *
   * @see #HOUR
   */
  public final static int HOUR_OF_DAY = 11;

  /**
   * Field number for <code>get</code> and <code>set</code> indicating the
   * minute within the hour.
   * E.g., at 10:04:15.250 PM the <code>MINUTE</code> is 4.
   */
  public final static int MINUTE = 12;

  /**
   * Field number for <code>get</code> and <code>set</code> indicating the
   * second within the minute.
   * E.g., at 10:04:15.250 PM the <code>SECOND</code> is 15.
   */
  public final static int SECOND = 13;

  /**
   * Field number for <code>get</code> and <code>set</code> indicating the
   * millisecond within the second.
   * E.g., at 10:04:15.250 PM the <code>MILLISECOND</code> is 250.
   */
  public final static int MILLISECOND = 14;

  /**
   * Value of the {@link #DAY_OF_WEEK} field indicating
   * Sunday.
   */
  public final static int SUNDAY = 1;

  /**
   * Value of the {@link #DAY_OF_WEEK} field indicating
   * Monday.
   */
  public final static int MONDAY = 2;

  /**
   * Value of the {@link #DAY_OF_WEEK} field indicating
   * Tuesday.
   */
  public final static int TUESDAY = 3;

  /**
   * Value of the {@link #DAY_OF_WEEK} field indicating
   * Wednesday.
   */
  public final static int WEDNESDAY = 4;

  /**
   * Value of the {@link #DAY_OF_WEEK} field indicating
   * Thursday.
   */
  public final static int THURSDAY = 5;

  /**
   * Value of the {@link #DAY_OF_WEEK} field indicating
   * Friday.
   */
  public final static int FRIDAY = 6;

  /**
   * Value of the {@link #DAY_OF_WEEK} field indicating
   * Saturday.
   */
  public final static int SATURDAY = 7;

  /**
   * Value of the {@link #MONTH} field indicating the
   * first month of the year in the Gregorian and Julian calendars.
   */
  public final static int JANUARY = 0;

  /**
   * Value of the {@link #MONTH} field indicating the
   * second month of the year in the Gregorian and Julian calendars.
   */
  public final static int FEBRUARY = 1;

  /**
   * Value of the {@link #MONTH} field indicating the
   * third month of the year in the Gregorian and Julian calendars.
   */
  public final static int MARCH = 2;

  /**
   * Value of the {@link #MONTH} field indicating the
   * fourth month of the year in the Gregorian and Julian calendars.
   */
  public final static int APRIL = 3;

  /**
   * Value of the {@link #MONTH} field indicating the
   * fifth month of the year in the Gregorian and Julian calendars.
   */
  public final static int MAY = 4;

  /**
   * Value of the {@link #MONTH} field indicating the
   * sixth month of the year in the Gregorian and Julian calendars.
   */
  public final static int JUNE = 5;

  /**
   * Value of the {@link #MONTH} field indicating the
   * seventh month of the year in the Gregorian and Julian calendars.
   */
  public final static int JULY = 6;

  /**
   * Value of the {@link #MONTH} field indicating the
   * eighth month of the year in the Gregorian and Julian calendars.
   */
  public final static int AUGUST = 7;

  /**
   * Value of the {@link #MONTH} field indicating the
   * ninth month of the year in the Gregorian and Julian calendars.
   */
  public final static int SEPTEMBER = 8;

  /**
   * Value of the {@link #MONTH} field indicating the
   * tenth month of the year in the Gregorian and Julian calendars.
   */
  public final static int OCTOBER = 9;

  /**
   * Value of the {@link #MONTH} field indicating the
   * eleventh month of the year in the Gregorian and Julian calendars.
   */
  public final static int NOVEMBER = 10;

  /**
   * Value of the {@link #MONTH} field indicating the
   * twelfth month of the year in the Gregorian and Julian calendars.
   */
  public final static int DECEMBER = 11;

  /**
   * Value of the {@link #MONTH} field indicating the
   * thirteenth month of the year. Although <code>GregorianCalendar</code>
   * does not use this value, lunar calendars do.
   */
  public final static int UNDECIMBER = 12;

  /**
   * Value of the {@link #AM_PM} field indicating the
   * period of the day from midnight to just before noon.
   */
  public final static int AM = 0;

  /**
   * Value of the {@link #AM_PM} field indicating the
   * period of the day from noon to just before midnight.
   */
  public final static int PM = 1;

  /**
   * <code>True</code> if this calendar allows out-of-range field values during computation
   * of <code>time</code> from <code>fields[]</code>.
   * @see #setLenient
   * @see #isLenient
   * @serial
   */
  private boolean         lenient = true;

  /**
   * Returns a <code>Date</code> object representing this
   * <code>Calendar</code>'s time value (millisecond offset from the <a
   * href="#Epoch">Epoch</a>").
   *
   * @return a <code>Date</code> representing the time value.
   * @see #setTime(Date)
   * @see #getTimeInMillis()
   */
  public abstract Date getTime();

  /**
   * Sets this Calendar's time with the given <code>Date</code>.
   * <p>
   * Note: Calling <code>setTime()</code> with
   * <code>Date(Long.MAX_VALUE)</code> or <code>Date(Long.MIN_VALUE)</code>
   * may yield incorrect field values from <code>get()</code>.
   *
   * @param date the given Date.
   * @see #getTime()
   * @see #setTimeInMillis(long)
   */
  public abstract void setTime(Date date);

  /**
   * Returns this Calendar's time value in milliseconds.
   *
   * @return the current time as UTC milliseconds from the epoch.
   * @see #getTime()
   * @see #setTimeInMillis(long)
   */
  public abstract long getTimeInMillis();

  /**
   * Sets this Calendar's current time from the given long value.
   *
   * @param millis the new time in UTC milliseconds from the epoch.
   * @see #setTime(Date)
   * @see #getTimeInMillis()
   */
  public abstract void setTimeInMillis(long millis);

  public abstract int get(int field);

  /**
   * Sets the given calendar field to the given value. The value is not
   * interpreted by this method regardless of the leniency mode.
   *
   * @param field the given calendar field.
   * @param value the value to be set for the given calendar field.
   * @throws ArrayIndexOutOfBoundsException if the specified field is out of range
   *             (<code>field &lt; 0 || field &gt;= FIELD_COUNT</code>).
   * in non-lenient mode.
   * @see #set(int,int,int)
   * @see #set(int,int,int,int,int)
   * @see #set(int,int,int,int,int,int)
   * @see #get(int)
   */
  public abstract void set(int field, int value);

  public abstract void add(int field, int value);
  
  public abstract void clear();

  public int getFirstDayOfWeek() {
    return firstDayOfWeek;
  }

  public void setFirstDayOfWeek(int newFirstDayOfWeek) {
    this.firstDayOfWeek = newFirstDayOfWeek;
  }
  
  public int getMinimalDaysInFirstWeek() {
	  return minimalDaysInFirstWeek;
  }
  
  public void setMinimalDaysInFirstWeek(int newMinimalDaysInFirstWeek) {
	  this.minimalDaysInFirstWeek = newMinimalDaysInFirstWeek;
  }

  /**
   * The first day of the week, with possible values <code>SUNDAY</code>,
   * <code>MONDAY</code>, etc.  This is a locale-dependent value.
   * @serial
   */
  private int firstDayOfWeek = SUNDAY;
  
  /**
   * The number of days required for the first week in a month or year,
   * with possible values from 1 to 7.  This is a locale-dependent value.
   * @serial
   */
  private int minimalDaysInFirstWeek = 1;

  public abstract Object clone();

  public static Calendar getInstance() {
	  return new GregorianCalendar();
  }


  /**
   * Returns whether this <code>Calendar</code> represents a time
   * before the time represented by the specified
   * <code>Object</code>. This method is equivalent to:
   * <pre><blockquote>
   *         compareTo(when) < 0
   * </blockquote></pre>
   * if and only if <code>when</code> is a <code>Calendar</code>
   * instance. Otherwise, the method returns <code>false</code>.
   *
   * @param when the <code>Object</code> to be compared
   * @return <code>true</code> if the time of this
   * <code>Calendar</code> is before the time represented by
   * <code>when</code>; <code>false</code> otherwise.
   * @see     #compareTo(Calendar)
   */
  public boolean before(Object when) {
      return when instanceof Calendar
          && compareTo((Calendar)when) < 0;
  }

  /**
   * Returns whether this <code>Calendar</code> represents a time
   * after the time represented by the specified
   * <code>Object</code>. This method is equivalent to:
   * <pre><blockquote>
   *         compareTo(when) > 0
   * </blockquote></pre>
   * if and only if <code>when</code> is a <code>Calendar</code>
   * instance. Otherwise, the method returns <code>false</code>.
   *
   * @param when the <code>Object</code> to be compared
   * @return <code>true</code> if the time of this <code>Calendar</code> is
   * after the time represented by <code>when</code>; <code>false</code>
   * otherwise.
   * @see     #compareTo(Calendar)
   */
  public boolean after(Object when) {
      return when instanceof Calendar
          && compareTo((Calendar)when) > 0;
  }

  /**
   * Compares this <code>Calendar</code> to the specified
   * <code>Object</code>.  The result is <code>true</code> if and only if
   * the argument is a <code>Calendar</code> object of the same calendar
   * system that represents the same time value (millisecond offset from the
   * <a href="#Epoch">Epoch</a>) under the same
   * <code>Calendar</code> parameters as this object.
   *
   * <p>The <code>Calendar</code> parameters are the values represented
   * by the <code>isLenient</code>, <code>getFirstDayOfWeek</code>,
   * <code>getMinimalDaysInFirstWeek</code> and <code>getTimeZone</code>
   * methods. If there is any difference in those parameters
   * between the two <code>Calendar</code>s, this method returns
   * <code>false</code>.
   *
   * <p>Use the {@link #compareTo(Calendar) compareTo} method to
   * compare only the time values.
   *
   * @param obj the object to compare with.
   * @return <code>true</code> if this object is equal to <code>obj</code>;
   * <code>false</code> otherwise.
   */
  public boolean equals(Object obj) {
      if (this == obj)
          return true;
      try {
          Calendar that = (Calendar)obj;
          
          return getMillisOf(that) == getMillisOf(this);
      } catch (Exception e) {
          // Note: GregorianCalendar.computeTime throws
          // IllegalArgumentException if the ERA value is invalid
          // even it's in lenient mode.
      }
      return false;
  }

  /**
   * Compares the time values (millisecond offsets from the <a
   * href="#Epoch">Epoch</a>) represented by two
   * <code>Calendar</code> objects.
   *
   * @param anotherCalendar the <code>Calendar</code> to be compared.
   * @return the value <code>0</code> if the time represented by the argument
   * is equal to the time represented by this <code>Calendar</code>; a value
   * less than <code>0</code> if the time of this <code>Calendar</code> is
   * before the time represented by the argument; and a value greater than
   * <code>0</code> if the time of this <code>Calendar</code> is after the
   * time represented by the argument.
   * @exception NullPointerException if the specified <code>Calendar</code> is
   *            <code>null</code>.
   * @exception IllegalArgumentException if the time value of the
   * specified <code>Calendar</code> object can't be obtained due to
   * any invalid calendar values.
   * @since   1.5
   */
  public int compareTo(Calendar anotherCalendar) {
      return compareTo(getMillisOf(anotherCalendar));
  }

  /**
   * Specifies whether or not date/time interpretation is to be lenient.  With
   * lenient interpretation, a date such as "February 942, 1996" will be
   * treated as being equivalent to the 941st day after February 1, 1996.
   * With strict (non-lenient) interpretation, such dates will cause an exception to be
   * thrown. The default is lenient.
   *
   * @param lenient <code>true</code> if the lenient mode is to be turned
   * on; <code>false</code> if it is to be turned off.
   * @see #isLenient()
   * @see java.text.DateFormat#setLenient
   */
  public void setLenient(boolean lenient)
  {
      this.lenient = lenient;
  }

  /**
   * Tells whether date/time interpretation is to be lenient.
   *
   * @return <code>true</code> if the interpretation mode of this calendar is lenient;
   * <code>false</code> otherwise.
   * @see #setLenient(boolean)
   */
  public boolean isLenient()
  {
      return lenient;
  }

  private int compareTo(long t) {
      long thisTime = getMillisOf(this);
      return (thisTime > t) ? 1 : (thisTime == t) ? 0 : -1;
  }

  private static final long getMillisOf(Calendar calendar) {
	  if (calendar == null) {
		  return 0L;
	  } else {
		  return calendar.getTimeInMillis();
	  }
  }

	public int getActualMaximum(int field) {
	    throw new UnsupportedOperationException("GWT Calendar does not support getActualMaximum()");
	}

	public int getActualMinimum(int field) {
	    throw new UnsupportedOperationException("GWT Calendar does not support getActualMinimum()");
	}
}
