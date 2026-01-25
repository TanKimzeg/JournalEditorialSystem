package top.tankimzeg.editorial_system.events;

import top.tankimzeg.editorial_system.entity.Manuscript;
import top.tankimzeg.editorial_system.entity.Review;

public record FinishedReviewDecisionEvent(Manuscript manuscript, Review review) {}
