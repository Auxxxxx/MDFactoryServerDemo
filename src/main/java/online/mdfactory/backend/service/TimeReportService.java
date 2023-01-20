package online.mdfactory.backend.service;

import online.mdfactory.backend.excel.XlsGenerator;
import online.mdfactory.backend.model.Employee;
import online.mdfactory.backend.model.Shift;
import online.mdfactory.backend.excel.TimeReportLine;
import online.mdfactory.backend.repository.EmployeeRepository;
import online.mdfactory.backend.repository.ShiftRepository;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class TimeReportService {
    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private ShiftRepository shiftRepository;

    public XSSFWorkbook buildSingleLogEntryXls(String login, LocalDate today) {
        XlsGenerator excelGenerator = new XlsGenerator("Ежедневный отчёт по времени");
        excelGenerator.skip(1);
        excelGenerator.writeLine(getHeaderValues(), true, 16);
        excelGenerator.skip(1);
        excelGenerator.writeLine(getEmployeesLineValues(List.of(login)), false, 14);
        excelGenerator.skip(1);
        excelGenerator.writeLine(getPeriodLineValues(today, today), false, 14);
        excelGenerator.skip(2);

        Employee employee = findEmployee(login);
        List<Shift> shifts = findShifts(login, today, today);
        writeEmployee(excelGenerator, employee, shifts);

        return excelGenerator.get();
    }

    public XSSFWorkbook buildTimeXls(List<String> employeeLogins, LocalDate start, LocalDate end) {
        XlsGenerator excelGenerator = new XlsGenerator("Отчёт по времени");
        excelGenerator.skip(1);
        excelGenerator.writeLine(getHeaderValues(), true, 16);
        excelGenerator.skip(1);
        excelGenerator.writeLine(getEmployeesLineValues(employeeLogins), false, 14);
        excelGenerator.skip(1);
        excelGenerator.writeLine(getPeriodLineValues(start, end), false, 14);
        excelGenerator.skip(2);
        for (String login : employeeLogins) {
            Employee employee = findEmployee(login);
            List<Shift> shifts = findShifts(login, start, end);
            writeEmployee(excelGenerator, employee, shifts);
        }
        return excelGenerator.get();
    }

    private void writeEmployee(XlsGenerator excelGenerator, Employee employee, List<Shift> shifts) {
        excelGenerator.writeLine(getEmployeeNameValues(employee.getName()), true, 14);
        excelGenerator.skip(1);
        excelGenerator.writeLine(getTimeHeadingValues(), false, 14);

        TimeReportLine total = TimeReportLine.empty();
        for (Shift shift : shifts) {
            TimeReportLine timeReportLine = TimeReportLine.of(shift);
            total.plus(timeReportLine);
            excelGenerator.writeLine(getTimeValues(timeReportLine), false, 14);
        }
        excelGenerator.writeLine(getTotalValues(total), true, 14);
        excelGenerator.skip(1);
    }

    private List<XlsGenerator.RowValue> getHeaderValues() {
        List<XlsGenerator.RowValue> headerValues = new ArrayList<>();
        headerValues.add(new XlsGenerator.RowValue(1, 8, "Отчёт по времени работы над партией и простоя"));
        return headerValues;
    }

    private List<XlsGenerator.RowValue> getEmployeesLineValues(List<String> logins) {
        List<XlsGenerator.RowValue> employeesLineValues = new ArrayList<>();
        employeesLineValues.add(new XlsGenerator.RowValue(1, 3, "Выбранные сотрудники"));
        employeesLineValues.add(new XlsGenerator.RowValue(4, 8, String.join(",", logins)));
        return employeesLineValues;
    }

    private List<XlsGenerator.RowValue> getPeriodLineValues(LocalDate start, LocalDate end) {
        List<XlsGenerator.RowValue> periodLineValues = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        periodLineValues.add(new XlsGenerator.RowValue(1, 3, "Выбранный период"));
        periodLineValues.add(new XlsGenerator.RowValue(4,  formatter.format(start)));
        periodLineValues.add(new XlsGenerator.RowValue(5,  "-"));
        periodLineValues.add(new XlsGenerator.RowValue(6,  formatter.format(end)));
        return periodLineValues;
    }

    private Employee findEmployee(String employeeLogin) {
        return employeeRepository.findById(employeeLogin).orElseThrow();
    }

    private List<Shift> findShifts(String employeeLogin, LocalDate start, LocalDate end) {
        return shiftRepository.findShiftsInPeriod(employeeLogin, start, end);
    }

    private List<XlsGenerator.RowValue> getEmployeeNameValues(String name) {
        List<XlsGenerator.RowValue> employeeNameValues = new ArrayList<>();
        employeeNameValues.add(new XlsGenerator.RowValue(1, 8, name));
        return employeeNameValues;
    }

    private List<XlsGenerator.RowValue> getTimeHeadingValues() {
        List<XlsGenerator.RowValue> timeHeadingValues = new ArrayList<>();
        timeHeadingValues.add(new XlsGenerator.RowValue(1, 1, 3, "Дата"));
        timeHeadingValues.add(new XlsGenerator.RowValue(2, "Начало\n рабочей\n смены"));
        timeHeadingValues.add(new XlsGenerator.RowValue(3, "Окончание\n рабочей\n смены"));
        timeHeadingValues.add(new XlsGenerator.RowValue(4, "Работа над\n партиями"));
        timeHeadingValues.add(new XlsGenerator.RowValue(5, "Время простоя\n между партиями"));
        timeHeadingValues.add(new XlsGenerator.RowValue(6, "Перерывы"));
        timeHeadingValues.add(new XlsGenerator.RowValue(7, "Время простоя\n вне партий"));
        timeHeadingValues.add(new XlsGenerator.RowValue(8, "Закрыл рабочую\n смену (ДА/НЕТ)"));
        return timeHeadingValues;
    }
    private List<XlsGenerator.RowValue> getTimeValues(TimeReportLine timeReportLine) {
        List<XlsGenerator.RowValue> timeValues = new ArrayList<>();
        timeValues.add(new XlsGenerator.RowValue(1, timeReportLine.getDate()));
        timeValues.add(new XlsGenerator.RowValue(2, timeReportLine.getStartTime()));
        timeValues.add(new XlsGenerator.RowValue(3, timeReportLine.getFinishTime()));
        timeValues.add(new XlsGenerator.RowValue(4, timeReportLine.getBatchDuration()));
        timeValues.add(new XlsGenerator.RowValue(5, timeReportLine.getInnerWaitingDuration()));
        timeValues.add(new XlsGenerator.RowValue(6, timeReportLine.getBreakDuration()));
        timeValues.add(new XlsGenerator.RowValue(7, timeReportLine.getOuterWaitingDuration()));
        timeValues.add(new XlsGenerator.RowValue(8, timeReportLine.getFinishedManually()));
        return timeValues;
    }

    private List<XlsGenerator.RowValue> getTotalValues(TimeReportLine timeReportLine) {
        List<XlsGenerator.RowValue> totalValues = new ArrayList<>();
        totalValues.add(new XlsGenerator.RowValue(1, 3, "Итого:"));
        totalValues.add(new XlsGenerator.RowValue(4, timeReportLine.getBatchDuration()));
        totalValues.add(new XlsGenerator.RowValue(5, timeReportLine.getInnerWaitingDuration()));
        totalValues.add(new XlsGenerator.RowValue(6, timeReportLine.getBreakDuration()));
        totalValues.add(new XlsGenerator.RowValue(7, timeReportLine.getOuterWaitingDuration()));
        return totalValues;
    }
}
