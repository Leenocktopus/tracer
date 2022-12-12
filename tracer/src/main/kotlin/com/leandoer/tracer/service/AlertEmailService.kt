package com.leandoer.tracer.service

import com.leandoer.tracer.model.entity.AlertEvent
import liquibase.pro.packaged.it
import org.springframework.mail.SimpleMailMessage
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.stereotype.Service

@Service
class AlertEmailService(
    private val emailSender: JavaMailSender
) {

    fun sendAlertEvents(alertEvents: List<AlertEvent>) {
        alertEvents.forEach { alertEvent ->
            alertEvent.alert.contacts.forEach { contact ->
                val to = contact.email
                val subject = "Alert '${alertEvent.alert.name}' triggered"
                val text = alertEvent.reason + " Переглянути сповіщення і тести: http://localhost:3000/alerts?alertId=${alertEvent.alert.id}"
                sendEmail(to, subject, text)
            }
        }
    }

    fun sendEmail(to: String, subject: String, text: String) {
        val message = SimpleMailMessage()
        message.setTo(to)
        message.setSubject(subject)
        message.setText(text)
        emailSender.send(message)
    }
}