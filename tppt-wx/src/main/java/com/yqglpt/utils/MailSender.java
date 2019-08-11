package com.yqglpt.utils;
//package com.bookhouse.utils;
//
//import java.io.StringWriter;
//import java.util.List;
//import java.util.Locale;
//import java.util.Map;
//
//import javax.mail.internet.MimeMessage;
//
//import org.springframework.mail.javamail.JavaMailSenderImpl;
//import org.springframework.mail.javamail.MimeMessageHelper;
//
//import com.google.common.base.Splitter;
//import com.google.common.collect.Lists;
//import com.google.common.collect.Maps;
//import com.honzh.work.model.Mail;
//import com.honzh.work.model.Note;
//import com.honzh.work.model.NoteWeek;
//
//import freemarker.template.Configuration;
//import freemarker.template.Template;
//
//public class MailSender {
//	private Configuration cfg = new Configuration();
//	private JavaMailSenderImpl mailSender;// spring配置中定义
//	private String from;
//	private String to;
//	private String subject;
//	private String templateName;
//	private String password;
//
//	public MailSender(JavaMailSenderImpl mailSender) {
//		this.mailSender = mailSender;
//	}
//
//	/**
//	 * 发送模板邮件，支持html
//	 * 
//	 * @param parmas
//	 *            额外参数
//	 * @param note
//	 *            日志信息
//	 * @param weeks
//	 *            周报信息
//	 */
//	public void sendWithHtmlTemplate(final Map<String, String> parmas, List<Note> notes, NoteWeek week) {
//		final Map<String, String> model = Maps.newHashMap();
//		model.put("title", parmas.get("title"));// 标题
//		model.put("weekplan", week.getStr("weekplan") == null ? "无" : week.getStr("weekplan"));
//		model.put("weekplanexecution", week.getStr("weekplanexecution") == null ? "无" : week
//				.getStr("weekplanexecution"));
//		model.put("problem", week.getStr("problem") == null ? "无" : week.getStr("problem"));
//		model.put("method", week.getStr("method") == null ? "无" : week.getStr("method"));
//		model.put("idea", week.getStr("idea") == null ? "无" : week.getStr("idea"));
//		String noteStr = "";
//		for (Note note : notes) {
//			noteStr += noteStr(note.getStr("date"), note.getStr("daywork"), note.getStr("problem"), note
//					.getStr("method"), note.getStr("otherwork"));
//		}
//		model.put("notes", noteStr);
//		List<String> list = Lists.newArrayList(Splitter.on(",").split(to));
//		list.add(from);// 本人邮箱保存一份
//		for (final String toString : list) {
//			new Thread() {
//				public void run() {
//					mailSender.setUsername(from);
//					mailSender.setPassword(password);
//					MimeMessage mime = mailSender.createMimeMessage();
//					MimeMessageHelper helper = new MimeMessageHelper(mime);
//					String result = null;
//					// 邮件发送记录
//					Mail mail = new Mail();
//					mail.set("userid", parmas.get("userid"));
//					mail.set("sender", from);
//					mail.set("receiver", toString);
//					mail.set("title", parmas.get("title"));
//					try {
//						helper.setTo(toString);
//						helper.setFrom(from); // 发送人
//						helper.setSubject(subject);
//						// 嵌入邮件模版
//						cfg.setEncoding(Locale.CHINA, "utf-8");
//						cfg.setClassForTemplateLoading(getClass(), "/mail");
//						Template t = cfg.getTemplate(templateName);
//						StringWriter writer = new StringWriter();
//						// t.setEncoding("utf-8");
//						t.process(model, writer);
//						result = writer.toString();
//						helper.setText(result, true);
//						// 记录邮件发送,默认为0表示发送中
//						mail.save();
//						mailSender.send(mime);
//						// 发送成功后更新状态
//						mail.set("state", 1);// 表示发送成功
//						mail.update();
//					} catch (Exception e) {
//						// 邮件发送异常不处理
//						mail.set("state", -1);// 表示发送失败
//						mail.update();
//					}
//				};
//			}.start();
//		}
//	}
//
//	public void setTo(String to) {
//		this.to = to;
//	}
//
//	public void setSubject(String subject) {
//		this.subject = subject;
//	}
//
//	public void setTemplateName(String templateName) {
//		this.templateName = templateName;
//	}
//
//	public void setFrom(String from) {
//		this.from = from;
//	}
//
//	public void setPassword(String password) {
//		this.password = password;
//	}
//
//	private String noteStr(String date, String daywork, String problem, String method, String otherwork) {
//		return "<table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\">"
//				+ "<tbody><tr height=\"20\"><td></td></tr><tr><td style=\"font-size:14px;\">"
//				+ "<span style=\" font-weight:bold;\">【" + date
//				+ "】工作情况</span>：</td></tr><tr height=\"15\"><td></td></tr><tr><td style=\"font-size:12px;\">"
//				+ "<p style=\"text-indent:2em;\">当天具体事项：" + (daywork == null ? "无" : daywork)
//				+ "</p><p style=\"text-indent:2em;\">存在问题：" + (problem == null ? "无" : problem)
//				+ "</p><p style=\"text-indent:2em;\">解决措施：" + (method == null ? "无" : method)
//				+ "</p><p style=\"text-indent:2em;\">其他工作/特别事件：" + (otherwork == null ? "无" : otherwork)
//				+ "</p></td></tr></tbody></table>";
//	}
//}
