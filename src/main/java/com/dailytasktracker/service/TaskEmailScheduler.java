package com.dailytasktracker.service;

import com.dailytasktracker.model.Account;
import com.dailytasktracker.model.Task;
import com.dailytasktracker.repository.AccountRepository;
import com.dailytasktracker.repository.TaskRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

@Service
public class TaskEmailScheduler {

    private static final Logger logger = LoggerFactory.getLogger(TaskEmailScheduler.class);

    private final TaskRepository taskRepository;
    private final AccountRepository accountRepository;
    private final JavaMailSender mailSender;

    public TaskEmailScheduler(TaskRepository taskRepository, AccountRepository accountRepository, JavaMailSender mailSender) {
        this.taskRepository = taskRepository;
        this.accountRepository = accountRepository;
        this.mailSender = mailSender;
    }

    @Scheduled(cron = "0 0 8,17,21 * * ?", zone = "America/New_York")
    //@Scheduled(cron = "0 */1 * * * ?", zone = "America/New_York") // Executes every 2 minutes in EST
    public void sendDailyTasks() {
        try {
            ZonedDateTime currentTime = ZonedDateTime.now(ZoneId.of("America/New_York"));
            logger.info("sendDailyTasks : Task scheduler triggered at: {}", currentTime);

            List<Account> accounts = accountRepository.findAll();

            for (Account account : accounts) {
                try {
                    List<Task> tasks = taskRepository.findByAccountOrderByDateCreatedDesc(account);

                    if (!tasks.isEmpty()) {
                        String taskList = generateTaskList(account.getFirstName(), tasks);
                        sendEmail(account.getEmail(), "Daily Task Reminder", taskList);
                        logger.info("sendDailyTasks : Email sent to account: {}", account.getEmail());
                    }
                } catch (Exception e) {
                    logger.error("sendDailyTasks : Failed to process tasks for account: {}", account.getEmail(), e);
                }
            }
        } catch (Exception e) {
            logger.error("sendDailyTasks: Error occurred during task scheduling: ", e);
        }
    }

    private String generateTaskList(String firstName, List<Task> tasks) {

        StringBuilder taskListBuilder = new StringBuilder()
                .append("Hi ").append(firstName).append(",\n\n")
                .append("The following are your 📋 *Task List*:\n\n");

        int taskNumber = 1;
        for (Task task : tasks) {
            taskListBuilder.append(taskNumber++)
                    .append(". ")
                    .append(task.getDescription())
                    .append("\n");
        }

        taskListBuilder.append("\n🎯 Keep up the great work!")
                .append("\n\n💡 Log in to the application to update your tasks.")
                .append("\n🌐 http://www.dttsam.com,\n")
                .append("\n\nThanks,\n")
                .append("DailyTaskTracker");

        return taskListBuilder.toString();
    }


    public void sendEmail(String to, String subject, String body) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message);

            // Set the sender's display name and email address
            helper.setFrom("dailytasktracker333@gmail.com", "DailyTaskTracker");

            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(body);

            // Send the email
            mailSender.send(message);
            logger.info("Email sent successfully to {}", to);
        } catch (MessagingException | MailException | UnsupportedEncodingException e) {
            logger.error("sendEmail : Error sending email to: {}", to, e);
        }
    }
}