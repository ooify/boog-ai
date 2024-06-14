package me.ooify.boogai.utils;

import cn.hutool.extra.mail.MailUtil;
import org.springframework.stereotype.Service;

@Service
public class Email {
    public void sentEmail(String email, Integer code) {
        MailUtil.send(email, "boog", "<div style=\"display: flex; flex-direction: column; justify-items: center;align-items: center; text-align: center;\">\n" +
                "    <h5 style=\"font-size:larger; font-weight: 400;\">这是您的boog书城启动代码！</h5>\n" +
                "    <div style=\"border: 1px solid #eaeaea; border-radius: 1rem; width: 40vw; color:#656565; padding: 2rem;\">\n" +
                "      输入以下代码继续注册boog书城：\n" +
                "      <div>\n" +
                "        <p\n" +
                "          style=\"padding: 1rem; background-color: #EAF5FF;width: 20vw; margin: 1rem auto; border-radius: 1rem; font-weight: bold;\">\n" +
                "          " + code + "\n" +
                "        </p>\n" +
                "      </div>\n" +
                "    </div>\n" +
                "    <div style=\"text-align: left; width: 40vw;margin-top: 1rem; font-weight: 200; font-size: 0.75rem;\">\n" +
                "      完成后，您可以开始使用 boog 的所有功能，包括：购书、购物车、订单、个人中心、个性化推荐、评论等功能。\n" +
                "      <div style=\"border-top: 1px solid #eaeaea; margin: 1rem 0;\"></div>\n" +
                "      <div style=\" font-size: 0.85rem;\">\n" +
                "        您收到这封电子邮件是因为您最近创建了一个新的 boog 帐户，请在5分钟内完成验证。如果这不是您本人，请忽略这封电子邮件。\n" +
                "      </div>\n" +
                "    </div>\n" +
                "  </div>", true);
    }
}
