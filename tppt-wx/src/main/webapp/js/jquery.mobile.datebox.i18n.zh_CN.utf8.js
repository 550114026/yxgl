/*
 * jQuery Mobile Framework : plugin to provide a date and time picker.
 * Copyright (c) JTSage
 * CC 3.0 Attribution.  May be relicensed without permission/notifcation.
 * https://github.com/jtsage/jquery-mobile-datebox
 *
 * Translation by: J.T.Sage <jtsage@gmail.com>
 *
 */

jQuery.extend(jQuery.mobile.datebox.prototype.options.lang, {
  'zh': {
    setDateButtonLabel: "确定",
//    setTimeButtonLabel: "Set Time",
//    setDurationButtonLabel: "Set Duration",
    calTodayButtonLabel: "今天",
    titleDateDialogLabel: "选择日期",
    titleTimeDialogLabel: "选择时间",
    daysOfWeek: ['星期日','星期一','星期二','星期三','星期四','星期五','星期六'], 
    daysOfWeekShort: ['周日','周一','周二','周三','周四','周五','周六'],
    monthsOfYear: ['一月','二月','三月','四月','五月','六月',  
            '七月','八月','九月','十月','十一月','十二月'],
    monthsOfYearShort: ['一','二','三','四','五','六',  
            '七','八','九','十','十一','十二'],
    durationLabel: ["Days", "Hours", "Minutes", "Seconds"],
    durationDays: ["Day", "Days"],
    tooltip: "选择日期",
    nextMonth: '下月>', 
    prevMonth: '<上月',
    timeFormat: 12,
    headerFormat: '',
    dateFieldOrder: ['y','m','d'],
    timeFieldOrder: ['h', 'i', 'a'],
    slideFieldOrder: ['y', 'm', 'd'],
    dateFormat: "%Y-%m-%d",
    useArabicIndic: false,
    isRTL: false,
    calStartDay: 0,
    clearButton: "Clear",
    durationOrder: ['d', 'h', 'i', 's'],
    meridiem: ["AM", "PM"],
    timeOutput: "%l:%M %p",
    durationFormat: "%Dd %DA, %Dl:%DM:%DS"
  }
});
jQuery.extend(jQuery.mobile.datebox.prototype.options, {
  useLang: 'zh'
});
