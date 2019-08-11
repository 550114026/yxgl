package com.ikkong.core.base;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.ikkong.core.model.AjaxResult;
import com.ikkong.core.toolbox.Func;
import com.ikkong.core.toolbox.kit.DateKit;
import com.jfinal.kit.StrKit;
import com.jfinal.validate.Validator;

public abstract class BaseValidator extends Validator {
	protected AjaxResult result = new AjaxResult();
	
	/**
	 * Add message when validate failure.
	 */
	protected void addError(String errorMessage) {
		if (!invalid) {
			this.invalid = true;
			result.addError(errorMessage);
			//通过这个方法触发短路
			setShortCircuit(true);
			addError("", "");
		} else {
			throw new RuntimeException();
		}
	}
	
	/**
	 * Judge whether the two values are equal
	 */
	protected void validateTwoEqual(String field1, String field2, String errorMessage) {
		if (Func.isAllEmpty(field1, field2)) {// 字符串为 null 或者为 "" 时返回 true
			addError(errorMessage);
		}
		String value1 = getController().getPara(field1);
		String value2 = getController().getPara(field2);
		if (!value1.equals(value2)) {
			addError(errorMessage);
		}
	}
	
	/**
	 * Judge whether the two values are not equal
	 */
	protected void validateTwoNotEqual(String field1, String field2, String errorMessage) {
		if (Func.isAllEmpty(field1, field2)) {// 字符串为 null 或者为 "" 时返回 true
			addError(errorMessage);
		}
		String value1 = getController().getPara(field1);
		String value2 = getController().getPara(field2);
		if (value1.equals(value2)) {
			addError(errorMessage);
		}
	}

	/**
	 * Check sql
	 */
	protected void validateSql(String field, String errorMessage) {
		if (StrKit.isBlank(field)) {// 字符串为 null 或者为 "" 时返回 true
			addError(errorMessage);
		}
		String sql = getController().getPara(field);
		sql = sql.toLowerCase();
		if (sql.indexOf("delete") >= 0 || sql.indexOf("update") >= 0
				|| sql.indexOf("insert") >= 0 || sql.indexOf("drop") >= 0) {
			addError(errorMessage);
		}
	}

	/**
	 * Validate the illegal characters
	 */
	protected void validateStringExt(String field, String errorMessage) {
		if (Func.isAllEmpty(field)) {// 字符串为 null 或者为 "" 时返回 true
			addError(errorMessage);
		}
		String val = getController().getPara(field);
		if (val.indexOf("<") >= 0) {
			addError(errorMessage);
		}
	}

	/**
	 * Validate Required. Allow space characters.
	 */
	protected void validateRequired(String field, String errorMessage) {
		String value = getController().getPara(field);
		if (value == null || "".equals(value)) // 经测试,form表单域无输入时值为"",跳格键值为"\t",输入空格则为空格" "
			addError(errorMessage);
	}

	/**
	 * Validate required string.
	 */
	protected void validateRequiredString(String field, String errorMessage) {
		if (StrKit.isBlank(getController().getPara(field)))
			addError(errorMessage);
	}

	/**
	 * Validate integer.
	 */
	protected void validateInteger(String field, int min, int max,
			String errorMessage) {
		validateIntegerValue(getController().getPara(field), min, max,
				errorMessage);
	}

	private void validateIntegerValue(String value, int min, int max,
			String errorMessage) {
		if (StrKit.isBlank(value)) {
			addError(errorMessage);
			return;
		}
		try {
			int temp = Integer.parseInt(value.trim());
			if (temp < min || temp > max)
				addError(errorMessage);
		} catch (Exception e) {
			addError(errorMessage);
		}
	}

	/**
	 * Validate integer.
	 */
	protected void validateInteger(String field, String errorMessage) {
		validateIntegerValue(getController().getPara(field), errorMessage);
	}

	private void validateIntegerValue(String value, String errorMessage) {
		if (StrKit.isBlank(value)) {
			addError(errorMessage);
			return;
		}
		try {
			Integer.parseInt(value.trim());
		} catch (Exception e) {
			addError(errorMessage);
		}
	}

	/**
	 * Validate long.
	 */
	protected void validateLong(String field, long min, long max,
			String errorMessage) {
		validateLongValue(getController().getPara(field), min, max, errorMessage);
	}

	private void validateLongValue(String value, long min, long max,
			String errorMessage) {
		if (StrKit.isBlank(value)) {
			addError(errorMessage);
			return;
		}
		try {
			long temp = Long.parseLong(value.trim());
			if (temp < min || temp > max)
				addError(errorMessage);
		} catch (Exception e) {
			addError(errorMessage);
		}
	}

	/**
	 * Validate long.
	 */
	protected void validateLong(String field, String errorMessage) {
		validateLongValue(getController().getPara(field), errorMessage);
	}

	private void validateLongValue(String value, String errorMessage) {
		if (StrKit.isBlank(value)) {
			addError(errorMessage);
			return;
		}
		try {
			Long.parseLong(value.trim());
		} catch (Exception e) {
			addError(errorMessage);
		}
	}

	/**
	 * Validate double.
	 */
	protected void validateDouble(String field, double min, double max,
			String errorMessage) {
		String value = getController().getPara(field);
		if (StrKit.isBlank(value)) {
			addError(errorMessage);
			return;
		}
		try {
			double temp = Double.parseDouble(value.trim());
			if (temp < min || temp > max)
				addError(errorMessage);
		} catch (Exception e) {
			addError(errorMessage);
		}
	}

	/**
	 * Validate double.
	 */
	protected void validateDouble(String field, String errorMessage) {
		String value = getController().getPara(field);
		if (StrKit.isBlank(value)) {
			addError(errorMessage);
			return;
		}
		try {
			Double.parseDouble(value.trim());
		} catch (Exception e) {
			addError(errorMessage);
		}
	}

	/**
	 * Validate date. Date formate: yyyy-MM-dd
	 */
	protected void validateDate(String field, String errorMessage) {
		String value = getController().getPara(field);
		if (StrKit.isBlank(value)) {
			addError(errorMessage);
			return;
		}
		if (!DateKit.isValidDate(Func.format(value),"yyyy-MM-dd")) {
			addError(errorMessage);
		}
	}

	/**
	 * Validate date.
	 */
	protected void validateDate(String field, Date min, Date max,
			String errorMessage) {
		String value = getController().getPara(field);
		if (StrKit.isBlank(value)) {
			addError(errorMessage);
			return;
		}
		try {
			Date temp = DateKit.parseTime(Func.format(value));
			if (temp.before(min) || temp.after(max))
				addError(errorMessage);
		} catch (Exception e) {
			addError(errorMessage);
		}
	}

	/**
	 * Validate date. Date formate: yyyy-MM-dd
	 */
	protected void validateDate(String field, String min, String max,
			String errorMessage) {
		// validateDate(field, Date.valueOf(min), Date.valueOf(max), errorKey,
		// errorMessage); 为了兼容 64位 JDK
		try {
			validateDate(field, DateKit.parseTime(Func.format(min)),
					DateKit.parseTime(Func.format(max)), errorMessage);
		} catch (Exception e) {
			addError(errorMessage);
		}
	}

	/**
	 * Validate equal field. Usually validate password and password again
	 */
	protected void validateEqualField(String field_1, String field_2,
			String errorMessage) {
		String value_1 = getController().getPara(field_1);
		String value_2 = getController().getPara(field_2);
		if (value_1 == null || value_2 == null || (!value_1.equals(value_2)))
			addError(errorMessage);
	}

	/**
	 * Validate equal string.
	 */
	protected void validateEqualString(String s1, String s2, String errorMessage) {
		if (s1 == null || s2 == null || (!s1.equals(s2)))
			addError(errorMessage);
	}

	/**
	 * Validate equal integer.
	 */
	protected void validateEqualInteger(Integer i1, Integer i2,
			String errorMessage) {
		if (i1 == null || i2 == null || (i1.intValue() != i2.intValue()))
			addError(errorMessage);
	}

	/**
	 * Validate email.
	 */
	protected void validateEmail(String field, String errorMessage) {
		validateRegex(field, emailAddressPattern, false, errorMessage);
	}

	/**
	 * Validate URL.
	 */
	protected void validateUrl(String field, String errorMessage) {
		String value = getController().getPara(field);
		if (StrKit.isBlank(value)) {
			addError(errorMessage);
			return;
		}
		try {
			value = value.trim();
			if (value.startsWith("https://"))
				value = "http://" + value.substring(8); // URL doesn't
														// understand the https
														// protocol, hack it
			new URL(value);
		} catch (MalformedURLException e) {
			addError(errorMessage);
		}
	}

	/**
	 * Validate regular expression.
	 */
	protected void validateRegex(String field, String regExpression,
			boolean isCaseSensitive, String errorMessage) {
		String value = getController().getPara(field);
		if (value == null) {
			addError(errorMessage);
			return;
		}
		Pattern pattern = isCaseSensitive ? Pattern.compile(regExpression)
				: Pattern.compile(regExpression, Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(value);
		if (!matcher.matches())
			addError(errorMessage);
	}

	/**
	 * Validate regular expression and case sensitive.
	 */
	protected void validateRegex(String field, String regExpression,
			String errorMessage) {
		validateRegex(field, regExpression, true, errorMessage);
	}

	/**
	 * Validate string.
	 */
	protected void validateString(String field, int minLen, int maxLen,
			String errorMessage) {
		validateStringValue(getController().getPara(field), minLen, maxLen,
				errorMessage);
	}

	private void validateStringValue(String value, int minLen, int maxLen,
			String errorMessage) {
		if (StrKit.isBlank(value)) {
			addError(errorMessage);
			return;
		}
		if (value.length() < minLen || value.length() > maxLen)
			addError(errorMessage);
	}

	/**
	 * validate boolean.
	 */
	protected void validateBoolean(String field, String errorMessage) {
		validateBooleanValue(getController().getPara(field), errorMessage);
	}

	private void validateBooleanValue(String value, String errorMessage) {
		if (StrKit.isBlank(value)) {
			addError(errorMessage);
			return;
		}
		value = value.trim().toLowerCase();
		if ("1".equals(value) || "true".equals(value)) {
			return;
		} else if ("0".equals(value) || "false".equals(value)) {
			return;
		}
		addError(errorMessage);
	}
}
