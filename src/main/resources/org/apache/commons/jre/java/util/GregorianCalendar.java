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

import sun.util.calendar.BaseCalendar;

public class GregorianCalendar extends Calendar implements Cloneable {

	private Date cached = null;
	private int year = 0;
	private int month = 0;
	private int day = 0;
	private int hour = 0;
	private int minute = 0;
	private int second = 0;

    /**
     * Value of the {@link #ERA} field indicating
     * the period before the common era, the same value as {@link #BC}.
     *
     * @see #CE
     */
    static final int BCE = 0;

    /**
     * Value of the {@link #ERA} field indicating
     * the common era, the same value as {@link #AD}.
     *
     * @see #BCE
     */
    static final int CE = 1;

    private static final int EPOCH_OFFSET   = 719163; // Fixed date of January 1, 1970 (Gregorian)
    private static final int EPOCH_YEAR     = 1970;

    static final int MONTH_LENGTH[]
        = {31,28,31,30,31,30,31,31,30,31,30,31}; // 0-based
    static final int LEAP_MONTH_LENGTH[]
        = {31,29,31,30,31,30,31,31,30,31,30,31}; // 0-based

    // Useful millisecond constants.  Although ONE_DAY and ONE_WEEK can fit
    // into ints, they must be longs in order to prevent arithmetic overflow
    // when performing (bug 4173516).
    private static final int  ONE_SECOND = 1000;
    private static final int  ONE_MINUTE = 60*ONE_SECOND;
    private static final int  ONE_HOUR   = 60*ONE_MINUTE;
    private static final long ONE_DAY    = 24*ONE_HOUR;
    private static final long ONE_WEEK   = 7*ONE_DAY;

    /*
     * <pre>
     *                            Greatest       Least
     * Field name        Minimum   Minimum     Maximum     Maximum
     * ----------        -------   -------     -------     -------
     * ERA                     0         0           1           1
     * YEAR                    1         1   292269054   292278994
     * MONTH                   0         0          11          11
     * WEEK_OF_YEAR            1         1          52*         53
     * WEEK_OF_MONTH           0         0           4*          6
     * DAY_OF_MONTH            1         1          28*         31
     * DAY_OF_YEAR             1         1         365*        366
     * DAY_OF_WEEK             1         1           7           7
     * DAY_OF_WEEK_IN_MONTH   -1        -1           4*          6
     * AM_PM                   0         0           1           1
     * HOUR                    0         0          11          11
     * HOUR_OF_DAY             0         0          23          23
     * MINUTE                  0         0          59          59
     * SECOND                  0         0          59          59
     * MILLISECOND             0         0         999         999
     * ZONE_OFFSET        -13:00    -13:00       14:00       14:00
     * DST_OFFSET           0:00      0:00        0:20        2:00
     * </pre>
     * *: depends on the Gregorian change date
     */
    static final int MIN_VALUES[] = {
        BCE,            // ERA
        1,              // YEAR
        JANUARY,        // MONTH
        1,              // WEEK_OF_YEAR
        0,              // WEEK_OF_MONTH
        1,              // DAY_OF_MONTH
        1,              // DAY_OF_YEAR
        SUNDAY,         // DAY_OF_WEEK
        1,              // DAY_OF_WEEK_IN_MONTH
        AM,             // AM_PM
        0,              // HOUR
        0,              // HOUR_OF_DAY
        0,              // MINUTE
        0,              // SECOND
        0,              // MILLISECOND
        -13*ONE_HOUR,   // ZONE_OFFSET (UNIX compatibility)
        0               // DST_OFFSET
    };
    static final int LEAST_MAX_VALUES[] = {
        CE,             // ERA
        292269054,      // YEAR
        DECEMBER,       // MONTH
        52,             // WEEK_OF_YEAR
        4,              // WEEK_OF_MONTH
        28,             // DAY_OF_MONTH
        365,            // DAY_OF_YEAR
        SATURDAY,       // DAY_OF_WEEK
        4,              // DAY_OF_WEEK_IN
        PM,             // AM_PM
        11,             // HOUR
        23,             // HOUR_OF_DAY
        59,             // MINUTE
        59,             // SECOND
        999,            // MILLISECOND
        14*ONE_HOUR,    // ZONE_OFFSET
        20*ONE_MINUTE   // DST_OFFSET (historical least maximum)
    };
    static final int MAX_VALUES[] = {
        CE,             // ERA
        292278994,      // YEAR
        DECEMBER,       // MONTH
        53,             // WEEK_OF_YEAR
        6,              // WEEK_OF_MONTH
        31,             // DAY_OF_MONTH
        366,            // DAY_OF_YEAR
        SATURDAY,       // DAY_OF_WEEK
        6,              // DAY_OF_WEEK_IN
        PM,             // AM_PM
        11,             // HOUR
        23,             // HOUR_OF_DAY
        59,             // MINUTE
        59,             // SECOND
        999,            // MILLISECOND
        14*ONE_HOUR,    // ZONE_OFFSET
        2*ONE_HOUR      // DST_OFFSET (double summer time)
    };

	public GregorianCalendar() {
		super();
		setTime(new Date());
	}
	
	public Object clone() {
		final GregorianCalendar clone = new GregorianCalendar();
		clone.setFirstDayOfWeek(getFirstDayOfWeek());
		clone.setMinimalDaysInFirstWeek(getMinimalDaysInFirstWeek());
		clone.setTime(getTime());
		return clone;
	}
	
	@SuppressWarnings("deprecation")
	public void clear() {
		cached = new Date(1970 - 1900, 0, 1, 0, 0, 0);
		computeFields();
	}
	
	@SuppressWarnings("deprecation")
	public void computeFields() {
		year = cached.getYear() + 1900;
		month = cached.getMonth();
		day = cached.getDate();
		hour = cached.getHours();
		minute = cached.getMinutes();
		second = cached.getSeconds();
	}
	
	@SuppressWarnings("deprecation")
	public void computeTime() {
		cached = new Date(year - 1900, month, day, hour, minute, second);		
		computeFields();  // recompute the fields because they may have been rolled
	}
		
	public Date getTime() {
		computeTime();
		return new Date(cached.getTime());
	}

    public void setTime(Date date) {
    	cached = new Date(date.getTime());
    	computeFields();
    }

	public void setTimeInMillis(long millis) {
		cached = new Date(millis);
		computeFields();
	}

	public long getTimeInMillis() {
		computeTime();
		return cached.getTime();
	}

	@SuppressWarnings("deprecation")
	public int get(int field) {
		computeTime();
		switch (field) {
		case YEAR: {
			return year;
		}
		case MONTH: {
			return month;
		}
		case DAY_OF_MONTH: {
			return day;
		}
		case HOUR: {
			return (hour > 11) ? hour - 12 : hour;
		}
		case AM_PM: {
			if (hour < 12) return AM;
			else return PM;
		}
		case HOUR_OF_DAY: {
			return hour;
		}
		case MINUTE: {
			return minute;
		}
		case SECOND: {
			return second;
		}
		case DAY_OF_WEEK: {
			return cached.getDay() + 1;
		}
		case DAY_OF_YEAR: {
			return (compareDate(new Date(cached.getYear(), 0, 1), cached) + 1);
		}
		case WEEK_OF_YEAR: {
			Date weekOne = getWeekOne(cached.getYear());
			if (weekOne.after(cached)) {
				weekOne = getWeekOne(cached.getYear() - 1);
			}
			int days = compareDate(weekOne, cached);
			int week = ((days / 7) + 1);
			if (week < 1) {
				return 1;
			} else if (week < 53) {
				return week;
			} else {
				Date weekOneNextYear = getWeekOne(cached.getYear() + 1);
				if (cached.before(weekOneNextYear)) {
					return 53;
				} else {
					return 1;
				}
			}
		}
		}
		return 0;
	}

	public void set(int field, int value) {
		switch (field) {
		case YEAR: {
			year = value;
			break;
		}
		case MONTH: {
			month = value;
			break;
		}
		case DAY_OF_MONTH: {
			day = value;
			break;
		}
		case HOUR: {
			hour = (hour < 12) ? value : (value + 12);
			break;
		}
		case HOUR_OF_DAY: {
			hour = value;
			break;
		}
		case AM_PM: {
			if ((value == AM) && (hour > 11)) hour -= 12;
			else if ((value == PM) && (hour < 12)) hour += 12;
			break;
		}		
		case MINUTE: {
			minute = value;
			break;
		}
		case SECOND: {
			second = value;
			break;
		}
		case DAY_OF_WEEK: {
			computeTime();
			if (value < getFirstDayOfWeek()) value += 7;
			int dayOfWeek = get(DAY_OF_WEEK);
			if (dayOfWeek < getFirstDayOfWeek()) dayOfWeek += 7;
			day += (value - dayOfWeek);
			computeTime();
			break;
		}
		case DAY_OF_YEAR: {
			computeTime();
			int dayOfYear = get(DAY_OF_YEAR);
			day += (value - dayOfYear);
			computeTime();
			break;
		}
		case WEEK_OF_YEAR: {
			computeTime();
			int currentWeekOfYear = get(WEEK_OF_YEAR);
			add(WEEK_OF_YEAR, (value - currentWeekOfYear));
			computeTime();
			break;
		}
		}
	}

	public void add(int field, int value) {
		switch (field) {
		case YEAR: {
			year += value;
			break;
		}
		case MONTH: {
			month += value;
			break;
		}
		case DAY_OF_MONTH: {
			day += value;
			break;
		}
		case HOUR:
		case HOUR_OF_DAY: {
			hour += value;
			break;
		}
		case MINUTE: {
			minute += value;
			break;
		}
		case SECOND: {
			second += value;
			break;
		}
		case DAY_OF_WEEK: {
			day += value;
			break;
		}
		case DAY_OF_YEAR: {
			day += value;
			break;
		}
		case WEEK_OF_YEAR: {
			day += (value * 7);
			break;
		}
		}
	}

	/**
	 * Calculate the number of days between two dates
	 * 
	 * @param a Date
	 * @param b Date
	 * @return the difference in days between b and a (b - a)
	 */
	private int compareDate(Date a, Date b) {
		final Date ta = new Date(a.getTime());
		final Date tb = new Date(b.getTime());
		final long d1 = setHourToZero(ta).getTime();
		final long d2 = setHourToZero(tb).getTime();
		return (int) Math.round((d2 - d1) / 1000.0 / 60.0 / 60.0 / 24.0);
	}

	/**
	 * Set hour, minutes, second and milliseconds to zero.
	 * 
	 * @param d Date
	 * @return Modified date
	 */
	@SuppressWarnings("deprecation")
	private Date setHourToZero(Date in) {
		final Date d = new Date(in.getTime());
		d.setHours(0);
		d.setMinutes(0);
		d.setSeconds(0);
		// a trick to set milliseconds to zero
		long t = d.getTime() / 1000;
		t = t * 1000;
		return new Date(t);
	}
	
	/**
	 * Gets the date of the first week for the specified year.
	 * @param year This is year - 1900 as returned by Date.getYear()
	 * @return
	 */
	@SuppressWarnings("deprecation")
	private Date getWeekOne(int year) {
		GregorianCalendar weekOne = new GregorianCalendar();
		weekOne.setFirstDayOfWeek(getFirstDayOfWeek());
		weekOne.setMinimalDaysInFirstWeek(getMinimalDaysInFirstWeek());
		weekOne.setTime(new Date(year, 0, 1));
		// can we use the week of 1/1/year as week one?
		int dow = weekOne.get(DAY_OF_WEEK);
		if (dow < weekOne.getFirstDayOfWeek()) dow += 7;
		int eow = weekOne.getFirstDayOfWeek() + 7;
		if ((eow - dow) < weekOne.getMinimalDaysInFirstWeek()) {
			// nope, week one is the following week
			weekOne.add(DATE, 7);
		}
		weekOne.set(DAY_OF_WEEK, weekOne.getFirstDayOfWeek());
		return weekOne.getTime();
	}
}
