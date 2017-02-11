package io.github.miguelrf.contacts.util.ser;

import java.time.YearMonth;

import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.joda.PackageVersion;

@SuppressWarnings("serial")
public class Java8TimeModule extends SimpleModule {

	public Java8TimeModule() {
		super(PackageVersion.VERSION);

		addDeserializer(YearMonth.class, new YearMonthDeserializer());
		addSerializer(YearMonth.class, new YearMonthSerializer());
	}
}
