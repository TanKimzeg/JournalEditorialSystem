package top.tankimzeg.editorial_system.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.tankimzeg.editorial_system.entity.*;
import top.tankimzeg.editorial_system.repository.ManuscriptProcessRepo;
import top.tankimzeg.editorial_system.repository.ManuscriptRepo;

/**
 * @author Kim
 * @date 2026/1/19 17:11
 * @description 稿件处理流程服务类
 */
@Service
public class ManuscriptProcessService {

    @Autowired
    private ManuscriptProcessRepo manuscriptProcessRepo;

    @Autowired
    private ManuscriptRepo manuscriptRepo;

    /**
     * 添加评审流程记录，稿件进入评审状态。分配审稿人时调用此方法。
     *
     * @param reviewer:     审稿人用户
     * @param manuscriptId: 稿件ID
     * @return 稿件流程
     */
    @Transactional
    public ManuscriptProcess addReviewProcess(User reviewer, Long manuscriptId) {
        Manuscript manuscript = manuscriptRepo.findById(manuscriptId)
                .orElseThrow(() -> new RuntimeException("稿件不存在"));
        manuscript.setStatus(Manuscript.ManuscriptStatus.UNDER_REVIEW);
        manuscriptRepo.save(manuscript);

        ManuscriptProcess process = new ManuscriptProcess();
        process.setManuscript(manuscript);
        process.setStage(ManuscriptProcess.Stage.PEER_REVIEW);
        process.setProcessedBy(reviewer);
        return manuscriptProcessRepo.save(process);
    }

    /**
     * 添加修订流程记录，稿件进入修订状态。让作者开始修订时调用此方法。
     *
     * @param author:       稿件提交的作者用户
     * @param manuscriptId: 稿件ID
     * @return 稿件流程
     */
    @Transactional
    public ManuscriptProcess addRevisionProcess(User author, Long manuscriptId, String comment) {
        Manuscript manuscript = manuscriptRepo.findById(manuscriptId)
                .orElseThrow(() -> new RuntimeException("稿件不存在"));
        manuscript.setStatus(Manuscript.ManuscriptStatus.UNDER_REVISION);
        manuscriptRepo.save(manuscript);

        ManuscriptProcess process = new ManuscriptProcess();
        process.setManuscript(manuscript);
        process.setStage(ManuscriptProcess.Stage.REVISION);
        process.setProcessedBy(author);
        process.setComments(comment);
        return manuscriptProcessRepo.save(process);
    }

    /**
     * 添加提交流程记录，稿件进入提交状态。作者提交稿件时调用此方法。
     *
     * @param editor:       准备处理该稿件的编辑用户
     * @param manuscriptId: 稿件ID
     * @return 稿件流程
     */
    @Transactional
    public ManuscriptProcess addSubmissionProcess(User editor, Long manuscriptId) {
        Manuscript manuscript = manuscriptRepo.findById(manuscriptId)
                .orElseThrow(() -> new RuntimeException("稿件不存在"));

        ManuscriptProcess process = new ManuscriptProcess();
        process.setManuscript(manuscript);
        process.setStage(ManuscriptProcess.Stage.INITIAL_REVIEW);
        process.setProcessedBy(editor);
        return manuscriptProcessRepo.save(process);
    }

    /**
     * 添加最终决定流程记录，稿件进入最终决定状态。完成最后一次修改后调用此方法。
     *
     * @param editor:       做出最终决定的编辑用户
     * @param manuscriptId: 稿件ID
     * @return 稿件流程
     */
    @Transactional
    public ManuscriptProcess addFinalDecisionProcess(User editor, Long manuscriptId) {
        Manuscript manuscript = manuscriptRepo.findById(manuscriptId)
                .orElseThrow(() -> new RuntimeException("稿件不存在"));
        manuscript.setStatus(Manuscript.ManuscriptStatus.UNDER_REVIEW);
        manuscriptRepo.save(manuscript);

        ManuscriptProcess process = new ManuscriptProcess();
        process.setManuscript(manuscript);
        process.setStage(ManuscriptProcess.Stage.FINAL_DECISION);
        process.setProcessedBy(editor);
        return manuscriptProcessRepo.save(process);
    }

    /**
     * 添加编辑评审流程记录，稿件进入编辑评审状态。编辑进行初步评审时调用此方法。
     *
     * @param editor:       进行编辑评审的编辑用户
     * @param manuscriptId: 稿件ID
     * @return 稿件流程
     */
    @Transactional
    public ManuscriptProcess addEditorialReviewProcess(User editor, Long manuscriptId) {
        ManuscriptProcess process = new ManuscriptProcess();
        process.setManuscript(manuscriptRepo.findById(manuscriptId)
                .orElseThrow(() -> new RuntimeException("稿件不存在")));
        process.setStage(ManuscriptProcess.Stage.EDITORIAL_REVIEW);
        process.setProcessedBy(editor);
        return manuscriptProcessRepo.save(process);
    }
}
