package top.tankimzeg.editorial_system.events;

import top.tankimzeg.editorial_system.entity.Manuscript;

public record ManuscriptSubmittedEvent(Manuscript manuscript) {}
