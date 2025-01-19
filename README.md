# Date Time Utility
[![GitHub](https://img.shields.io/badge/GitHub-Repository-blue)](https://github.com/seleniumbrain/date-time-utils)

Date Time Utility provides support to handle any date string in multiple ways.

## Features

- Validate date strings against supported formats
- Determine the format of a given date string
- Format date strings from one format to another
- Convert date strings to `LocalDateTime`, `LocalDate`, `LocalTime`, and `Date` objects
- Compare date and date-time strings
- Calculate the difference between dates and times
- Calculate the number of workdays between dates

## Tools & Technologies

| Tools/Technologies                                                                                                                                 |                Name                |       Version       |
|:---------------------------------------------------------------------------------------------------------------------------------------------------|:----------------------------------:|:-------------------:|
| <img src="https://user-images.githubusercontent.com/25181517/117201156-9a724800-adec-11eb-9a9d-3cd0f67da4bc.png" alt="JDK" width="40" />           |              **JDK**               |        `17+`        |
| <img src="https://user-images.githubusercontent.com/25181517/117207242-07d5a700-adf4-11eb-975e-be04e62b984b.png" alt="Maven" width="40" />         |             **Maven**              |       `3.8.9`       |
| <img src="https://user-images.githubusercontent.com/25181517/190229463-87fa862f-ccf0-48da-8023-940d287df610.png" alt="Lombok" width="40" />        |             **Lombok**             |      `Latest`       |
| <img src="https://user-images.githubusercontent.com/25181517/192108890-200809d1-439c-4e23-90d3-b090cf9a4eea.png" alt="IntelliJ Idea" width="40" /> | **Recommended IDE I   - IntelliJ** | `Community Edition` |
| <img src="https://user-images.githubusercontent.com/25181517/192108891-d86b6220-e232-423a-bf5f-90903e6887c3.png" alt="VS Code" width="40" />       |  **Recommended IDE II - VS Code**  | `Community Edition` |
| <img src="https://user-images.githubusercontent.com/25181517/192108892-6e9b5cdf-4e35-4a70-ad9a-801a93a07c1c.png" alt="Eclipse" width="40" />       | **Recommended IDE III - Eclipse**  | `Community Edition` |

## Installation

Add the following dependency to your `pom.xml` file:

```xml
<dependency>
    <groupId>io.github.seleniumbrain</groupId>
    <artifactId>date-time-util</artifactId>
    <version>1.0.0</version>
</dependency>
```

## Usage

### Validate Date Strings

```java
DateTimeUtils utils = new DateTimeUtils(Locale.US);
boolean isValid = utils.isValid("2024-12-20 14:30:00");
System.out.println(isValid); // Output: true
```

### Determine Date Format

```java
String format = utils.getFormatOf("2024-12-20 14:30:00");
System.out.println(format); // Output: yyyy-MM-dd HH:mm:ss
```

### Format Date Strings

```java
String formattedDate = utils.formatTo("12/20/2024 02:30:00 PM", DateTimeFormat.FORMAT_ISO_LOCAL_DATE);
System.out.println(formattedDate); // Output: 2024-12-20
```

### Convert Date Strings

```java
LocalDateTime dateTime = utils.toLocalDateTime("2024-12-20 14:30:00");
System.out.println(dateTime); // Output: 2024-12-20T14:30
```

### Compare Dates

```java
boolean isSameDate = utils.isDateEqualTo("2024-12-20", "2024-12-20T14:30:00+05:30");
System.out.println(isSameDate); // Output: true
```

### Calculate Days Between Dates

```java
long days = utils.calculateDaysBetween("2024-12-20", "2024-12-25");
System.out.println(days); // Output: 5
```

### Calculate Workdays Between Dates

```java
long workdays = utils.calculateWorkdaysBetween("2024-12-20", "2024-12-25");
System.out.println(workdays); // Output: 3
```

## License

This project is licensed under the MIT License—see the LICENSE file for details.

## Contact

For any questions or suggestions, please contact the project maintainers `rajoviyaa.s@gmail.com`

---

This `README.md` provides an overview of the `date-time-util` project, its features, installation instructions, and usage examples.