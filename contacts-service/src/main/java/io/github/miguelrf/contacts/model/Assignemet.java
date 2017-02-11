package io.github.miguelrf.contacts.model;

import java.time.YearMonth;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import com.google.common.base.MoreObjects;

public class Assignemet {

	@NotNull
	private YearMonth month;

	@Min(0)
	@Max(31)
	@NotNull
	private int daysCount;

	// used for RDB/JPA, not necessary with MongoDB
	// private Contact contact;

	public Assignemet() {
	}

	public Assignemet(YearMonth month, int daysCount
	// , Contact contact
	) {
		this.month = month;
		this.daysCount = daysCount;
		// this.contact = contact;
	}

	public YearMonth getMonth() {
		return month;
	}

	public void setMonth(YearMonth month) {
		this.month = month;
	}

	public int getDaysCount() {
		return daysCount;
	}

	public void setDaysCount(int daysCount) {
		this.daysCount = daysCount;
	}

	@Override
	public String toString() {
		return MoreObjects.toStringHelper(Assignemet.class)
				.add("month", month).add("daysCount", daysCount).toString();
	}
}
