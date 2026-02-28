package top.tankimzeg.editorial_system.listener;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import top.tankimzeg.editorial_system.events.*;
import top.tankimzeg.editorial_system.service.NotificationService;

@Component
@RequiredArgsConstructor
public class NotificationEventListener {

    private final NotificationService notificationService;
    private final TemplateEngine templateEngine;

    // ------------------- 给作者发送通知 -------------------

    @TransactionalEventListener
    public void onManuscriptSubmitted(ManuscriptSubmittedEvent event) {
        var manuscript = event.manuscript();
        Context ctx = new Context();
        ctx.setVariable("title", manuscript.getTitle());
        ctx.setVariable("author", manuscript.getAuthor().getRealName());
        String html = templateEngine.process("mail/manuscript-submitted", ctx);
        notificationService.sendHtml(manuscript.getAuthor().getEmail(), "稿件已提交", html);
    }

    @TransactionalEventListener
    public void onRevisionRequested(RevisionTaskEvent event) {
        var process = event.process();
        Context ctx = new Context();
        ctx.setVariable("title", process.getManuscript().getTitle());
        String html = templateEngine.process("mail/revision-requested", ctx);
        notificationService.sendHtml(process.getManuscript().getAuthor().getEmail(), "稿件修改通知", html);
    }

    @TransactionalEventListener
    public void onReviewDecisionFinalized(FinishedReviewDecisionEvent event) {
        var manuscript = event.manuscript();
        var review = event.review();
        Context ctx = new Context();
        ctx.setVariable("title", manuscript.getTitle());
        ctx.setVariable("decision", review.getDecision().name());
        String html = templateEngine.process("mail/final-decision", ctx);
        notificationService.sendHtml(manuscript.getAuthor().getEmail(), "审稿结果通知", html);
    }

    @TransactionalEventListener
    public void onInitialReviewFinished(FinishedInitialReviewEvent event) {
        var manuscript = event.manuscript();
        Context ctx = new Context();
        ctx.setVariable("title", manuscript.getTitle());
        String html = templateEngine.process("mail/initial-review-finished", ctx);
        notificationService.sendHtml(manuscript.getAuthor().getEmail(), "初审结果通知", html);
    }

    @TransactionalEventListener
    public void onSelfRecommendationApproved(SelfRecommendationApprovedEvent event) {
        var rec = event.recommendation();
        Context ctx = new Context();
        ctx.setVariable("expert", rec.getApprover());
        String html = templateEngine.process("mail/self-recommendation-approved", ctx);
        notificationService.sendHtml(rec.getApplicant().getEmail(), "自荐专家审核", html);
    }


    // ------------------- 给审稿人发送通知 -------------------

    @TransactionalEventListener
    public void onReviewAssigned(ReviewTaskEvent event) {
        var process = event.process();
        Context ctx = new Context();
        ctx.setVariable("title", process.getManuscript().getTitle());
        ctx.setVariable("reviewer", process.getProcessedBy().getRealName());
        String html = templateEngine.process("mail/review-assigned", ctx);
        notificationService.sendHtml(process.getProcessedBy().getEmail(), "您有新的稿件需要评审", html);

    }

    // ------------------- 给编辑发送通知 -------------------

    @TransactionalEventListener
    public void onEditorDealProcess(EditorTaskEvent event) {
        var process = event.process();
        Context ctx = new Context();
        ctx.setVariable("title", process.getManuscript().getTitle());
        ctx.setVariable("editor", process.getProcessedBy().getRealName());
        String html = templateEngine.process("mail/editor-deal-process", ctx);
        notificationService.sendHtml(process.getProcessedBy().getEmail(), "稿件处理提醒", html);
    }

    @TransactionalEventListener
    public void onSelfRecommendationApply(SelfRecommendationApplyEvent event) {
        var rec = event.recommendation();
        Context ctx = new Context();
        ctx.setVariable("applicant", rec.getApplicant().getRealName());
        String html = templateEngine.process("mail/self-recommendation-apply", ctx);
        notificationService.sendHtml(rec.getApprover().getEmail(), "有新的自荐专家申请待处理", html);
    }
}
