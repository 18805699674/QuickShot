package cn.iichen.quickshot.utils.mail

import java.util.*
import javax.mail.*
import javax.mail.internet.*


/**
 * 邮件发送器    lluozbrfagdlcadd    "1826522282@qq.com","123456"
 */
class MailSender {
    companion object{
        fun send(toAddress:String,code : String) {
            val sms = MailSender()
            Thread {
                sms.sendTextMail(toAddress,code)
            }.start()
        }
    }


    fun sendTextMail(toAddress:String,code : String): Boolean {

        // 判断是否需要身份认证
        var authenticator: MyAuthenticator? = null

        val pro: Properties = Properties()
        pro["mail.smtp.host"] = "smtp.qq.com"
        pro["mail.smtp.port"] = "587"
        pro["mail.transport.protocol"] = "smtp"
        pro["mail.smtp.auth"] = true

        // 如果需要身份认证，则创建一个密码验证器      PEBKOSOXCGSTDEVW
        authenticator = MyAuthenticator("475539335@qq.com", "zdtytasotxmhbgeh")
        // 根据邮件会话属性和密码验证器构造一个发送邮件的session
        val sendMailSession = Session.getDefaultInstance(pro)

        try {
            // 根据session创建一个邮件消息
            val mailMessage: Message = MimeMessage(sendMailSession)
            // 创建邮件发送者地址
            val from: Address = InternetAddress("475539335@qq.com")
            // 设置邮件消息的发送者
            mailMessage.setFrom(from)
            // 创建邮件的接收者地址，并设置到邮件消息中
            val to: Address = InternetAddress(toAddress)
            mailMessage.setRecipient(Message.RecipientType.TO, to)
            // 设置邮件消息的主题
            mailMessage.subject = "IIchen"
            // 设置邮件消息发送的时间
            mailMessage.sentDate = Date()

            // 设置邮件消息的主要内容
            val mailContent: String = "您的验证码：$code,5分钟内有效 请尽快验证"
            mailMessage.setText(mailContent)
            mailMessage.saveChanges()
            val transport = sendMailSession.transport
            transport.connect(authenticator.username,authenticator.password)
            // 发送邮件
            transport.sendMessage(mailMessage,mailMessage.allRecipients)
            transport.close()
            return true
        } catch (ex: MessagingException) {
            ex.printStackTrace()
        }
        return false
    }
}


class MyAuthenticator(var username: String?, var password: String?) : Authenticator() {

    override fun getPasswordAuthentication(): PasswordAuthentication {
        return PasswordAuthentication(username, password)
    }
}

