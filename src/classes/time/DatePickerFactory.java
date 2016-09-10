package classes.time;

import java.time.LocalDate;

import com.github.lgooddatepicker.datepicker.DatePicker;

public class DatePickerFactory {
	
	public static DatePicker buildTodayDatePicker()
	{
		return buildEndDatePicker();
	}

	public static DatePicker buildStartDatePicker() {

		DatePicker startTime = new DatePicker();

		switch (LocalDate.now().getDayOfWeek()) {
		case MONDAY:
			startTime.setDate(LocalDate.now().minusDays(1));
			break;
		case TUESDAY:
			startTime.setDate(LocalDate.now().minusDays(2));
			break;
		case WEDNESDAY:
			startTime.setDate(LocalDate.now().minusDays(3));
			break;
		case THURSDAY:
			startTime.setDate(LocalDate.now().minusDays(4));
			break;
		case FRIDAY:
			startTime.setDate(LocalDate.now().minusDays(5));
			break;
		case SATURDAY:
			startTime.setDate(LocalDate.now().minusDays(6));
			break;
		case SUNDAY:
			startTime.setDate(LocalDate.now());
			break;

		}
		return startTime;

	}

	public static DatePicker buildEndDatePicker() {

		DatePicker endTime = new DatePicker();
		endTime.setDate(LocalDate.now());
		return endTime;
	}
}
